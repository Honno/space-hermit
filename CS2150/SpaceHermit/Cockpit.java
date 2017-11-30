package SpaceHermit;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import GraphicsLab.FloatBuffer;
import GraphicsLab.Normal;
import GraphicsLab.Vertex;

public class Cockpit {
	/* declare lever animation variables */
	// current mode in animation, 'd' is the default mode
	private char mode = 'd';
	// the tick counter for the current animation mode
	private int tick = 0;
	// 'c' mode, how long it takes the lever to charge up
	private float chargeTickLimit = 100;
	// 'r' mode, how long it takes the lever to return to it's rest position
	private float restTickLimit = chargeTickLimit * 4;
	// how many times the red light flashes when the lever is charging
	private int amountOfFlashes = 10;

	/* declare chasis variables */
	// the y displacement of the whole cockpit
	private float displaceY = -12f;
	// front properties
	private float frontDist = -56f;
	private float frontWidth = 0.5f;
	private float frontWidth2 = 2 * frontWidth;
	private float frontHeight = 12f;
	// side bars properties
	private float bottomY = 0.0f;
	private float bottomXMod = 1.5f;
	// middle bars properties
	private float middleY = 16f;
	private float middleXMod = 1.25f;
	private float middleZMod = 0.75f;
	// top bars describing
	private float topY = 0f;
	private float topXMod = 1.5f;
	// floor properties
	private float floorY = -24f;
	private float floorHeight = 6f;
	private float floorZMod = 0.5f;
	// bar that bridges top bar properties
	private float middleFrontY = 20f;
	private float middleFrontHeight = 6f;
	private float middleFrontZMod = 0.75f;
	// control board properties
	private float controlMod = 0.75f;
	// lever base properties
	private float leverBaseHeight = 4f;
	private float leverBaseWidth = 2f;
	private float leverBaseDepth = 0.5f;
	private float leverBaseInwardMod = 0.75f;
	private float leverBaseMod = 0.85f;
	// lever positioning properties
	private float leverY = -frontWidth + displaceY;
	private float leverZMod = 0.5f;
	private float leverZMid = frontDist * leverBaseMod;
	private float leverRotationMod = 35.0f;
	private float leverZ;
	private float leverRotation;

	/* declare vertexes of cockpit */
	// nb: letter 'd' stands for an "in-depth" version of the vertex with the
	// same number, used to make everything look 3D

	/* front bar vertexes */
	// bottom left
	private Vertex v1 = new Vertex(-frontHeight, -frontWidth + displaceY,
			frontDist);
	// top left
	private Vertex v2 = new Vertex(-frontHeight, frontWidth + displaceY,
			frontDist);
	// top right
	private Vertex v3 = new Vertex(frontHeight, frontWidth + displaceY,
			frontDist);
	// bottom right
	private Vertex v4 = new Vertex(frontHeight, -frontWidth + displaceY,
			frontDist);

	private Vertex v2d = new Vertex(-frontHeight - frontWidth, frontWidth
			+ displaceY, frontDist - frontWidth2);
	private Vertex v3d = new Vertex(frontHeight + frontWidth, frontWidth
			+ displaceY, frontDist - frontWidth2);

	/* side bars vertexes */
	// calculate total x displacement of side bars
	private float bottomTotalX = bottomXMod * frontHeight;
	// calculate total y displacement of side bars
	private float bottomTotalY = displaceY - bottomY;

	// top left
	private Vertex v5 = new Vertex(-bottomTotalX, frontWidth + bottomTotalY, 0);
	// bottom left
	private Vertex v6 = new Vertex(-bottomTotalX, -frontWidth + bottomTotalY, 0);

	private Vertex v5d = new Vertex(-bottomTotalX - frontWidth2, frontWidth
			+ bottomTotalY, 0);

	// top right
	private Vertex v7 = new Vertex(bottomTotalX, frontWidth + bottomTotalY, 0);
	// bottom right
	private Vertex v8 = new Vertex(bottomTotalX, -frontWidth + bottomTotalY, 0);

	private Vertex v7d = new Vertex(bottomTotalX + frontWidth2, frontWidth
			+ bottomTotalY, 0);

	/* middle bars vertexes */
	// calculate total x displacement of middle bars
	private float middleTotalX = middleXMod * frontHeight;
	// calculate total y displacement of middle bars
	private float middleTotalY = frontWidth + displaceY + middleY;

	// calculate total z displacement of middle bars
	private float middleTotalZ = middleZMod * frontDist;

