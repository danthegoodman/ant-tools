# ant-tools

A smattering of classes to use with ant.

## Build
   
    ant jar

## Usage

    ant -lib ant-tools.jar

## QuietLogger

Supresses much of ant's default logging. 

On task completion, prints current date and time.

    ant -lib ant-tools.jar -logger us.kirchmeier.tools.QuietLogger

Compare:

	$ ant jar
	Buildfile: /###/ant-tools/build.xml

	javac:
	    [mkdir] Created dir: /###/ant-tools/out
		[javac] Compiling 2 source files to /###/ant-tools/out

	jar:
	   [delete] Deleting: /###/ant-tools/ant-tools.jar
          [jar] Building jar: /###/ant-tools/ant-tools.jar

	BUILD SUCCESSFUL
	Total time: 0 seconds

	$ ant -lib ant-tools.jar -logger us.kirchmeier.tools.QuietLogger
	Buildfile: /###/ant-tools/build.xml
	Created dir: /###/ant-tools/out
	Compiling 2 source files to /###/ant-tools/out
	Deleting: /###/ant-tools/ant-tools.jar
	Building jar: /###/ant-tools/ant-tools.jar

	Build Completed at 7:15:42 PM

## JUnitFailureReporter

A quieter JUnit reporter. 
Reports only failing tests. 
No news is good news.

    <junit>
	    <classpath location="ant-tools.jar" />
	    <formatter classname="us.kirchmeier.tools.JUnitFailureReporter" usefile="false"/>
	</junit>
