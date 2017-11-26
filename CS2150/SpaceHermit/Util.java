package SpaceHermit;

import org.lwjgl.opengl.GL11;

import GraphicsLab.FloatBuffer;
import GraphicsLab.Normal;
import GraphicsLab.Vertex;

public class Util {
	public static void drawRect(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
		GL11.glBegin(GL11.GL_POLYGON);
		new Normal(v1.toVector(), v2.toVector(), v3.toVector(), v4.toVector()).submit();
		GL11.glTexCoord2f(1.0f,0.0f);
		v1.submit();
		GL11.glTexCoord2f(1.0f,1.0f);
		v2.submit();
		GL11.glTexCoord2f(0.0f,1.0f);
		v3.submit();
		GL11.glTexCoord2f(0.0f,0.0f);
		v4.submit();
		GL11.glEnd();
	}
	
	public static void drawTri(Vertex v1, Vertex v2, Vertex v3) {
		GL11.glBegin(GL11.GL_TRIANGLES);
		new Normal(v1.toVector(), v2.toVector(), v3.toVector()).submit();
		v1.submit();
		v2.submit();
		v3.submit();
		GL11.glEnd();
	}
	
	public static void material(float shininess, float[] specular, float[] colour) {
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shininess);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(specular));
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, FloatBuffer.wrap(colour));
	}
}
