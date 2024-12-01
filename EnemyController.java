/////////////////////////////////////////////////////////////
//class: EnemyController
//Description:  container the logic that control the enemy's action
//              This class extends Thread because it runs concurrently with
//              game gameBoard's main thread of the action
//Programmer: Miaofei Mei
/////////////////////////////////////////////////////////////

import java.lang.Object;

public class EnemyController extends Thread {
  private int SLEEP_PERIOD;
  private final int ENEMY_SPEED;
  private boolean pause;
  private GameBoard gameBoard;
  private Enemy enemy;    // the enemy this thread control

  private EnemyController enemyControllers[];

  public EnemyController(GameBoard b, int speed, EnemyController enemies[]) {
    gameBoard = b;

    ENEMY_SPEED = 100 / speed;
    SLEEP_PERIOD = ENEMY_SPEED;
    enemy = new Enemy();
    enemy.setEnemyLocation(0, 0);

    enemyControllers = enemies;

    pause = false;
  }


  public void run() {
    int previousEnemyRow, previousEnemyCol;
    int enemyHeight, enemyWidth;

    while (true) {
      try {
        Thread.sleep(SLEEP_PERIOD);
      } catch (Exception e) {
        System.out.println(e);
      }

      if (!pause) {
        previousEnemyRow = enemy.getEnemyRow();
        previousEnemyCol = enemy.getEnemyCol();

        if (enemy.getEnemyStatus() == enemy.ALIVE) {
          enemyHeight = SquareType.ENEMY_HEIGHT;
          enemyWidth = SquareType.ENEMY_WIDTH;
        } else {
          enemyHeight = SquareType.SHELL_HEIGHT;
          enemyWidth = SquareType.SHELL_HEIGHT;
        }

        if (enemy.falling) {
          enemy.setEnemyLocation(enemy.getEnemyRow() + 1, enemy.getEnemyCol());

          if (gameBoard.getSquareType(enemy.getEnemyRow() + enemyHeight, enemy.getEnemyCol()) != SquareType.EMPTY || gameBoard.getSquareType(enemy.getEnemyRow() + enemyHeight, enemy.getEnemyCol() + enemyWidth - 1) != SquareType.EMPTY) {
            enemy.falling = false;
          }
        } else if (gameBoard.getSquareType(enemy.getEnemyRow() + enemyHeight, enemy.getEnemyCol()) == SquareType.EMPTY && gameBoard.getSquareType(enemy.getEnemyRow() + enemyHeight, enemy.getEnemyCol() + enemyWidth - 1) == SquareType.EMPTY) {
          enemy.falling = true;
        }

        if (enemy.left && enemy.getEnemyStatus() != enemy.WEAKENED) {
          if (enemy.getEnemyCol() - enemy.ENEMY_SPEED < 0) {
            if (enemy.getEnemyRow() > gameBoard.getBoardRows() - SquareType.TILE_HEIGHT - SquareType.ENEMY_HEIGHT - 1) {
              killEnemy(enemy.getEnemyRow(), enemy.getEnemyCol(), SquareType.ENEMY_LEFT);
            } else {
              enemy.setEnemyLocation(enemy.getEnemyRow(), gameBoard.getBoardColumns() - 1 - enemyWidth);
            }
          } else if (gameBoard.getSquareType(enemy.getEnemyRow(), enemy.getEnemyCol() - enemy.ENEMY_SPEED) == SquareType.EMPTY && gameBoard.getSquareType(enemy.getEnemyRow() + enemyHeight - 1, enemy.getEnemyCol() - enemy.ENEMY_SPEED) == SquareType.EMPTY) {
            enemy.setEnemyLocation(enemy.getEnemyRow(), enemy.getEnemyCol() - enemy.ENEMY_SPEED);
          } else {
            enemy.left = false;
          }
        } else if (!enemy.left && enemy.getEnemyStatus() != enemy.WEAKENED) {
          if (enemy.getEnemyCol() + enemyWidth + enemy.ENEMY_SPEED >= gameBoard.getBoardColumns()) {
            if (enemy.getEnemyRow() > gameBoard.getBoardRows() - SquareType.TILE_HEIGHT - SquareType.ENEMY_HEIGHT - 1) {
              killEnemy(enemy.getEnemyRow(), enemy.getEnemyCol(), SquareType.ENEMY_RIGHT);
            } else {
              enemy.setEnemyLocation(enemy.getEnemyRow(), 0);
            }
          } else if (enemy.getEnemyStatus() == enemy.KICKED && (gameBoard.getSquareType(enemy.getEnemyRow(), enemy.getEnemyCol() + enemyWidth) == SquareType.ENEMY_RIGHT && gameBoard.getSquareType(enemy.getEnemyRow() + enemyHeight - 1, enemy.getEnemyCol() + enemyWidth) == SquareType.ENEMY_RIGHT) || (gameBoard.getSquareType(enemy.getEnemyRow(), enemy.getEnemyCol() + enemyWidth) == SquareType.ENEMY_LEFT && gameBoard.getSquareType(enemy.getEnemyRow() + enemyHeight - 1, enemy.getEnemyCol() + enemyWidth) == SquareType.ENEMY_LEFT)) {
            enemy.setEnemyLocation(enemy.getEnemyRow(), enemy.getEnemyCol() + enemy.ENEMY_SPEED);
          } else if (gameBoard.getSquareType(enemy.getEnemyRow(), enemy.getEnemyCol() + enemyWidth) == SquareType.EMPTY && gameBoard.getSquareType(enemy.getEnemyRow() + enemyHeight - 1, enemy.getEnemyCol() + enemyWidth) == SquareType.EMPTY) {
            enemy.setEnemyLocation(enemy.getEnemyRow(), enemy.getEnemyCol() + enemy.ENEMY_SPEED);
          } else {
            enemy.left = true;
          }
        }

        gameBoard.setSquareType(previousEnemyRow, previousEnemyCol, SquareType.EMPTY);

        if (enemy.getEnemyStatus() == enemy.ALIVE) {
          if (enemy.left) {
            gameBoard.setSquareType(enemy.getEnemyRow(), enemy.getEnemyCol(), SquareType.ENEMY_LEFT);
          } else {
            gameBoard.setSquareType(enemy.getEnemyRow(), enemy.getEnemyCol(), SquareType.ENEMY_RIGHT);
          }
        } else {
          gameBoard.setSquareType(enemy.getEnemyRow(), enemy.getEnemyCol(), SquareType.ENEMY_WEAKENED);
        }

        for (int i = 0 ; i < enemyControllers.length ; i++) {
          if (enemyControllers[i] != this && enemy.getEnemyStatus() == enemy.ALIVE && enemyControllers[i].getEnemy().getEnemyStatus() == enemy.KICKED) {
            int enemyRow = enemyControllers[i].getEnemy().getEnemyRow();
            int enemyCol = enemyControllers[i].getEnemy().getEnemyCol();

            if (enemyRow >= enemy.getEnemyRow() && enemyRow <= enemy.getEnemyRow() + SquareType.ENEMY_HEIGHT && enemyCol >= enemy.getEnemyCol() && enemyCol <= enemy.getEnemyCol() + SquareType.ENEMY_WIDTH) {
              if (enemy.left) {
                killEnemy(enemy.getEnemyRow(), enemy.getEnemyCol(), SquareType.ENEMY_LEFT);
              } else {
                killEnemy(enemy.getEnemyRow(), enemy.getEnemyCol(), SquareType.ENEMY_RIGHT);
              }
            }
          }
        }

      }
    }
  }