	// bottom left inward
	private Vertex v9 = new Vertex(-frontHeight + frontWidth2, frontWidth
			+ displaceY, frontDist);
	// top left inward
	private Vertex v10 = new Vertex(-middleTotalX + frontWidth2, middleTotalY,
			middleTotalZ);
	// top left outward
	private Vertex v11 = new Vertex(-middleTotalX, middleTotalY, middleTotalZ);

	private Vertex v9d = new Vertex(-frontHeight + frontWidth2, frontWidth
			+ displaceY, frontDist - frontWidth2);
	private Vertex v10d = new Vertex(-middleTotalX + frontWidth2, frontWidth
			+ middleTotalY, middleTotalZ - frontWidth2);

	// bottom right inward
	private Vertex v12 = new Vertex(frontHeight - frontWidth2, frontWidth
			+ displaceY, frontDist);
	// top right inward
	private Vertex v13 = new Vertex(middleTotalX - frontWidth2, middleTotalY,
			middleTotalZ);
	// top right outward
	private Vertex v14 = new Vertex(middleTotalX, middleTotalY, middleTotalZ);

	private Vertex v12d = new Vertex(frontHeight - frontWidth2, frontWidth
			+ displaceY, frontDist - frontWidth2);
	private Vertex v13d = new Vertex(middleTotalX - frontWidth2, frontWidth
			+ displaceY + middleY + frontWidth, middleTotalZ - frontWidth2);

	/* middle-top bars vertexes */
	// calculate total y displacement of middle bars
	private float middleTopTotalY = middleTotalY + frontWidth2;

	// left inward
	private Vertex v15 = new Vertex(-middleTotalX, middleTopTotalY,
			middleTotalZ);
	// left outward
	private Vertex v16 = new Vertex(-middleTotalX - frontWidth2,
			middleTopTotalY, middleTotalZ);

	private Vertex v15d = new Vertex(-middleTotalX, middleTopTotalY
			+ frontWidth, middleTotalZ - frontWidth);

	// right inward
	private Vertex v17 = new Vertex(middleTotalX, middleTopTotalY, middleTotalZ);
	// right outward
	private Vertex v18 = new Vertex(middleTotalX + frontWidth2,
			middleTopTotalY, middleTotalZ);

	private Vertex v17d = new Vertex(middleTotalX,
			middleTopTotalY + frontWidth, middleTotalZ - frontWidth);

	/* top bar vertexes */
	// calculate total x displacement of top bars
	private float topTotalX = topXMod * frontHeight;
	// calculate total y displacement of top bars
	private float topTotalY = middleTopTotalY + topY;

	// bottom left
	private Vertex v19 = new Vertex(-topTotalX, topTotalY, 0);
	// top left
	private Vertex v20 = new Vertex(-topTotalX + frontWidth2, topTotalY
			+ frontWidth2, 0);

	private Vertex v20d = new Vertex(-topTotalX + frontWidth2, topTotalY
			+ frontWidth2 * 2, 0);

	// bottom right
	private Vertex v21 = new Vertex(topTotalX, topTotalY, 0);
	// top right
	private Vertex v22 = new Vertex(topTotalX - frontWidth2, topTotalY
			+ frontWidth2, 0);

	private Vertex v22d = new Vertex(topTotalX - frontWidth2, topTotalY
			+ frontWidth2 * 2, 0);

	/* floor vertexes */
	// calculate total y displacement of floor
	private float floorTotalY = -frontWidth + displaceY + floorY;
	// calculate total z displacement of floor
	private float floorTotalZ = floorZMod * frontDist;
	
	// left
	private Vertex v23 = new Vertex(-floorHeight, floorTotalY, floorTotalZ);
	// right
	private Vertex v24 = new Vertex(floorHeight, floorTotalY, floorTotalZ);

	/* middle-front bar vertexes */
	// calculate total y displacement of the middle-front bar
	private float middleFrontTotalY = displaceY + middleFrontY;
	// calculate total z displacement of the middle-front bar
	private float middleFrontTotalZ = middleFrontZMod * frontDist;

	// bottom left
	private Vertex v25 = new Vertex(-middleFrontHeight, middleFrontTotalY
			- frontWidth, middleFrontTotalZ);
	// top left
	private Vertex v26 = new Vertex(-middleFrontHeight, middleFrontTotalY
			+ frontWidth, middleFrontTotalZ + frontWidth);
	// top right
	private Vertex v27 = new Vertex(middleFrontHeight, middleFrontTotalY
			+ frontWidth, middleFrontTotalZ + frontWidth);
	// bottom right
	private Vertex v28 = new Vertex(middleFrontHeight, middleFrontTotalY
			- frontWidth, middleFrontTotalZ);

