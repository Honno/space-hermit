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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import com.sun.prism.paint.Color;

import GraphicsLab.*;

/**
 * This sample demonstrates the use of user input and various types of animation
 * to add a dynamic aspect to a 3D scene
 * 
 * <p>Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin
 * <li>Press L to lower the sun
 * <li>Press R to raise the sun
 * </ul>
 *
 * <p>Adapted from Mark Bernard's LWJGL NeHe samples
 *
 * @author Anthony Jones and Dan Cornford
 */
public class Scene extends GraphicsLab
{
	private Random rnd = new Random();
	
	private float aspect; 
	
	private boolean warping = false;
	private float stallTickLimit = 100 * getAnimationScale();
	private float fadeInTickLimit = 50 * getAnimationScale();
	private float warpingTickLimit = 25 * getAnimationScale();
	private float fadeOutTickLimit = 50 * getAnimationScale();
	private float endStallTickLimit = 150 * getAnimationScale();
	private int tick = 0;
	private char mode = 'n';
	
	float globalAmbient = 0.125f;
	float currentAmbient;
	
	float alpha = 0.0f;
	
	float povMax = 45.0f;
	float povMin = 25.0f;
	float pov = povMax;
	
    private Cockpit cockpit;
    
    float bgHeight = 64.0f;
    float bgZ = 96.0f;
    
    private String pckgDir = "SpaceHermit";
    private String skyboxDir = "skyboxes";
    private String[] skyboxNames = {"corona_ft.png", "redeclipse_ft.png", "unnamedspace_ft.jpg", "unnamedspace3_ft.png"};
    private List<Texture> skyboxes;
    
    private Texture currentSkybox;
    private int currentSkyboxIndex = -1;

    public static void main(String args[])
    {   
    	new Scene().run(WINDOWED,"Scene",1.0f);
    }

    protected void initScene() throws Exception
    {
    	aspect = (float) displayMode.getWidth() / (float) displayMode.getHeight();
    	resetAmbient();
    	
    	cockpit = new Cockpit(getAnimationScale());

    	skyboxes = loadTextures(pckgDir + "/" + skyboxDir, skyboxNames);
    	newSkybox();
        
        float ambient0[]  = {0.0625f,  0.0625f, 0.0625f, 1.0f};
        float diffuse0[]  = {0.125f,  0.125f, 0.25f, 1.0f};
        float position0[] = {0.0f, 16.0f, -16.0f, 1.0f};

        // supply OpenGL with the properties for the first light
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
        // enable the first light
        GL11.glEnable(GL11.GL_LIGHT0);

        // enable lighting calculations
        GL11.glEnable(GL11.GL_LIGHTING);
        // ensure that all normals are re-normalised after transformations automatically
        GL11.glEnable(GL11.GL_NORMALIZE);
 
    }
    protected void checkSceneInput() {
    	cockpit.checkSceneInput();
    }
    protected void updateScene()
    {
    	float r; // ratio
    	if(!warping) {
    		switch(mode) {
    			case 'o':
    				r = getRatio(fadeOutTickLimit);
    				if(r >= 1) {
    					mode = 'e';
    					tickReset();
    					resetAmbient();
    				} else {
    					fadeOut(r);
    					tick++;
    				}
    				break;
    			case 'e': {
    				r = getRatio(endStallTickLimit);
    				if(r > 1) {
    					pov = povMax;
    					mode = 'n';
    					tickReset();
    				} else {
    					tick++;
    					pov = povMin + r * (povMax - povMin);
    				}
    				break;
    			}
    		}
    		
    		warping = cockpit.updateScene(warping, getAnimationScale());
    		if(warping) {
    			mode = 's';
    			tickReset();
    		}
    		tick++;
    	} else {
    		switch(mode) {
    			case 's': // stall
    				r = getRatio(stallTickLimit);
    				if(r >= 1) {
    					pov = povMin;
    					mode = 'i';
    					tickReset();
    					
    				} else {
    					tick++;
    				}
    				break;
    			case 'i': // fade in
    				r = getRatio(fadeInTickLimit);
    				if(r >= 1) {
    					mode = 'w';
    					tickReset();
    					fullBright();
    					newSkybox();
    				} else {
    					fadeIn(r);
    					tick++;
    				}
    				break;
    			case 'w': // warping
    				r = getRatio(warpingTickLimit);
    				if(r >= 1) {
    					mode = 'o';
    					warping = false;
    					tickReset();
    				} else {
    					tick++;
    				}
    				break;
    		}
    	}
    }
    protected void renderScene()
    {
        // set the global ambient lighting
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT,FloatBuffer.wrap(new float[]{currentAmbient, currentAmbient, currentAmbient, 1.0f}));
    	
