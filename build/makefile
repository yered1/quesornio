JFLAGS = -g -cp .
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
       *.java 
	#logreadP/*.java
	# CryptoMotor.java \
        CryptoMotorR.java \
        PasswordEncryption.java \
        PasswordEncryptionR.java \
	LogEntry.java \
	LogEntryR.java \
	Logread.java \
	Logappend.java 
ifndef classpath
export classpath = $(PWD)/logappendP
endif

ifndef classpath2
export classpath2 = $(PWD)/logreadP
endif

default: 
	cd logappendP; make
	cd logreadP; make
	
classes: $(patsubst %.java,$(classpath)/%.class,$(CLASSES))
classes2: $(patsubst %.java,$(classpath2)/%.class,$(CLASSES))
#classes: $(CLASSES:%.java=%.class)

clean:
	$(RM) $(classpath)/*.class
	$(RM) $(classpath2)/*.class
