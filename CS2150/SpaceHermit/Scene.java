/* SpaceHermit
 * <desc>
 * dd/mm/yyyy
 * 
 * Scene Graph:
 *  Scene origin
 *  |
 *  +-- 
 */
package SpaceHermit;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import GraphicsLab.Colour;
import GraphicsLab.FloatBuffer;
import GraphicsLab.GraphicsLab;
import GraphicsLab.Vertex;

/**
 * My projects simulates being inside of a space jet. You can push the lever and
 * be warped (FTL travel) to a different, randomly generated place in the
 * universe!
 * <p>
 * Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis,
 * respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down
 * cursor keys to increase or decrease the viewpoint's distance from the scene
 * origin
 * <li>Press space when the lever is in it's rest position to start the warp
 * protocol
 * </ul>
 */
public class Scene extends GraphicsLab {
	/* declare tools */
	private Random rnd = new Random();

	/* declare aspect ratio */
	private float aspect;

	/* declare warp animation variables */
	// stores whether the user is warping
	private boolean warping = true;
	// stores whether the user warping animation has finished
	private boolean warpFinished = true;
	// current mode in animation, 'd' is the default mode
	private char mode = 'd';
	// stores time since last iteration of game render loop
	private long lastFrameTime;
	private long dT;
	// tick counter for the current animation mode
	private float tick = 0;
	// 's' mode, before the actual warp begins to provide tension
	private float startStallTickLimit = 4.0f;
	// 'i' mode, fades scene into warp
	private float fadeInTickLimit = 2.0f;
	// 'w' mode, warp active and scene is fully bright
	private float warpingTickLimit = 1.0f;
	// 'o' mode, fades scene out of warp
	private float fadeOutTickLimit = 2.0f;
	// default and current values of ambient lighting
	private float globalAmbient = 0.125f;
	private float currentAmbient;
	// default alpha property of white plane that covers the scene
	private float alpha = 0.0f;
	// range and current point of view angles
	private float povMin = 25.0f;
	private float povMax = 45.0f;
	private float pov = povMax;

	// declare the cockpit that contains check input, update and draw methods
	// for a cockpit object
	private Cockpit cockpit;

	/* declare cockpit shaking variables */
	// amplitude parameters
	private float ampMaxDefault = 0.5f;
	private float ampWarpMax = 2.0f;
	private float ampMax;
	// period parameters
	private float periodDefault = 10.0f;
	private float period;
	// current translation parameters
	private float shakeX = 0.0f;
	private float shakeY = 0.0f;
	private float shakeZ = 0.0f;
	// current tick
	private float xTick = 0;
	private float yTick = 0;
	private float zTick = 0;
	// store double value of 2PI
	public static double rad = 2 * Math.PI;

	/* declare background variables */
	// positioning values of background plane
	float bgHeight = 64.0f;
	float bgZ = 96.0f;
	// path values to access skyboxes
	public static String pckgDir = "SpaceHermit";
	private String skyboxDir = "textures";
	private String[] skyboxNames = { "corona_ft.png", "redeclipse_ft.png",
			"unnamedspace_ft.jpg", "unnamedspace3_ft.png" };
	// stores loaded skybox textures
	private List<Texture> skyboxes;
	// stores skybox texture currently in use
	private Texture currentSkybox;
	// used to reference previously used skybox
	private int currentSkyboxIndex = -1;

	public static void main(String args[]) {
		new Scene().run(WINDOWED, "Scene", 1.0f);
	}

	protected void initScene() throws Exception {
		// define aspect ratio of program
		aspect = (float) displayMode.getWidth() / displayMode.getHeight();

		// assigns new instance of cockpit class, passes animation scale in
		// constructor so the cockpit's animations runs in-sync with scene
		cockpit = new Cockpit();

		// sets random values for shakebing effect
		resetShake();
		initShake();

		// loads skyboxes, and set a random one as current
		skyboxes = Util.loadTextures(pckgDir + "/" + skyboxDir, skyboxNames);
		newSkybox();

		// sets the global ambient lighting to it's default value
		resetFade();

		// enable lighting calculations
		GL11.glEnable(GL11.GL_LIGHTING);
		// ensure that all normals are re-normalised after transformations
		// automatically
		GL11.glEnable(GL11.GL_NORMALIZE);

		// initialise last frame time with how long program has executed for
		lastFrameTime = System.nanoTime();
	}

	protected void checkSceneInput() {
		cockpit.checkSceneInput();
	}

