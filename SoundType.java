/////////////////////////////////////////////////////////////
//class: SoundType
//Description: defines the different types of sounds in the game
//Programmer: Miaofei Mei
/////////////////////////////////////////////////////////////
public class SoundType {

  //NUM_SOUNDS is equal to how many sound type there are
  static private final int NUM_SOUNDS = 5;

  //declare a constant for each of your different sounds
  static public final int MENU = 0;
  static public final int THEME = 1;
  static public final int JUMP = 2;
  static public final int ATTACK = 3;
  static public final int KILL = 4;

  //***************************************************************************************
  //getNumTypes: returns the number of different sound types
  //
  static public int getNumTypes() {
    return NUM_SOUNDS;
  }
}
