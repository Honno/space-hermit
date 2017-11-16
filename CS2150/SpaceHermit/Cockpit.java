package SpaceHermit;

public class Cockpit {
	private Chasis chasis;
	
	public Cockpit() {
		chasis = new Chasis();
	}
	
	public void draw() {
		chasis.draw();
	}
}
