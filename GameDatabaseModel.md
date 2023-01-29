
Game Table
----------
id                  PK
roundCompleted      boolean
rolls               int
currentHand         array

Score Table
-----------
id                  PK
gameId              FK
scoreCategoryId     FK
handOfDice          array
// Note: score will be recalculated when the object is reconstituted

ScoreCategory Table
-------------------
id                  PK
CategoryName        string


Memento Design
--------------
Game.Memento
    roundCompleted  boolean
    rolls           int
    currentHand     List<Integer> (not HandOfDice)
    scoreboard      EnumMap<ScoreCategory (enum), List<Integer>>


NEXT STEPS:
==========
#. Create DB tables [Ted will do separately]
#. Introduce memento [Ted will do separately]
#. Create GameRepository adapter
