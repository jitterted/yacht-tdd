
Game Table
----------
id                  PK
roundCompleted      boolean
rolls               int
handOfDice          array

Score Table
-----------
id                  PK
gameId              FK
scoreCategoryId     FK
score               int

ScoreCategory Table
-------------------
id                  PK
CategoryName        string


Memento Design
--------------
Game
    roundCompleted  boolean
    rolls           int
    handOfDice      array
    scoreboard      Scoreboard

Scoreboard
    scores          Map<String, int>


NEXT STEPS:
==========
#. Create DB tables [Ted will do separately]
#. Introduce memento [Ted will do separately]
#. Create GameRepository adapter
