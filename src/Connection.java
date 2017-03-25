import java.io.*;
import java.net.*;
import java.lang.Thread;
public class Connection
{
  final static String SERVER_URL = "bots.purduehackers.com";
  final static int SERVER_PORT = 8080;
  static BufferedReader input;
  static PrintWriter writer;
  static String buffer = "";
  static Socket socket;

  static boolean debugging = true;
  //Turning this to true will print Messages Sending and Recieving

  /**
   * Connect to the server and handle incoming messages
   */
  public static void connect() {

    try {
      //Connect reader/writer to server
      socket = new Socket(SERVER_URL,SERVER_PORT);
      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      writer = new PrintWriter(socket.getOutputStream(),true);

      //Read commands from the server
      while(true) {
        String data = input.readLine();
        if(data == null)
          break;
        buffer += data;
        if(debugging)
          System.out.println("Incoming Data: " + data + ". Buffer is now: " + buffer);
        //Parse Messages
        while(true) {
          //Is there a complete message available?
          int pos = buffer.indexOf(";");
          if(pos >= 0) {
            String message = buffer.substring(0,pos);
            buffer = buffer.substring(pos+1);
            handle(message);
          } else {
            break;
          }
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }

  public void kill() throws IOException {
    socket.close();
  }

  /**
   * Handle a message from the server
   * @param  message The message from the server
   */
  public static void handle(String message) {
    if(debugging)
      System.out.println("Handling message: " + message);

    String[] words = message.split(" ");

    //Process message
    if(words[0].equals("REQUEST_MOVE")) {
      buildBoard(words[1], words[2]);
      System.out.println("making move");
      String direction = Bot.makeMove();
      System.out.println("made move");
      writer.write("MOVE " + direction + ";");
      writer.flush();
    } else if (words[0].equals("PLAYER_DIED")) {
      System.out.println("Lost the Game!");
    }
    else if (words[0].equals("PLAYER_WIN")) {
      System.out.println("Won the Game!");
    }
    else if (words[0].equals("AUTH_VALID")) {
      System.out.println("Auth key accepted. Waiting for opponent...");
    }
    else if (words[0].equals("ERR_AUTH_INVALID")) {
      System.out.println("Auth key rejected. Is <" + Bot.AUTH_KEY + "> actually your key?");
    }
    else if (words[0].equals("REQUEST_KEY")) {
      writer.write("AUTH "+Bot.AUTH_KEY+";");
      writer.flush();
      System.out.println("Sent auth key to server");
    }
    else if (words[0].equals("GAME_START")) {
      Bot.myPlayerNumber = Integer.parseInt(words[1]);

      Bot.totalPlayers = Integer.parseInt(words[2].replaceAll(";",""));

      System.out.println("Your player # is : "+ Bot.myPlayerNumber);
      System.out.println("Total # of players is: " + Bot.totalPlayers);
    }
    else if (words[0].equals("ERR_TIMEOUT")) {
      System.out.println("You took too long to respond to the server. " +
          "Try to double check your time, look for programming delays, " +
          "were you using a debugger?");
    } else {
      System.out.println("Unknown Server Message: " + words[0]+"\n"+message);
    }
  }

  /**
   * Process the boardString into a meaningful 2D-array
   * @param size Size of the game board from server
   * @param boardString Board given as one long string
   */
  private static void buildBoard(String size, String boardString) {
    if (debugging)
      System.out.println("Building Board!");
    if (Bot.gameBoard == null) {
      Bot.gameBoard = new Integer[Integer.parseInt(size)][Integer.parseInt(size)];
    }

    int counter = 0;

    String[] boardArray = boardString.split(",");

    for(int i = 0; i < Bot.gameBoard.length; i++) {
      for(int j = 0; j < Bot.gameBoard[i].length; j++) {
        try {
          Bot.gameBoard[i][j] = Integer.parseInt(boardArray[counter]);
        }
        catch (Exception e) {
          System.out.println("Error inside private void buildBoard()" + e.getStackTrace());
          e.printStackTrace();
        }
        counter++;
      }
    }
    printBoard();
  }

  public static void printBoard() {
    System.out.println("         -Current Grid-");
    System.out.println("----------------------------------");
    for(int i = 0; i < Bot.gameBoard.length; i++)
    {
      for(int j = 0; j < Bot.gameBoard[i].length; j++)
      {
        System.out.printf("%3d", Bot.gameBoard[i][j]);
      }
      System.out.println("");
    }
    System.out.println("\n\n");
    //System.out.println("\n----------------------------------\n");
  }
}
