module.exports.mySecret = "MyPasswordHere";  //TODO- SET SECRET HERE

/**
 * Return the direction you want to move your bike in
 * @param  {Number} gridSize        The size of the boardState
 * @param  {Array} boardState       An array of integers to represent the boardState
 * @param  {Number} myPlayerNumber  The number which represents your player on the grid
 * @return {String}                 The string UP, DOWN, LEFT, or RIGHT
 */
module.exports.selectMove = function selectMove(gridSize,boardState,myPlayerNumber) {
  module.exports.printBoard(boardState); //Print board for debugging

  //=====================================
  //TODO: YOUR CODE HERE
  //TODO: Return "UP", "DOWN", "LEFT", or "RIGHT"
  //=====================================

  return "DOWN";
};

/**
 * A handy debug function which prints the state of the boardState
 * @param  {Array} boardState     The board state from the selectMove() method
 */
module.exports.printBoard = function printBoard(boardState) {
  var size = boardState[0].length;
  var x=0, y=0;
  for(y=0; y<size;y++) {
    row = "";
    for(x=0; x<size;x++) {
      row += boardState[x][y] + " ";
    }
    console.log(row);
  }
};
