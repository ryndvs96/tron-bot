import java.util.*;
import java.awt.*;

public class Bot {

  final static String AUTH_KEY = "NHRTA1490394385793";

  static Integer[][] gameBoard;
  static int myPlayerNumber;
  static int totalPlayers;
  static int myCurrentRow;
  static int myCurrentColumn;

  public static void main(String[] args) {
    System.out.println("I am alive");
    Connection.connect();
  }

  /*===========================================
    Start writing your code here!
    ===========================================*/
  public static String makeMove() {
    if (gameBoard == null || gameBoard.length == 0 || gameBoard[0].length == 0) {
      System.out.println("Game board was messed up coming into public void makeMove()");
      return "DOWN";
    }
    return "UP";
    //Return UP, DOWN, LEFT, or RIGHT to move that direction
    //Best of luck!
  }

  public static String vorMove() {
    // vor move move
  }

  //Small helper Method
  public static boolean isInsideBoard(int i, int j) {
    if (i > gameBoard.length || j > gameBoard.length) {
      return false;
    } else if (i < 0 || j < 0) {
      return false;
    }
    return true;
  }
}
