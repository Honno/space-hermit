package SpaceHermit;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import GraphicsLab.Colour;
import GraphicsLab.FloatBuffer;

public class Chasis {
    private float frameShininess  = 0.0f;
    private float frameSpecular[] = {0.75f, 0.75f, 0.75f, 1.0f};
    private float frameDiffuse[]  = {0.6f, 0.2f, 0.2f, 1.0f};
	
    public void draw()
    {
    	// draw a front
        drawFrameCylandir(-0.5f, 0.0f, -96.0f,
        		90.0f, 0.0f, 1.0f, 0.0f,
        		1.0f, 1.0f, 1.0f, 10, 10);
    }
    
    private void drawFrameCylandir(float xt, float yt, float zt,
    		float Dr, float xr, float yr, float zr,
    		float xs, float ys, float zs, int As, int Bs) {
    	
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, frameShininess);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(frameSpecular));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(frameDiffuse));
        
        GL11.glTranslatef(xt, yt, zt);
        
        GL11.glPushMatrix();
        
        GL11.glRotatef(Dr, xr, yr, zr);
        new Cylinder().draw(xs, ys, zs, As, Bs);
        
        GL11.glPopMatrix();
    	
    }
    
    private void drawChasisBottom() {
    	
    }
}