	protected void updateScene() {
		// find time since last frame, and update tick accordingly
		dT = System.nanoTime() - lastFrameTime;
		// assign last frame time with how long program has executed for
		lastFrameTime = System.nanoTime();
		// update tick values
		tick();

		// updates cockpit, value returned tells scene whether warping has
		// been activated
		warping = cockpit.updateScene(warpFinished, dT, getAnimationScale());

		// stores ratio of tick to the tick limit of current animation mode
		float ratio;

		// execute non-warping or warping animation modes
		if (!warping) {
			// checks what non-warp modes are active if any
			if (mode == 'o') { // fade out
				ratio = getRatio(fadeOutTickLimit);
				if (ratio > 1) {
					// change mode to default
					mode = 'd';
					// set global ambient lighting to it's default value
					resetFade();
					// reset pov to default value
					pov = povMax;

					tickReset();
				} else {
					// decrease global ambience, and alpha of white screen
					currentAmbient = 1.0f - ratio * (1.0f - globalAmbient);
					alpha = 1.0f - ratio;
					// increase pov
					pov = povMin + ratio * (povMax - povMin);
				}
			}
		} else {
			// checks what warping modes are active
			switch (mode) {
			case 'd':
				// if warp has been activated then change mode to start stall
				mode = 's';
				// update warping animation variables
				warpFinished = false;

				tickReset();
				break;
			case 's': // start stall
				ratio = getRatio(startStallTickLimit);
				if (ratio > 1) {
					// change mode to fade in
					mode = 'i';

					tickReset();
				} else {
					// increase the amplitude and frequency of shaking effect
					increaseShake((float) tick
							/ (startStallTickLimit + fadeInTickLimit));
				}
				break;
			case 'i': // fade in
				ratio = getRatio(fadeInTickLimit);
				if (ratio > 1) {
					// change mode to warping
					mode = 'w';
					// set ambience to most extreme value
					currentAmbient = 1.0f;
					// make white screen fully opaque
					alpha = 1.0f;
					// change current skybox texture
					newSkybox();

					tickReset();
				} else {
					// increase the amplitude and frequency of shaking effect
					increaseShake((float) (tick + startStallTickLimit)
							/ (startStallTickLimit + fadeInTickLimit));
					// decrease pov
					pov = povMax - ratio * (povMax - povMin);
					// increase global ambience and alpha of white screen
					currentAmbient = ratio * (1.0f - globalAmbient)
							+ globalAmbient;
					alpha = ratio;
				}
				break;
			case 'w': // warping
				ratio = getRatio(warpingTickLimit);
				if (ratio >= 1) {
					resetShake();
					// change mode to fade out
					mode = 'o';
					// update warping animation variables
					warping = false;
					warpFinished = true;

					tickReset();
				}
				break;
			}
		}

		// find amplitudes of x, y and z shaking effects
		nextShake();
	}

	protected void renderScene() {
		// change the geometry colour to white so that the texture
		// is bright and details can be seen clearly
		Colour.WHITE.submit();

		// set the global ambient lighting to use current ambient level
		GL11.glLightModel(
				GL11.GL_LIGHT_MODEL_AMBIENT,
				FloatBuffer.wrap(new float[] { currentAmbient, currentAmbient,
						currentAmbient, 1.0f }));

		// draw background
		GL11.glPushMatrix();
		drawBackground(currentSkybox);
		GL11.glPopMatrix();

		// draw cockpit
		GL11.glPushMatrix();
		GL11.glTranslatef(shakeX, shakeY, shakeZ);
		cockpit.renderScene();
		GL11.glPopMatrix();

		// draw the white plane that covers the scene used in fading animations
		GL11.glPushMatrix();
		drawWhitePlane(alpha);
		GL11.glPopMatrix();

	}

	protected void setSceneCamera() {
		// set perspective projection, point of view assigned to a variable
		// modified by warping sequence
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(pov, aspect, 0.1f, 100.0f);

		// default viewpoint is positioned at the scene origin facing along the
		// negative Z axis
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}

	protected void cleanupScene() {// TODO: Clean up your resources here
	}

	/**
	 * Draws a plane to the back of the scene and applies a provided texture to
	 * it.
	 * 
	 * @param texture
	 *            the OpenGL texture to apply to plane
	 */
	private void drawBackground(Texture texture) {
		// disable lighting calculations so that they don't affect
		// the appearance of the texture
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);

