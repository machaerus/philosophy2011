import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class philosophy2011 extends PApplet {



Wittgenstein player;
ArrayList<Char> characters;
ArrayList<GameObject> objects;
ArrayList<Field> scene;
PFont f;
int sizeX = 39, sizeY = 30;
int margin = 25;
int fWidth = 20;
int fHeight = 20;
int Q;
int SPEED = 18;
boolean STOP = false;
boolean PAUSE = false;
boolean verbose = false;
boolean WON = false;
Minim minim;
AudioPlayer bgMusic;
AudioSample explosion, encounter, tlf;


public void setup() {
  
	f = createFont("Arial Black",48,true);
	frameRate(SPEED);
	Q = 0; // kwantowanie czasu xD

	size(
		830,
		650,
		P2D
	);

	minim = new Minim(this);
	explosion = minim.loadSample("explosion.wav");
  	encounter = minim.loadSample("encounter.wav");
  	tlf = minim.loadSample("tlf.wav");

	sceneInit2();
	player = new Wittgenstein(1, 1);
	characters = new ArrayList<Char>();
	objects = new ArrayList<GameObject>();

	Char ch;
	for(int i = 0; i < 1; i++) {
		ch = new GEMoore(10, 10);
		characters.add(ch);
	}	

  	bgMusic = minim.loadFile("Slipped.mp3");
  	bgMusic.play();

}


public void draw() {
	background(222);
	for(int i = 0; i < scene.size(); i++)
		scene.get(i).display();

	for(int i = 0; i < objects.size(); i++) 
		objects.get(i).display();

	player.display();
	
	for(int i = 0; i < characters.size(); i++)
		characters.get(i).display();

	if(!STOP && !PAUSE) {
		Q = (Q + 1) % 4;
		if(Q == 0) {
			for(int i = 0; i < characters.size(); i++) {
				characters.get(i).smartMove();
			}
			for(int i = 0; i < objects.size(); i++) 
				objects.get(i).update();
		}

		checkCollisions();
	}

	if(PAUSE) {
		fill(0, 150);
		noStroke();
		rect(0, 0, width, height);
	}

	if(STOP) {
		fill(0, 200);
		noStroke();
		rect(0, 0, width, height);
		textFont(f, 48);
		fill(255,255);
		textAlign(CENTER);
		String message;
		message = WON ? "YOU WIN!" : "GAME OVER!";
		text(message, width/2, height/2);
	}
}

public void sceneInit() {

	scene = new ArrayList<Field>();
	int posX = margin, posY = margin;
	boolean bool;
	int c;

	for(int i = 0; i < sizeY; i++) {
		for(int j = 0; j < sizeX; j++) {
			if(random(30) < 1) {
				bool = true;
				c = color(150,255);
			} else {
				bool = false;
				c = color(222,255);
			} 
			scene.add(new Field(posX,posY,bool,c));
			posX += fWidth;
		}
		posX = margin;
		posY += fHeight;
	}

}

public void sceneInit2() {

	scene = new ArrayList<Field>();
	int posX = margin, posY = margin;
	boolean bool;
	int c;

	int Xcounter = 0, Ycounter = -1;
	int Xswitcher = 0, Yswitcher = 0;

	for(int i = 0; i < sizeY; i++) {

		Ycounter++;
		if( Ycounter == 3 ) {
			Yswitcher = ( Yswitcher + 1 ) % 3;
			Ycounter = 0;
		}

		Xswitcher = 0;
		Xcounter = -1;
		for(int j = 0; j < sizeX; j++) {

			//if(random(30) < 1) bool = true;
			//else bool = false;

			if( Yswitcher != 0 ) {
				Xcounter++;
				if( Xcounter == 3 ) {
					Xswitcher = ( Xswitcher + 1 ) % 3;
					Xcounter = 0;
				}
				//bool = boolean(Xswitcher);
				if( Xswitcher != 0 ) {
					bool = true;
					c = color(150,255);
				} else {
					bool = false;
					c = color(222,255);
				}
				scene.add(new Field(posX,posY,bool,c));
			} else {
				c = color(222,255);
				scene.add(new Field(posX,posY,false,c));
			}

			posX += fWidth;
		}

		posX = margin;
		posY += fHeight;
	}

}

public void keyPressed() {
	if(!STOP){
		if( key == 'p' ) togglePause();
		else if(!PAUSE) {
			if( keyCode == ESC )            exit();
			else if( keyCode == UP )        player.move(0);
			else if( keyCode == RIGHT )     player.move(1);
			else if( keyCode == DOWN )      player.move(2);
			else if( keyCode == LEFT )      player.move(3);
			else if( keyCode == ENTER )     player.fire();
		}
	}
}

public void togglePause() {
	if(PAUSE) {
		PAUSE = false;
	} else {
		PAUSE = true;
	}
}

public void checkCollisions() {
	Char ch;
	boolean die;

	for(int i = 0; i < characters.size(); i++) {
		ch = characters.get(i);
		if( dist(player.getX(), player.getY(), ch.getX(), ch.getY()) < 3) {
			//println("kolizja!");
			STOP = true;
		}
	}

	die = false;
	for(int i = 0; i < 3; i++) {
		for(int j = 0; j < 3; j++) {
			if(scene.get(sizeX*( player.getY() - 1 + i) + player.getX() - 1 + j).isAblaze()) {
				die = true;
			}
		}
	}
	if(die) {
		STOP = true;
		encounter.trigger();
	}

	for(int c = 0; c < characters.size(); c++) {
		ch = characters.get(c);
		die = false;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(scene.get(sizeX*( ch.getY() - 1 + i) + ch.getX() - 1 + j).isAblaze()) {
					die = true;
				}
			}
		}
		if(die) {
			WON = true;
			STOP = true;
			encounter.trigger();
		}
	}

}


	
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

	public void display() {
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
public abstract class Char {

  int posX, posY;
  int blocked;
  int currentDir;
  
  Char(int x, int y) {
    posX = x;
    posY = y;
    blocked = 4;
    currentDir = 4;
  }
  
  public boolean move(int dir) {
    
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
        return true;
      } else {
        blocked = dir;
        currentDir = 4;
        return false;
      }
    } else return false;
  }
  

  public void smartMove() {
    float r = random(1000);
    if ( blocked == 4 && currentDir == 4 ) {
      // brak danych, zaczynamy w losowym kierunku 
      if      ( r < 250 )  move(0);
      else if ( r < 500 )  move(1);
      else if ( r < 750 )  move(2);
      else if ( r < 1000 ) move(3);
    } else if ( blocked != 4 && currentDir == 4 ) {
      // trafili\u015bmy na przeszkod\u0119 i szukamy przej\u015bcia
      // najlepiej i\u015b\u0107 wzd\u0142u\u017c przeszkody (chyba)
      if      ( r < 10 )   move( blocked );
      else if ( r < 400 )  move( (blocked - 1) % 4 );
      else if ( r < 790 )  move( (blocked + 1) % 4 );
      else if ( r < 1000 ) move( (blocked + 2) % 4 );
    } else if ( blocked == 4 && currentDir != 4 ) {
      // nie jeste\u015bmy zablokowani, staramy si\u0119 utrzyma\u0107 kierunek
      if      ( r < 20 )   move( (currentDir + 2) % 4 );
      else if ( r < 120 )  move( (currentDir + 1) % 4 );
      else if ( r < 220 )  move( (currentDir - 1) % 4 );
      else if ( r < 1000 ) move( currentDir );
    }
  } 

  public int getX() {
    return this.posX;
  }

  public int getY() {
    return this.posY;
  }

  public void speak(String s) {
    if(verbose) print(s);
  }

  public void speakln(String s) {
    if(verbose) println(s);
  }
  
  public abstract void display();

}
public class Explosion extends GameObject {
	
