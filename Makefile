JAVAC="javac"
JAVA="java"

build:
	rm -rf out/make
	mkdir -p out/make
	$(JAVAC) -d out/make -s out/make src/**/*.java src/*.java -Xlint:unchecked -cp src/:lib/*

runsolver: build
	java -cp out/make:lib/* "main.MainWrapper" ${ARGS}

testgen: build
	java -cp out/make:lib/* "Main" ${ARGS}
