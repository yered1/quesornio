JFLAGS = -g -O
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

default: classes

classes: $(CLASSES:%.java=%.class)

clean:
	$(RM) *.class
