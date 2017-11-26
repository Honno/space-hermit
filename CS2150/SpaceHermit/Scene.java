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
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

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
	
	private boolean warping = false;
	private int stallTickLimit = 20;
	private int fadeInTickLimit = 20;
	private int warpingTickLimit = 100;
	private int fadeOutTickLimit = fadeInTickLimit;
	private int tick = 0;
	private char mode = 'n';
	
	float[] globalAmbient = {0.125f, 0.125f,  0.125f, 1.0f};
	float[] currentAmbient = globalAmbient;
	
    private Cockpit cockpit;
    
    private String pckgDir = "SpaceHermit";
    private String skyboxDir = "skyboxes";
    private String[] skyboxNames = {"corona_ft.png", "redeclipse_ft.png", "unnamedspace_ft.jpg", "unnamedspace3_ft.png"};
    private List<Texture> skyboxes;
    
    private Texture currentSkybox;
    private int currentSkyboxIndex = -1;

    public static void main(String args[])
    {   
    	new Scene().run(WINDOWED,"Scene",0.01f);
    }


    protected void initScene() throws Exception
    {
    	cockpit = new Cockpit();

    	skyboxes = loadTextures(pckgDir + "/" + skyboxDir, skyboxNames);
    	newSkybox();
        
        float ambient0[]  = {0.0625f,  0.0625f, 0.0625f, 1.0f};
        float diffuse0[]  = {0.125f,  0.125f, 0.25f, 1.0f};
        float position0[] = {-8.0f, 16.0f, -16.0f, 1.0f};

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
    	if(!warping) {
    		warping = cockpit.updateScene(warping);
    		if(warping) {
    			mode = 's';
    		}
    	} else {
    		switch(mode) {
    			case 's':
    				if(tick > stallTickLimit) {
    					mode = 'i';
    					tickReset();
    					// init warp
    					
    				} else {
    					tick++;
    				}
    				break;
    			case 'i':
    				if(tick > fadeInTickLimit) {
    					mode = 'w';
    					tickReset();
    					newSkybox();
    				} else {
    					tick++;
    				}
    				break;
    			case 'w':
    				if(tick > warpingTickLimit) {
    					mode = 'o';
    					tickReset();
    				} else {
    					tick++;
    				}
    			case 'o':
    				if(tick > fadeOutTickLimit) {
    					mode = 'n';
    					warping = false;
    					tickReset();
    				} else {
    					tick++;
    				}
    				break;
    			default:
    				warping = false;
    				break;
    				
    		}
    	}
    }
    protected void renderScene()
    {
        // set the global ambient lighting
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT,FloatBuffer.wrap(currentAmbient));
    	
    	GL11.glPushMatrix();
    	drawBackground(currentSkybox);
        GL11.glPopMatrix();
    	
    	GL11.glPushMatrix();
    	cockpit.renderScene();
        GL11.glPopMatrix();
        
        
    }
    
    protected void cleanupScene()
    {// empty
    }
    
    private void resetAnimations()
    {
        
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
        
        // position, scale and draw the back plane
        float bgHeight = 48.0f;
        float bgZ = 64.0f;
        
        Vertex v1 = new Vertex(-bgHeight, -bgHeight, -bgZ); // bottom left
        Vertex v2 = new Vertex(-bgHeight, bgHeight, -bgZ); // top left
        Vertex v3 = new Vertex(bgHeight, bgHeight, -bgZ); // top right
        Vertex v4 = new Vertex(bgHeight, -bgHeight, -bgZ); // bottom right
        
        // draw the plane geometry. order the vertices so that the plane faces up
        Util.drawRect(v4, v3, v2, v1);
        
        // disable textures and reset any local lighting changes
        GL11.glDisable(GL11.GL_TEXTURE_2D);
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
}
