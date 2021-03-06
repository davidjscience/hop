package org.apache.hop.core.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hop.core.exception.HopException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfigFileSerializer implements IHopConfigSerializer {
  @Override public void writeToFile( String filename, Map<String, Object> configMap ) throws HopException {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String niceJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString( configMap );

      // Write to a new new file...
      //
      File newFile = new File( filename + ".new" );
      if ( newFile.exists() ) {
        if ( !newFile.delete() ) {
          throw new HopException( "Unable to delete new config file " + newFile.getPath() );
        }
      }

      // Write to the new file (hop.config.new)
      //
      FileOutputStream fos = new FileOutputStream( newFile );
      fos.write( niceJson.getBytes( "UTF-8" ) );
      fos.close();

      // if this worked, delete the old file  (hop.config.old)
      //
      File oldFile = new File( filename + ".old" );
      if ( oldFile.exists() ) {
        if ( !oldFile.delete() ) {
          throw new HopException( "Unable to delete old config file " + oldFile.getPath() );
        }
      }

      // If this worked, rename the file to the old file  (hop.config -> hop.config.old)
      //
      File file = new File( filename );
      if (file.exists()) { // could be a new file
        if ( !file.renameTo( oldFile ) ) {
          throw new HopException( "Unable to rename config file to .old : " + file.getPath() );
        }
      }

      // Now rename the new file to the final value...
      //
      if ( !newFile.renameTo( file ) ) {
        throw new HopException( "Unable to rename config .new file to : " + file.getPath() );
      }
    } catch ( Exception e ) {
      throw new HopException( "Error writing to Hop configuration file : " + filename, e );
    }
  }

  @Override public Map<String, Object> readFromFile( String filename ) throws HopException {
    try {
      File file = new File( filename );
      if (!file.exists()) {
        // Just an empty config map.
        //
        return new HashMap<>();
      }
      ObjectMapper objectMapper = new ObjectMapper();
      TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
      HashMap<String, Object> configMap = objectMapper.readValue( new File( filename ), typeRef );
      return configMap;
    } catch ( Exception e ) {
      throw new HopException( "Error reading Hop configuration file " + filename, e );
    }
  }
}
