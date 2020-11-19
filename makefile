# REMEMBER -> sudo cp src/libjpcap.so /usr/lib

.PHONI := c r

c :
	@if [ ! -d build/ ]; then mkdir build/; fi
	javac -classpath ./lib/jpcap.jar src/**/*.java -d ./build/

r : 
	java -Djava.library.path=./lib/ -classpath ./lib/jpcap.jar:./build/ r0p3.Main

d :
	jdb -sourcepath ./src -Djava.library.path=./lib/ -classpath ./lib/jpcap.jar:./build/ r0p3.Main

t :
	if [ ! -d build_test/ ]; then mkdir build_test/; fi
	javac -classpath ./lib/jpcap.jar test/*.java -d ./build_test/
	java -Djava.library.path=./lib/ -classpath ./lib/jpcap.jar:./build_test/ Main
