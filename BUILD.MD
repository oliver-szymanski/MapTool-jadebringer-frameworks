First build MapTool with

./gradlew clean build install deploy

To generate a lib jar containing all dependencies. You might need to rename something like:
MapTool-SNAPSHOT-2dc19cf.jar

to MapTool-Jaderbinger.jar to make it consistent to the dependendy setup in settings.gradle

Then build this extension jar with 

./gradlew clean build