	int lifespan = 10;
	int power;

	Explosion(int x, int y) {
		super(x, y, 3, 3);
		power = 5;

		explosion.trigger();

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				scene.get(sizeX*(y - 1 + i) + x - 1 + j).makeAblaze();
			}
		}

		for(int d = 0; d < 4; d++) {	// dla ka\u017cdego kierunku
			for(int i = 1; i <= power; i++) {	// na odleg\u0142o\u015b\u0107 tak\u0105, jak\u0105 wyznacza moc wybuchu
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

	public boolean checkOccupied(int dir, int x, int y) {
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

	public void display() {
		if(time < lifespan) {
			fill(255-time*18,0,0);
			noStroke(); 

			// \u015brodek
			rect(
				margin + (posX - 1) * fWidth,
				margin + (posY - 1) * fHeight,
				fWidth * 3,
				fHeight * 3
			);

			int x = posX, y = posY;
			for(int d = 0; d < 4; d++) {	// dla ka\u017cdego kierunku
				for(int i = 1; i <= power; i++) {	// na odleg\u0142o\u015b\u0107 tak\u0105, jak\u0105 wyznacza moc wybuchu
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
					// rysujemy wybuch tylko je\u015bli pole jest wolne
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
			for(int d = 0; d < 4; d++) {	// dla ka\u017cdego kierunku
				for(int i = 1; i <= power; i++) {	// na odleg\u0142o\u015b\u0107 tak\u0105, jak\u0105 wyznacza moc wybuchu
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
class Field {

  	int posX, posY;
  	boolean occupied;
  	boolean ablaze;
  	int col;
  
  	Field(int x, int y, boolean occ, int c) {
		posX = x;
		posY = y;
		occupied = occ;
		col = c;
		ablaze = false;
  	}
  
  	public boolean isOccupied() {
		return occupied;
  	}

  	public boolean isAblaze() {
		return ablaze;
  	}

  	public void toggleOccupied() {
		occupied = isOccupied() ? false : true;
  	}

  	public void toggleAblaze() {
		ablaze = isAblaze() ? false : true;
  	}

  	public void makeAblaze() {
  		ablaze = true;
  	}

  	public void makeNAblaze() {
  		ablaze = false;
  	}
  
  	public void display() {
		stroke(150);
		// if(isAblaze())
		// 	fill(0);
		// else
			fill(col);
		rect(posX, posY, fWidth, fHeight);
  	}

}
public class GEMoore extends Mob {

	PImage gemooreU, gemooreR, gemooreD, gemooreL;

	GEMoore(int x, int y) {
		super(x, y);
		gemooreU = loadImage("gemoore.png");
	    gemooreR = loadImage("gemoore.png");
	    gemooreD = loadImage("gemoore.png");
	    gemooreL = loadImage("gemoore.png");
	}

	public void display() {  
	    
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

	public void display() {
		fill(50);
		noStroke();
		rect(
			margin + (posX - 1) * fWidth,
			margin + (posY - 1) * fHeight,
			fWidth * objectWidth,
			fHeight * objectHeight );
	}

	public void update() {
		time++;
	}

}
public class Mob extends Char {

	Mob(int x, int y) {
		super(x, y);
	}

	/*
	boolean move(int dir) {
	    
	    // 0 - up
	    // 1 - right
	    // 2 - down
	    // 3 - left 
	    // 4 - NULL
	    
	    if ( dir == 1 ) {  // right
		    posX++;
		    return true;
		} else if ( dir == 3 ) {  // left
		    posX--;
		    return true;
		} else if ( dir == 0 ) {  // up
		    posY--;
		    return true;
		} else if ( dir == 2 ) {  // down
		    posY++;
		    return true;
	    } else return false;
	}
	*/
  
	public void smartMove() {
	  	
	  	float r;
	  	if ( currentDir == 4 ) {
		    // brak danych, zaczynamy w losowym kierunku 
		    speakln("ide w dowolnym kierunku.");
		    r = random(1000);
		    if ( r < 250 ) {
		    	move(0);
		    } else if ( r < 500 ) {
		    	move(1);
		    } else if ( r < 750 ) {
		    	move(2);
		    } else if ( r < 1000 ) {
		    	move(3);
		    }
	    } else {
		  	int dir = 0;
		  	ArrayList<Integer> dirs = new ArrayList<Integer>();
		  	dirs.add(currentDir);
		  	dirs.add((currentDir+1)%4);
		  	dirs.add((currentDir+3)%4);
		  	// najpierw sprawdzamy dowolny z kierunk\u00f3w:
		  	// prz\u00f3d, prawo albo lewo
		  	speak("z opcji: "+dirs.get(0)+", "+dirs.get(1)+", "+dirs.get(2)+" ");
		  	r = random(1000);
		  	if ( r < 500 ) {
		  		dir = dirs.get(0);
		  		dirs.remove(0);
		  	} else if ( r < 750 ) {
		  		dir = dirs.get(1);
		  		dirs.remove(1);
		  	} else {
		  		dir = dirs.get(2);
		  		dirs.remove(2);
		  	}
		 	speak("wybralam "+dir+": ");
		  	// je\u015bli wolny, idziemy tam
		  	if ( lookout(dir) == 0 ) {
		  		speakln("wolne, ide.");
		  		move(dir);
		  	} else {
		  		// sprawdzamy kolejny kierunek z dw\u00f3ch pozosta\u0142ych
		  		speakln("losuje dalej...");
		  		speak("z opcji: "+dirs.get(0)+", "+dirs.get(1)+" ");
		  		r = random(1000);
		  		if ( r < 500 ) {
		  			dir = dirs.get(0);
		  			dirs.remove(0);
		  		} else {
		  			dir = dirs.get(1);
		  			dirs.remove(1);
		  		}
		  		speak("wybralam "+dir+": ");
		  		// je\u015bli wolny, idziem
		  		if ( lookout(dir) == 0 ) {
		  			speakln("okej, ide.");
		  			move(dir);
		  		} else {
		  			// sprawdzamy ostatni\u0105 opcj\u0119
		  			speakln("nie da sie.");
		  			speak("ostatnia opcja: "+dirs.get(0)+": ");
		  			dir = dirs.get(0);
		  			if ( lookout(dir) == 0 ) {
		  				speakln("dobra, ide.");
		  				move(dir);
		  			} else {
		  				speakln("tez sie nie da.");
		  				// zawracamy - \u015blepa uliczka
		  				dir = (currentDir + 2) % 4;
		  				if ( lookout(dir) == 0 ) {
		  					speakln("chuj, zawracam.");
		  					move(dir);
		  				} else {
		  					// ojej, jeste\u015bmy uwi\u0119zieni xD
		  					// co za nieszcz\u0119\u015bcie
		  					speakln("uwiezion xD");
		  				}
		  			}
		  		}
		  	}
	  	}
	}

	public int lookout(int dir) {

	  	int l = 0;
	  	// 0 - czysto
	  	// 1 - przeszkoda
	  	if ( dir == 0 ) {
	  		if (
	  			(this.posY > 1)
		        && !scene.get(sizeX*(posY - 2) + posX - 1).isOccupied() 
		        && !scene.get(sizeX*(posY - 2) + posX).isOccupied()
		        && !scene.get(sizeX*(posY - 2) + posX + 1).isOccupied()
	  		) l = 0;
	  		else l = 1;
	  	} else if ( dir == 1 ) {
	  		if (
	  			(this.posX < sizeX - 2)
		        && !scene.get(sizeX*(posY - 1) + posX + 2).isOccupied()
		        && !scene.get(sizeX*posY + posX + 2).isOccupied()
		        && !scene.get(sizeX*(posY + 1) + posX + 2).isOccupied() 
	  		) l = 0;
	  		else l = 1;
	  	} else if ( dir == 2 ) {
	  		if (
	  			(this.posY < sizeY - 2)
		        && !scene.get(sizeX*(posY + 2) + posX - 1).isOccupied() 
		        && !scene.get(sizeX*(posY + 2) + posX).isOccupied()
		        && !scene.get(sizeX*(posY + 2) + posX + 1).isOccupied()
	  		) l = 0;
	  		else l = 1;
	  	} else if ( dir == 3 ) {
	  		if (
	  			(this.posX > 1)
		        && !scene.get(sizeX*(posY - 1) + posX - 2).isOccupied() 
		        && !scene.get(sizeX*posY + posX - 2).isOccupied() 
		        && !scene.get(sizeX*(posY + 1) + posX - 2).isOccupied() 
	  		) l = 0;
	  		else l = 1;
	  	}
	  	return l;
	}

	public void display() {
	    noStroke();
	    fill(100);
	    ellipse(
	      margin + this.posX * fWidth + fWidth/2,
	      margin + this.posY * fHeight + fHeight/2,
	      60, 60);  
	} 
	
}
public class Nietzsche extends Mob {

	PImage nietzscheU, nietzscheR, nietzscheD, nietzscheL;

	Nietzsche(int x, int y) {
		super(x, y);
		nietzscheU = loadImage("nietzsche.png");
	    nietzscheR = loadImage("nietzsche.png");
	    nietzscheD = loadImage("nietzsche.png");
	    nietzscheL = loadImage("nietzsche.png");
	}
	
	public void display() {  
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
public class Wittgenstein extends Char {
	
	int state;
	PImage wittgenstein1, wittgenstein2;

	Wittgenstein(int x, int y) {
		super(x, y);
		state = 0;
		wittgenstein1 = loadImage("wittgenstein.png");
		wittgenstein2 = loadImage("wittgenstein.png");
	}

	public void display() {
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

	public void changeState() {
		state = ( state + 1 ) % 2;
	}

	public void fire() {
		Bomb bomb = new Bomb(posX, posY);
		objects.add(bomb);
		tlf.trigger();
	}

	public boolean move(int dir) {
	
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "philosophy2011" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
