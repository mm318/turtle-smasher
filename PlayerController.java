/////////////////////////////////////////////////////////////
//class: PlayerController
//Description:  container the logic that control the enemy's action
//              This class extends Thread because it runs concurrently with
//              game gameBoard's main thread of the action
//Programmer: Miaofei Mei
/////////////////////////////////////////////////////////////

import java.awt.event.KeyEvent;
import java.lang.Object;

public class PlayerController extends Thread {
  private final int SLEEP_PERIOD = 40;
  private boolean pause;
  private GameBoard gameBoard;

  public int playerNum;
  private Player player;                          //this thread control
  private boolean direction[] = new boolean[3];  //boolean for keypressed, 0=left, 1=right, 2=up
  public final int LEFT = 0, RIGHT = 1, UP = 2;

  private EnemyController enemyControllers[];     //for communicating with other controllers
  private ItemController itemControllers[];

  public int left, right, up;                     //keys for player movement

  public PlayerController(GameBoard b, int pNum, int startingRow, int startingCol, int minToCollect, EnemyController enemies[], ItemController items[]) {
    gameBoard = b;
    gameBoard.setSquareType(startingRow, startingCol, SquareType.PLAYER_RIGHT_STANDING);

    player = new Player();
    player.setPlayerLocation(startingRow, startingCol);
    player.setMinToCollect(minToCollect);
    switch (pNum) {
      case 0:
        left = KeyEvent.VK_LEFT;
        right = KeyEvent.VK_RIGHT;
        up = KeyEvent.VK_UP;
        break;
      case 1:
        left = KeyEvent.VK_A;
        right = KeyEvent.VK_D;
        up = KeyEvent.VK_W;
        break;
      case 2:
        left = KeyEvent.VK_J;
        right = KeyEvent.VK_L;
        up = KeyEvent.VK_I;
        break;
      case 3:
        left = KeyEvent.VK_NUMPAD4;
        right = KeyEvent.VK_NUMPAD6;
        up = KeyEvent.VK_NUMPAD8;
        break;
    }
    playerNum = pNum;

    enemyControllers = enemies;
    itemControllers = items;

    pause = false;
  }


