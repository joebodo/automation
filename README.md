# automation

Groovy scripts are sent and executed in Hac.

<p float="left">
<img src="https://github.com/joebodo/automation/raw/main/.assets/tasks.jpg?raw=true" width="313" height="390">
<img src="https://github.com/joebodo/automation/raw/main/.assets/scripts.jpg?raw=true" width="313" height="390">
</p>

####Setup
clone project
copy gradle.properties-EXAMPLE to gradle.properties
edit gradle.properties to include correct paths

In Intellij, click File -> New -> Module from existing sources...
select the automation project and select Gradle when prompted.

####Custom tasks
Custom tasks can be added in the tasks folder. Custom tasks can invoke shell scripts, ant targets, etc.

####Custom scripts
Drop groovy scripts into the scripts folder and a corresponding gradle task will be created.

####Run arbitrary groovy script in Hac
<p float="left">
<img src="https://github.com/joebodo/automation/raw/main/.assets/iTool.jpg?raw=true" width="300" height="318">
<img src="https://github.com/joebodo/automation/raw/main/.assets/iRun.jpg?raw=true" width="300" height="410">
</p>