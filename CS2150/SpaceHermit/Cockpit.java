package SpaceHermit;

import org.lwjgl.opengl.GL11;

import GraphicsLab.FloatBuffer;
import GraphicsLab.Normal;
import GraphicsLab.Vertex;

public class Cockpit {
	
    private float frameShininess  = 0.8f;
    private float frameSpecular[] = {0.75f, 0.75f, 0.75f, 1.0f};
    private float frameDiffuse[]  = {0.5f, 0.5f, 0.5f, 1.0f};
	

	
	public void draw() {
		drawChasis();
	}
	
	private void drawChasis() {
    	GL11.glTranslatef(0.0f, 0.0f, 0.0f);
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);
        
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, frameShininess);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(frameSpecular));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(frameDiffuse));
    	
        float displaceY = 12f;
        
        // front
        float frontDist = 64f;
        float frontWidth = 0.5f;
        float frontWidth2 = 2*frontWidth;
        float frontHeight = 12f;
        
        Vertex v1 = new Vertex(-frontHeight, -frontWidth - displaceY, -frontDist); // bottom left
        Vertex v2 = new Vertex(-frontHeight, frontWidth - displaceY, -frontDist); // top left
        Vertex v3 = new Vertex(frontHeight, frontWidth - displaceY, -frontDist); // top right
        Vertex v4 = new Vertex(frontHeight, -frontWidth - displaceY, -frontDist); // bottom right
        
        // bottom
        float bottomY = 0.0f;
        float bottomXMod = 1.5f;
        
        Vertex v5 = new Vertex(-bottomXMod*frontHeight, frontWidth - displaceY - bottomY, 0);
        Vertex v6 = new Vertex(-bottomXMod*frontHeight, -frontWidth - displaceY - bottomY, 0);
        
        Vertex v7 = new Vertex(bottomXMod*frontHeight, frontWidth - displaceY - bottomY, 0);
        Vertex v8 = new Vertex(bottomXMod*frontHeight, -frontWidth - displaceY - bottomY, 0);
        
        // middle
        float middleY = 16f;
        float middleXMod = 1.25f;
        float middleZMod = 0.75f;
        
        Vertex v9 = new Vertex(-frontHeight + frontWidth2, frontWidth - displaceY, -frontDist);
        Vertex v10 = new Vertex(-middleXMod*frontHeight + frontWidth2, frontWidth - displaceY + middleY, -middleZMod*frontDist); // inward
        Vertex v11 = new Vertex(-middleXMod*frontHeight, frontWidth - displaceY + middleY, -middleZMod*frontDist); // outward
        
        Vertex v12 = new Vertex(frontHeight - frontWidth2, frontWidth - displaceY, -frontDist);
        Vertex v13 = new Vertex(middleXMod*frontHeight - frontWidth2, frontWidth - displaceY + middleY, -middleZMod*frontDist); // inward
        Vertex v14 = new Vertex(middleXMod*frontHeight, frontWidth - displaceY + middleY, -middleZMod*frontDist); // outward
        
        // middle-top
        
        Vertex v15 = new Vertex(-middleXMod*frontHeight, frontWidth - displaceY + middleY + frontWidth2, -middleZMod*frontDist); // inward
        Vertex v16 = new Vertex(-middleXMod*frontHeight - frontWidth2, frontWidth - displaceY + middleY + frontWidth2, -middleZMod*frontDist); // outward
        
        Vertex v17 = new Vertex(middleXMod*frontHeight, frontWidth - displaceY + middleY + frontWidth2, -middleZMod*frontDist); // inward
        Vertex v18 = new Vertex(middleXMod*frontHeight + frontWidth2, frontWidth - displaceY + middleY + frontWidth2, -middleZMod*frontDist); // outward
        
        // top
        float topY = 8f;
        float topXMod = 1.5f;
        
        Vertex v19 = new Vertex(-topXMod*frontHeight, frontWidth - displaceY + middleY + topY, 0);
        Vertex v20 = new Vertex(-topXMod*frontHeight + frontWidth2, frontWidth - displaceY + middleY + topY + frontWidth2, 0);
        
        Vertex v21 = new Vertex(topXMod*frontHeight, frontWidth - displaceY + middleY + topY, 0);
        Vertex v22 = new Vertex(topXMod*frontHeight - frontWidth2, frontWidth - displaceY + middleY + topY + frontWidth2, 0);
        
        // floor
        float floorY = 20f;
        float floorHeight = 6f;
        float floorZMod = 0.5f;
        
        Vertex v23 = new Vertex(-floorHeight, -frontWidth - displaceY - floorY, -floorZMod*frontDist);
        Vertex v24 = new Vertex(floorHeight, -frontWidth - displaceY - floorY, -floorZMod*frontDist);
        
        // middle-front
        float middleFrontY = 20f;
        float middleFrontHeight = 6f;
        float middleFrontZMod = 0.8f;
        
        Vertex v25 = new Vertex(-middleFrontHeight, -frontWidth - displaceY + middleFrontY, -middleFrontZMod*frontDist); // bottom left
        Vertex v26 = new Vertex(-middleFrontHeight, frontWidth - displaceY + middleFrontY, -middleFrontZMod*frontDist); // top left
        Vertex v27 = new Vertex(middleFrontHeight, frontWidth - displaceY + middleFrontY, -middleFrontZMod*frontDist); // top right
        Vertex v28 = new Vertex(middleFrontHeight, -frontWidth - displaceY + middleFrontY, -middleFrontZMod*frontDist); // bottom right
        
        // draw everything
        
        Util.drawRect(v4, v3, v2, v1); // draw front
        
        Util.drawRect(v6, v1, v2, v5); // draw bottom left
        Util.drawRect(v8, v7, v3, v4); // draw bottom right
        
        Util.drawRect(v11, v2, v9, v10); // draw middle left
        Util.drawRect(v14, v13, v12, v3); // draw middle right
        
        Util.drawRect(v16, v11, v10, v15); // draw middle-top left
        Util.drawRect(v18, v17, v13, v14); // draw middle-top right
        
        Util.drawRect(v20, v19, v16, v15); // draw top left side
        Util.drawRect(v22, v17, v18, v21); // draw top right side
        
        Util.drawRect(v24, v4, v1, v23); // draw floor front
        Util.drawTri(v23, v1, v6); // draw floor left
        Util.drawTri(v24, v8, v4); // draw floor right

        Util.drawRect(v28, v27, v26, v25); // draw middle front
    }
	
}
