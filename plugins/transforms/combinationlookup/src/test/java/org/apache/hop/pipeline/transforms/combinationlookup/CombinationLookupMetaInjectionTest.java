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

package org.apache.hop.pipeline.transforms.combinationlookup;

import org.apache.hop.core.injection.BaseMetadataInjectionTest;
import org.apache.hop.junit.rules.RestoreHopEngineEnvironment;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class CombinationLookupMetaInjectionTest extends BaseMetadataInjectionTest<CombinationLookupMeta> {
  @ClassRule public static RestoreHopEngineEnvironment env = new RestoreHopEngineEnvironment();

  @Before
  public void setup() throws Exception {
    setup( new CombinationLookupMeta() );
  }

  @Test
  public void test() throws Exception {
    check( "SCHEMA_NAME", new IStringGetter() {
      public String get() {
        return meta.getSchemaName();
      }
    } );
    check( "TABLE_NAME", new IStringGetter() {
      public String get() {
        return meta.getTableName();
      }
    } );
    check( "REPLACE_FIELDS", new IBooleanGetter() {
      public boolean get() {
        return meta.replaceFields();
      }
    } );
    check( "KEY_FIELDS", new IStringGetter() {
      public String get() {
        return meta.getKeyField()[ 0 ];
      }
    } );
    check( "KEY_LOOKUP", new IStringGetter() {
      public String get() {
        return meta.getKeyLookup()[ 0 ];
      }
    } );
    check( "USE_HASH", new IBooleanGetter() {
      public boolean get() {
        return meta.useHash();
      }
    } );
    check( "HASH_FIELD", new IStringGetter() {
      public String get() {
        return meta.getHashField();
      }
    } );
    check( "TECHNICAL_KEY_FIELD", new IStringGetter() {
      public String get() {
        return meta.getTechnicalKeyField();
      }
    } );
    check( "SEQUENCE_FROM", new IStringGetter() {
      public String get() {
        return meta.getSequenceFrom();
      }
    } );
    check( "COMMIT_SIZE", new IIntGetter() {
      public int get() {
        return meta.getCommitSize();
      }
    } );
    check( "PRELOAD_CACHE", new IBooleanGetter() {
      public boolean get() {
        return meta.getPreloadCache();
      }
    } );
    check( "CACHE_SIZE", new IIntGetter() {
      public int get() {
        return meta.getCacheSize();
      }
    } );
    check( "AUTO_INC", new IBooleanGetter() {
      public boolean get() {
        return meta.isUseAutoinc();
      }
    } );
    check( "TECHNICAL_KEY_CREATION", new IStringGetter() {
      public String get() {
        return meta.getTechKeyCreation();
      }
    } );
    check( "LAST_UPDATE_FIELD", new IStringGetter() {
      public String get() {
        return meta.getLastUpdateField();
      }
    } );
    skipPropertyTest( "CONNECTIONNAME" );
  }
}
