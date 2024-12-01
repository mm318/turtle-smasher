/////////////////////////////////////////////////////////////
//class: HighScoreList
//Description: stores the list of high scores and has methods act on the list
//PRogrammer: Miaofei Mei
//Last Modified: June 2, 2005
/////////////////////////////////////////////////////////////

import java.io.*;

public class HighScoreList
{
    //list of users
    User playerList[];
    //***************************************************************************************
    //HighScoreList: constructor: initializes global fields including list
    //Parameters:
    //  maxPlayers: the max number of players in high score list
    //
    public HighScoreList (int maxPlayers)
    {
	playerList = new User [maxPlayers];

	for (int player = 0 ; player < playerList.length ; player++)
	    playerList [player] = new User ();
    }


    //***************************************************************************************
    //readList: read high score list from  a file
    //Parameters:
    //  filename: high score file name
    //completed by Ms. Cairns
    public boolean readList (String filename)
    {
	try // attempt to read from input stream
	{
	    FileReader file = new FileReader (filename);
	    BufferedReader buffer = new BufferedReader (file);

	    for (int player = 0 ; player < playerList.length ; player++)
		playerList [player].readUserInfo (buffer);  //this will fill user info from file

	    buffer.close ();
	    return true;
	}
	catch (IOException e)  //catch the error an print an error message
	{
	    System.out.println (e);
	    return false;
	}
    }


    public void sortList ()
    {
	User tempUser;

	for (int i = 0 ; i < playerList.length ; i++)
	{
	    for (int j = 1 ; j < 10 - i ; j++)
	    {
		if (playerList [j].compareTo (playerList [j - 1]) == 1)
		{
		    tempUser = playerList [j];
		    playerList [j] = playerList [j - 1];
		    playerList [j - 1] = tempUser;
		}
	    }
	}
    }


    //***************************************************************************************
    //updateList: add player in order
    //Parameters:
    //  player: player to add to list
    //return: true if player made list; false otherwise
    public boolean updateList (User player)  //updates list if score warrants it; high enough
    {
	int i;

	for (i = playerList.length - 1 ; i > -1 ; i--)
	{
	    if (player.compareTo (playerList [i]) != 1)
		break;
	}
	i = i + 1;

	if (i > playerList.length - 1)
	    return false;
	else
	{
	    User tempUser = playerList [i];
	    playerList [i] = player;
	    for (int j = i + 1 ; j < playerList.length - 1 ; j++)
	    {
		playerList [j] = tempUser;
		tempUser = playerList [j + 1];
	    }
	    return true;
	}
    }


    //***************************************************************************************
    //saveList: save high score list to file
    //Parameters:
    //  filename: high score file name
    //return: true if list could be written to file
    //completed by Ms. Cairns
    public boolean saveList (String filename)
    {
	try // attempt to read from input stream
	{
	    FileWriter file = new FileWriter (filename);
	    BufferedWriter buffer = new BufferedWriter (file);

	    for (int player = 0 ; player < playerList.length ; player++)
		playerList [player].writeUserInfo (buffer);  //this will fill user info from file

	    buffer.close ();
	    return true;
	}
	catch (IOException e)  // catch the error an print an error message
	{
	    System.out.println (e);
	    return false;
	}
    }


    //***************************************************************************************
    //getPlayer: get player info from a particular position in the list
    //Parameters:
    //  position: index of player within the list
    //return: User information
    public User getPlayer (int position)
    {
	return playerList [position];
    }


    //***************************************************************************************
    //makeList: check whether the given player has made the highscore list
    //Parameters:
    //  player: player to check
    //return:  true if the given score is good enough to make the list... false otherwise
    public boolean makeList (User player)
    {
	if (player.compareTo (playerList [playerList.length - 1]) > 0)
	    return true;
	else
	    return false;
    }
}
