public class Explosion extends GameObject {
	
	int lifespan = 10;
	int power;

	Explosion(int x, int y) {
		super(x, y, 3, 3);
		power = 5;

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				scene.get(sizeX*(y - 1 + i) + x - 1 + j).makeAblaze();
			}
		}

		for(int d = 0; d < 4; d++) {	// dla każdego kierunku
			for(int i = 1; i <= power; i++) {	// na odległość taką, jaką wyznacza moc wybuchu
				if(d == 0) {
					x = posX;
					y = posY - i;
				} else if(d == 1) {
					x = posX + i;
					y = posY;
				} else if(d == 2) {
					x = posX;
					y = posY + i;
				} else if(d == 3) {
					x = posX - i;
					y = posY;
				}
				if(checkOccupied(d, x, y)) {
					if(d == 0) {
						scene.get(sizeX*(y - 1) + x - 1).makeAblaze();
						scene.get(sizeX*(y - 1) + x).makeAblaze();
						scene.get(sizeX*(y - 1) + x + 1).makeAblaze();
					} else if(d == 1) {
						scene.get(sizeX*(y - 1) + x + 1).makeAblaze();
						scene.get(sizeX*y + x + 1).makeAblaze();
						scene.get(sizeX*(y + 1) + x + 1).makeAblaze();
					} else if(d == 2) {
						scene.get(sizeX*(y + 1) + x - 1).makeAblaze();
						scene.get(sizeX*(y + 1) + x).makeAblaze();
						scene.get(sizeX*(y + 1) + x + 1).makeAblaze();
					} else if(d == 3) {
						scene.get(sizeX*(y - 1) + x - 1).makeAblaze();
						scene.get(sizeX*y + x - 1).makeAblaze();
						scene.get(sizeX*(y + 1) + x - 1).makeAblaze();
					}
				} else break;
			}
		}
	}

	boolean checkOccupied(int dir, int x, int y) {
		if ( dir == 1 ) {  // right
			if ( 
				(x < sizeX - 2)
				&& !scene.get(sizeX*(y - 1) + x + 2).isOccupied()
				&& !scene.get(sizeX*y + x + 2).isOccupied()
				&& !scene.get(sizeX*(y + 1) + x + 2).isOccupied()  
			) {
				return true;
			} else {
				return false;
			}
		} else if ( dir == 3 ) {  // left
		  	if ( 
				(x > 1)
				&& !scene.get(sizeX*(y - 1) + x - 2).isOccupied() 
				&& !scene.get(sizeX*y + x - 2).isOccupied() 
				&& !scene.get(sizeX*(y + 1) + x - 2).isOccupied() 
		  	) {
				return true;
		  	} else {
				return false;
		  	}
		} else if ( dir == 0 ) {  // up
		  	if ( 
				(y > 1)
				&& !scene.get(sizeX*(y - 2) + x - 1).isOccupied() 
				&& !scene.get(sizeX*(y - 2) + x).isOccupied()
				&& !scene.get(sizeX*(y - 2) + x + 1).isOccupied()
		  	) {
				return true;
		  	} else {
				return false;
		  	}
		} else if ( dir == 2 ) {  // down
		  	if ( 
				(y < sizeY - 2)
				&& !scene.get(sizeX*(y + 2) + x - 1).isOccupied() 
				&& !scene.get(sizeX*(y + 2) + x).isOccupied()
				&& !scene.get(sizeX*(y + 2) + x + 1).isOccupied()
		  	) {
				return true;
		  	} else {
				return false;
		  	}
		} else return false;
	}

	void display() {
		if(time < lifespan) {
			fill(255-time*18,0,0);
			noStroke(); 

			// środek
			rect(
				margin + (posX - 1) * fWidth,
				margin + (posY - 1) * fHeight,
				fWidth * 3,
				fHeight * 3
			);

			int x = posX, y = posY;
			for(int d = 0; d < 4; d++) {	// dla każdego kierunku
				for(int i = 1; i <= power; i++) {	// na odległość taką, jaką wyznacza moc wybuchu
					if(d == 0) {
						x = posX;
						y = posY - i;
					} else if(d == 1) {
						x = posX + i;
						y = posY;
					} else if(d == 2) {
						x = posX;
						y = posY + i;
					} else if(d == 3) {
						x = posX - i;
						y = posY;
					}
					// rysujemy wybuch tylko jeśli pole jest wolne
					if(checkOccupied(d, x, y)) {
						rect(
							margin + (x - 1) * fWidth,
							margin + (y - 1) * fHeight,
							fWidth * 3,
							fHeight * 3
						);
					} else break;
				}
			}

		} else {
			int x = posX, y = posY;

			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					scene.get(sizeX*(y - 1 + i) + x - 1 + j).makeNAblaze();
				}
			}
			for(int d = 0; d < 4; d++) {	// dla każdego kierunku
				for(int i = 1; i <= power; i++) {	// na odległość taką, jaką wyznacza moc wybuchu
					if(d == 0) {
						x = posX;
						y = posY - i;
					} else if(d == 1) {
						x = posX + i;
						y = posY;
					} else if(d == 2) {
						x = posX;
						y = posY + i;
					} else if(d == 3) {
						x = posX - i;
						y = posY;
					}
					if(checkOccupied(d, x, y)) {
						if(d == 0) {
							scene.get(sizeX*(y - 1) + x - 1).makeNAblaze();
							scene.get(sizeX*(y - 1) + x).makeNAblaze();
							scene.get(sizeX*(y - 1) + x + 1).makeNAblaze();
						} else if(d == 1) {
							scene.get(sizeX*(y - 1) + x + 1).makeNAblaze();
							scene.get(sizeX*y + x + 1).makeNAblaze();
							scene.get(sizeX*(y + 1) + x + 1).makeNAblaze();
						} else if(d == 2) {
							scene.get(sizeX*(y + 1) + x - 1).makeNAblaze();
							scene.get(sizeX*(y + 1) + x).makeNAblaze();
							scene.get(sizeX*(y + 1) + x + 1).makeNAblaze();
						} else if(d == 3) {
							scene.get(sizeX*(y - 1) + x - 1).makeNAblaze();
							scene.get(sizeX*y + x - 1).makeNAblaze();
							scene.get(sizeX*(y + 1) + x - 1).makeNAblaze();
						}
					} else break;
				}
			}
			objects.remove(this);
		}
	}
}
