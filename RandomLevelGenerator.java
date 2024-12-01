/////////////////////////////////////////////////////////////
//class: RandomGameboardGenerator
//Programmer: Miaofei Mei
/////////////////////////////////////////////////////////////

import java.util.Random;
import java.io.*;

public class RandomLevelGenerator {
  public static void generateLevel(String filename) {
    try {
      Random randomGenerator = new Random();
      int tempInt;
      BufferedWriter bufferOut = new BufferedWriter(new FileWriter(filename, false));

      for (int i = 0 ; i < 4 ; i++) {
        for (int j = 0 ; j < 17 ; j++) {
          tempInt = randomGenerator.nextInt(2);
          switch (tempInt) {
            case 0:
              bufferOut.write('A');
              break;
            case 1:
              bufferOut.write('B');
              break;
            case 2:
              bufferOut.write('C');
              break;
          }
          bufferOut.write(' ');
        }
        bufferOut.newLine();
      }

      tempInt = 0;
      while (tempInt == 0) {
        tempInt = randomGenerator.nextInt(7);
      }
      for (int i = 0 ; i < tempInt ; i++) {
        bufferOut.write(randomGenerator.nextInt(8) + '1');
      }
      bufferOut.newLine();

      bufferOut.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
