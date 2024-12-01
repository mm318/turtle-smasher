/////////////////////////////////////////////////////////////
//class: EnemyController
//Description:  container the logic that control the item's action
//              This class extends Thread because it runs concurrently with
//              game gameBoard's main thread of the action
//Programmer: Miaofei Mei
/////////////////////////////////////////////////////////////

import java.lang.Object;

public class ItemController extends Thread {
  private final int SLEEP_PERIOD = 100;
  private boolean pause;
  private GameBoard gameBoard;
  private Item item;                      // the item this thread control

  public ItemController(GameBoard b) {
    gameBoard = b;

    item = new Item();
    item.setItemLocation(0, 0);

    pause = false;
  }


  public void run() {
    int previousItemRow, previousItemCol;

    while (true) {
      try {
        Thread.sleep(SLEEP_PERIOD);
      } catch (Exception e) {
        System.out.println(e);
      }

      if (!pause) {
        previousItemRow = item.getItemRow();
        previousItemCol = item.getItemCol();

        if (item.falling) {
          item.setItemLocation(item.getItemRow() + 1, item.getItemCol());

          if (gameBoard.getSquareType(item.getItemRow() + SquareType.ITEM_SIDE, item.getItemCol()) != SquareType.EMPTY || gameBoard.getSquareType(item.getItemRow() + SquareType.ITEM_SIDE, item.getItemCol() + SquareType.ITEM_SIDE - 1) != SquareType.EMPTY) {
            item.falling = false;
          }
        } else if (gameBoard.getSquareType(item.getItemRow() + SquareType.ITEM_SIDE, item.getItemCol()) == SquareType.EMPTY && gameBoard.getSquareType(item.getItemRow() + SquareType.ITEM_SIDE, item.getItemCol() + SquareType.ITEM_SIDE - 1) == SquareType.EMPTY) {
          item.falling = true;
        }

        if (item.left) {
          if (item.getItemCol() - item.ITEM_SPEED < 0) {
            if (item.getItemRow() > gameBoard.getBoardRows() - SquareType.TILE_HEIGHT - SquareType.ITEM_SIDE - 1) {
              respawnItem(item.getItemRow(), item.getItemCol());
            } else {
              item.setItemLocation(item.getItemRow(), gameBoard.getBoardColumns() - 1 - SquareType.ITEM_SIDE);
            }
          } else if (gameBoard.getSquareType(item.getItemRow(), item.getItemCol() - item.ITEM_SPEED) == SquareType.EMPTY && gameBoard.getSquareType(item.getItemRow() + SquareType.ITEM_SIDE - 1, item.getItemCol() - item.ITEM_SPEED) == SquareType.EMPTY) {
            item.setItemLocation(item.getItemRow(), item.getItemCol() - item.ITEM_SPEED);
          } else {
            item.left = false;
          }
        } else {
          if (item.getItemCol() + SquareType.ITEM_SIDE + item.ITEM_SPEED >= gameBoard.getBoardColumns()) {
            if (item.getItemRow() > gameBoard.getBoardRows() - SquareType.TILE_HEIGHT - SquareType.ITEM_SIDE - 1) {
              respawnItem(item.getItemRow(), item.getItemCol());
            } else {
              item.setItemLocation(item.getItemRow(), 0);
            }
          } else if (gameBoard.getSquareType(item.getItemRow(), item.getItemCol() + SquareType.PLAYER_WIDTH) == SquareType.EMPTY && gameBoard.getSquareType(item.getItemRow() + SquareType.ITEM_SIDE - 1, item.getItemCol() + SquareType.ITEM_SIDE) == SquareType.EMPTY) {
            item.setItemLocation(item.getItemRow(), item.getItemCol() + item.ITEM_SPEED);
          } else {
            item.left = true;
          }
        }

        gameBoard.setSquareType(previousItemRow, previousItemCol, SquareType.EMPTY);
        gameBoard.setSquareType(item.getItemRow(), item.getItemCol(), SquareType.ITEM_COLLECTIBLE);
      }
    }
  }


  public Item getItem() {
    return item;
  }


  public void pause(boolean p) {
    pause = p;
  }


  public void respawnItem(int itemRow, int itemColumn) {
    final int FALLING_SPEED = 40;

    gameBoard.setSquareType(itemRow, itemColumn, SquareType.EMPTY);
    int previousSquareType = SquareType.EMPTY;

    for (int i = itemRow + 1 ; i < gameBoard.getBoardRows() ; i++) {
      try {
        Thread.sleep(FALLING_SPEED);
      } catch (Exception e) {
        System.out.println(e);
      }
      gameBoard.setSquareType(i - 1, itemColumn, previousSquareType);
      previousSquareType = gameBoard.getSquareType(i, itemColumn);
      gameBoard.setSquareType(i, itemColumn, SquareType.ITEM_COLLECTIBLE);
    }
    gameBoard.setSquareType(gameBoard.getBoardRows() - 1, itemColumn, SquareType.EMPTY);

    if (gameBoard.spawningSideLeft) {
      item.setItemLocation(0, 0);
      item.left = false;
      gameBoard.spawningSideLeft = false;
    } else {
      item.setItemLocation(0, gameBoard.getBoardColumns() - 1 - SquareType.ITEM_SIDE);
      item.left = true;
      gameBoard.spawningSideLeft = true;
    }
  }
}