		// enable texturing and bind an appropriate texture
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());

		// draw back plane
		// bottom left
		Vertex v1 = new Vertex(-bgHeight, -bgHeight, -bgZ);
		// top left
		Vertex v2 = new Vertex(-bgHeight, bgHeight, -bgZ);
		// top right
		Vertex v3 = new Vertex(bgHeight, bgHeight, -bgZ);
		// bottom right
		Vertex v4 = new Vertex(bgHeight, -bgHeight, -bgZ);
		// draw the plane geometry
		Util.drawTexRect(v4, v3, v2, v1);

		// disables textures and reset any local lighting changes
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPopAttrib();
	}

	/**
	 * Draws a white plane that covers the whole scene with a provided alpha
	 * value applied to it.
	 * 
	 * @param alpha
	 *            the desired transparency of the plane to be drawn
	 */
	private void drawWhitePlane(float alpha) {
		// disable lighting calculations so that they don't affect
		// the appearance of the plane
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);

		// change geometry colour to white with the provided alpha value used
		// for transparency
		GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);

		// enable blending and set blend function to interpolate the plane's
		// transparency to the whole scene
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// draw back plane to (more than) cover the scene
		// bottom left
		Vertex v1 = new Vertex(-displayMode.getWidth(),
				-displayMode.getHeight(), -1.0f);
		// top left
		Vertex v2 = new Vertex(-displayMode.getWidth(),
				displayMode.getHeight(), -1.0f);
		// top right
		Vertex v3 = new Vertex(displayMode.getWidth(), displayMode.getHeight(),
				-1.0f);
		// bottom right
		Vertex v4 = new Vertex(displayMode.getWidth(),
				-displayMode.getHeight(), -1.0f);
		// draw the plane geometry
		Util.drawRect(v4, v3, v2, v1);

		// disable blending and reset any local lighting changes
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
	}

	/**
	 * Change the current skybox to a new texture, ensuring no skyboxes are
	 * shown twice in a row.
	 */
	private void newSkybox() {
		// stores whether a valid index has been found yet
		boolean found = false;

		// stores currently found index
		int index = 0;

		// keeps on iterating until a new skybox index which isn't the previous
		// index is found with a random number generator
		while (!found) {
			index = rnd.nextInt(skyboxes.size());
			if (index != currentSkyboxIndex) {
				currentSkyboxIndex = index;
				found = true;
			}
		}
		// gets the new skybox texture from the pre-loaded list of skybox
		// textures
		currentSkybox = skyboxes.get(currentSkyboxIndex);
	}

	/**
	 * Update tick values with the time that has passed since last render call.
	 */
	private void tick() {
		// Amount to update by.
		// Converts delta time from nanoseconds to seconds.
		// Multiplies by animation scale of the scene.
		float updateTime = (float) ((dT * Math.pow(10, -9)) * getAnimationScale());

		// update tick values
		tick += updateTime;
		xTick += updateTime;
		yTick += updateTime;
		zTick += updateTime;
	}

	/**
	 * Resets the tick count.
	 */
	private void tickReset() {
		tick = 0;
	}

	/**
	 * Calculates the ratio of the tick to a given value.
	 * 
	 * @param tickLimit
	 *            the tick limit which acts as the divisor
	 * @return the ratio of tick to tickLimit
	 */
	private float getRatio(float tickLimit) {
		return (float) tick / tickLimit;
	}

	/**
	 * Sets the current ambient and alpha values to their default state.f
	 */
	private void resetFade() {
		currentAmbient = globalAmbient;
		alpha = 0.0f;
	}

	/**
	 * Reset the values of the shaking animation (amplitude and period).
	 */
	private void resetShake() {
		ampMax = ampMaxDefault;
		period = periodDefault;
	}

	/**
	 * Start of the x, y and z tick counters in random positions.
	 */
	private void initShake() {
		// random position in animation of each axis separately creates more
		// natural looking scene
		xTick = rnd.nextFloat() * period;
		yTick = rnd.nextFloat() * period;
		zTick = rnd.nextFloat() * period;

	}

	/**
	 * Calculate the x, y and z translation values of the camera-shaked object.
	 */
	private void nextShake() {
		// The respective axis current place in the period is found and
		// converted to a ratio.
		// Multiplying by 2PI finds the respective position of in a full cycle
		// of a sine curve.
		// The given value is then multiplied by the current maximum amplitude
		// to find the shift in the respective axis.
		shakeX = (float) Math.sin(((xTick) / period) * rad) * ampMax;
		shakeY = (float) Math.sin(((yTick) / period) * rad) * ampMax;
		shakeZ = (float) Math.sin(((zTick) / period) * rad) * ampMax;
	}

	/**
	 * Increase the shaking effect (increment ampMax and decrement period) using
	 * the given ratio.
	 * 
	 * @param ratio
	 *            the percentage completion of the animation
	 */
	private void increaseShake(float ratio) {
		// increment maximum amplitude with ratio multiplied by difference
		// between the maximum warp amplitude and the default maximum amplitude.
		ampMax = ampMaxDefault + ratio * (ampWarpMax - ampMaxDefault);
		// decrement period with ratio multiplied the default period length.
		period = periodDefault - ratio * periodDefault;
	}
}
