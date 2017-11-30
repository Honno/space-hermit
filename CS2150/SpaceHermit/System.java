package SpaceHermit;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;

public class System {
	private Random rnd = new Random();
	
	private float animationScale;
	private float distMin;
	private float distMax;
	private float dist;
	
	private float sizeMin = 48.0f;
	private float sizeMax = 32.0f;
	private float size;
	
	private float x;
	private float y;
	
	//TODO path values to access skyboxes
	private String pckgDir = "SpaceHermit";
	private String textureDir = "skyboxes";
	private String[] plantTextureNames = { "corona_ft.png", "redeclipse_ft.png",
			"unnamedspace_ft.jpg", "unnamedspace3_ft.png" };
	private String[] planetTextureNames = { "corona_ft.png", "redeclipse_ft.png",
			"unnamedspace_ft.jpg", "unnamedspace3_ft.png" };
	// stores loaded planet textures
	private List<Texture> textures;
	// stores planet texture currently in use
	private Texture currentTexture;
	
	private Sphere planet;
	private Moon[] moons;
	
	public System(float animationScale, float distMin, float distMax) {
		this.animationScale = animationScale;
		this.distMin = distMin;
		this.distMax = distMax;
		newSystem();
		textures = Util.loadTextures(pckgDir + "/" + textureDir, planetTextureNames);
	}
	
	public void updateScene() {
		
	}
	
	public void renderScene() {
		drawPlanet();
	}
	
	public void newSystem() {
		size = rnd.nextFloat() * (sizeMax - sizeMin);
		dist = rnd.nextFloat() * ((distMax + size * 2) - (distMin - size * 2));
		planet = new Sphere();
	}
	
	private void drawPlanet() {
		/* reset scaling and rotation */
		GL11.glScalef(1.0f, 1.0f, 1.0f);
		GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);

		/* set position of planet */
		GL11.glTranslatef(0.0f, 0.0f, dist);
		
		/* set material properties */
		float shininess = 1.0f;
		float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] colour = { 0.75f, 0.75f, 0.75f, 1.0f };

		Util.material(shininess, specular, colour);
		
		planet.draw(size, 16, 16);
	}
	
	
}
