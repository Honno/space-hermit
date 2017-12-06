package SpaceHermit;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;

import com.sun.prism.paint.Color;

import GraphicsLab.Colour;

public class Hologram {
	private String textureDir = "textures";
	private String path = Scene.pckgDir + "/" + textureDir + "/";
	
	private float earthRotatePeriod = 30.0f;
	private Texture earthTex;
	private Sphere earth;
	
	private float moonRotatePeriod = 10.0f;
	private float moonOrbitPeriod = 10.0f;
	private float moonOrbitDist = 2.0f;
	private float moonOrbitAmp = 0.125f;
	private Texture moonTex;
	private Sphere moon;
	
	private Cylinder cone;
	
	private boolean flicker = false;
    private int constant = 24;
    private int exponential = 4;
    private float shortTime = 0.03125f;
    private float nextTime = shortTime;
    private int c = 1;
    private float m = 4.0f;
	
	private float tick = 0.0f;
	private float flickerTick = 0.0f;
	
	private boolean flickerAnim = true;
	
	public Hologram()  {
		try {
			earthTex = Util.loadTexture(path + "earth.png", "PNG");
			moonTex = Util.loadTexture(path + "moon.png", "PNG");
		} catch (IOException e) {
			//
		}
		
		earth = new Sphere();
		earth.setDrawStyle(GLU.GLU_FILL);
		earth.setTextureFlag(true);
		earth.setNormals(GLU.GLU_SMOOTH);
		
		moon = new Sphere();
		moon.setDrawStyle(GLU.GLU_FILL);
		moon.setTextureFlag(true);
		moon.setNormals(GLU.GLU_SMOOTH);
		
		cone = new Cylinder();
	}
	
	public void updateScene(boolean start, long dT, float animationScale) {
		float updateAmount = (float) ((dT * Math.pow(10, -9)) * animationScale);
		tick += updateAmount;
		flickerTick += updateAmount;
		
		if(flickerAnim) {
			if(c <= constant) {
				if(flickerTick > nextTime) {
					flicker = !flicker;
					flickerTick = 0.0f;
					c++;
				}
			} else if (c <= constant + exponential) {
				if(flickerTick > nextTime) {
					flicker = !flicker;
					nextTime = shortTime * m * m * (c - constant);
					flickerTick = 0.0f;
					c++;
				}
			} else {
				nextTime = shortTime;
				flickerTick = 0.0f;
				c = 0;
				flickerAnim = false;
			}
		} else if(start) {
			flickerAnim = true;
		}
		
		
	}
	
	public void renderScene() {
		if(!flicker) {
			GL11.glPushMatrix();
			drawCone();
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			drawEarth();
			GL11.glPopMatrix();
			
			float y = (float) Math.sin((double) (tick / moonOrbitPeriod) * Scene.rad) * moonOrbitAmp;
			
			GL11.glPushMatrix();
			GL11.glRotatef(360.0f * (tick / moonOrbitPeriod), 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef(moonOrbitDist, y, 0.0f);
			drawMoon();
			GL11.glPopMatrix();
		}
	}
	
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
	
	private void drawCone() {
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);
		// change geometry colour to translucent blue
		GL11.glColor4f(0.25f, 0.25f, 1.0f, 0.5f);
		
		// enable blending and set blend function to interpolate the plane's
		// transparency to the whole scene
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glTranslatef(0.0f, -0.875f, 0.0f);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		cone.draw(1.0f, 0.5f, 0.875f, 24, 24);
		
		Colour.WHITE.submit();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
	}
}
