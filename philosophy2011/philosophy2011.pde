import ddf.minim.*;

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


void setup() {
  
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


void draw() {
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

void sceneInit() {

	scene = new ArrayList<Field>();
	int posX = margin, posY = margin;
	boolean bool;
	color c;

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

void sceneInit2() {

	scene = new ArrayList<Field>();
	int posX = margin, posY = margin;
	boolean bool;
	color c;

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

void keyPressed() {
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

void togglePause() {
	if(PAUSE) {
		PAUSE = false;
	} else {
		PAUSE = true;
	}
}

void checkCollisions() {
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


	
