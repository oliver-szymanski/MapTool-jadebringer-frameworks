# MapTool-jadebringer-frameworks

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

## Install

You can use the existing installation of MapTool v1.5.1 or higher with this extension framework.
To have the additional functions just change the MapTool config file which you can find inside the MapTool installation directory, e.g. in

C:\Users\username\AppData\Local\MapTool-1.5.1\app\MapTool.cfg

Change the line with "app.classpath" to include the "MapTool-jadebringer-frameworks.jar"

app.classpath=MapTool-1.5.1.jar;MapTool-jadebringer-frameworks.jar

And the line with "app.mainclass" to match this:
app.mainclass=de/jadebringer/maptool/frameworks/LaunchInstructionsWrapper

Then copy the "MapTool-jadebringer.jar" inside this app directory. Now you can start MapTool as usual.