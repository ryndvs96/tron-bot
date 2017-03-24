# Tron Starter Code

Started code is provided for Java, JavaScript, Python2, and Python3. Here's how to get started:

* Clone this repository onto your machine with `git clone https://github.com/DonaldBough/TRON_starter_code.git`
* Open the `bot` file for your preferred language and change the auth token to the one you got after checking in
* Find the `Select Move` method in the same file and add your bot's code there. Returning `UP`, `DOWN`, `LEFT` or `RIGHT` here will send your bot in that direction.
* The `Select Move` method will contain an array that represents the state of the board. Each player has a unique number to represent them, and your number is will be indicated by the `myPlayerNumber` variable.
* A positive number indicates the position of the corresponding player. A negative number indicates the position of the corresponding player's trail. So a `1` marks the position of player 1, and a `-1` marks the positions of player 1's trails.
* If your bike collides with another player or their trail, you die. If your bike runs off the edge of the game, you die. The last player standing wins the round!
