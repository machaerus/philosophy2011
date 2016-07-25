public class Nietzsche extends Mob {

	PImage nietzscheU, nietzscheR, nietzscheD, nietzscheL;

	Nietzsche(int x, int y) {
		super(x, y);
		nietzscheU = loadImage("nietzsche.png");
	    nietzscheR = loadImage("nietzsche.png");
	    nietzscheD = loadImage("nietzsche.png");
	    nietzscheL = loadImage("nietzsche.png");
	}
	
	void display() {  
	    PImage sprite;
	    if      ( currentDir == 0 || blocked == 0 ) sprite = nietzscheU;
	    else if ( currentDir == 1 || blocked == 1 ) sprite = nietzscheR;
	    else if ( currentDir == 2 || blocked == 2 ) sprite = nietzscheD;
	    else if ( currentDir == 3 || blocked == 3 ) sprite = nietzscheL;
	    else sprite = nietzscheU;
	    
	    image(sprite, 
	      margin + this.posX * fWidth - fWidth,
	      margin + this.posY * fHeight - fHeight
	    );
	} 

}