    	GL11.glPushMatrix();
    	drawBackground(currentSkybox);
        GL11.glPopMatrix();

    	GL11.glPushMatrix();
    	cockpit.renderScene();
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
    	drawWhitePlane(alpha);
    	GL11.glPopMatrix();
        
        
    }
    
    protected void cleanupScene()
    {// empty
    }
    
    private List<Texture> loadTextures(String dir, String[] names) {
    	List<Texture> textures = new ArrayList<Texture>();
    	Pattern ext = Pattern.compile("\\.(.+)");
    	for(String name: names) {
    		try {
    			Matcher match = ext.matcher(name);
    			match.find();
				textures.add(loadTexture(dir + "/" + name, match.group(1).toUpperCase()));
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return textures;
    }
    
    private void drawBackground(Texture texture) {
        // disable lighting calculations so that they don't affect
        // the appearance of the texture 
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        GL11.glDisable(GL11.GL_LIGHTING);
        // change the geometry colour to white so that the texture
        // is bright and details can be seen clearly
        Colour.WHITE.submit();
        // enable texturing and bind an appropriate texture
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,texture.getTextureID());
        // draw back plane
        Vertex v1 = new Vertex(-bgHeight, -bgHeight, -bgZ); // bottom left
        Vertex v2 = new Vertex(-bgHeight, bgHeight, -bgZ); // top left
        Vertex v3 = new Vertex(bgHeight, bgHeight, -bgZ); // top right
        Vertex v4 = new Vertex(bgHeight, -bgHeight, -bgZ); // bottom right
        
        // draw the plane geometry. order the vertices so that the plane faces up
        Util.drawRect(v4, v3, v2, v1);
        
        // disables textures and reset any local lighting changes
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }
    
    private void drawWhitePlane(float alpha) {
        // disable lighting calculations so that they don't affect
        // the appearance of the plane
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        GL11.glDisable(GL11.GL_LIGHTING);
        // change the geometry colour to white
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // draw back plane to double display size to ensure screen is fully covered
        Vertex v1 = new Vertex(-displayMode.getWidth(), -displayMode.getHeight(), -1.0f); // bottom left
        Vertex v2 = new Vertex(-displayMode.getWidth(), displayMode.getHeight(), -1.0f); // top left
        Vertex v3 = new Vertex(displayMode.getWidth(), displayMode.getHeight(), -1.0f); // top right
        Vertex v4 = new Vertex(displayMode.getWidth(), -displayMode.getHeight(), -1.0f); // bottom right
        
        // draw the plane geometry. order the vertices so that the plane faces up
        Util.drawRect(v4, v3, v2, v1);
        
        // reset any local lighting changes
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopAttrib();
    }
    
    private void newSkybox() {
    	boolean found = false;
    	int index = currentSkyboxIndex;
    	while(!found) {
    		index = rnd.nextInt(skyboxes.size());
    		if(index != currentSkyboxIndex) {
    			currentSkyboxIndex = index;
    			found = true;
    		}
    	}
    	currentSkybox = skyboxes.get(currentSkyboxIndex);
    }
    
    private void tickReset() {
    	tick = 0;
    }
    
    private float getRatio(float tickLimit) {
    	return (float) tick / tickLimit;
    }
    
    protected void setSceneCamera()
    {
        // default projection is a perspective projection with a 90 (45*2) degree field of view, width/height
        // aspect ratio and visible range of 0.1 to 100.0 scene units
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(pov,aspect,0.1f,100.0f);

        // default viewpoint is positioned at the scene origin facing along the negative Z axis
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }
    
    private void fadeIn(float ratio) {
    	currentAmbient = ratio * (1.0f - globalAmbient) + globalAmbient;
    	alpha = ratio;
    }
    
    private void fadeOut(float ratio) {
    	currentAmbient = 1.0f - ratio * (1.0f - globalAmbient);
    	alpha = 1 - ratio;
    }
    
    private void fullBright() {
    	currentAmbient = 1.0f;
    	alpha = 1.0f;
    }
    
    private void resetAmbient() {
    	currentAmbient = globalAmbient;
    	alpha = 0.0f;
    }
}
