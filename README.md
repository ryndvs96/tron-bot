# Tron Bot

Bot works in two phases, Space Saving and Survival.

## Space Saving

### Step 1: Heuristics

Based on Phase 3 from the [Google AI Tron Bot post mortem](https://www.a1k0n.net/2010/03/04/google-ai-postmortem.html).
The bot attempts to choose the move which gives the best score using the "[Voronoi](https://en.wikipedia.org/wiki/Voronoi_diagram) Heuristic".

The idea of the Voronoi Heuristic is, for each potential move, calculate the quantity of grid points bot 1 can reach before bot 2 and vice versa.
This can easily be done using a BFS from our position to all other available nodes in the graph and numbering the distances of each point accordingly.
Then run a BFS from the opponent's position to all other available nodes and tally up which nodes the opponent is closer to and equidistant from.
From there we calculate the score for each move as the difference between our bot's quantity and the opponent's.
We then choose the move with maximum score.
In the next phase, we want our bot to be able to reach more points than the other.

### Step 2: Disjoint Graphs

In running the BFS from each player's position we can verify if their BFS trees actually overlap (they can each reach a mutual point).
If their searches do not overlap for a particular move we create a split between nodes reachable to us and nodes reachable to our opponent.
From then on, we simply try to "survive" as long as possible and run out the clock for the other player.
We have a move that can make a split, ideally what we should do is check if the longest path in our reachable graph is larger than the opponents.
As you may know, finding the longest path is NP-Complete, so this wouldn't work well for any reasonably sized grid.
Instead a good option would be to estimate it simply based on the number of reachable nodes. 
Although our algorithm is very dumb and doesn't like conflict, so whenever it has a move which cuts itself off from the opponent, it takes that move.

## Survival

In this phase we presumably already have disjoint sets of reachable nodes.
From here on we simply need to survive longer than our opponent.
This means avoiding dead-end paths (when possible), not hitting ourselves, and making the most use of the space available.
Ideally, we would find the longest path in our area and simply follow that, but again this is NP-Complete.
We instead used a [wall-hugging](http://csclub.uwaterloo.ca/contest/xiao_strategy.php#sect_wall) stategy which attempts to make moves following a wall.
This makes very good use of our space available in survival scenarios.


