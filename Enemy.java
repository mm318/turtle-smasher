/////////////////////////////////////////////////////////////
//class: Enemy
//Description:  stores enemy data and methods that act on that data
//Programmer: Miaofei Mei
/////////////////////////////////////////////////////////////

public class Enemy {
  public final int ENEMY_SPEED = 1;
  public boolean left;
  public boolean falling;

  private int enemyStatus;
  public final int ALIVE = 0;
  public final int WEAKENED = 1;
  public final int KICKED = 2;

  private int enemyRow, enemyCol;

  public Enemy() {
    falling = true;
    enemyStatus = ALIVE;
  }


  public void setEnemyLocation(int row, int col) {
    enemyRow = row;
    enemyCol = col;
  }


  //***************************************************************************************
  //getEnemyRow: get row location of enemy
  //
  public int getEnemyRow() {
    return enemyRow;
  }


  //***************************************************************************************
  //getEnemyCol: get col location of enemy
  //
  public int getEnemyCol() {
    return enemyCol;
  }


  public void setEnemyStatus(int status) {
    enemyStatus = status;
  }


  public int getEnemyStatus() {
    return enemyStatus;
  }
}