	private Vertex v26d = new Vertex(-middleFrontHeight, middleFrontTotalY
			+ frontWidth2, middleFrontTotalZ + frontWidth);
	private Vertex v27d = new Vertex(middleFrontHeight, middleFrontTotalY
			+ frontWidth2, middleFrontTotalZ + frontWidth);

	/* control board vertexes */
	// calculate total x displacement of control board
	private float controlTotalX = bottomXMod * frontHeight * controlMod;
	// calculate total y displacement of the control board
	private float controlTotalY = -frontWidth + displaceY - bottomY
			* controlMod;
	// calculate total z displacement of control board
	private float controlTotalZ = controlMod * frontDist;

	// top left
	private Vertex v29 = new Vertex(-controlTotalX, controlTotalY,
			controlTotalZ);
	// bottom left
	private Vertex v30 = new Vertex(-controlTotalX,
			controlTotalY - frontWidth2 * 2, controlTotalZ);

	// top right
	private Vertex v31 = new Vertex(controlTotalX, controlTotalY, frontDist
			* controlMod);
	// bottom right
	private Vertex v32 = new Vertex(controlTotalX, controlTotalY - frontWidth2 * 2,
			controlTotalZ);

	/* lever base bottom vertexes */
	// calculate total y displacement of the control board
	private float leverBaseTotalY = -frontWidth + displaceY - bottomY
			* leverBaseMod;
	// calculate total z displacement of control board
	private float leverBaseTotalZ = leverBaseMod * frontDist;

	// bottom left
	private Vertex v33 = new Vertex(-leverBaseWidth, leverBaseTotalY,
			leverBaseTotalZ + leverBaseHeight);
	// top left
	private Vertex v34 = new Vertex(-leverBaseWidth, leverBaseTotalY,
			leverBaseTotalZ - leverBaseHeight);
	// top right
	private Vertex v35 = new Vertex(leverBaseWidth, leverBaseTotalY,
			leverBaseTotalZ - leverBaseHeight);
	// bottom right
	private Vertex v36 = new Vertex(leverBaseWidth, leverBaseTotalY,
			leverBaseTotalZ + leverBaseHeight);

	/* lever base top vertexes */
	// calculate total x displacement for top of lever base
	private float leverBaseTopTotalX = leverBaseWidth * leverBaseInwardMod;
	// calculate total y displacement of the control board
	private float leverBaseTopTotalY = leverBaseTotalY + leverBaseDepth;
	
	// bottom left
	private Vertex v37 = new Vertex(-leverBaseTopTotalX, leverBaseTopTotalY,
			leverBaseTotalZ + leverBaseHeight);
	// top left
	private Vertex v38 = new Vertex(-leverBaseTopTotalX, leverBaseTopTotalY,
			leverBaseTotalZ - leverBaseHeight);
	// top right
	private Vertex v39 = new Vertex(leverBaseTopTotalX, leverBaseTopTotalY,
			leverBaseTotalZ - leverBaseHeight);
	// bottom right
	private Vertex v40 = new Vertex(leverBaseTopTotalX, leverBaseTopTotalY,
			leverBaseTotalZ + leverBaseHeight);

	/**
	 * Construct cockpit with default values for lever properties, and modify tick limits to adjust with the animation scale.
	 * @param animationScale the animation scale desired by the instantiating class
	 */
	public Cockpit(float animationScale) {
		/* set default values for lever position */
		leverZ = leverZMid + leverZMod;
		leverRotation = leverRotationMod;
		/* modify tick limits with animation scale */
		chargeTickLimit = chargeTickLimit * animationScale;
		restTickLimit = restTickLimit * animationScale;
	}

	public float getFrontDist() {
		return frontDist;
	}

	public float getDisplaceY() {
		return displaceY;
	}

