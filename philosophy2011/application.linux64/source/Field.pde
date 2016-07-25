class Field {

  	int posX, posY;
  	boolean occupied;
  	boolean ablaze;
  	color col;
  
  	Field(int x, int y, boolean occ, color c) {
		posX = x;
		posY = y;
		occupied = occ;
		col = c;
		ablaze = false;
  	}
  
  	boolean isOccupied() {
		return occupied;
  	}

  	boolean isAblaze() {
		return ablaze;
  	}

  	void toggleOccupied() {
		occupied = isOccupied() ? false : true;
  	}

  	void toggleAblaze() {
		ablaze = isAblaze() ? false : true;
  	}

  	void makeAblaze() {
  		ablaze = true;
  	}

  	void makeNAblaze() {
  		ablaze = false;
  	}
  
  	void display() {
		stroke(150);
		// if(isAblaze())
		// 	fill(0);
		// else
			fill(col);
		rect(posX, posY, fWidth, fHeight);
  	}

}
