package SpaceHermit;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

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
	
	private Sphere planet;
	private Moon[] moons;
	
	public System(float animationScale, float distMin, float distMax) {
		this.animationScale = animationScale;
		this.distMin = distMin;
		this.distMax = distMax;
		newSystem();
	}
	
	public void updateScene() {
		
	}
	
	public void renderScene() {
		GL11.glTranslatef(0.0f, 0.0f, dist);
		planet.draw(size, 16, 16);
		//for(Moon moon: moons) {
			//
		//}
	}
	
	public void newSystem() {
		size = rnd.nextFloat() * (sizeMax - sizeMin);
		dist = rnd.nextFloat() * ((distMax + size * 2) - (distMin - size * 2));
		planet = new Sphere();
		
	}
	
	
}
