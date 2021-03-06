import java.util.*;
import java.awt.Point;

class DirPair {
  public Point point;
  public String direction;

  public DirPair(Point point, String direction) {
    this.point = point;
    this.direction = direction;
  }
}

public class Bot {

  final static String AUTH_KEY = "NHRTA1490394385793";

  static boolean survive = false;
  static Integer[][] gameBoard;
  static int myPlayerNumber;
  static int totalPlayers;
  static int myCurrentRow;
  static int myCurrentColumn;
  static boolean usingSurvival = false;

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
   
    String move = "UP";
    if (survive && usingSurvival) {
      move = survivalMode();
    } else {
      move = vorMove();
    }
    System.out.println("move : " + move);
    
    return move;
    //Return UP, DOWN, LEFT, or RIGHT to move that direction
    //Best of luck!
  }

  public static String survivalMode() {
    ArrayList<DirPair> valid = validAdj(myPosition());
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
    ArrayList<DirPair> adj = new ArrayList<DirPair>();

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
    Point mine = myPosition();
    Point theirs = theirPosition();
    System.out.printf("my point: %s\n", mine.toString());
    System.out.printf("their point: %s\n", theirs.toString());
    int[][] theirReach = bfs(theirs, flatten());

    int[][] flat = flatten();

    System.out.printf("current point: %s\n", mine.toString());

    Point best = null;
    int bestScore = Integer.MIN_VALUE;
    // find score for each move
    for (Point p : nbs(mine, flat)) {
      int[][] myflat = flatten();
      myflat[p.x][p.y] = -1; // make move
      int[][] myReach = bfs(p, myflat);

      int score = comp(myReach, theirReach);
      if (score == -5000) {
        survive = true;
        return translate(p, mine);
      }
      if (best == null || score > bestScore) {
        best = p;
        bestScore = score;
      }
      System.out.printf("point %s score = %d\n", p.toString(), score);
    }

    return translate(best, mine);
  }

  // translate best to UP DOWN L R
  // p1 relative to p2
  public static String translate(Point p1, Point p2) {
    if (p1 == null)
      return "UP";
    if (p1.x == p2.x - 1 && p1.y == p2.y) {
      return "UP";
    }
    if (p1.x == p2.x + 1 && p1.y == p2.y) {
      return "DOWN";
    }
    if (p1.x == p2.x && p1.y == p2.y + 1) {
      return "RIGHT";
    }
    if (p1.x == p2.x && p1.y == p2.y - 1) {
      return "LEFT";
    }
    // should never happen
    return "UP";
  }

  // compare reach grids
  public static int comp(int[][] mine, int[][] theirs) {
    System.out.println("MY GRID");
    print(mine);
    System.out.println("THEIR GRID");
    print(theirs);

    boolean overlap = false;
    int myScore = 0;
    int theirScore = 0;
    int n = mine.length;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (mine[i][j] == -1)
          continue;
        if (mine[i][j] != 0 && mine[i][j] < theirs[i][j]) {
          myScore++;
          overlap = true;
        } else if (mine[i][j] > theirs[i][j] && theirs[i][j] != 0) {
          theirScore++;
          overlap = true;
        }
      }
    }
    if (!overlap && usingSurvival) {
      return -5000; 
    }
    return myScore - theirScore;
  }

  // convert grid to -1s (invalid) and 0s (valid)
  public static int[][] flatten() {
    int n = gameBoard.length;
    int[][] grid = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        grid[i][j] = gameBoard[i][j] != 0 ? -1 : 0;
      }
    }
    return grid;
  }

  public static void print(int[][] grid) {
    int n = grid.length;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        System.out.printf("%3d ", grid[i][j]);
      }
      System.out.println();
    }
  }
  // bfs
  // returns numbers in grid:
  // 0 = not reachable
  // -1 = wall
  // other = distance from start
  public static int[][] bfs(Point start, int[][] grid) {
    // queue
    LinkedList<Point> q = new LinkedList<>();
    q.addLast(start);
    // want strings not objects
    HashMap<String, Integer> dist = new HashMap<>();
    dist.put(start.toString(), 0);

    while (!q.isEmpty()) {
      Point p = q.removeFirst();
      int d = dist.get(p.toString());
      for (Point n : nbs(p, grid)) {
        // already reached
        if (dist.containsKey(n.toString())) 
          continue;
        if (grid[n.x][n.y] == -1)
          continue;

        // not reached yet or dist is less (second case shouldn't happen)
        if (grid[n.x][n.y] == 0 || d < grid[n.x][n.y]) {
          grid[n.x][n.y] = d + 1;
        }
        dist.put(n.toString(), d + 1); // adds as string
        q.addLast(n);
      }
    }
    
    return grid;
  }

  public static ArrayList<Point> nbs(Point p, int[][] grid) {
    ArrayList<Point> list = new ArrayList<>();
    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        if (Math.abs(i) == Math.abs(j))
          continue;
        int pi = p.x + i;
        int pj = p.y + j;
        
        if (i == j || !inBoard(pi, pj, grid) || grid[pi][pj] == -1) 
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
    int me = myPlayerNumber;
    int n = gameBoard.length;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (gameBoard[i][j] > 0 && gameBoard[i][j] == me) {
          return new Point(i, j);
        }
      }
    }
    // shouldn't happen :/
    return null; 
  }

  public static Point theirPosition() {
    int me = myPlayerNumber;
    int n = gameBoard.length;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (gameBoard[i][j] > 0 && gameBoard[i][j] != me) {
          return new Point(i, j);
        }
      }
    }
    // shouldn't happen :/
    return null; 
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
