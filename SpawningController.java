/////////////////////////////////////////////////////////////
//class: PlayerController
//Description:  container the logic that control the enemy's action
//              This class extends Thread because it runs concurrently with
//              game gameBoard's main thread of the action
//PRogrammer: Miaofei Mei
//Last Modified: June 2, 2005
/////////////////////////////////////////////////////////////

import java.awt.event.KeyEvent;
import java.lang.Object;

public class SpawningController extends Thread
{
    private final int SPAWNING_INTERVAL = 1000;

    private GameBoard gameBoard;
    private EnemyController enemyControllers[];     //for communicating with other controllers
    private ItemController itemControllers[];

    public SpawningController (GameBoard board, EnemyController enemies[], ItemController items[])
    {
	gameBoard = board;
	enemyControllers = enemies;
	itemControllers = items;
    }


    public void run ()
    {
	for (int i = 0 ; i < enemyControllers.length ; i++)
	{
	    if (gameBoard.spawningSideLeft)
	    {
		enemyControllers [i].getEnemy ().setEnemyLocation (0, 0);
		enemyControllers [i].getEnemy ().left = false;
		gameBoard.spawningSideLeft = false;
	    }
	    else
	    {
		enemyControllers [i].getEnemy ().setEnemyLocation (0, gameBoard.getBoardColumns () - 1 - SquareType.ENEMY_WIDTH);
		enemyControllers [i].getEnemy ().left = true;
		gameBoard.spawningSideLeft = true;
	    }
	    enemyControllers [i].start ();

	    try
	    {
		Thread.sleep (SPAWNING_INTERVAL);
	    }
	    catch (Exception e)
	    {
		System.out.println (e);
	    }
	}

	for (int i = 0 ; i < itemControllers.length ; i++)
	{
	    if (gameBoard.spawningSideLeft)
	    {
		itemControllers [i].getItem ().setItemLocation (0, 0);
		itemControllers [i].getItem ().left = false;
		gameBoard.spawningSideLeft = false;
	    }
	    else
	    {
		itemControllers [i].getItem ().setItemLocation (0, gameBoard.getBoardColumns () - 1 - SquareType.ITEM_SIDE);
		itemControllers [i].getItem ().left = true;
		gameBoard.spawningSideLeft = true;
	    }
	    itemControllers [i].start ();

	    try
	    {
		Thread.sleep (SPAWNING_INTERVAL);
	    }
	    catch (Exception e)
	    {
		System.out.println (e);
	    }
	}

	stop ();
    }
}
