JFLAGS = -g
JC = javac
JAR = mssql-jdbc-11.2.0.jre11.jar

build:
	$(JC) -cp .:$(JAR) *.java

run: build
	java -cp .:$(JAR) RoadSafetyTool

clean:
	rm *.class