/*! ******************************************************************************
 *
 * Hop : The Hop Orchestration Platform
 *
 * http://www.project-hop.org
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.apache.hop.testing.xp;

import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.extension.ExtensionPoint;
import org.apache.hop.core.extension.IExtensionPoint;
import org.apache.hop.core.logging.ILogChannel;
import org.apache.hop.core.util.StringUtil;
import org.apache.hop.pipeline.PipelineMeta;
import org.apache.hop.testing.PipelineUnitTest;
import org.apache.hop.testing.gui.TestingGuiPlugin;
import org.apache.hop.testing.util.DataSetConst;

@ExtensionPoint(
  extensionPointId = "HopGuiPipelineMetaExecutionStart",
  id = "HopGuiFlagUnitTestExtensionPoint",
  description = "Change the pipeline metadata prior to execution but only in HopGui"
)
public class HopGuiFlagUnitTestExtensionPoint implements IExtensionPoint<PipelineMeta> {

  @Override
  public void callExtensionPoint( ILogChannel log, PipelineMeta pipelineMeta ) throws HopException {

    PipelineUnitTest unitTest = TestingGuiPlugin.getCurrentUnitTest( pipelineMeta );
    if ( unitTest == null ) {
      return;
    }

    String unitTestName = unitTest.getName();

    if ( !StringUtil.isEmpty( unitTestName ) ) {
      // We're running in HopGui and there's a unit test selected : test it
      //
      pipelineMeta.setVariable( DataSetConst.VAR_RUN_UNIT_TEST, "Y" );
      pipelineMeta.setVariable( DataSetConst.VAR_UNIT_TEST_NAME, unitTestName );
    }
  }

}
