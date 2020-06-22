# The Game of Yacht

## Next Steps

[ ] Create a representation of a Player that stores the state of the Yacht game,
    i.e., current score, which categories have been used, the rolls of the game, etc.

[ ] From the UI have a way to create a Player, initially could just be anonymous and
    a new player is created whenever a "Start Game" button is clicked.

## Game Flow

* Click "Enter Game Area" (creates a new Player)
* Start Yacht Game (creates a Game)
    * Roll Dice
    * Choose category for scoring
    * Show score
    * Repeat until no categories are left unassigned
    * Show final score
  