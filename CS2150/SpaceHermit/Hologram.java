package SpaceHermit;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;

import GraphicsLab.Colour;

public class Hologram {
	private String textureDir = "textures";
	private String path = Scene.pckgDir + "/" + textureDir + "/";
	
	private Sphere planet;
	private Sphere moon;
	
	private Texture planetTex;
	private Texture moonTex;
	
	public Hologram()  {
		try {
			planetTex = Util.loadTexture(path + "earth.png", "PNG");
			moonTex = planetTex;
		} catch (IOException e) {
			//
		}
		planet = new Sphere();
	}
	
	public void updateScene() {
		
	}
	
	public void renderScene() {
			GL11.glRotatef(30.0f, 1.0f, 0.0f, 1.0f);
			drawPlanet();
	}
	
	private void drawPlanet() {
		// disable lighting calculations so that they don't affect
		// the appearance of the plane
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);
		// enable texturing and bind an appropriate texture
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, planetTex.getTextureID());
		// enable blending and set blend function to interpolate the plane's
		// transparency to the whole scene
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
	    GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
        GL11.glEnable(GL11.GL_TEXTURE_GEN_T);            
 
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        // Set The Texture Generation Mode For S To Sphere Mapping
        GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);          
        GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
		
        planet.draw(1.0f, 32, 32);
        
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
	}
}
