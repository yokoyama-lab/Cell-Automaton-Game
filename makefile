
#makefile

JCC = javac
JFLAGS = -g

default: CellPattern.class Const.class GameFrame.class LifeCell.class LifeGame.class Range.class  



CellPattern.class: CellPattern.java
	      $(JCC) $(JFLAGS) CellPattern.java

Const.class: Const.java
	      $(JCC) $(JFLAGS) Const.java

GameFrame.class: GameFrame.java
	      $(JCC) $(JFLAGS) GameFrame.java

LifeCell.class: LifeCell.java
	      $(JCC) $(JFLAGS) LifeCell.java

LifeGame.class: LifeGame.java
	      $(JCC) $(JFLAGS) LifeGame.java

Range.class: Range.java
	      $(JCC) $(JFLAGS) Range.java


clean: 
	$(RM) *.class

