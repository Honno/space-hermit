package SpaceHermit;

import org.lwjgl.opengl.GL11;

import GraphicsLab.Normal;
import GraphicsLab.Vertex;

public class Util {
	public static void drawRect(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
		GL11.glBegin(GL11.GL_POLYGON);
		new Normal(v1.toVector(), v2.toVector(), v3.toVector(), v4.toVector()).submit();
		v1.submit();
		v2.submit();
		v3.submit();
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
}
