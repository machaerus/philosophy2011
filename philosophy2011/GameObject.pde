public abstract class GameObject {
	
	int posX;
	int posY;
	int objectWidth;
	int objectHeight;
	int time;

	GameObject(int x, int y, int w, int h) {
		posX = x;
		posY = y;
		objectWidth = w;
		objectHeight = h;
		time = 0;
	}

	void display() {
		fill(50);
		noStroke();
		rect(
			margin + (posX - 1) * fWidth,
			margin + (posY - 1) * fHeight,
			fWidth * objectWidth,
			fHeight * objectHeight );
	}

	void update() {
		time++;
	}

}