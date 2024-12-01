/////////////////////////////////////////////////////////////
//class: User
//Description: a user record which stores name and score
//PRogrammer: Miaofei Mei
//Last Modified: June 2, 2005
/////////////////////////////////////////////////////////////

import java.io.*;   //so it can use BufferedReader

public class User
{
    private String name = "";
    private int score = 0;

    //***************************************************************************************
    //User: constructor initializes the name and score to default values
    //
    public User ()
    {
    }


    //***************************************************************************************
    //setName: sets the name field
    //Parameters:
    //  name: player name
    //
    public void setName (String n)
    {
	name = n;
    }


    //***************************************************************************************
    //setScore: sets the score field
    //Parameters:
    //  score: player score
    //
    public void setScore (int s)
    {
	score = s;
    }


    //***************************************************************************************
    //getName: returns the value of the name field
    //
    public String getName ()
    {
	return name;
    }


    //***************************************************************************************
    //getScore: returns the value of the score field
    //
    public int getScore ()
    {
	return score;
    }


    //***************************************************************************************
    //readUserInfo: reads a users information in from a file pointed to by buffer
    //Parameters:
    //  buffer: file pointer
    //throw: throws an IO error if file can't be read from
    public void readUserInfo (BufferedReader buffer) throws IOException
    {
	name = buffer.readLine ();
	score = Integer.parseInt (buffer.readLine ());
    }


    //***************************************************************************************
    //writeUserInfo: writes a users information to a file pointed to by buffer
    //Parameters:
    //  buffer: file pointer
    //throw: throws an IO error if file can't be written to
    public void writeUserInfo (BufferedWriter buffer) throws IOException
    {
	buffer.write (name, 0, name.length ());
	buffer.newLine ();
	buffer.write (Integer.toString (score), 0, Integer.toString (score).length ());
	buffer.newLine ();
    }


    //***************************************************************************************
    //compareTo: compares the score of the parameter u to this classe's user score
    //Parameters:
    //  u: an user
    //return values
    // 0: returned if user scores are equal
    //-1: returned if class user score is less than parameter's user score
    // 1: returned if class user score is greater than parameter's user score
    public int compareTo (User u)
    {
	if (score < u.getScore ())
	    return -1;
	else if (score > u.getScore ())
	    return 1;
	else
	    return 0;
    }
}
