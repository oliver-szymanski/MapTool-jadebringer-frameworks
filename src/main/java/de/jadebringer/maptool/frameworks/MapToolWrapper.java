package de.jadebringer.maptool.frameworks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.functions.frameworkfunctions.FrameworksFunctions;
import net.rptools.parser.function.Function;

public class MapToolWrapper {

  public static void main(String[] args) throws IOException {
    FrameworksFunctions frameworkFunctions = FrameworksFunctions.getInstance();
    List<Function> macroFunctions = MapTool.getParser().getMacroFunctions();
    if (!macroFunctions.contains(frameworkFunctions)) {
      MapTool.getParser().getMacroFunctions().add(frameworkFunctions);
    }
    //MapTool.main(args);
    String byteArray = "[99, 2, 0, 109, 0, 16, 102, 105, 110, 100, 65, 108, 108, 73, 110, 115, 116, 97, 110, 99, 101, 115, 122, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]";

    String[] byteValues = byteArray.substring(1, byteArray.length() - 1).split(",");
    byte[] bytes = new byte[23];

    System.out.println("len: "+byteArray.length());
    for (int i=0, len=bytes.length; i<len; i++) {
       bytes[i] = Byte.parseByte(byteValues[i].trim());     
    }
    
    String request = new String(bytes);
    System.out.println("hessian request: '"+request+"'");

    String str = new String(bytes);
    URL url = new URL("http://services.rptools.net/maptool_registry-1_3.php");
    java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
    con.setDoOutput(true);
    OutputStream out = con.getOutputStream();
    out.write(bytes);
    
    java.io.InputStream in = con.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line = reader.readLine();
    System.out.println(line);
  }
}
