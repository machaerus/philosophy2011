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
  

  void smartMove() {
    float r = random(1000);
    if ( blocked == 4 && currentDir == 4 ) {
      // brak danych, zaczynamy w losowym kierunku 
      if      ( r < 250 )  move(0);
      else if ( r < 500 )  move(1);
      else if ( r < 750 )  move(2);
      else if ( r < 1000 ) move(3);
    } else if ( blocked != 4 && currentDir == 4 ) {
      // trafiliśmy na przeszkodę i szukamy przejścia
      // najlepiej iść wzdłuż przeszkody (chyba)
      if      ( r < 10 )   move( blocked );
      else if ( r < 400 )  move( (blocked - 1) % 4 );
      else if ( r < 790 )  move( (blocked + 1) % 4 );
      else if ( r < 1000 ) move( (blocked + 2) % 4 );
    } else if ( blocked == 4 && currentDir != 4 ) {
      // nie jesteśmy zablokowani, staramy się utrzymać kierunek
      if      ( r < 20 )   move( (currentDir + 2) % 4 );
      else if ( r < 120 )  move( (currentDir + 1) % 4 );
      else if ( r < 220 )  move( (currentDir - 1) % 4 );
      else if ( r < 1000 ) move( currentDir );
    }
  } 

  int getX() {
    return this.posX;
  }

  int getY() {
    return this.posY;
  }

  void speak(String s) {
    if(verbose) print(s);
  }

  void speakln(String s) {
    if(verbose) println(s);
  }
  
  abstract void display();

}
