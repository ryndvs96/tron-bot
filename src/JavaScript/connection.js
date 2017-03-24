//=====================================
// DON'T EDIT THIS FILE
// PLACE YOUR CODE IN bot.js INSTEAD
//=====================================

var http = require('http');
var net = require('net');
var incomingData = ""; //Partial messages coming through socket
var bot = require('./bot.js');
var SECRET = bot.mySecret;
var myPlayerNumber = -1;
var totalPlayerCount = -1;

//TCP Socket Server for bots
var client = net.Socket();
client.connect(8080,'bots.purduehackers.com', () => {
    console.log("Connected to server");
});

client.on('data', function(data) {
  incomingData += data.toString().split("\n").join(""); //Strip newlines
  while(true) {
    var pos = incomingData.indexOf(";");
    if(pos >= 0) {
      var message = incomingData.substring(0,pos);
      incomingData = incomingData.substring(pos+1);
      handleMessage(message);
    } else {
      break;
    }
  }
});

client.on('close', function() {
	console.log('Connection closed');
});

//Handle incoming messages from the server
function handleMessage(message) {
  var messageWords = message.split(" ");
  switch(messageWords[0]) {
    //Authenticate with server
    case "REQUEST_KEY":
      sendMessage("AUTH " + SECRET);
      break;

    //Server accepted our credentials
    case "AUTH_VALID":
      console.log("Connected! Searching for opponent...");
      break;

    //Server rejected our credentials
    case "ERR_AUTH_INVALID":
      console.log("Error! Server rejected our credentials.\nRe-check your key. Is <" + SECRET + "> correct?");
      client.destroy();
      break;

    //Game has started
    case "GAME_START":
      myPlayerNumber = parseInt(messageWords[1]);
      totalPlayerCount = parseInt(messageWords[2]);
      console.log("Game started! I am player #" + myPlayerNumber + ", out of " + totalPlayerCount + ".");
      break;

    //Server says we took too long
    case "ERR_TIMEOUT":
      console.log("Error! Server kicked client for taking too long.\nCheck your selectMove() function to ensure it does not take longer than a second to return.");
      break;

    //Server says we died
    case "PLAYER_DIED":
      console.log("You died!");
      break;

    //Server says we won
    case "PLAYER_WIN":
      console.log("You won!");
      break;

    //Server wants us to send a move
    case "REQUEST_MOVE":
      var boardData = parseBoardState(messageWords);
      var responseMove = bot.selectMove(boardData.gridSize, boardData.boardState,myPlayerNumber);
      sendMessage("MOVE " + responseMove);
      break;

    default:
      console.log("Unknown Command [" + message + "]");
      break;
  }
}

//Send a message to the server
function sendMessage(message) {
  if(!client) {
    console.log("Error! Client is not alive");
    return;
  }
  client.write(message +";");
}

//Parse the split message into a usable board state
function parseBoardState(splitMessage) {
  var gridSize = parseInt(splitMessage[1]);

  //Init array
  var boardState = [];
  for(var i=0; i < gridSize; i++) {
    boardState[i] = [];
  }

  //Populate Array
  var cellData = splitMessage[2].split(",");
  var x = 0, y = 0;
  for(var j=0; j<cellData.length; j++) {
    boardState[x][y] = cellData[j];
    x += 1;
    if(x == gridSize) {
      x = 0;
      y++;
    }
  }

  return {
    gridSize: gridSize,
    boardState: boardState
  };
}
