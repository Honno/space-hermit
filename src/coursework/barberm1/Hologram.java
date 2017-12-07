package coursework.barberm1;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;

import GraphicsLab.Colour;

public class Hologram {
	/* path values to access textures */
	private String textureDir = "textures";
	private String path = Util.pckgDir + "/" + textureDir + "/";

	/* declare Earth and position variables */
	private float earthRotatePeriod = 30.0f;
	private Texture earthTex;
	private Sphere earth;

	/* declare Moon and animation variables */
	private float moonRotatePeriod = 10.0f;
	private int moonOrbitDips = 4;
	private float moonOrbitPeriod = 5.0f;
	private float moonOrbitDist = 2.0f;
	private float moonOrbitAmp = 0.125f;
	private float moonOrbitY;
	private Texture moonTex;
	private Sphere moon;

	/* declare light cone */
	private Cylinder cone;

	/* declare flicker animation variables */
	private boolean flicker = false;
	private int constantFlickers = 16;
	private int exponentialFlickers = 2;
	private float shortTime = 0.03125f;
	private float nextTime = shortTime;
	private int counter = 1;
	private float m = 6.0f;
	private float flickerTick = 0.0f;
	private boolean flickerAnim = false;

	/* declare tick */
	private float tick = 0.0f;

	/**
	 * Initialize the Earth, Moon and light cone of the hologram.
	 */
	public Hologram() {
		// load textures for earth and moon
		try {
			earthTex = Util.loadTexture(path + "earth.png", "PNG");
			moonTex = Util.loadTexture(path + "moon.png", "PNG");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// create earth and enable texture to rotate with it
		earth = new Sphere();
		earth.setDrawStyle(GLU.GLU_FILL);
		earth.setTextureFlag(true);
		earth.setNormals(GLU.GLU_SMOOTH);

		// create moon and enable texture to rotate with it
		moon = new Sphere();
		moon.setDrawStyle(GLU.GLU_FILL);
		moon.setTextureFlag(true);
		moon.setNormals(GLU.GLU_SMOOTH);

		// create light cone
		cone = new Cylinder();
	}

	/**
	 * @param start
	 *            whether to start animation again
	 * @param dT
	 *            time since last render update
	 * @param animationScale
	 *            the animation scale of the program
	 */
	public void updateScene(boolean start, long dT, float animationScale) {
		// find amount to update tick values by
		float updateAmount = (float) ((dT * Math.pow(10, -9)) * animationScale);
		// update tick values
		tick += updateAmount;
		flickerTick += updateAmount;

		// if flicker animation is active, execute animation code
		if (flickerAnim) {
			// flicker image at constant rate
			if (counter <= constantFlickers) {
				if (flickerTick > nextTime) {
					// flicker hologram
					flicker = !flicker;

					resetFlickerTick();
					counter++;
				}
				// flicker images at exponential rate
			} else if (counter <= constantFlickers + exponentialFlickers) {
				if (flickerTick > nextTime) {
					// flicker hologram
					flicker = !flicker;

					// find the next time
					nextTime = shortTime * m * (counter - constantFlickers);

					resetFlickerTick();
					counter++;
				}
				// end flicker animation
			} else {
				// reset next time value to start from the begining time
				nextTime = shortTime;

				resetFlickerTick();
				resetCounter();

				// turn of flicker and flicker animation mode
				flicker = false;
				flickerAnim = false;
			}
			// cockpit says to turn off hologram and get ready for flicker
		} else if (start) {
			flicker = true;
			// cockpit says start flicker animation
		} else if (!start && flicker) {
			resetFlickerTick();
			resetCounter();
			flickerAnim = true;
		}

		// find y position of moon
		moonOrbitY = (float) Math.sin((double) ((tick / moonOrbitDips) / moonOrbitPeriod) * Util.rad) * moonOrbitAmp;
	}

	public void renderScene() {
		// only render hologram if not on flicker
		if (!flicker) {
			// draw light cone
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0f, -0.875f, 0.0f);
			drawCone();
			GL11.glPopMatrix();

			// draw moon
			GL11.glPushMatrix();
			GL11.glRotatef(360.0f * (tick / moonOrbitPeriod), 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef(moonOrbitDist, moonOrbitY, 0.0f);
			drawMoon();
			GL11.glPopMatrix();

			// draw earth
			GL11.glPushMatrix();
			drawEarth();
			GL11.glPopMatrix();
		}
	}

	/**
	 * Draw the Earth.
	 */
	private void drawEarth() {
		// find value of rotation
		float rotate = 360.0f * (tick / earthRotatePeriod);

		// disable lighting calculations so that they don't affect
		// the appearance of the plane
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);

		// enable texturing and bind an appropriate texture
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, earthTex.getTextureID());

		// enable blending and set blend function to interpolate the plane's
		// transparency to the whole scene
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// transform and draw the earth
		GL11.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(rotate, 0.0f, 0.0f, 1.0f);
		earth.draw(1.0f, 24, 24);

		// disables textures and blending, and reset any local lighting changes
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
	}

	/**
	 * Draw the Moon.
	 */
	private void drawMoon() {
		// find value of rotation
		float rotate = 360.0f * (tick / moonRotatePeriod);

		// disable lighting calculations so that they don't affect
		// the appearance of the plane
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);

		// enable texturing and bind an appropriate texture
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, moonTex.getTextureID());

		// enable blending and set blend function to interpolate the plane's
		// transparency to the whole scene
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// transform and draw the earth
		GL11.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(rotate, 0.0f, 0.0f, 1.0f);
		earth.draw(0.25f, 24, 24);

		// disables textures and blending, and reset any local lighting changes
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
	}

	/**
	 * Draw the light cone.
	 */
	private void drawCone() {
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);
		// change geometry colour to translucent blue
		GL11.glColor4f(0.0f, 0.75f, 1.0f, 0.125f);

		// enable blending and set blend function to interpolate the plane's
		// transparency to the whole scene
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		//cone.draw(1.0f, 0.5f, 0.875f, 24, 24);
		cone.draw(-1.0f, -0.5f, 0.875f, 24, 24);

		// reset colour changes
		Colour.WHITE.submit();

		// disable blending and reset any local lighting changes
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
	}

	/**
	 * Reset flicker tick.
	 */
	private void resetFlickerTick() {
		flickerTick = 0.0f;
	}

	/**
	 * Reset counter.
	 */
	private void resetCounter() {
		counter = 0;
	}

}
