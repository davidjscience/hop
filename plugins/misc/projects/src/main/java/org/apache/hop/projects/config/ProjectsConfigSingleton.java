package org.apache.hop.projects.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.hop.core.config.HopConfig;
import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.logging.LogChannel;

public class ProjectsConfigSingleton {

  private static ProjectsConfigSingleton configSingleton;

  private ProjectsConfig projectsConfig;

  private ProjectsConfigSingleton() {
    // Load from the HopConfig store
    //
    Object configObject = HopConfig.getInstance().getConfigMap().get( ProjectsConfig.HOP_CONFIG_PROJECTS_CONFIG_KEY );
    if ( configObject == null ) {
      projectsConfig = new ProjectsConfig();
    } else {
      // The way Jackson stores these simple POJO is with a map per default...
      // So we don't really need to mess around with Deserializer and so on.
      // This way we can keep the class name out of the JSON as well.
      //
      try {
        ObjectMapper mapper = new ObjectMapper();
        projectsConfig = mapper.readValue( new Gson().toJson( configObject ), ProjectsConfig.class );
      } catch ( Exception e ) {
        LogChannel.GENERAL.logError( "Error reading environments configuration, check property '" + ProjectsConfig.HOP_CONFIG_PROJECTS_CONFIG_KEY + "' in the Hop config json file", e );
        projectsConfig = new ProjectsConfig();
      }
    }
    HopConfig.getInstance().getConfigMap().put( ProjectsConfig.HOP_CONFIG_PROJECTS_CONFIG_KEY, projectsConfig );
  }

  public static ProjectsConfig getConfig() {
    if ( configSingleton == null ) {
      configSingleton = new ProjectsConfigSingleton();
    }
    return configSingleton.projectsConfig;
  }

  public static void saveConfig() throws HopException {
    HopConfig.getInstance().saveOption( ProjectsConfig.HOP_CONFIG_PROJECTS_CONFIG_KEY, configSingleton.projectsConfig );
    HopConfig.getInstance().saveToFile();
  }
}
