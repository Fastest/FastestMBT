SHELL=cmd.exe
all: TcrlParser.class

TcrlParser.class: tcrl.jj 
	javacc tcrl.jj
	
clean:
	del *.class
	del *.java
	
