package SpaceHermit;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

import GraphicsLab.FloatBuffer;
import GraphicsLab.Normal;
import GraphicsLab.Vertex;

public class Chasis {
    private float frameShininess  = 0.2f;
    private float frameSpecular[] = {0.75f, 0.75f, 0.75f, 1.0f};
    private float frameDiffuse[]  = {0.6f, 0.2f, 0.2f, 1.0f};
	
    public void draw()
    {
    	GL11.glPushMatrix();
    	drawChasisFrame();
    	GL11.glPopMatrix();
    	
        //drawFrame();
    }
    
    private void drawFrame() {
    	// middle bottom
    	drawFrameCylandir(-16f, -12.0f, -96.0f,
        		90.0f, 0.0f, 1.0f, 0.0f,
        		0.5f, 0.5f, 32.0f, 24, 24);
    	
    	// left bottom
    	drawFrameCylandir(-16f, -12.0f, -96.0f,
        		90.0f, 0.5f, 1.0f, 0.0f,
        		0.5f, 0.5f, 32.0f, 24, 24);
    	
    	// right bottom
    	drawFrameCylandir(-16f, -12.0f, -96.0f,
        		90.0f, 0.0f, 1.0f, 0.0f,
        		0.5f, 0.5f, 32.0f, 24, 24);
    }
    
    private void drawFrameCylandir(float xt, float yt, float zt,
    		float Dr, float xr, float yr, float zr,
    		float baseRadius, float topRadius, float height, int slices, int stacks) {
    	
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, frameShininess);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(frameSpecular));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(frameDiffuse));
        
        GL11.glTranslatef(xt, yt, zt);
        
        GL11.glPushMatrix();
        
        GL11.glRotatef(Dr, xr, yr, zr);
        new Cylinder().draw(baseRadius, topRadius, height, slices, stacks);
        
        GL11.glPopMatrix();
    	
    }
    
    private void drawChasisFrame() {
    	GL11.glTranslatef(0.0f, 0.0f, 0.0f);
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);
    	
    	GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, frameShininess);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(frameSpecular));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(frameDiffuse));
    	
        float displaceY = 8f;
        
        // front
        float frontDist = 64f;
        float frontWidth = 0.25f;
        float frontHeight = 16f;
        
        Vertex v1 = new Vertex(-frontHeight, -frontWidth - displaceY, -frontDist); // bottom left
        Vertex v2 = new Vertex(-frontHeight, frontWidth - displaceY, -frontDist); // top left
        Vertex v3 = new Vertex(frontHeight, frontWidth - displaceY, -frontDist); // top right
        Vertex v4 = new Vertex(frontHeight, -frontWidth - displaceY, -frontDist); // bottom right
        // bottom
        float bottomY = 4* displaceY;
        
        Vertex v5 = new Vertex(-1.5f*frontHeight, frontWidth - bottomY, 0);
        Vertex v6 = new Vertex(-1.5f*frontHeight, -frontWidth - bottomY, 0);
        
        Vertex v7 = new Vertex(1.5f*frontHeight, frontWidth - bottomY, 0);
        Vertex v8 = new Vertex(1.5f*frontHeight, -frontWidth - bottomY, 0);
        
        
        // draw front
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v3.toVector(),v2.toVector(),v1.toVector(),v4.toVector()).submit();
            
            v3.submit();
            v2.submit();
            v1.submit();
            v4.submit();
        }
        GL11.glEnd();
        
        // draw bottom left
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v1.toVector(),v2.toVector(),v5.toVector(),v6.toVector()).submit();
            
            v1.submit();
            v2.submit();
            v5.submit();
            v6.submit();
        }
        GL11.glEnd();
        
        // draw bottom right
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v3.toVector(),v4.toVector(),v8.toVector(),v7.toVector()).submit();
            
            v3.submit();
            v4.submit();
            v8.submit();
            v7.submit();
        }
        GL11.glEnd();
        
    }
}
