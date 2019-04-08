# Build MapTool-jadebringer-frameworks

## Why building it?

If you are a developer and want to build the framework from source code this is the way to 
go. It will create the ".jar" file that you need for installing it into MapTool (which 
is described in the [README](README.md)).

If you are a Java developer you can also add you own functions for MapTool macros easily. 
A documentation how to do that will follow. Until then feel free to contact the Jadebringer 
team for more info on that (see contact details in README).

## How to build?

MapTool-jadebringer-frameworks is based on Gradle build and includes a Gradle Wrapper. 
So basically all you need it Java SDK 10+. If you got that you can build it. Just check 
the source out from github as usual and build with gradle.

E.g.

~~~
git clone GITHUB-URL
./gradlew clean build
~~~

This will download the sources and dependencies needed to compile, including some sources 
from MapTool. You don't need that later, it is just needed to compile/build.

The generated artifact that you care about is then under build/libs, usually called 
something like

~~~
MapTool-jadebringer-frameworks-1.0.0.jar
~~~

That's it. You're good to go (see [README](README.md) for what to do with it).

