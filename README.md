# ant-tools

A smattering of classes to use with ant.

## Build
   
    ant jar

## Usage

    ant -lib ant-tools.jar

## QuietLogger

Supresses much of ant's default logging. 
`stdout` and `stderr` are still printed.

On task completion, prints current date and time.

    ant -lib ant-tools.jar -logger us.kirchmeier.tools.QuietLogger


