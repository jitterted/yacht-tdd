# The Game of Yacht

This is a dice game similar to Yahtzee™.
Scoring rules are based on the description at https://www.mathsisfun.com/games/yacht-dice-game.html and more details
at https://en.wikipedia.org/wiki/Yacht_(dice_game).

## Terminology

GAME has PLAYERs, each of whom have a SCOREBOARD that contains ROLLs and SCORE and CATEGORYs that are used/not yet used.
We know when a GAME is over when all PLAYERs have filled all of their CATEGORYs in the SCOREBOARD.

## Playing the Game

### Start the Game

* Within IntelliJ IDEA: Run the `YachtApplication` class

* From the command line: Use `./mvnw spring-boot:run` (or Windows: `mvnw spring-boot:run`).

### Open in Browser

In your browser, go to: http://localhost:9090/

## Development Next Steps

* Create a representation of a Game that stores the state of their part of the game, i.e., current score, which
  categories have been used, the rolls of the game, etc.

* From the UI have a way to create a Player, initially could just be anonymous and a new player is created whenever a "
  Start Game" button is clicked.

## Game Flow

* Click "Enter Game Area" (creates a new Player)
    * Get Player NAME
    * Clear their "scoreboard"
* Start Yacht Game
    1. Roll Dice
    2. Choose a category for scoring
    3. Update score
    4. Repeat from 1. until no categories are left unassigned
    5. Show final score

## Related Projects

### Vue

The Vue (2.x) front-end repository is at: https://github.com/jitterted/vue-yacht

### JavaFX/TornadoFX

A desktop app version is at: https://github.com/jitterted/tornado-yacht

## Done

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

[X] Rule: Must assign roll to category before doing a new roll

[X] Rule: Game is over when all categories have been assigned - UI will take the player to "game over" page when done

   [X] Implemented this rule at the domain Game level
   [X] Create "game over" page
   [X] Don't forget to refactor Game!!

[X] Add other scoring categories (should total 12 categories, which means 12 rounds): 
     * Four-of-a-kind: score = sum of the four dice
     * Little Straight (1-5) score = 30
     * Big Straight (2-6) score = 30

[X] "Undefined Behavior": Game doesn't restart when going to "home" page

[X] Forgot these scoring categories:
     * Choice score = sum of dice
     * Yacht (5-of-a-kind) score = 50

[X] Rename the lastRollAssigned variable as it no longer seems to make sense in the context of
the roll-result page, also see YachtControllerRuleTest.newGameDoesNotRollDiceSoNoRollToAssign

[X] Rule: a category can only be used (have a dice roll assigned to it) once per round

[X] Need to reflect this constraint in the user interface

## Next Time

[ ] BUG: Should not be able to assign a roll to a category once it's already been assigned.

[ ] Refactor to consolidate the 3 places we have to modify code in order to add a scoring category

[ ] Think about naming of ScoreCategory (the enum) vs. ScoredCategory (with a "d") as they're easily confused.

[ ] Refactoring: Instead of dieToCountMap() returning a Map, transform to list of object that has .dieValue() and
.count() to provide more meaning than .getKey() and .getValue(), which comes from the EntrySet that we're currently
getting off of the Map.

[ ] Rule: Can't re-roll after assigned to category before new roll

[X] Implement UI constraint for this rule

[ ] Pre-check the dice that were held from previous roll? Or pre-check all dice?

[ ] Change full house to score 25 (as per https://www.mathsisfun.com/games/yacht-dice-game.html)
  
[ ] Introduce "Die" as a value object (to replace Integer)
