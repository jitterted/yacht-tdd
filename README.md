# The Game of Yacht

## Terminology

GAME has PLAYERs, each of whom have a SCOREBOARD that contains ROLLs and SCORE and CATEGORYs
that are used/not yet used.
We know when a GAME is over when all PLAYERs have filled all of their CATEGORYs in the SCOREBOARD.

## Next Steps

* Create a representation of a Game that stores the state of their part of the game,
  i.e., current score, which categories have been used, the rolls of the game, etc.

* From the UI have a way to create a Player, initially could just be anonymous and
  a new player is created whenever a "Start Game" button is clicked.

## Game Flow

* Click "Enter Game Area" (creates a new Player)
    * Get Player NAME
    * Clear their "scoreboard"
* Start Yacht Game
    1. Roll Dice
    1. Choose a category for scoring
    1. Update score
    1. Repeat from 1. until no categories are left unassigned
    1. Show final score

## To Do

* Full House validation is broken (see disabled test)

* Categories can currently be assigned to multiple times, 
  need to constrain to only once per category per round
  
* Introduce "Die" as a value object (to replace Integer)
