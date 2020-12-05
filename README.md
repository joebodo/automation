# automation

Automation is a gradle project to help automate all things when working with a hybris project. Easily create custom tasks and groovy scripts for your specific project. Just create a fork for your project and add/update scripts to automate various tasks in the project.

## Setup
clone project

copy gradle.properties-EXAMPLE to gradle.properties

edit gradle.properties to include correct paths

In Intellij, click File -> New -> Module from existing sources...

select the automation project and select Gradle when prompted.

## Custom tasks
Custom tasks can be added in the tasks folder. Custom tasks can invoke shell scripts, ant targets, etc.

## Custom scripts
Groovy scripts are sent and executed in Hac.

Drop groovy scripts into the scripts folder and a corresponding gradle task will be created.

<p float="left">
<img src="https://github.com/joebodo/automation/raw/main/.assets/tasks.jpg?raw=true" width="313" height="390">
<img src="https://github.com/joebodo/automation/raw/main/.assets/scripts.jpg?raw=true" width="313" height="390">
</p>

## Run arbitrary groovy script in Hac
Program: Select the gradlew command from the automation project

Arguments: -DgroovyScript=$FilePath$ runScript

Uncheck: Synchronize files after execution

<p float="left">
<img src="https://github.com/joebodo/automation/raw/main/.assets/iTool.jpg?raw=true" width="300" height="318">
<img src="https://github.com/joebodo/automation/raw/main/.assets/iRun.jpg?raw=true" width="300" height="410">
</p>

## Hac customizations 
<img src="https://github.com/joebodo/automation/raw/main/.assets/hac.jpg?raw=true" width="530" height="316">

