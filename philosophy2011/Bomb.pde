public class Bomb extends GameObject {

	int lifespan = 15;
	PImage tfl;

	Bomb(int x, int y) {
		super(x, y, 3, 3);
		tfl = loadImage("tfl.png");
		for(int i = 0; i < objectHeight; i++) {
			for(int j = 0; j < objectWidth; j++) {
				scene.get( sizeX * (posY - 1 + i) + (posX - 1 + j) ).toggleOccupied();
			}
		}
	}

	void display() {
		if(time < lifespan) {
			image(tfl, 
				margin + (posX - 1) * fWidth,
				margin + (posY - 1) * fHeight
			);
		} else {
			for(int i = 0; i < objectHeight; i++) {
				for(int j = 0; j < objectWidth; j++) {
					scene.get( sizeX * (posY - 1 + i) + (posX - 1 + j) ).toggleOccupied();
				}
			}
			objects.add(new Explosion(posX, posY));
			objects.remove(this);
		}
	}

}