/////////////////////////////////////////////////////////////
//class: Player
//Description: stores player data and methods that act on that data
//Programmer: Miaofei Mei
/////////////////////////////////////////////////////////////

import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.awt.event.*;

public class Player {
  public final int PLAYER_SPEED = 1; //a speed of 1 grid per move

  private int jumpPhase;            //current jumping phase(gaining height, falling, etc.)
  public final int JUMPING = 1, FALLING = -1, STATIONARY = 0;
  private int jumpTime;             //determines duration of jump
  public final int JUMP_DURATION = 20;  //defines the duration allowed for gaining height, falling, etc.

  private int points;                 //player points
  private int playerRow, playerCol;   //current location of player
  private int min_to_collect, num_collected;        //minimum number of items to collect

  //***************************************************************************************
  //Player: constructor: initializes all of the player data
  //
  public Player() {                  //constructor
    //initialize all instance variables
    points = 0;
  }


  //***************************************************************************************
  //setMinToCollect: set the minimum number of items to collect before exit
  //Parameters:
  //  amount: the minimum number of items
  //
  public void setMinToCollect(int amount) {
    min_to_collect = amount;
  }


  //***************************************************************************************
  //getMinToCollect: get the minimum number of items to collect before exit
  // returns: minimum items to collect
  public int getMinToCollect() {
    return min_to_collect;
  }


  //***************************************************************************************
  //isFullyCollected: indicates whether the player has collected all necessary items
  //returns: true if  collected >= minimum required otherwise false
  public boolean isFullyCollected() {
    if (num_collected >= min_to_collect) {
      return true;
    } else {
      return false;
    }
  }


  //***************************************************************************************
  //addCollected: increase the number of items collected
  public void addCollected() {
    num_collected++;
  }


  //***************************************************************************************
  //getNumCollected: get the  number of items collected
  //returns: how many items currently collected
  public int getNumCollected() {
    return num_collected;
  }


  //***************************************************************************************
  //addPoints: add amount points to their score
  //Parameters:
  //  amount: the amount of points to add
  //
  public void addPoints(int amount) {
    points = points + amount;
  }


  //***************************************************************************************
  //subtractPoints: subtract amount points to their score
  //Parameters:
  //  amount: the amount of points to subtract
  //
  public void subtractPoints(int amount) {
    points = points - amount;
  }


  //***************************************************************************************
  //getPoints: return the current points
  //returns: return the current points
  public int getPoints() {
    return points;
  }


  //***************************************************************************************
  //setPlayerLocation: set player location on board
  //Parameters:
  //  row,col: row and column of player on board
  //
  public void setPlayerLocation(int row, int col) {
    playerRow = row;
    playerCol = col;
  }


  public void setJumpPhase(int phase) {
    jumpPhase = phase;
  }


  public void setJumpTime(int time) {
    jumpTime = time;
  }


  //***************************************************************************************
  //getPlayerRow: get row location of player
  //
  public int getPlayerRow() {
    return playerRow;
  }


  //***************************************************************************************
  //getPlayerCol: get column location of player
  //
  public int getPlayerCol() {
    return playerCol;
  }


  public int getJumpPhase() {
    return jumpPhase;
  }


  public int getJumpTime() {
    return jumpTime;
  }
}
