# REMEMBER -> sudo cp src/libjpcap.so /usr/lib

.PHONI := c r

c :
	javac -classpath ./lib/jpcap.jar src/**/*.java -d ./build/

r : 
	java -Djava.library.path=./lib/ -classpath ./lib/jpcap.jar:./build/ r0p3.Main
