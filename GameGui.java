/////////////////////////////////////////////////////////////
//class: GameGui
//Description: handles the GUI of Turtle Smasher
//Programmer: Miaofei Mei
/////////////////////////////////////////////////////////////

import javax.swing.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.Time;

//this applet is the culminating game
public class GameGui extends JApplet implements KeyListener {
  public int LEVEL_NUM;
  public int NUM_PLAYERS;                         //up to 3 players, others won't have keys

  public static final int BOARD_COLUMNS = 119;        //number of board columns
  public static final int BOARD_ROWS = 95;            //number of board rows
  private static final int TILES_COLUMNS_NUM = 17;    //number of tile columns
  private static final int TILES_ROWS_NUM = 4;        //number of tile rows
  private static final int COLUMN_WIDTH = 5;          //pixel width of board column
  private static final int ROW_HEIGHT = 5;            //pixel height of board row
  private static final int APPLET_WIDTH = BOARD_COLUMNS * COLUMN_WIDTH;   //applet window width
  private static final int APPLET_HEIGHT = BOARD_ROWS * ROW_HEIGHT;       //applet window height

  private final Color BACKGROUND = Color.black, UNSELECTED = Color.white, SELECTED = Color.red, GAME_STATS = new Color(49, 142, 1);

  private Image doubleBufferImage;
  private Graphics doubleBufferGraphics;

  Timer timer;
  private int time;
  private String timeLeft = "";
  private static final int UPDATE_SECONDS = 1;        //how often to update timer

  private ImageIcon imageList[];                      //a list of picture files associate with different square types
  private ImageIcon gameBackground;
  private ImageIcon logoIcon;
  private String soundList[];                         //a list of sound files associated with different square types

  private GameBoard board;                            //the game board; keeps track of the board in memory

  private int gameStatus = 0;                         //int indicating the screen at which the game is
  private final int MAIN_MENU = 0, CHOOSE_NUMPLAYER = 1, HIGH_SCORES = 2, HIGH_SCORES_PAUSED = 3, IN_GAME_INTRO = 4, IN_GAME = 5, IN_GAME_PAUSED = 6, GAME_OVER = 7;

  private int menuOption = 0;
  private final int OPTION1 = 0, OPTION2 = 1, OPTION3 = 2, OPTION4 = 3;

  private static final int MAX_HIGH_SCORES = 10;
  private static String HIGHSCORE_FILE = "highscores/fame.txt";
  HighScoreList playerList;                           //high score list
  User player;
  String playerName = "";


  //***************************************************************************************
  //init: initializes the applet window and other features
  //
  public void init() {
    setSize(APPLET_WIDTH, APPLET_HEIGHT);          //set applet window dimensions

    addKeyListener(this);                          //??
    doubleBufferImage = createImage(this.getSize().width, this.getSize().height);
    doubleBufferGraphics = doubleBufferImage.getGraphics();

    initImageList();                               //initialize game's list of images
    initSoundList();
    logoIcon = new ImageIcon("images/Logo.png");
    gameBackground = new ImageIcon("images/Background.jpg");

    requestFocus();

    repaint();
  } // init method