  public Enemy getEnemy() {
    return enemy;
  }


  public void pause(boolean p) {
    pause = p;
  }


  public void attackEnemy() {
    if (enemy.getEnemyStatus() == enemy.ALIVE) {
      enemy.setEnemyStatus(enemy.WEAKENED);
    } else if (enemy.getEnemyStatus() == enemy.WEAKENED) {
      enemy.setEnemyStatus(enemy.KICKED);
      SLEEP_PERIOD = 10;
    }
  }


  public void killEnemy(int enemyRow, int enemyColumn, int enemyDirection) {
    final int FALLING_SPEED = 40;

    gameBoard.setSquareType(enemyRow, enemyColumn, SquareType.EMPTY);
    int previousSquareType = SquareType.EMPTY;

    for (int i = enemyRow + 1 ; i < gameBoard.getBoardRows() ; i++) {
      try {
        Thread.sleep(FALLING_SPEED);
      } catch (Exception e) {
        System.out.println(e);
      }
      gameBoard.setSquareType(i - 1, enemyColumn, previousSquareType);
      previousSquareType = gameBoard.getSquareType(i, enemyColumn);
      gameBoard.setSquareType(i, enemyColumn, enemyDirection);
    }
    gameBoard.setSquareType(gameBoard.getBoardRows() - 1, enemyColumn, SquareType.EMPTY);

    SLEEP_PERIOD = ENEMY_SPEED;
    enemy = new Enemy();
    if (gameBoard.spawningSideLeft) {
      enemy.setEnemyLocation(0, 0);
      enemy.left = false;
      gameBoard.spawningSideLeft = false;
    } else {
      enemy.setEnemyLocation(0, gameBoard.getBoardColumns() - 1 - SquareType.ENEMY_WIDTH);
      enemy.left = true;
      gameBoard.spawningSideLeft = true;
    }
  }
}
