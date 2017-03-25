import java.util.*;
import java.awt.Point;

class DirPair {
  public Point point;
  public String direction;

  DirPair(point, direction) {
    this.point = point;
    this.direction = direction;
  }
}

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

  public static String survivalMode() {
    ArrayList<DirPair> valid = validAdj(new Point(myCurrentRow, myCurrentColumn));
    Collections.shuffle(valid);

    for (DirPair validMove : valid) {
      ArrayList<DirPair> wallNext = adj(validMove.point);
      wallNext.removeIf(dp -> isInsideBoard(dp.point.x, dp.point.y) && gameBoard[dp.point.x][dp.point.y] == 0);
      Collections.shuffle(wallNext);

      if (wallNext.size() != 0) {
        return wallNext.get(0).direction;
      }
    }

    return "UP";
  }

  public static ArrayList<DirPair> validAdj(Point current) {
    ArrayList<DirPair> valid = adj(current);

    valid.removeIf(dp -> !isInsideBoard(dp.point.x, dp.point.y) || gameBoard[dp.point.x][dp.point.y] != 0);

    return valid;
  }

  public static ArrayList<DirPair> adj(Point current) {
    ArrayList<DirPair> adj = new ArrayList<Point>();

    Point left = current.getLocation(); left.translate(0, -1);
    Point right = current.getLocation(); right.translate(0, 1);
    Point up = current.getLocation(); up.translate(-1, 0);
    Point down = current.getLocation(); down.translate(1, 0);

    adj.add(new DirPair(left, "LEFT"));
    adj.add(new DirPair(right, "RIGHT"));
    adj.add(new DirPair(up, "UP"));
    adj.add(new DirPair(down, "DOWN"));

    return adj;
  }

  public static String vorMove() {
    // vor move move
    // current position
    Point curr = myPosition();

    return "";
  }

  // bfs
  public static int[][] bfs(Point start, int[][] grid) {
    // queue
    LinkedList<Point> q = new LinkedList<>();
    q.addLast(start);

    HashSet<String> set = new HashSet<>();
    while (!q.isEmpty()) {
      Point p = q.removeFirst();
      List<Point> neighbors = nbs(p, grid);
    }


    return grid;
  }

  public static ArrayList<Point> nbs(Point p, int[][] grid) {
    ArrayList<Point> list = new ArrayList<>();
    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        int pi = p.x + i;
        int pj = p.y + j;
        if (i == j || !inBoard(pi, pj, grid) || grid[pi][pj] != 0)
          continue;

        list.add(new Point(pi, pj));
      }
    }
    return list;
  }

  public static boolean inBoard(int i, int j, int[][] grid) {
    return i >= 0 && i < grid.length && j >= 0 && j < grid.length;
  }

  public static Point myPosition() {
    return new Point(myCurrentRow, myCurrentColumn);
  }

  //Small helper Method
  public static boolean isInsideBoard(int i, int j) {
    if (i >= gameBoard.length || j >= gameBoard.length) {
      return false;
    } else if (i < 0 || j < 0) {
      return false;
    }
    return true;
  }
}
