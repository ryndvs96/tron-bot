import socket

class TronSocket:

    def __init__(self, select_move, secret, sock=None):
        if sock is None:
            self.sock = socket.socket(
                socket.AF_INET, socket.SOCK_STREAM)
        else:
            self.sock = sock


        self.sock.connect(("bots.purduehackers.com", 8080))
        self.chunks = ""
        text = self.initialize(secret)
        self.my_player_number = text[1]
        self.total_player_count = text[2]
        self.play(select_move)

    def sendmsg(self, msg):
        sent = self.sock.send(msg + ";")
        if sent == 0:
            raise RuntimeError("socket connection broken")

    def receivemsg(self):
        bytes_recd = 0
        if ';' in self.chunks:
          pos = self.chunks.index(';')
          retmsg = self.chunks[:pos]
          self.chunks = self.chunks[pos+1:]
          return retmsg
        else:
          data = self.sock.recv(2048)
          if "\n" in data:
            data.replace("\n", "")
            self.chunks += data
          else:
            self.chunks += data
        pos = self.chunks.index(';')
        retmsg = self.chunks[:pos]
        self.chunks = self.chunks[pos+1:]
        return retmsg

    def initialize(self, secret):
        text = self.receivemsg().split()

        if text[0] == "REQUEST_KEY":
            self.sendmsg("AUTH " + secret)
        else:
            raise RuntimeError("Server Error E01")
        text = self.receivemsg().split()

        if text[0] == "AUTH_VALID":
            print "Connected! Searching for opponent"
        elif text[0] == "ERR_AUTH_INVALID":
            print "Error! Server rejected our credentials.\nRe-check your key. Is <" + secret + "> correct?"
            self.sock.close()
            quit()
        else:
            raise RuntimeError("Server Error E02")

        text = self.receivemsg().split()
        if text[0] == "GAME_START":
            print "Game started! I am player #" + str(text[1]) + ", out of " + str(text[2]) + "."
            return text
        else:
            raise RuntimeError("Server Error E03 " + text[0])

    def parse_board(self, text):
        grid_size = int(text[0])
        board_state = []
        for i in range(0, grid_size):
            board_state.append([])
        cell_data = text[1].split(",")
        x = 0
        for j in range(0,len(cell_data)):
          board_state[x/grid_size].append(cell_data[j])
          x += 1
        return [grid_size, board_state]


    def play(self, player_function):
        while True:
            text = self.receivemsg()
            text = text.split()
            if text[0] == "ERR_TIMEOUT":
                print "Error! Server kicked client for taking too" + " long.\nCheck your selectMove() function to ensure it does " + "not take longer than a second to return."
                self.sock.close()
                quit()
            elif text[0] == "PLAYER_DIED":
                print "You died!"
                self.sock.close()
                quit()
            elif text[0] == "PLAYER_WIN":
                print "You won!"
                self.sock.close()
                quit()
            elif text[0] == "REQUEST_MOVE":
                grid_size, board_state = self.parse_board(text[1:])
                cmd = player_function(grid_size, board_state, self.my_player_number,
                 self.total_player_count)
                self.sendmsg("MOVE " + cmd)
