package org.apidb.apicommon.datasetPresenter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Parse dataset properties files from a directory.  These are created from the Datasets and DatasetClasses
 * used by the workflow
 */
public class DatasetPropertiesParser  {

   /**
    * Parse a dataset properties file.  For each dataset in the file make a map of properties
    * and add it to the datasetNameToProperties map provided.
    * @param propFileName
    * @param datasetnameToProperties
    */
  static void parseFile(String propFileName, Map<String,Map<String,String>> datasetNameToProperties) {
    BufferedReader in = null;

    Map<String,String> datasetProperties = null;
    try {
      try {
        in = new BufferedReader(new FileReader(propFileName));

        while (in.ready()) {
          String line = in.readLine();
          if (line == null)
            break; // to dodge findbugs erro
          line = line.trim();
          if (line.startsWith("#"))
            continue;
          if (line.length() == 0)
            continue;
          
          String[] a = line.split("=",2);
          if (a[0].equals("datasetLoaderName")) {
            datasetProperties = new HashMap<String, String>();
            if (datasetNameToProperties.containsKey(a[1])) throw new UserException("Dataset Properties file " + propFileName + " contains duplicate dataset " + a[1]);
            if (!a[1].endsWith("_RSRC")) throw new UserException("Dataset Properties file " + propFileName + " contains dataset " + a[1] + " which does not end in _RSRC");
            datasetNameToProperties.put(a[1], datasetProperties);
          }
          datasetProperties.put(a[0], a[1]);
        }
      } catch (FileNotFoundException ex) {
        throw new UserException("Dataset Properties file " + propFileName
            + " not found");
      } finally {
        if (in != null)
          in.close();
      }
    } catch (IOException ex) {
      throw new UnexpectedException(ex);
    }
  }

  Map<String,Map<String,String>> parseAllPropertyFiles() {
    Map<String,Map<String,String>> answer = new HashMap<String,Map<String,String>>();
    String gus_home = System.getenv("GUS_HOME");
    Path startingDir = Paths.get(gus_home + "/lib/prop/datasetProperties");
    try {
      Files.walkFileTree(startingDir, new PropFileParser(answer));
      return answer;
    } catch (IOException ex) {
      throw new UnexpectedException(ex);
    }    
  }

  class PropFileParser extends SimpleFileVisitor<Path> {
    private Map<String,Map<String,String>> answer;
    
    PropFileParser(Map<String,Map<String,String>> answer) {
      super();
      this.answer = answer;
    }
    
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
      if (file.toString().endsWith(".prop")) parseFile(file.toString(), answer);
      return FileVisitResult.CONTINUE;
    }
  }
}
