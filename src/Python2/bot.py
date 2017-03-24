from tron_connection import TronSocket

secret = "MyPasswordHere" #TODO: SET SECRET HERE

'''
Print the board
@param {double array} board_state: A matrix representing the state of the board
'''
def print_board(board_state):
  for row in board_state:
    for cell in row:
      print cell,
    print ""

'''
 Return the direction you want to move your bike in
 @param  {int} grid_size:            The length of one side of the square grid
 @param  {double array} board_state: A 2D matrix of ints representing the state of the board
 @param  {int} my_player_number:     The number which represens your player on the board
 @param  {int} total_player_count:   The amount of players in this gane
 @return {String}                    The move to be played ("UP", "DOWN", "LEFT", or "RIGHT")
'''
def select_move(grid_size, board_state, my_player_number, total_player_count):

  #=================================================#
  # TODO: YOUR CODE HERE                            #
  # TODO: Return "UP", "DOWN", "LEFT", or "RIGHT"   #
  #=================================================#

  print_board(board_state) # Print board for debugging
  print ""

  # Return "UP", "LEFT", "LEFT", or "RIGHT"
  return "UP"



#DON'T TOUCH BELOW
socket_connection = TronSocket(select_move, secret)
#DON'T TOUCH ABOVE