  //***************************************************************************************
  //displayMain: sets up the main menu
  public void displayMain(Graphics g) {
    g.setFont(new Font("ARIAL", Font.BOLD, 18));

    g.setColor(BACKGROUND);
    g.fillRect(0, 0, APPLET_WIDTH, APPLET_HEIGHT);

    logoIcon.paintIcon(this, g, (APPLET_WIDTH - logoIcon.getIconWidth()) / 2, 20);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION1) {
      g.setColor(SELECTED);
    }
    g.drawString("New Game", 230, 300);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION2) {
      g.setColor(SELECTED);
    }
    g.drawString("High Scores", 230, 320);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION3) {
      g.setColor(SELECTED);
    }
    g.drawString("Quit", 230, 340);
  }


  public void displayNumPlayer(Graphics g) {
    g.setFont(new Font("ARIAL", Font.BOLD, 18));

    g.setColor(BACKGROUND);
    g.fillRect(0, 0, APPLET_WIDTH, APPLET_HEIGHT);

    logoIcon.paintIcon(this, g, (APPLET_WIDTH - logoIcon.getIconWidth()) / 2, 20);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION1) {
      g.setColor(SELECTED);
    }
    g.drawString("1 Player", 230, 300);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION2) {
      g.setColor(SELECTED);
    }
    g.drawString("2 Players", 230, 320);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION3) {
      g.setColor(SELECTED);
    }
    g.drawString("3 Players", 230, 340);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION4) {
      g.setColor(SELECTED);
    }
    g.drawString("4 Players", 230, 360);
  }


  //***************************************************************************************
  //displayGame: display gaming screen
  public void displayGame(Graphics g) {
    g.setFont(new Font("ARIAL", Font.BOLD, 18));

    gameBackground.paintIcon(this, g, 0, 0);
    imageList[SquareType.EXIT].paintIcon(this, g, (BOARD_COLUMNS - SquareType.EXIT_WIDTH) / 2 * COLUMN_WIDTH, ((BOARD_ROWS + 1) - SquareType.TILE_HEIGHT - SquareType.EXIT_HEIGHT) * ROW_HEIGHT);

    for (int i = 0 ; i < BOARD_ROWS ; i = i + 1) {
      for (int j = 0 ; j < BOARD_COLUMNS ; j = j + 1) {
        if (board.getSquareType(i, j) > 0 && board.getSquareType(i, j) <= 11) {
          imageList[board.getSquareType(i, j)].paintIcon(this, g, j * COLUMN_WIDTH, i * ROW_HEIGHT);
        }
      }
    }

    g.setColor(GAME_STATS);
    if (NUM_PLAYERS == 1) {
      g.setColor(GAME_STATS);
      g.fillRect(30, 5, 130, 25);
      for (int i = 0 ; i < board.getNumCollected(0) && i < 5 ; i++) {
        imageList[SquareType.ITEM_COLLECTIBLE].paintIcon(this, g, i * 25 + 35, 8);
      }
      g.drawString(timeLeft, 260, 20);
      String score = Integer.toString(board.getScore(0));
      while (score.length() < 6) {
        score = " " + score;
      }
      g.drawString(score, 510, 20);
      g.drawString("Level: " + Integer.toString(LEVEL_NUM), 263, 470);
    } else {
      g.drawString(timeLeft, 260, 20);
      g.drawString("Round: " + Integer.toString(LEVEL_NUM), 263, 470);
    }
  }


  public void displayPausedGame(Graphics g) {
    displayGame(g);

    g.setColor(BACKGROUND);
    g.fillRect((APPLET_WIDTH - 400) / 2, (APPLET_HEIGHT - 300) / 2, 400, 300);

    logoIcon.paintIcon(this, g, (APPLET_WIDTH - logoIcon.getIconWidth()) / 2, 110);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION1) {
      g.setColor(SELECTED);
    }
    g.drawString("Resume Game", 230, 300);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION2) {
      g.setColor(SELECTED);
    }
    g.drawString("High Scores", 230, 320);

    g.setColor(UNSELECTED);
    if (menuOption == OPTION3) {
      g.setColor(SELECTED);
    }
    g.drawString("Quit Game", 230, 340);
  }


  //***************************************************************************************
  //displayHighScores: display HighScores screen
  public void displayHighScores(Graphics g) {
    g.setFont(new Font("ARIAL", Font.BOLD, 18));

    g.setColor(BACKGROUND);
    g.fillRect(0, 0, APPLET_WIDTH, APPLET_HEIGHT);

    logoIcon.paintIcon(this, g, (APPLET_WIDTH - logoIcon.getIconWidth()) / 2, 20);

    g.setColor(UNSELECTED);
    g.drawString("High Scores", 140, 190);
    g.drawString("Player:", 140, 230);
    g.drawString("Score:", 420, 230);
    for (int i = 0 ; i < MAX_HIGH_SCORES ; i++) {
      g.drawString(Integer.toString(i + 1) + ".", 90, 250 + 20 * i);
      g.drawString(playerList.getPlayer(i).getName(), 140, 250 + 20 * i);
      String score = Integer.toString(playerList.getPlayer(i).getScore());
      while (score.length() < 6) {
        score = " " + score;
      }
      g.drawString(score, 450, 250 + 20 * i);
    }

    g.setColor(SELECTED);
    if (gameStatus == HIGH_SCORES_PAUSED) {
      g.drawString("BACK TO GAME MENU", 190, 460);
    } else {
      g.drawString("BACK TO MAIN MENU", 190, 460);
    }
  }


  public void nextLevel(int points) {
    LEVEL_NUM++;
    board = new GameBoard(BOARD_ROWS, BOARD_COLUMNS, TILES_ROWS_NUM, TILES_COLUMNS_NUM, NUM_PLAYERS, this);
    board.setScore(0, points);
    repaint();
  }


  public void gameOver(int points) {
    initHighScores();
    player = new User();
    player.setScore(points);

    gameStatus = GAME_OVER;
    board = null;
  }


  public void gameOverMultiplayer(int winner) {
    System.out.println("the winner is player " + Integer.toString(winner));
  }


  public void displayGameOver(Graphics g) {
    g.setFont(new Font("ARIAL", Font.BOLD, 18));

    g.setColor(BACKGROUND);
    g.fillRect(0, 0, APPLET_WIDTH, APPLET_HEIGHT);

    logoIcon.paintIcon(this, g, (APPLET_WIDTH - logoIcon.getIconWidth()) / 2, 20);

    g.setColor(UNSELECTED);
    g.drawString("GAME OVER", 230, 200);

    if (playerList.makeList(player)) {
      g.drawString("You have made it onto the high scores list!", 100, 240);
      g.drawString("Please enter your name: ", 100, 280);
      g.drawString("Your name is: " + playerName + "_", 100, 320);
      g.drawString("Your score is: " + Integer.toString(player.getScore()), 100, 360);
    } else {
      g.drawString("You have not made it onto the high scores list", 100, 280);
      g.drawString("Press Enter to return to the main menu.", 100, 360);
      g.setColor(SELECTED);
      g.drawString("BACK TO MAIN MENU", 190, 460);
    }
  }


  //***************************************************************************************
  //initImageList: sets up the imagefiles for each square type
  ////student to complete
  public void initImageList() {
    imageList = new ImageIcon[SquareType.getNumTypes()];

    imageList[SquareType.TILE] = new ImageIcon("images/Tile.gif");
    imageList[SquareType.EXIT] = new ImageIcon("images/Exit.gif");
    imageList[SquareType.PLAYER_RIGHT_STANDING] = new ImageIcon("images/Humanoid_right_standing.png");
    imageList[SquareType.PLAYER_LEFT_STANDING] = new ImageIcon("images/Humanoid_left_standing.png");
    imageList[SquareType.PLAYER_RIGHT_WALKING] = new ImageIcon("images/Humanoid_right_walking.png");
    imageList[SquareType.PLAYER_LEFT_WALKING] = new ImageIcon("images/Humanoid_left_walking.png");
    imageList[SquareType.ENEMY_RIGHT] = new ImageIcon("images/Turtle_right.png");
    imageList[SquareType.ENEMY_LEFT] = new ImageIcon("images/Turtle_left.png");
    imageList[SquareType.ENEMY_WEAKENED] = new ImageIcon("images/Shell.png");
    imageList[SquareType.ITEM_COLLECTIBLE] = new ImageIcon("images/Unit.png");
  }


  //***************************************************************************************
  //initSoundList: sets up the sound files for each sound type
  //student to complete
  public void initSoundList() {
    soundList = new String[SoundType.getNumTypes()];

    soundList[SoundType.MENU] = ("sound/menuOption.wav");
    soundList[SoundType.THEME] = ("");
    soundList[SoundType.JUMP] = ("sound/jump.wav");
    soundList[SoundType.ATTACK] = ("");
    soundList[SoundType.KILL] = ("");
  }


  public void initHighScores() {
    playerList = new HighScoreList(MAX_HIGH_SCORES);
    playerList.readList(HIGHSCORE_FILE);
    playerList.sortList();
    playerList.saveList(HIGHSCORE_FILE);
  }


  //***************************************************************************************
  //keyPressed: invoked(runs) when a key is pressed on the keyboard.  passes the key to the game to take action.
  //Parameters:
  //  evt: contains information about the key press event
  //
  public void keyPressed(KeyEvent evt) {
    int key = evt.getKeyCode();    //the Unicode of the pressed key

    if (gameStatus == IN_GAME) {
      if (key == KeyEvent.VK_ESCAPE) {
        timer.stop();
        gameStatus = IN_GAME_PAUSED;
        menuOption = OPTION1;
        board.pauseGame();
        repaint();
      } else {
        board.handleKeyPressed(key);
      }
    } else if (gameStatus == IN_GAME_PAUSED) {
      if (key == KeyEvent.VK_ESCAPE) {
        timer.start();
        gameStatus = IN_GAME;
        board.unpauseGame();
        repaint();
      } else if (key == KeyEvent.VK_UP && menuOption > OPTION1) {
        menuOption = menuOption - 1;
        playSound(SoundType.MENU);
        repaint();
      } else if (key == KeyEvent.VK_DOWN && menuOption < OPTION3) {
        menuOption = menuOption + 1;
        playSound(SoundType.MENU);
        repaint();
      } else if (key == KeyEvent.VK_ENTER) {
        if (menuOption == OPTION1) {
          timer.start();
          gameStatus = IN_GAME;
          board.unpauseGame();
          repaint();
        } else if (menuOption == OPTION2) {
          timer.stop();
          gameStatus = HIGH_SCORES_PAUSED;
          initHighScores();
          repaint();
        } else if (menuOption == OPTION3) {
          timer.stop();
          board = null;
          gameStatus = MAIN_MENU;
          menuOption = OPTION1;
          repaint();
        }
      }
    } else if (gameStatus == MAIN_MENU) {
      if (key == KeyEvent.VK_UP && menuOption > OPTION1) {
        menuOption = menuOption - 1;
        playSound(SoundType.MENU);
        repaint();
      } else if (key == KeyEvent.VK_DOWN && menuOption < OPTION3) {
        menuOption = menuOption + 1;
        playSound(SoundType.MENU);
        repaint();
      } else if (key == KeyEvent.VK_ENTER) {
        if (menuOption == OPTION1) {
          gameStatus = CHOOSE_NUMPLAYER;
          LEVEL_NUM = 1;
          menuOption = OPTION1;
          repaint();
        } else if (menuOption == OPTION2) {
          gameStatus = HIGH_SCORES;
          initHighScores();
          repaint();
        } else if (menuOption == OPTION3) {
          quit();
        }
      }
    } else if (gameStatus == CHOOSE_NUMPLAYER) {
      if (key == KeyEvent.VK_UP && menuOption > OPTION1) {
        menuOption = menuOption - 1;
        playSound(SoundType.MENU);
        repaint();
      } else if (key == KeyEvent.VK_DOWN && menuOption < OPTION4) {
        menuOption = menuOption + 1;
        playSound(SoundType.MENU);
        repaint();
      } else if (key == KeyEvent.VK_ENTER) {
        if (menuOption == OPTION1) {
          NUM_PLAYERS = 1;
          gameStatus = IN_GAME;
          repaint();
        } else if (menuOption == OPTION2) {
          NUM_PLAYERS = 2;
          gameStatus = IN_GAME;
          repaint();
        } else if (menuOption == OPTION3) {
          NUM_PLAYERS = 3;
          gameStatus = IN_GAME;
          repaint();
        } else if (menuOption == OPTION4) {
          NUM_PLAYERS = 4;
          gameStatus = IN_GAME;
          repaint();
        }
      } else if (key == KeyEvent.VK_ESCAPE) {
        gameStatus = MAIN_MENU;
        repaint();
      }
    } else if (gameStatus == HIGH_SCORES || gameStatus == HIGH_SCORES_PAUSED) {
      if (key == KeyEvent.VK_ENTER) {
        playerList.saveList(HIGHSCORE_FILE);
        if (gameStatus == HIGH_SCORES) {
          gameStatus = MAIN_MENU;
          menuOption = OPTION1;
          repaint();
        } else {
          gameStatus = IN_GAME_PAUSED;
          menuOption = OPTION1;
          repaint();
        }
      }
    } else if (gameStatus == GAME_OVER && playerList.makeList(player) && key == KeyEvent.VK_ENTER) {
      player.setName(playerName);
      playerList.updateList(player);
      gameStatus = HIGH_SCORES;
      playerName = "";
      player = null;
      repaint();
    } else if (gameStatus == GAME_OVER && !playerList.makeList(player) && key == KeyEvent.VK_ENTER) {
      gameStatus = MAIN_MENU;
      repaint();
    }
  }


  //***************************************************************************************
  //keyReleased: invoked(runs) when a key is released on the keyboard
  //Parameters:
  //  evt: contains information about the key release event
  //
  public void keyReleased(KeyEvent evt) {
    if (gameStatus == IN_GAME && board != null) {
      int key = evt.getKeyCode();             //the Unicode of the pressed key
      board.handleKeyReleased(key);
    }
  }


  //***************************************************************************************
  //keyTyped: invoked(runs) when a key is typed
  //Parameters:
  //  evt: contains information about the key typed event
  //
  public void keyTyped(KeyEvent evt) {
    if (gameStatus == GAME_OVER && playerList.makeList(player)) {
      int key = evt.getKeyChar();

      if (key == KeyEvent.VK_BACK_SPACE && playerName.length() > 0) {
        playerName = playerName.substring(0, playerName.length() - 1);
      } else if (key != KeyEvent.VK_BACK_SPACE) {
        playerName = playerName + Character.toString((char) key);
      }

      repaint();
    }
  }


  public void update(Graphics g) {
    paint(doubleBufferGraphics);         // draw elements in background
    g.drawImage(doubleBufferImage, 0, 0, this);        // draw image on the screen
  }


  public void paint(Graphics g) {
    if (gameStatus == MAIN_MENU) {
      displayMain(g);
    } else if (gameStatus == CHOOSE_NUMPLAYER) {
      displayNumPlayer(g);
    } else if (gameStatus == HIGH_SCORES || gameStatus == HIGH_SCORES_PAUSED) {
      displayHighScores(g);
    } else if (gameStatus == IN_GAME) {
      if (board == null) {
        board = new GameBoard(BOARD_ROWS, BOARD_COLUMNS, TILES_ROWS_NUM, TILES_COLUMNS_NUM, NUM_PLAYERS, this);
        timer = new Timer(UPDATE_SECONDS * 20, new TimerListener());
        time = board.TOTAL_TIME;
        timer.start();
      }
      displayGame(g);
    } else if (gameStatus == IN_GAME_PAUSED) {
      displayPausedGame(g);
    } else if (gameStatus == GAME_OVER) {
      displayGameOver(g);
    }
  }


  //***************************************************************************************
  //playSound: playes the given sound clip
  //Parameters:
  //sound_type: an integer indicating which sound type... acts as index for soundList
  public void playSound(int sound_type) {
    AudioClip ac = this.getAudioClip(getCodeBase(), soundList[sound_type]);
    ac.play();
  }


  public void quit() {
    System.exit(0);
  }


  //***************************************************************************************
  //internal class to handle updating the games timer
  class TimerListener implements ActionListener {
    Time t;
    int timeTracker;

    public void actionPerformed(ActionEvent e) {
      repaint();

      if (time == 0) {
        timer.stop();
        gameOver(board.getScore(0));
      }

      timeTracker++;

      if (timeTracker == 50) {
        t = new Time(0, 0, time);
        timeLeft = t.toString();
        time = time - UPDATE_SECONDS;
        timeTracker = 0;
      }
    }
  }
} // end of GameGui class
