public class Wittgenstein extends Char {
	
	int state;
	PImage wittgenstein1, wittgenstein2;

	Wittgenstein(int x, int y) {
		super(x, y);
		state = 0;
		wittgenstein1 = loadImage("wittgenstein.png");
		wittgenstein2 = loadImage("wittgenstein.png");
	}

	void display() {
		PImage sprite;
		if( state == 0 )
			sprite = wittgenstein1;
		else if( state == 1 )
			sprite = wittgenstein2;
		else
			sprite = wittgenstein1;

		image(sprite, 
		  margin + this.posX * fWidth - fWidth,
		  margin + this.posY * fHeight - fHeight
		);
	} 

	void changeState() {
		state = ( state + 1 ) % 2;
	}

	void fire() {
		Bomb bomb = new Bomb(posX, posY);
		objects.add(bomb);
	}

	boolean move(int dir) {
	
		// 0 - up
		// 1 - right
		// 2 - down
		// 3 - left 
		// 4 - NULL
		
		if ( dir == 1 ) {  // right
		  if ( 
			(this.posX < sizeX - 2)
			&& !scene.get(sizeX*(posY - 1) + posX + 2).isOccupied()
			&& !scene.get(sizeX*posY + posX + 2).isOccupied()
			&& !scene.get(sizeX*(posY + 1) + posX + 2).isOccupied()  
		  ) {
			posX++;
			currentDir = dir;
			blocked = 4;
			changeState();
			return true;
		  } else {
			blocked = dir;
			currentDir = 4;
			return false;
		  }
		} else if ( dir == 3 ) {  // left
		  if ( 
			(this.posX > 1)
			&& !scene.get(sizeX*(posY - 1) + posX - 2).isOccupied() 
			&& !scene.get(sizeX*posY + posX - 2).isOccupied() 
			&& !scene.get(sizeX*(posY + 1) + posX - 2).isOccupied() 
		  ) {
			posX--;
			currentDir = dir;
			blocked = 4;
			changeState();
			return true;
		  } else {
			blocked = dir;
			currentDir = 4;
			return false;
		  }
		} else if ( dir == 0 ) {  // up
		  if ( 
			(this.posY > 1)
			&& !scene.get(sizeX*(posY - 2) + posX - 1).isOccupied() 
			&& !scene.get(sizeX*(posY - 2) + posX).isOccupied()
			&& !scene.get(sizeX*(posY - 2) + posX + 1).isOccupied()
		  ) {
			posY--;
			currentDir = dir;
			blocked = 4;
			changeState();
			return true;
		  } else {
			blocked = dir;
			currentDir = 4;
			return false;
		  }
		} else if ( dir == 2 ) {  // down
		  if ( 
			(this.posY < sizeY - 2)
			&& !scene.get(sizeX*(posY + 2) + posX - 1).isOccupied() 
			&& !scene.get(sizeX*(posY + 2) + posX).isOccupied()
			&& !scene.get(sizeX*(posY + 2) + posX + 1).isOccupied()
		  ) {
			posY++;
			currentDir = dir;
			blocked = 4;
			changeState();
			return true;
		  } else {
			blocked = dir;
			currentDir = 4;
			return false;
		  }
		} else return false;
	}

}
