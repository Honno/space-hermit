package SpaceHermit;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import GraphicsLab.Colour;
import GraphicsLab.FloatBuffer;

public class Chasis {
    private float cylandirShininess  = 0.0f;
    private float cylandirSpecular[] = {0.75f, 0.75f, 0.75f, 1.0f};
    private float cylandirDiffuse[]  = {0.6f, 0.2f, 0.2f, 1.0f};
	
    public void draw()
    {
    	// draw a cylandir
        drawUnitCylandir();
    }
    
    private void drawUnitCylandir() {
    	
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, cylandirShininess);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(cylandirSpecular));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(cylandirDiffuse));
        
        GL11.glTranslatef(-0.5f, 0.0f, -10.0f);
        
        GL11.glPushMatrix();
        
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        new Cylinder().draw(1.0f, 1.0f, 1.0f, 10, 10);
        GL11.glPopMatrix();
    	
    }
}