  public void run() {
    int playerDirection, previousPlayerRow, previousPlayerCol;

    while (true) {
      try {
        Thread.sleep(SLEEP_PERIOD);
      } catch (Exception e) {
        System.out.println(e);
      }

      if (!pause) {
        previousPlayerRow = player.getPlayerRow();
        previousPlayerCol = player.getPlayerCol();
        playerDirection = gameBoard.getSquareType(previousPlayerRow, previousPlayerCol);

        if (player.isFullyCollected() && player.getPlayerRow() >= gameBoard.getBoardRows() - SquareType.TILE_HEIGHT - SquareType.EXIT_HEIGHT && player.getPlayerCol() >= (gameBoard.getBoardColumns() - SquareType.EXIT_WIDTH) / 2 && player.getPlayerCol() <= (gameBoard.getBoardColumns() + SquareType.EXIT_WIDTH) / 2) {
          gameBoard.gameOver(this, false);
        }

        if (player.getJumpPhase() == player.JUMPING) {
          if (player.getJumpTime() > player.JUMP_DURATION || gameBoard.getSquareType(player.getPlayerRow() - 1, player.getPlayerCol()) != SquareType.EMPTY || gameBoard.getSquareType(player.getPlayerRow() - 1, player.getPlayerCol() + SquareType.PLAYER_WIDTH - 1) != SquareType.EMPTY || player.getPlayerRow() - 1 <= 0) {
            player.setJumpPhase(player.FALLING);
          }

          player.setPlayerLocation(player.getPlayerRow() - player.getJumpPhase(), player.getPlayerCol());
          player.setJumpTime(player.getJumpTime() + 1);
        } else if (player.getJumpPhase() == player.FALLING) {
          player.setPlayerLocation(player.getPlayerRow() - player.getJumpPhase(), player.getPlayerCol());

          for (int i = 0 ; i < enemyControllers.length ; i++) {
            int enemyRow = enemyControllers[i].getEnemy().getEnemyRow();
            int enemyCol = enemyControllers[i].getEnemy().getEnemyCol();
            int enemyHeight, enemyWidth;

            if (enemyControllers[i].getEnemy().getEnemyStatus() == enemyControllers[i].getEnemy().ALIVE) {
              enemyHeight = SquareType.ENEMY_HEIGHT;
              enemyWidth = SquareType.ENEMY_WIDTH;
            } else {
              enemyHeight = SquareType.SHELL_HEIGHT;
              enemyWidth = SquareType.SHELL_WIDTH;
            }

            if (enemyRow == player.getPlayerRow() + SquareType.PLAYER_HEIGHT && enemyCol <= player.getPlayerCol() && enemyCol + SquareType.ENEMY_WIDTH >= player.getPlayerCol() + SquareType.PLAYER_WIDTH) {
              if (enemyControllers[i].getEnemy().getEnemyStatus() == enemyControllers[i].getEnemy().WEAKENED) {
                player.addPoints(100);
              }

              if (player.getPlayerCol() < enemyCol + enemyWidth / 2) {
                enemyControllers[i].getEnemy().left = false;
              } else {
                enemyControllers[i].getEnemy().left = true;
              }
              enemyControllers[i].attackEnemy();

              player.setJumpPhase(player.JUMPING);
              player.setJumpTime(0);
              player.setPlayerLocation(player.getPlayerRow() - player.getJumpPhase(), player.getPlayerCol());
            }
          }

          if (gameBoard.getSquareType(player.getPlayerRow() + SquareType.PLAYER_HEIGHT, player.getPlayerCol()) != SquareType.EMPTY || gameBoard.getSquareType(player.getPlayerRow() + SquareType.PLAYER_HEIGHT, player.getPlayerCol() + SquareType.PLAYER_WIDTH - 1) != SquareType.EMPTY) {
            player.setJumpPhase(player.STATIONARY);
          }
        } else if (gameBoard.getSquareType(player.getPlayerRow() + SquareType.PLAYER_HEIGHT, player.getPlayerCol()) == SquareType.EMPTY && gameBoard.getSquareType(player.getPlayerRow() + SquareType.PLAYER_HEIGHT, player.getPlayerCol() + SquareType.PLAYER_WIDTH - 1) == SquareType.EMPTY) {
          player.setJumpPhase(player.FALLING);
        }

        if (direction[LEFT]) {
          if (playerDirection != SquareType.PLAYER_LEFT_STANDING && playerDirection != SquareType.PLAYER_LEFT_WALKING) {
            playerDirection = playerDirection + 1;
          }

          if (player.getPlayerCol() - player.PLAYER_SPEED < 0) {
            player.setPlayerLocation(player.getPlayerRow(), gameBoard.getBoardColumns() - 1 - SquareType.PLAYER_WIDTH);
          } else if (gameBoard.getSquareType(player.getPlayerRow(), player.getPlayerCol() - player.PLAYER_SPEED) == SquareType.EMPTY && gameBoard.getSquareType(player.getPlayerRow() + SquareType.PLAYER_HEIGHT - 1, player.getPlayerCol() - player.PLAYER_SPEED) == SquareType.EMPTY) {
            player.setPlayerLocation(player.getPlayerRow(), player.getPlayerCol() - player.PLAYER_SPEED);
          }
        } else if (direction[RIGHT]) {
          if (playerDirection != SquareType.PLAYER_RIGHT_STANDING && playerDirection != SquareType.PLAYER_RIGHT_WALKING) {
            playerDirection = playerDirection - 1;
          }

          if (player.getPlayerCol() + SquareType.PLAYER_WIDTH + player.PLAYER_SPEED >= gameBoard.getBoardColumns()) {
            player.setPlayerLocation(player.getPlayerRow(), 0);
          } else if (gameBoard.getSquareType(player.getPlayerRow(), player.getPlayerCol() + SquareType.PLAYER_WIDTH) == SquareType.EMPTY && gameBoard.getSquareType(player.getPlayerRow() + SquareType.PLAYER_HEIGHT - 1, player.getPlayerCol() + SquareType.PLAYER_WIDTH) == SquareType.EMPTY) {
            player.setPlayerLocation(player.getPlayerRow(), player.getPlayerCol() + player.PLAYER_SPEED);
          }
        }

        if (direction[UP] && player.getJumpPhase() == player.STATIONARY) {
          gameBoard.playSound(SoundType.JUMP);

          player.setJumpPhase(player.JUMPING);
          player.setJumpTime(0);
          player.setPlayerLocation(player.getPlayerRow() - player.getJumpPhase(), player.getPlayerCol());
        }

        gameBoard.setSquareType(previousPlayerRow, previousPlayerCol, SquareType.EMPTY);

        if (!direction[LEFT] && !direction[RIGHT]) {
          if (playerDirection == SquareType.PLAYER_RIGHT_WALKING || playerDirection == SquareType.PLAYER_RIGHT_STANDING) {
            gameBoard.setSquareType(player.getPlayerRow(), player.getPlayerCol(), SquareType.PLAYER_RIGHT_STANDING);
          } else if (playerDirection == SquareType.PLAYER_LEFT_WALKING || playerDirection == SquareType.PLAYER_LEFT_STANDING) {
            gameBoard.setSquareType(player.getPlayerRow(), player.getPlayerCol(), SquareType.PLAYER_LEFT_STANDING);
          }
        } else {
          if (playerDirection == SquareType.PLAYER_RIGHT_WALKING) {
            gameBoard.setSquareType(player.getPlayerRow(), player.getPlayerCol(), SquareType.PLAYER_RIGHT_STANDING);
          } else if (playerDirection == SquareType.PLAYER_LEFT_WALKING) {
            gameBoard.setSquareType(player.getPlayerRow(), player.getPlayerCol(), SquareType.PLAYER_LEFT_STANDING);
          } else if (playerDirection == SquareType.PLAYER_RIGHT_STANDING) {
            gameBoard.setSquareType(player.getPlayerRow(), player.getPlayerCol(), SquareType.PLAYER_RIGHT_WALKING);
          } else if (playerDirection == SquareType.PLAYER_LEFT_STANDING) {
            gameBoard.setSquareType(player.getPlayerRow(), player.getPlayerCol(), SquareType.PLAYER_LEFT_WALKING);
          }
        }

        for (int i = 0 ; i < enemyControllers.length ; i++) {
          int enemyRow = enemyControllers[i].getEnemy().getEnemyRow();
          int enemyCol = enemyControllers[i].getEnemy().getEnemyCol();
          int enemyHeight, enemyWidth;

          if (enemyControllers[i].getEnemy().getEnemyStatus() == enemyControllers[i].getEnemy().ALIVE) {
            enemyHeight = SquareType.ENEMY_HEIGHT;
            enemyWidth = SquareType.ENEMY_WIDTH;
          } else {
            enemyHeight = SquareType.SHELL_HEIGHT;
            enemyWidth = SquareType.SHELL_WIDTH;
          }

          if (((enemyRow >= player.getPlayerRow() && enemyRow <= player.getPlayerRow() + SquareType.PLAYER_HEIGHT) || (enemyRow + enemyHeight <= player.getPlayerRow() + SquareType.PLAYER_HEIGHT && enemyRow + enemyHeight >= player.getPlayerRow())) && enemyCol <= player.getPlayerCol() && enemyCol + enemyWidth >= player.getPlayerCol() + SquareType.PLAYER_WIDTH) {
            if (enemyControllers[i].getEnemy().getEnemyStatus() == enemyControllers[i].getEnemy().WEAKENED) {
              player.addPoints(100);
              if (player.getPlayerCol() < enemyCol + enemyWidth / 2) {
                enemyControllers[i].getEnemy().left = false;
              } else {
                enemyControllers[i].getEnemy().left = true;
              }
              enemyControllers[i].attackEnemy();
            } else {
              killPlayer(player.getPlayerRow(), player.getPlayerCol(), playerDirection);
            }
          }
        }

        for (int i = 0 ; i < itemControllers.length ; i++) {
          if (((itemControllers[i].getItem().getItemRow() >= player.getPlayerRow() && itemControllers[i].getItem().getItemRow() <= player.getPlayerRow() + SquareType.PLAYER_HEIGHT) || (itemControllers[i].getItem().getItemRow() + SquareType.ITEM_SIDE <= player.getPlayerRow() + SquareType.PLAYER_HEIGHT && itemControllers[i].getItem().getItemRow() + SquareType.ITEM_SIDE >= player.getPlayerRow())) && itemControllers[i].getItem().getItemCol() <= player.getPlayerCol() && itemControllers[i].getItem().getItemCol() + SquareType.ITEM_SIDE >= player.getPlayerCol() + SquareType.PLAYER_WIDTH) {
            player.addPoints(200);
            player.addCollected();
            gameBoard.setSquareType(itemControllers[i].getItem().getItemRow(), itemControllers[i].getItem().getItemCol(), SquareType.EMPTY);
            if (gameBoard.spawningSideLeft) {
              itemControllers[i].getItem().setItemLocation(0, 0);
              itemControllers[i].getItem().left = false;
              gameBoard.spawningSideLeft = false;
            } else {
              itemControllers[i].getItem().setItemLocation(0, gameBoard.getBoardColumns() - 1 - SquareType.ITEM_SIDE);
              itemControllers[i].getItem().left = true;
              gameBoard.spawningSideLeft = true;
            }
          }
        }
      }
    }
  }


  public void setMovingDirection(int dir, boolean b) {
    direction[dir] = b;
  }


  public Player getPlayer() {
    return player;
  }


  public void pause(boolean p) {
    pause = p;
  }


  public void killPlayer(int playerRow, int playerColumn, int playerDirection) {
    final int FALLING_SPEED = 40;

    gameBoard.setSquareType(playerRow, playerColumn, SquareType.EMPTY);
    int previousSquareType = SquareType.EMPTY;

    for (int i = playerRow ; i < gameBoard.getBoardRows() ; i++) {
      try {
        Thread.sleep(FALLING_SPEED);
      } catch (Exception e) {
        System.out.println(e);
      }
      gameBoard.setSquareType(i - 1, playerColumn, previousSquareType);
      previousSquareType = gameBoard.getSquareType(i, playerColumn);
      gameBoard.setSquareType(i, playerColumn, playerDirection);
    }
    gameBoard.setSquareType(gameBoard.getBoardRows() - 1, playerColumn, SquareType.EMPTY);
    gameBoard.gameOver(this, true);
  }
}
