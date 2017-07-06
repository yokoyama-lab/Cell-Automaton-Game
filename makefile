# Makefile for java Frametest

PACKAGE = Frametest
SRCS    = $(PACKAGE).java
OBJS    = $(SRCS:.java=.o)

FILES   = README Makefile $(SRCS)
VER     = 'date +%Y%m%d'

TARGET  =Frametest

#java (*.java)
javac   




all:	$(PACKAGE).class
	java $(PACKAGE)
run:	$(PACKAGE).class
	appletviewer $(PACKAGE).html
clean:	
	-rm *.class
.eucjava.java:
	native2ascii -encoding EUCJIS $*.eucjava $*.java
.java.class:
	javac -deprecation $<
