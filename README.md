# MapTool-jadebringer-frameworks

MapTool-jadebringer-frameworks is an extension for [MapTool](https://www.rptools.net/toolbox/maptool/) (by RPTools.net). MapTool 
is a full and free Virtual Table Top application. These extensions bring new functionality 
for MapTool. We will contribute to the official MapTool (in short MT) as well, but not 
all the features here will directly make it to MT and some maybe never. So if you want 
to have them now, get this extension.

### But what's Jadebringer?

Jadebringer is a Role Playing System based on the Midwinter Chronicles Fantasy novels.
More info on [jadebringer.de](http://www.jadebringer.de)
	
## Features:

- have a "/call functionName(parameters)" chat macro to call any function via chat
- show chat in fullscreen
- show initiative in fullscreen
- show button frames, even in fullscreen 
    - can be dragged, resized, minimized
    - have buttons to call macros with title or icon
    - buttons can be created, deactivated or hidden via macros
    - control the frame/buttons with macros
- manipulate token on other maps safely
- inspect and manipulate variables in macros
    -  via debug_inspect(variableNames) or debug_manipulate(variablesNames) 
    - other debug related functions as debug/trace/warn/error
- build in outputTo() function
- input dialog with yes/no/cancel buttons via inputYesNoCancel(...) 
- map macros to center map/show whole map area
    - maps_center()
    - maps_extent()
    - maps_getExtent()
- convenience functions for load/save content (any content type from token macros/properties/tables)
    - content_load("mycontentMacro", "tokenMacro", "mytoken")
    - content_load("mycontent", "tokenProperty", "mytoken")
    - content_load("1", "table", "mytable")
    - content_save("mycontent", "tokenMacro", "mytoken", "any content") 
- convenience functions for load/save settings in token properties
    - listSettings, deleteSetting, setSetting, getSetting 
- convenience functions to create macro links 
    - links_createAnchor
    - links_createLink
    - links_execLink   
- function to quickly call macros locally or on another client/player and define where to send the output
    - /call macros_executeMacro("test@Lib:JadebringerSettings")
    - /call macros_executeMacroSendOutput("test@Lib:JadebringerSettings","	Oliver")
    - /call macros_sendExecuteMacro("userThatWeWantToRunTheMacro","please respond", "click here","test@Lib:JadebringerSettings","userOrChannelThatGetsTheResponse")
    - of course works with existing auto execution of macros

Optional: the Java extension framework (more info for Java developers on request)
- extensions are call macros and macro function, so you can do more via macros and chat
- add call macros and macro functions via Java dynamically
- security build in, checks for trusted and that no not allowed code is executed
- additional feature: load Java extensions as java jars via file system or any URL

## Requisites

The only thing you need to make this extension available in MapTool is the release artifact.
E.g. that's _MapTool-jadebringer-frameworks-1.5.1.0.jar_
You can download one from the releases in Github or if you are a developer you can build 
it from source as described in the BUILD documentation.

And cause this is an extension you need to install [MapTool](https://www.rptools.net/toolbox/maptool/) (version 1.5.1 or higher) 
before. We might  go with a full fork of MT in the future, which means you would only 
install a special fork of MT with the extension included, but there are no plans for 
that yet.

## Install

You can use the existing installation of MapTool v1.5.1 or higher with this extension framework.
To have the additional functions just change the MapTool config file which you can find inside the MapTool installation directory, e.g. in

~~~

C:\Users\username\AppData\Local\MapTool-1.5.1\app\MapTool.cfg

~~~

Note: You might want to copy the file just in case that you do not want to use the framework 
anymore. If that is the case (we hope not) just copy this backup of the original file 
back and override the change file.

Now open the file and change the line with "app.classpath" to include the "MapTool-jadebringer-frameworks-1.5.1.0.jar"

~~~
app.classpath=MapTool-1.5.1.jar;MapTool-jadebringer-frameworks-1.5.1.0.jar
~~~

	

And the line with "app.mainclass" to match this:
~~~
app.mainclass=de/jadebringer/maptool/frameworks/LaunchInstructionsWrapper
~~~

Then copy the "MapTool-jadebringer-frameworks-1.5.1.0.jar" inside this app directory. Now you can start MapTool as usual.

## First steps

To see if the extension framework was correctly setup you can now open MapTool as usual 
and type 

~~~

/call ping

~~~

in the chat. This should output the used version of the MapTool-extension-frameworks. 
Don't worry if it does not match the filenames version. This will show an internal version 
of the jadebringer-base-framework that is the default framework from the extension.

~~~

/call frames_showAllFrames

~~~

This will open all the button frames that the jadebringer-base-framework defines by 
default. Have fun clicking on then. These are examples what you can do with button frames 
using macros yourself.

All other changes are basically new functions for use in macros. So feel free to create 
or modify a macro and use the macro editor auto completion feature to see them.

E.g. type "frames_" followed by CTRL-space (or CMD-space on Mac) and you will see all the button frames related functions.

Have fun ;)

## Questions

Any questions? Feel free to message to _naciron_ in the official [MapTool discord](https://discord.gg/crpk7FM).
In there you can have live chats with the devs from this extension and also the official  team behind MapTool. 
Or email to contact (AT) jadebringer . de

