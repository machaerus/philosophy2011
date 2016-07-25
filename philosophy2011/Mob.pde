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
  
	void smartMove() {
	  	
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
		  	// najpierw sprawdzamy dowolny z kierunków:
		  	// przód, prawo albo lewo
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
		  	// jeśli wolny, idziemy tam
		  	if ( lookout(dir) == 0 ) {
		  		speakln("wolne, ide.");
		  		move(dir);
		  	} else {
		  		// sprawdzamy kolejny kierunek z dwóch pozostałych
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
		  		// jeśli wolny, idziem
		  		if ( lookout(dir) == 0 ) {
		  			speakln("okej, ide.");
		  			move(dir);
		  		} else {
		  			// sprawdzamy ostatnią opcję
		  			speakln("nie da sie.");
		  			speak("ostatnia opcja: "+dirs.get(0)+": ");
		  			dir = dirs.get(0);
		  			if ( lookout(dir) == 0 ) {
		  				speakln("dobra, ide.");
		  				move(dir);
		  			} else {
		  				speakln("tez sie nie da.");
		  				// zawracamy - ślepa uliczka
		  				dir = (currentDir + 2) % 4;
		  				if ( lookout(dir) == 0 ) {
		  					speakln("chuj, zawracam.");
		  					move(dir);
		  				} else {
		  					// ojej, jesteśmy uwięzieni xD
		  					// co za nieszczęście
		  					speakln("uwiezion xD");
		  				}
		  			}
		  		}
		  	}
	  	}
	}

	int lookout(int dir) {

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

	void display() {
	    noStroke();
	    fill(100);
	    ellipse(
	      margin + this.posX * fWidth + fWidth/2,
	      margin + this.posY * fHeight + fHeight/2,
	      60, 60);  
	} 
	
}