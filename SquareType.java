/////////////////////////////////////////////////////////////
//class: SquareType
//Description: defines the square types
//PRogrammer: Miaofei Mei
//Last Modified: June 2, 2005
/////////////////////////////////////////////////////////////

public class SquareType
{
    //NUM_TYPES is equal to how many square type there are in the game
    static private final int NUM_TYPES = 11;

    //declare a constant for each of your different square types
    public static final int EMPTY = 0;
    public static final int TILE = 1;
    public static final int EXIT = 2;
    public static final int PLAYER_RIGHT_STANDING = 3;
    public static final int PLAYER_LEFT_STANDING = 4;
    public static final int PLAYER_RIGHT_WALKING = 5;
    public static final int PLAYER_LEFT_WALKING = 6;
    public static final int ENEMY_RIGHT = 7;
    public static final int ENEMY_LEFT = 8;
    public static final int ENEMY_WEAKENED = 9;
    public static final int ITEM_COLLECTIBLE = 10;

    public static final int TILE_HEIGHT = 7;
    public static final int TILE_WIDTH = 7;
    public static final int EXIT_HEIGHT = 12;
    public static final int EXIT_WIDTH = 9;
    public static final int PLAYER_HEIGHT = 7;
    public static final int PLAYER_WIDTH = 3;
    public static final int ENEMY_HEIGHT = 4;
    public static final int ENEMY_WIDTH = 6;
    public static final int SHELL_HEIGHT = 4;
    public static final int SHELL_WIDTH = 5;
    public static final int ITEM_SIDE = 4;


    //***************************************************************************************
    //getNumTypes: returns the number of different square types
    //
    public static int getNumTypes ()
    {
	return NUM_TYPES;   //return the number of types declared above
    }
}
