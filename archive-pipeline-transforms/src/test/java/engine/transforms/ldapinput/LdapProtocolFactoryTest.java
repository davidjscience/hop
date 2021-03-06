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

package org.apache.hop.pipeline.transforms.ldapinput;

import org.apache.hop.core.logging.LogChannelInterface;
import org.apache.hop.core.variables.IVariables;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

public class LdapProtocolFactoryTest {

  @Test
  public void createLdapProtocol() throws Exception {
    String ldapVariable = "${ldap_protocol_variable}";
    String ldap = "LDAP";
    String host = "localhost";

    LdapProtocolFactory ldapProtocolFactory = new LdapProtocolFactory( Mockito.mock( LogChannelInterface.class ) );
    IVariables variables = Mockito.mock( IVariables.class );
    LdapMeta meta = Mockito.mock( LdapMeta.class );
    Mockito.doReturn( ldapVariable ).when( meta ).getProtocol();
    Mockito.doReturn( ldap ).when( variables ).environmentSubstitute( ldapVariable );
    Mockito.doReturn( host ).when( meta ).getHost();
    Mockito.doReturn( host ).when( variables ).environmentSubstitute( host );

    ldapProtocolFactory.createLdapProtocol( variables, meta, Collections.emptyList() );
    Mockito.verify( variables, Mockito.times( 1 ) ).environmentSubstitute( ldapVariable );

  }
}
