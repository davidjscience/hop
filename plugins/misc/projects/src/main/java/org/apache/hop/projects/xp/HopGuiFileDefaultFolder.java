package org.apache.hop.projects.xp;

import org.apache.hop.core.extension.IExtensionPoint;
import org.apache.hop.core.logging.ILogChannel;
import org.apache.hop.core.util.StringUtil;
import org.apache.hop.core.variables.IVariables;
import org.apache.hop.projects.config.ProjectsConfig;
import org.apache.hop.projects.config.ProjectsConfigSingleton;
import org.apache.hop.projects.project.ProjectConfig;
import org.apache.hop.ui.core.dialog.IFileDialog;
import org.apache.hop.ui.core.gui.HopNamespace;
import org.apache.hop.ui.hopgui.HopGui;
import org.apache.hop.ui.hopgui.delegates.HopGuiFileDialogExtension;

public class HopGuiFileDefaultFolder implements IExtensionPoint<HopGuiFileDialogExtension> {

  @Override public void callExtensionPoint( ILogChannel log, HopGuiFileDialogExtension ext ) {

    // Is there an active project?
    //
    IVariables variables = HopGui.getInstance().getVariables();
    String projectName = HopNamespace.getNamespace();
    if ( StringUtil.isEmpty(projectName)) {
      return;
    }
    try {
      ProjectsConfig config = ProjectsConfigSingleton.getConfig();
      ProjectConfig projectConfig = config.findProjectConfig( projectName );
      if (projectConfig==null) {
        return;
      }
      String homeFolder = projectConfig.getProjectHome();
      if (homeFolder!=null) {
        IFileDialog dialog = ext.getFileDialog();
        dialog.setFilterPath(homeFolder);
      }
    } catch(Exception e) {
      log.logError( "Error setting default folder for project "+projectName, e );
    }
  }
}
