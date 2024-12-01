/////////////////////////////////////////////////////////////
//class: Enemy
//Description:  stores enemy data and methods that act on that data
//Programmer: Miaofei Mei
/////////////////////////////////////////////////////////////

public class Item {
  public final int ITEM_SPEED = 1;
  public boolean left;
  public boolean falling;

  private int itemRow, itemCol;

  public Item() {
    falling = true;
  }


  public void setItemLocation(int row, int col) {
    itemRow = row;
    itemCol = col;
  }


  //***************************************************************************************
  //getEnemyRow: get row location of enemy
  //
  public int getItemRow() {
    return itemRow;
  }


  //***************************************************************************************
  //getEnemyCol: get col location of enemy
  //
  public int getItemCol() {
    return itemCol;
  }
}