	/**
	 * If in the default animation mode, check whether user has press the space bar to active the lever charge and subsequently the warp protocol.
	 */
	protected void checkSceneInput() {
		if (mode == 'd') {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				mode = 'c';
			}
		}
	}

	/**
	 * @return boolean value that tells the instantiating class that the warp protocol has been activated.
	 */
	protected boolean updateScene() {
		// checks what modes are active if any
		switch (mode) {
		case 'c': // lever charging
			
			if (tick > chargeTickLimit) {
				// change mode to lever reset
				mode = 'r';
				tickReset();
				// tell initiating class that warp protocol has been activated
				return true;
			} else {
				// animate lever
				animLever(chargeTickLimit, 0);
				tick++;
			}
			break;
		case 'r': // lever reset
			if (tick > restTickLimit) {
				// change mode to default
				mode = 'd';
				tickReset();
			} else {
				// animate lever
				animLever(restTickLimit, 1);
				tick++;
			}
			break;
		}
		// tell initiating class that warp protocol has not been activated
		return false;
	}

	public void renderScene() {
		/* reset positioning */
		GL11.glTranslatef(0.0f, 0.0f, 0.0f);
		GL11.glScalef(1.0f, 1.0f, 1.0f);
		GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);

		/* draw static objects */
		drawFrame();
		drawFloor();
		drawControlBoard();
		drawLeverBase();
		
		/* translate, rotate and draw lever */
		GL11.glTranslatef(0, leverY, leverZ);
		GL11.glRotatef(leverRotation, 1.0f, 0.0f, 0.0f);
		drawLever();
	}

	/**
	 * Draw the cockpit's frame.
	 */
	private void drawFrame() {
		/* set material properties */
		float shininess = 1.0f;
		float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] colour = { 0.75f, 0.75f, 0.75f, 1.0f };

		Util.material(shininess, specular, colour);

		/* draw everything */
		// draw front
		Util.drawRect(v4, v3, v2, v1);
		Util.drawRect(v3d, v2d, v2, v3);

		// draw bottom left
		Util.drawRect(v6, v1, v2, v5);
		Util.drawRect(v5d, v5, v2, v2d);
		// draw bottom right
		Util.drawRect(v8, v7, v3, v4);
		Util.drawRect(v7d, v3d, v3, v7);
		
		// draw middle left
		Util.drawRect(v11, v2, v9, v10);
		Util.drawRect(v10d, v10, v9, v9d);
		// draw middle right
		Util.drawRect(v14, v13, v12, v3);
		Util.drawRect(v13d, v12d, v12, v13);

		// draw middle-top left
		Util.drawRect(v16, v11, v10, v15);
		// draw middle-top right
		Util.drawRect(v18, v17, v13, v14); 

		// draw top left side
		Util.drawRect(v20, v19, v16, v15); 
		Util.drawRect(v20d, v20, v15, v15d);
		// draw top right side
		Util.drawRect(v22, v17, v18, v21); 
		Util.drawRect(v22d, v17d, v17, v22);

		// draw middle front
		Util.drawRect(v28, v27, v26, v25); 
		Util.drawRect(v27d, v26d, v26, v27);
		// draw middle front left
		Util.drawRect(v26, v15, v10, v25); 
		Util.drawRect(v26d, v15d, v15, v26);
		// draw middle front right
		Util.drawRect(v28, v13, v17, v27); 
		Util.drawRect(v27d, v27, v17, v17d);
	}

	/**
	 * Draw the cockpit's floor.
	 */
	public void drawFloor() {
		/* set material properties */
		float shininess = 0.0f;
		float[] specular = { 0.5f, 0.0f, 0.0f, 1.0f };
		float[] colour = { 0.625f, 0.75f, 0.625f, 1.0f };

		Util.material(shininess, specular, colour);

		/* draw everything */
		// draw floor front
		Util.drawRect(v24, v4, v1, v23);
		// draw floor left
		Util.drawTri(v23, v1, v6);
		// draw floor right
		Util.drawTri(v24, v8, v4); 
	}

	/**
	 * Draw the control board.
	 */
	public void drawControlBoard() {
		/* set material properties */
		float shininess = 1.0f;
		float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] colour = { 0.75f, 0.75f, 0.75f, 1.0f };

		Util.material(shininess, specular, colour);

		/* draw everything */
		// draw top
		Util.drawRect(v31, v4, v1, v29);
		// draw front
		Util.drawRect(v32, v31, v29, v30); 
	}

	/**
	 * Draw the base of the lever.
	 */
	public void drawLeverBase() {
		/* set material properties */
		float shininess = 0.0f;
		float[] specular = { 0.125f, 0.125f, 0.125f, 1.0f };
		float[] colour = { 0.3125f, 0.3125f, 0.3125f, 1.0f };

		Util.material(shininess, specular, colour);

		/* draw everything */
		// draw bottom
		Util.drawRect(v36, v35, v34, v33);
		// draw top
		Util.drawRect(v40, v39, v38, v37);
		// draw front
		Util.drawRect(v40, v37, v33, v36);
		// draw left
		Util.drawRect(v38, v34, v33, v37);
		// draw right
		Util.drawRect(v40, v36, v35, v39); 
	}

	/**
	 * Draw the lever.
	 */
	public void drawLever() {
		/* set material properties */
		float shininess = 0.0f;
		float[] specular = { 0.5f, 0.0f, 0.0f, 1.0f };
		float[] colour = { 1.0f, 0.0f, 0.0f, 1.0f };

		Util.material(shininess, specular, colour);
		
		/* declare lever properties */
		float leverHeight = 0.1f * leverBaseHeight;
		float leverDepth = 8 * leverHeight;
		float leverExtendX = 2 * leverHeight;
		
		/* declare lever vertexes */
		// bottom vertexes
		// left
		Vertex vb1 = new Vertex(-leverHeight, 0, leverHeight);
		//  right
		Vertex vb2 = new Vertex(leverHeight, 0, leverHeight); 

		Vertex vb1d = new Vertex(-leverHeight, 0, -leverHeight);
		Vertex vb2d = new Vertex(leverHeight, 0, -leverHeight);
		
		// middle vertexes
		// left
		Vertex vb3 = new Vertex(-leverHeight, leverDepth, leverHeight);
		// right
		Vertex vb4 = new Vertex(leverHeight, leverDepth, leverHeight);

		Vertex vb3d = new Vertex(-leverHeight, leverDepth, -leverHeight);
		Vertex vb4d = new Vertex(leverHeight, leverDepth, -leverHeight);
		
		// top vertexes
		// bottom left
		Vertex vb5 = new Vertex(-leverHeight - leverExtendX, leverDepth,
				leverHeight);
		// top left
		Vertex vb6 = new Vertex(-leverHeight - leverExtendX, leverDepth
				+ leverHeight * 2, leverHeight); 
		// top right
		Vertex vb7 = new Vertex(leverHeight + leverExtendX, leverDepth
				+ leverHeight * 2, leverHeight);
		// bottom right
		Vertex vb8 = new Vertex(leverHeight + leverExtendX, leverDepth,
				leverHeight);

		Vertex vb5d = new Vertex(-leverHeight - leverExtendX,
				leverDepth, leverHeight - leverHeight * 2);
		Vertex vb6d = new Vertex(-leverHeight - leverExtendX, leverDepth
				+ leverHeight, leverHeight - leverHeight * 2);
		Vertex vb7d = new Vertex(leverHeight + leverExtendX, leverDepth
				+ leverHeight, leverHeight - leverHeight * 2);
		Vertex vb8d = new Vertex(leverHeight + leverExtendX, leverDepth,
				leverHeight - leverHeight * 2);

		/* draw everything */
		// draw sides
		// bottom left
		Util.drawRect(vb3d, vb1d, vb1, vb3);
		// top left
		Util.drawRect(vb6d, vb5d, vb5, vb6);
		// bottom right
		Util.drawRect(vb4d, vb4, vb2, vb2d);
		// top right
		Util.drawRect(vb8d, vb7d, vb7, vb8);
		
		// draw middle bottom sides
		// left
		Util.drawRect(vb5d, vb3d, vb3, vb5);
		// right
		Util.drawRect(vb8d, vb8, vb4, vb4d);
		
		// draw front
		GL11.glBegin(GL11.GL_POLYGON);
		new Normal(vb6.toVector(), vb1.toVector(), vb2.toVector(),
				vb7.toVector()).submit();
		vb4.submit();
		vb8.submit();
		vb7.submit();
		vb6.submit();
		vb5.submit();
		vb3.submit();
		vb1.submit();
		vb2.submit();
		GL11.glEnd();
		
		// draw top side
		Util.drawRect(vb7d, vb6d, vb6, vb7);
	}

	/**
	 * Change z position and rotation of lever depending on the tick to current tick limit ratio.
	 * 
	 * @param tickLimit the current tick limit
	 * @param mode whether increasing (mode = 0) or decreasing (mode = 1) values
	 */
	public void animLever(float tickLimit, int mode) {
		// use a cosine function to simulate natural movement of how a human would push a lever
		double halfRadians = (double) (tick / tickLimit + mode) * Math.PI;
		float mod = (float) Math.cos(halfRadians);
		// adjust z position and rotation depending on found modification value
		leverZ = leverZMid + mod * leverZMod;
		leverRotation = mod * leverRotationMod;
	}

	/**
	 * Reset the tick count to 0.
	 */
	public void tickReset() {
		tick = 0;
	}
}
