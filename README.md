# MapTool-jadebringer-frameworks

## Ideas
- findToken with returning tokenId, name, map, ...
- doWithToken, changing to the zone and back again

Features:

- have a "/call functionName(parameters)" chat macro to call any function via chat
- show chat in fullscreen
- show button frames, even in fullscreen 
    - can be dragged, resized, minimized
    - have buttons to call macros with title or icon
- manipulate token on other maps safely
- inspect and manipulate variables via inspect(variableNames) or manipulate(variablesNames) 
- load/save content (like csv, xml, json, ... any from token macros/properties/tables)
    - fits perfectly with the new xml/json/... syntax highlighting property editor
    - loadContent("mycontent", "token", "mytoken")
    - loadContent("mycontent", "property", "mytoken")
    - loadContent("1", "table", "mytable")
    - saveContent("mycontent", "token", "mytoken", "any content")    
- function to quickly call macros locally or on another client/player and define where to send the output
    - /call executeMacro("test@Lib:JadebringerSettings")
    - /call executeMacroSendOutput("test@Lib:JadebringerSettings","	Oliver")
    - /call sendExecuteMacro("userThatWeWantToRunTheMacro","please respond", "click here","test@Lib:JadebringerSettings","userOrChannelThatGetsTheResponse")
    - of course works with existing auto execution of macros

Optional: the Java extension framework (more info for Java developers on request)
- extensions are call macros and macro function, so you can do more via macros and chat
- add call macros and macro functions via Java dynamically
- security build in, checks for trusted and that no not allowed code is executed
- additional feature: load Java extensions as java jars via file system or any URL
