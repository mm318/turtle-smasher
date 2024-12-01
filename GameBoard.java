/////////////////////////////////////////////////////////////
//class: GameBoard
//Description: handles the game strategy of Turtle Smasher
//PRogrammer: Miaofei Mei
//Last Modified: June 2, 2005
/////////////////////////////////////////////////////////////

import javax.swing.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class GameBoard
{
    //global instance constants
    public final int TOTAL_TIME = 300; //time to play in seconds
    public final int MIN_ITEMS = 5;

    //global instance variables
    private int board[] [];
    private GameGui Gui;
    private PlayerController playerControllers[];
    private EnemyController enemyControllers[];
    private ItemController itemControllers[];

    public boolean spawningSideLeft = true;
    private SpawningController spawningController;

    //***************************************************************************************
    //GameBoard: constructor: initialize board and give values to instance variables
    //Parameters:
    //  maxRows,maxCols: board dimensions
    //  Gui: gui interface for the game
    public GameBoard (int maxRows, int maxCols, int tileRows, int tileCols, int numPlayers, GameGui gui)
    {
	Gui = gui;
	board = new int [maxRows] [maxCols];

	initBoard (tileRows, tileCols);         //populate the initial board (in memory)

	itemControllers = new ItemController [5];
	for (int i = 0 ; i < 5 ; i++)
	    itemControllers [i] = new ItemController (this);

	playerControllers = new PlayerController [numPlayers];
	for (int i = 0 ; i < numPlayers ; i++)
	    playerControllers [i] = new PlayerController (this, i, (maxRows - SquareType.TILE_HEIGHT - SquareType.PLAYER_HEIGHT), maxCols / 2 + (i * 5), MIN_ITEMS, enemyControllers, itemControllers);
	for (int i = 0 ; i < playerControllers.length ; i++)
	    playerControllers [i].start ();

	spawningController = new SpawningController (this, enemyControllers, itemControllers);
	spawningController.start ();
    }


    //***************************************************************************************
    //initBoard: initializes the board; sets up what is on each square
    public void initBoard (int row, int col)
    {
	RandomLevelGenerator.generateLevel ("board/level.txt");
	try
	{
	    FileReader file = new FileReader ("board/level.txt");
	    BufferedReader buffer = new BufferedReader (file);

	    int input;
	    int height = 0, width = 0;
	    final int y_increment = getBoardRows () / (row + 1);
	    final int x_increment = getBoardColumns () / (col);

	    for (int i = 0 ; i < row ; i++)
	    {
		for (int j = 0 ; j < col ; j++)
		{
		    input = buffer.read () - 'A';
		    board [y_increment * (i + 1) - SquareType.TILE_HEIGHT] [x_increment * j] = input;

		    if (input == SquareType.TILE) //filling in the area of the table for later logics (squaretype*100+squaretype)
		    {
			height = SquareType.TILE_HEIGHT;
			width = SquareType.TILE_WIDTH;

			for (int k = 0 ; k < height ; k++)
			{
			    for (int l = 0 ; l < width ; l++)
			    {
				if (!(k == 0 && l == 0))
				    board [y_increment * (i + 1) - SquareType.TILE_HEIGHT + k] [x_increment * j + l] = input * 100 + input;
			    }
			}
		    }
		    buffer.read ();
		}
		buffer.readLine ();
	    }

	    for (int i = 0 ; i < col ; i++)
	    {
		board [y_increment * (row + 1) - SquareType.TILE_HEIGHT] [x_increment * i] = SquareType.TILE;

		height = SquareType.TILE_HEIGHT;
		width = SquareType.TILE_WIDTH;

		for (int k = 0 ; k < height ; k++)
		{
		    for (int l = 0 ; l < width ; l++)
		    {
			if (!(k == 0 && l == 0))
			    board [y_increment * (row + 1) - SquareType.TILE_HEIGHT + k] [x_increment * i + l] = SquareType.TILE * 100 + SquareType.TILE;
		    }
		}
	    }

	    int count = 0;
	    String input2 = buffer.readLine ();
	    for (int i = 0 ; i < input2.length () ; i++)
	    {
		if (input2.charAt (i) >= '1' && input2.charAt (i) <= '9')
		    count = count + 1;
	    }
	    enemyControllers = new EnemyController [count];
	    count = -1;
	    for (int i = 0 ; i < input2.length () ; i++)
	    {
		if (input2.charAt (i) >= '1' && input2.charAt (i) <= '9')
		{
		    count = count + 1;
		    enemyControllers [count] = new EnemyController (this, input2.charAt (i) - ('1' - 1), enemyControllers);
		}
	    }

	    buffer.close ();
	}
	catch (Exception e)
	{
	    System.out.print (e);
	}
    }


    //***************************************************************************************
    //handleKeyPress: called when a key has been pressed.  Takes appropriate action based on the key pressed and
    //and updates the memory and the screen
    //Parameters:
    //  key: key that was pressed by the user
    public void handleKeyPressed (int key)
    {
	for (int i = 0 ; i < playerControllers.length ; i++)
	{
	    if (key == playerControllers [i].left)
	    {
		playerControllers [i].setMovingDirection (playerControllers [i].LEFT, true);
		playerControllers [i].setMovingDirection (playerControllers [i].RIGHT, false);
	    }
	    else if (key == playerControllers [i].right)
	    {
		playerControllers [i].setMovingDirection (playerControllers [i].RIGHT, true);
		playerControllers [i].setMovingDirection (playerControllers [i].LEFT, false);
	    }
	    else if (key == playerControllers [i].up)
		playerControllers [i].setMovingDirection (playerControllers [i].UP, true);
	}
    }


    public void handleKeyReleased (int key)
    {
	for (int i = 0 ; i < playerControllers.length ; i++)
	{
	    if (key == playerControllers [i].left)
		playerControllers [i].setMovingDirection (playerControllers [i].LEFT, false);
	    else if (key == playerControllers [i].right)
		playerControllers [i].setMovingDirection (playerControllers [i].RIGHT, false);
	    else if (key == playerControllers [i].up)
		playerControllers [i].setMovingDirection (playerControllers [i].UP, false);
	}
    }


    public void pauseGame ()
    {
	for (int i = 0 ; i < enemyControllers.length ; i++)
	    enemyControllers [i].pause (true);
	for (int i = 0 ; i < itemControllers.length ; i++)
	    itemControllers [i].pause (true);
	for (int i = 0 ; i < playerControllers.length ; i++)
	    playerControllers [i].pause (true);
    }


    public void unpauseGame ()
    {
	for (int i = 0 ; i < enemyControllers.length ; i++)
	    enemyControllers [i].pause (false);
	for (int i = 0 ; i < itemControllers.length ; i++)
	    itemControllers [i].pause (false);
	for (int i = 0 ; i < playerControllers.length ; i++)
	    playerControllers [i].pause (false);
    }


    //***************************************************************************************
    //setSquareType: sets the square type of a particular location
    //Parameters:
    //  row,col: location within board
    //  type: type of square (eg. player, empty...)
    public void setSquareType (int row, int col, int type)
    {
	board [row] [col] = type;
    }


    //***************************************************************************************
    //getSquareType: returns the square type of the given location
    //Parameters:
    //  row,col: square location
    public int getSquareType (int row, int col)
    {
	return board [row] [col];
    }


    public int getBoardRows ()
    {
	return board.length;
    }


    public int getBoardColumns ()
    {
	return board [0].length;
    }


    public void setScore (int index, int points)
    {
	playerControllers [index].getPlayer ().addPoints (points);
    }


    //***************************************************************************************
    //getScore: get the player's score
    //
    public int getScore (int index)
    {
	return playerControllers [index].getPlayer ().getPoints ();
    }


    public int getNumCollected (int index)
    {
	return playerControllers [index].getPlayer ().getNumCollected ();
    }


    public void gameOver (PlayerController playerController, boolean lost)
    {
	if (playerControllers.length == 1)
	{
	    if (lost)
		Gui.gameOver (playerController.getPlayer ().getPoints ());
	    else
		Gui.nextLevel (playerController.getPlayer ().getPoints ());

	    for (int i = 0 ; i < enemyControllers.length ; i++)
		enemyControllers [i].stop ();
	    for (int i = 0 ; i < itemControllers.length ; i++)
		itemControllers [i].stop ();
	    for (int i = 0 ; i < playerControllers.length ; i++)
		playerControllers [i].stop ();
	}

	else
	{
	    if (lost)
	    {
		playerController.stop ();
		boolean areSurvivors = false;
		for (int i = 0 ; i < playerControllers.length ; i++)
		{
		    System.out.println (playerControllers [i].isAlive ());
		    if (playerControllers [i].isAlive ())
			areSurvivors = true;
		}
		if (!areSurvivors)
		    Gui.gameOverMultiplayer (0);
	    }
	    else if (!lost)
	    {
		Gui.gameOverMultiplayer (playerController.playerNum + 1);
		for (int i = 0 ; i < enemyControllers.length ; i++)
		    enemyControllers [i].stop ();
		for (int i = 0 ; i < itemControllers.length ; i++)
		    itemControllers [i].stop ();
		for (int i = 0 ; i < playerControllers.length ; i++)
		    playerControllers [i].stop ();
	    }
	}
    }


    public void playSound (int soundType)
    {
	Gui.playSound (soundType);
    }
}
