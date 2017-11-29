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
	
	/* declare chasis variables */
	// the y displacement of the whole cockpit
	private float displaceY = 12f;
	// front properties
	private float frontDist = 64f;
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
	private float floorY = 20f;
	private float floorHeight = 6f;
	private float floorZMod = 0.5f;
	// bar that bridges top bar properties
	private float middleFrontY = 20f;
	private float middleFrontHeight = 6f;
	private float middleFrontZMod = 0.75f;
	// control board properties
	private float controlMod = 0.75f;
	// button base properties
	private float buttonBaseHeight = 4f;
	private float buttonBaseWidth = 2f;
	private float buttonBaseDepth = 0.5f;
	private float buttonBaseInwardMod = 0.75f;
	private float buttonBaseMod = 0.85f;
	// button properties
	private float buttonHeight = 0.1f * buttonBaseHeight;
	private float buttonDepth = 8 * buttonHeight;
	private float buttonExtendX = 2 * buttonHeight;
	private float buttonY = -frontWidth - displaceY;
	private float buttonZMod = 0.5f;
	private float buttonZMid = -frontDist * buttonBaseMod;
	private float buttonRotationMod = 30.0f;
	private float buttonZ;
	private float buttonRotation;

	/* declare vertexes of cockpit 
	 * letter 'd' stands for a literal "in-depth" version of a vertex, used to make everything look 3D
	 * */
	/* front bar vertexes */
	// bottom left
	private Vertex v1 = new Vertex(-frontHeight, -frontWidth - displaceY,
			-frontDist); 
	// top left
	private Vertex v2 = new Vertex(-frontHeight, frontWidth - displaceY,
			-frontDist); 
	// top right
	private Vertex v3 = new Vertex(frontHeight, frontWidth - displaceY,
			-frontDist); 
	// bottom right
	private Vertex v4 = new Vertex(frontHeight, -frontWidth - displaceY,
			-frontDist); 

	private Vertex v2d = new Vertex(-frontHeight - frontWidth, frontWidth
			- displaceY, -frontDist - frontWidth2);
	private Vertex v3d = new Vertex(frontHeight + frontWidth, frontWidth
			- displaceY, -frontDist - frontWidth2);

	/* side bars vertexes */
	// calculate total displace of side bars
	private float bottomTotalY = displaceY + bottomY;
	
	// top left
	private Vertex v5 = new Vertex(-bottomXMod * frontHeight, frontWidth -
			bottomTotalY, 0);
	// bottom left
	private Vertex v6 = new Vertex(-bottomXMod * frontHeight, -frontWidth -
			bottomTotalY, 0);

	private Vertex v5d = new Vertex(-bottomXMod * frontHeight - frontWidth2,
			frontWidth - bottomTotalY, 0);

	// top right
	private Vertex v7 = new Vertex(bottomXMod * frontHeight, frontWidth
			- bottomTotalY, 0);
	// bottom right
	private Vertex v8 = new Vertex(bottomXMod * frontHeight, -frontWidth
			- bottomTotalY, 0);

	private Vertex v7d = new Vertex(bottomXMod * frontHeight + frontWidth2,
			frontWidth - bottomTotalY, 0);

	/* middle bars vertexes */
	private float middleTotalY = frontWidth - displaceY + middleY;
	
	// bottom left inward
	private Vertex v9 = new Vertex(-frontHeight + frontWidth2, frontWidth
			- displaceY, -frontDist);
	// top left inward
	private Vertex v10 = new Vertex(-middleXMod * frontHeight + frontWidth2,
			middleTotalY, -middleZMod * frontDist);
	// top left outward
	private Vertex v11 = new Vertex(-middleXMod * frontHeight, middleTotalY, -middleZMod * frontDist);

	private Vertex v9d = new Vertex(-frontHeight + frontWidth2, frontWidth
			- displaceY, -frontDist - frontWidth2);
	private Vertex v10d = new Vertex(-middleXMod * frontHeight + frontWidth2,
			frontWidth + middleTotalY, -middleZMod
					* frontDist - frontWidth2);

	// bottom right inward
	private Vertex v12 = new Vertex(frontHeight - frontWidth2, frontWidth
			- displaceY, -frontDist);
	// top right inward
	private Vertex v13 = new Vertex(middleXMod * frontHeight - frontWidth2,
			middleTotalY, -middleZMod * frontDist);
	// top left inward
	private Vertex v14 = new Vertex(middleXMod * frontHeight, frontWidth
			+ middleTotalY	, -middleZMod * frontDist);

	private Vertex v12d = new Vertex(frontHeight - frontWidth2, frontWidth
			- displaceY, -frontDist - frontWidth2);
	private Vertex v13d = new Vertex(middleXMod * frontHeight - frontWidth2,
			frontWidth - displaceY + middleY + frontWidth, -middleZMod
					* frontDist - frontWidth2);

	// middle-top
	private Vertex v15 = new Vertex(-middleXMod * frontHeight, frontWidth
			- displaceY + middleY + frontWidth2, -middleZMod * frontDist); // inward
	private Vertex v16 = new Vertex(-middleXMod * frontHeight - frontWidth2,
			frontWidth - displaceY + middleY + frontWidth2, -middleZMod
					* frontDist); // outward

	private Vertex v15d = new Vertex(-middleXMod * frontHeight, frontWidth
			- displaceY + middleY + frontWidth2 + frontWidth, -middleZMod
			* frontDist + frontWidth);

	private Vertex v17 = new Vertex(middleXMod * frontHeight, frontWidth
			- displaceY + middleY + frontWidth2, -middleZMod * frontDist); // inward
	private Vertex v18 = new Vertex(middleXMod * frontHeight + frontWidth2,
			frontWidth - displaceY + middleY + frontWidth2, -middleZMod
					* frontDist); // outward

	private Vertex v17d = new Vertex(middleXMod * frontHeight, frontWidth
			- displaceY + middleY + frontWidth2 + frontWidth, -middleZMod
			* frontDist + frontWidth);

	// top
	private Vertex v19 = new Vertex(-topXMod * frontHeight, frontWidth
			- displaceY + middleY + topY, 0);
	private Vertex v20 = new Vertex(-topXMod * frontHeight + frontWidth2,
			frontWidth - displaceY + middleY + topY + frontWidth2, 0);

	private Vertex v20d = new Vertex(
			-topXMod * frontHeight + frontWidth2,
			frontWidth - displaceY + middleY + topY + frontWidth2 + frontWidth2,
			0);

	private Vertex v21 = new Vertex(topXMod * frontHeight, frontWidth
			- displaceY + middleY + topY, 0);
	private Vertex v22 = new Vertex(topXMod * frontHeight - frontWidth2,
			frontWidth - displaceY + middleY + topY + frontWidth2, 0);

	private Vertex v22d = new Vertex(
			topXMod * frontHeight - frontWidth2,
			frontWidth - displaceY + middleY + topY + frontWidth2 + frontWidth2,
			0);

	// floor
	private Vertex v23 = new Vertex(-floorHeight, -frontWidth - displaceY
			- floorY, -floorZMod * frontDist);
	private Vertex v24 = new Vertex(floorHeight, -frontWidth - displaceY
			- floorY, -floorZMod * frontDist);

	// middle-front
	private Vertex v25 = new Vertex(-middleFrontHeight, -frontWidth - displaceY
			+ middleFrontY, -middleFrontZMod * frontDist); // bottom left
	private Vertex v26 = new Vertex(-middleFrontHeight, frontWidth - displaceY
			+ middleFrontY, -middleFrontZMod * frontDist + frontWidth); // top
																		// left
	private Vertex v27 = new Vertex(middleFrontHeight, frontWidth - displaceY
			+ middleFrontY, -middleFrontZMod * frontDist + frontWidth); // top
																		// right
	private Vertex v28 = new Vertex(middleFrontHeight, -frontWidth - displaceY
			+ middleFrontY, -middleFrontZMod * frontDist); // bottom right

	private Vertex v26d = new Vertex(-middleFrontHeight, frontWidth - displaceY
			+ middleFrontY + frontWidth, -middleFrontZMod * frontDist
			+ frontWidth2 - frontWidth);
	private Vertex v27d = new Vertex(middleFrontHeight, frontWidth - displaceY
			+ middleFrontY + frontWidth, -middleFrontZMod * frontDist
			+ frontWidth2 - frontWidth);

	// control board
	private Vertex v29 = new Vertex(-bottomXMod * frontHeight * controlMod,
			-frontWidth - displaceY - bottomY * controlMod, -frontDist
					* controlMod); // top left
	private Vertex v30 = new Vertex(-bottomXMod * frontHeight * controlMod,
			-frontWidth - displaceY - bottomY * controlMod - frontWidth2 * 2f,
			-frontDist * controlMod); // bottom left

	private Vertex v31 = new Vertex(bottomXMod * frontHeight * controlMod,
			-frontWidth - displaceY - bottomY * controlMod, -frontDist
					* controlMod); // top right
	private Vertex v32 = new Vertex(bottomXMod * frontHeight * controlMod,
			-frontWidth - displaceY - bottomY * controlMod - frontWidth2 * 2f,
			-frontDist * controlMod); // bottom right

	// button base
	private Vertex v33 = new Vertex(-buttonBaseWidth, -frontWidth - displaceY
			- bottomY * buttonBaseMod, -frontDist * buttonBaseMod
			+ buttonBaseHeight); // bottom left
	private Vertex v34 = new Vertex(-buttonBaseWidth, -frontWidth - displaceY
			- bottomY * buttonBaseMod, -frontDist * buttonBaseMod
			- buttonBaseHeight); // top left
	private Vertex v35 = new Vertex(buttonBaseWidth, -frontWidth - displaceY
			- bottomY * buttonBaseMod, -frontDist * buttonBaseMod
			- buttonBaseHeight); // top right
	private Vertex v36 = new Vertex(buttonBaseWidth, -frontWidth - displaceY
			- bottomY * buttonBaseMod, -frontDist * buttonBaseMod
			+ buttonBaseHeight); // bottom right

	private Vertex v37 = new Vertex(
			-buttonBaseWidth * buttonBaseInwardMod,
			-frontWidth - displaceY - bottomY * buttonBaseMod + buttonBaseDepth,
			-frontDist * buttonBaseMod + buttonBaseHeight); // bottom left
	private Vertex v38 = new Vertex(
			-buttonBaseWidth * buttonBaseInwardMod,
			-frontWidth - displaceY - bottomY * buttonBaseMod + buttonBaseDepth,
			-frontDist * buttonBaseMod - buttonBaseHeight); // top left
	private Vertex v39 = new Vertex(
			buttonBaseWidth * buttonBaseInwardMod,
			-frontWidth - displaceY - bottomY * buttonBaseMod + buttonBaseDepth,
			-frontDist * buttonBaseMod - buttonBaseHeight); // top right
	private Vertex v40 = new Vertex(
			buttonBaseWidth * buttonBaseInwardMod,
			-frontWidth - displaceY - bottomY * buttonBaseMod + buttonBaseDepth,
			-frontDist * buttonBaseMod + buttonBaseHeight); // bottom right

	// button
	private Vertex vb1 = new Vertex(-buttonHeight, 0, buttonHeight); // left
	private Vertex vb2 = new Vertex(buttonHeight, 0, buttonHeight); // right

	private Vertex vb3 = new Vertex(-buttonHeight, buttonDepth, buttonHeight); // left
	private Vertex vb4 = new Vertex(buttonHeight, buttonDepth, buttonHeight); // right

	private Vertex vb5 = new Vertex(-buttonHeight - buttonExtendX, buttonDepth,
			buttonHeight); // bottom left
	private Vertex vb6 = new Vertex(-buttonHeight - buttonExtendX, buttonDepth
			+ buttonHeight * 2, buttonHeight); // top left
	private Vertex vb7 = new Vertex(buttonHeight + buttonExtendX, buttonDepth
			+ buttonHeight * 2, buttonHeight); // top right
	private Vertex vb8 = new Vertex(buttonHeight + buttonExtendX, buttonDepth,
			buttonHeight); // bottom right

	private Vertex vb5d = new Vertex(-buttonHeight - buttonExtendX,
			buttonDepth, buttonHeight - buttonHeight * 2); // bottom left in
	private Vertex vb6d = new Vertex(-buttonHeight - buttonExtendX, buttonDepth
			+ buttonHeight, buttonHeight - buttonHeight * 2); // top left in
	private Vertex vb7d = new Vertex(buttonHeight + buttonExtendX, buttonDepth
			+ buttonHeight, buttonHeight - buttonHeight * 2); // top right in
	private Vertex vb8d = new Vertex(buttonHeight + buttonExtendX, buttonDepth,
			buttonHeight - buttonHeight * 2); // bottom right

	public Cockpit(float animationScale) {
		buttonZ = buttonZMid + buttonZMod;
		buttonRotation = 30.0f;

		chargeTickLimit = chargeTickLimit * animationScale;
		restTickLimit = restTickLimit * animationScale;
	}

	public float getFronDist() {
		return frontDist;
	}

	public float getDisplaceY() {
		return displaceY;
	}

	protected void checkSceneInput() {
		if (mode == 'd') {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				mode = 'c';
			}
		}
	}

	protected boolean updateScene() {
		if(mode == 'c') {
			animButton(cos((float) tick / chargeTickLimit));
			if (tick > chargeTickLimit) {
				mode = 'r';
				tickReset();
				return true;
			} else {
				tick++;
			}
			
		} else if(mode == 'r') {
				animButton(cos((float) tick / restTickLimit + 1));
				if (tick > restTickLimit) {
					mode = 'd';
					tickReset();
				} else {
					tick++;
				}
		}
		return false;
	}

	public void renderScene() {
		/** reset positioning **/
		GL11.glTranslatef(0.0f, 0.0f, 0.0f);
		GL11.glScalef(1.0f, 1.0f, 1.0f);
		GL11.glRotatef(0.0f, 0.0f, 0.0f, 0.0f);

		/** draw static objects **/
		drawFrame();
		drawFloor();
		drawControlBoard();
		drawButtonBase();

		GL11.glTranslatef(0, buttonY, buttonZ);
		GL11.glRotatef(buttonRotation, 1.0f, 0.0f, 0.0f);
		drawButton();
	}

	private void drawFrame() {
		/** set material properties **/
		float shininess = 1.0f;
		float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] colour = { 0.75f, 0.75f, 0.75f, 1.0f };

		Util.material(shininess, specular, colour);

		/** draw everything **/

		Util.drawRect(v4, v3, v2, v1); // draw front
		Util.drawRect(v3d, v2d, v2, v3);

		Util.drawRect(v6, v1, v2, v5); // draw bottom left
		Util.drawRect(v5d, v5, v2, v2d);
		Util.drawRect(v8, v7, v3, v4); // draw bottom right
		Util.drawRect(v7d, v3d, v3, v7);

		Util.drawRect(v11, v2, v9, v10); // draw middle left
		Util.drawRect(v10d, v10, v9, v9d);
		Util.drawRect(v14, v13, v12, v3); // draw middle right
		Util.drawRect(v13d, v12d, v12, v13);

		Util.drawRect(v16, v11, v10, v15); // draw middle-top left
		Util.drawRect(v18, v17, v13, v14); // draw middle-top right

		Util.drawRect(v20, v19, v16, v15); // draw top left side
		Util.drawRect(v20d, v20, v15, v15d);
		Util.drawRect(v22, v17, v18, v21); // draw top right side
		Util.drawRect(v22d, v17d, v17, v22);

		Util.drawRect(v28, v27, v26, v25); // draw middle front
		Util.drawRect(v27d, v26d, v26, v27);
		Util.drawRect(v26, v15, v10, v25); // draw middle front left
		Util.drawRect(v26d, v15d, v15, v26);
		Util.drawRect(v28, v13, v17, v27); // draw middle front right
		Util.drawRect(v27d, v27, v17, v17d);
	}

	public void drawFloor() {
		/** set material properties **/
		float shininess = 0.0f;
		float[] specular = { 0.5f, 0.0f, 0.0f, 1.0f };
		float[] colour = { 0.625f, 0.75f, 0.625f, 1.0f };

		Util.material(shininess, specular, colour);

		/** draw everything **/

		Util.drawRect(v24, v4, v1, v23); // draw floor front
		Util.drawTri(v23, v1, v6); // draw floor left
		Util.drawTri(v24, v8, v4); // draw floor right
	}

	public void drawControlBoard() {
		/** set material properties **/
		float shininess = 1.0f;
		float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] colour = { 0.75f, 0.75f, 0.75f, 1.0f };

		Util.material(shininess, specular, colour);

		/** draw everything **/
		Util.drawRect(v31, v4, v1, v29); // draw top
		Util.drawRect(v32, v31, v29, v30); // draw front
	}

	public void drawButtonBase() {
		/** set material properties **/
		float shininess = 0.0f;
		float[] specular = { 0.125f, 0.125f, 0.125f, 1.0f };
		float[] colour = { 0.3125f, 0.3125f, 0.3125f, 1.0f };

		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shininess);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR,
				FloatBuffer.wrap(specular));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE,
				FloatBuffer.wrap(colour));

		/** draw everything **/
		Util.drawRect(v36, v35, v34, v33); // draw bottom
		Util.drawRect(v40, v39, v38, v37); // draw top
		Util.drawRect(v40, v37, v33, v36); // draw front
		Util.drawRect(v38, v34, v33, v37); // draw left
		Util.drawRect(v40, v36, v35, v39); // draw right
	}

	public void drawButton() {
		/** set material properties **/
		float shininess = 0.0f;
		float[] specular = { 0.5f, 0.0f, 0.0f, 1.0f };
		float[] colour = { 1.0f, 0.0f, 0.0f, 1.0f };

		Util.material(shininess, specular, colour);

		/** draw everything **/
		// draw top
		Util.drawRect(vb7d, vb6d, vb6, vb7);

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
	}

	public void animButton(float mod) {
		buttonZ = buttonZMid + mod * buttonZMod;
		buttonRotation = mod * buttonRotationMod;
	}

	public float cos(float ratio) {
		double radians = ratio * Math.PI;
		return (float) Math.cos(radians);
	}

	public void tickReset() {
		tick = 0;
	}
}
