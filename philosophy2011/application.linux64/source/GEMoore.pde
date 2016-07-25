public class GEMoore extends Mob {

	PImage gemooreU, gemooreR, gemooreD, gemooreL;

	GEMoore(int x, int y) {
		super(x, y);
		gemooreU = loadImage("gemoore.png");
	    gemooreR = loadImage("gemoore.png");
	    gemooreD = loadImage("gemoore.png");
	    gemooreL = loadImage("gemoore.png");
	}

	void display() {  
	    
	    PImage sprite;
	    if      ( currentDir == 0 || blocked == 0 ) sprite = gemooreU;
	    else if ( currentDir == 1 || blocked == 1 ) sprite = gemooreR;
	    else if ( currentDir == 2 || blocked == 2 ) sprite = gemooreD;
	    else if ( currentDir == 3 || blocked == 3 ) sprite = gemooreL;
	    else sprite = gemooreU;
	    
	    image(sprite, 
	      margin + this.posX * fWidth - fWidth,
	      margin + this.posY * fHeight - fHeight
	    );
	}
	
}
