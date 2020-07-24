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

[X] Check that the score is being rendered as desired

[X] Refactor the YachtController category assignment code

[X] Add scoring for the remaining categories to Scoreboard (and ScoreCategory)

[X] Bounce back out a layer to front-end web to see what functionality we need next

[X] Full House validation is broken (see disabled test)

[X] Add die selection (checkbox) to the UI to select the dice to "hold back" from a re-roll
   * We get a list of the indexes of the dice to keep

[X] Re-roll 1 or all 5 of the dice
      -> receive [0,1] indexes from the API (front-end)
        * We're getting indexes to avoid cheating
      -> translate to [6,6] and send it to the Game and have it roll 3 additional dice

[X] Rule: After 2 re-rolls (3 total rolls)
    
   * Scenario:
        player rolls 6,6,3,2,1 [roll 1]
        player holds 6,6 and re-rolls the other 3 dice [roll 2]
        player rolls 6,5,4
        player holds 6,6,6 and re-rolls the other 2 dice [roll 3]
        player rolls 3,3
        dice roll to be assigned is now 6,6,6,3,3
        since that's all re-rolls allowed, must assign to category (e.g., Full House)

## Next Time

[X] Rule: Must assign roll to category before doing a new roll

[ ] Rule: Can't re-roll after assigned to category before new roll

   [X] Implement UI constraint for this rule

[ ] Rule: can only assign roll to a category once per round

   [X] Need to reflect this constraint in the user interface 

[ ] Rule: Game is over when all categories have been assigned

[ ] Add other scoring categories (should total 12 categories, which means 12 rounds): 
     * 3-of-a-kind scores sum of dice
     * little straight (1-5) scores 30
     * big straight (2-6) scores 30
     * choice scores sum of dice, and Yacht (5-of-a-kind) scores 50 

[ ] Pre-check the dice that were held from previous roll? Or pre-check all dice?

[ ] Change full house to score 25 (as per https://www.mathsisfun.com/games/yacht-dice-game.html)
  
* Introduce "Die" as a value object (to replace Integer)
