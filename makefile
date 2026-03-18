
#makefile

JCC    = javac
JFLAGS = -g
SRC    = src
BIN    = bin
LIB    = lib/junit-platform-console-standalone.jar

SOURCES = $(SRC)/CellPattern.java $(SRC)/Const.java $(SRC)/GameFrame.java \
          $(SRC)/LifeCell.java $(SRC)/LifeGame.java $(SRC)/Mycallback.java \
          $(SRC)/Range.java

default:
	$(JCC) $(JFLAGS) -d $(BIN) $(SOURCES)

.PHONY: test
test:
	$(JCC) $(JFLAGS) -cp $(LIB) -d test/classes \
	    $(SRC)/Range.java $(SRC)/Const.java $(SRC)/Mycallback.java $(SRC)/LifeCell.java \
	    test/RangeTest.java test/LifeCellTest.java
	java -Djava.awt.headless=true -jar $(LIB) \
	    --class-path test/classes \
	    --select-class RangeTest --select-class LifeCellTest

run:
	java -cp $(BIN) LifeGame

clean:
	$(RM) $(BIN)/*.class
	$(RM) test/classes/*.class
