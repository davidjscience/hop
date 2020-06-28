/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
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

package org.apache.hop.pipeline.transforms.regexeval;

import java.util.List;

import org.apache.hop.core.CheckResult;
import org.apache.hop.core.Const;
import org.apache.hop.core.annotations.Transform;
import org.apache.hop.core.exception.HopPluginException;
import org.apache.hop.core.exception.HopTransformException;
import org.apache.hop.core.exception.HopXmlException;
import org.apache.hop.core.row.IRowMeta;
import org.apache.hop.core.row.IValueMeta;
import org.apache.hop.core.row.value.ValueMetaBoolean;
import org.apache.hop.core.row.value.ValueMetaFactory;
import org.apache.hop.core.row.value.ValueMetaString;
import org.apache.hop.core.util.Utils;
import org.apache.hop.core.variables.IVariables;
import org.apache.hop.core.xml.XmlHandler;
import org.apache.hop.i18n.BaseMessages;
import org.apache.hop.metadata.api.IHopMetadataProvider;
import org.apache.hop.pipeline.transform.*;
import org.w3c.dom.Node;

@Transform(
        id = "RegeVal",
        image = "regexevalsvg",
        i18nPackageName = "org.apache.hop.pipeline.transforms.regexeval",
        name = "BaseTransform.TypeLongDesc.RegexEval",
        description = "BaseTransform.TypeTooltipDesc.RegexEval",
        categoryDescription = "i18n:org.apache.hop.pipeline.transform:BaseTransform.Category.Transform",
        documentationUrl = "https://www.project-hop.org/manual/latest/plugins/transforms/regexeval.html"
)
public class RegexEvalMeta extends BaseTransformMeta implements ITransformMeta<RegexEval, RegexEvalData> {
  private static Class<?> PKG = RegexEvalMeta.class; // for i18n purposes, needed by Translator2!!

  private String script;
  private String matcher;
  private String resultfieldname;
  private boolean usevar;

  private boolean allowcapturegroups;
  private boolean replacefields;

  private boolean canoneq;
  private boolean caseinsensitive;
  private boolean comment;
  private boolean dotall;
  private boolean multiline;
  private boolean unicode;
  private boolean unix;

  private String[] fieldName;
  private int[] fieldType;
  private String[] fieldFormat;
  private String[] fieldGroup;
  private String[] fieldDecimal;
  private String[] fieldCurrency;
  private int[] fieldLength;
  private int[] fieldPrecision;
  private String[] fieldNullIf;
  private String[] fieldIfNull;
  private int[] fieldTrimType;

  public RegexEvalMeta() {
    super();
  }

  public Object clone() {
    RegexEvalMeta retval = (RegexEvalMeta) super.clone();

    int nrfields = fieldName.length;

    retval.allocate( nrfields );
    System.arraycopy( fieldName, 0, retval.fieldName, 0, nrfields );
    System.arraycopy( fieldType, 0, retval.fieldType, 0, nrfields );
    System.arraycopy( fieldLength, 0, retval.fieldLength, 0, nrfields );
    System.arraycopy( fieldPrecision, 0, retval.fieldPrecision, 0, nrfields );
    System.arraycopy( fieldFormat, 0, retval.fieldFormat, 0, nrfields );
    System.arraycopy( fieldGroup, 0, retval.fieldGroup, 0, nrfields );
    System.arraycopy( fieldDecimal, 0, retval.fieldDecimal, 0, nrfields );
    System.arraycopy( fieldCurrency, 0, retval.fieldCurrency, 0, nrfields );
    System.arraycopy( fieldNullIf, 0, retval.fieldNullIf, 0, nrfields );
    System.arraycopy( fieldIfNull, 0, retval.fieldIfNull, 0, nrfields );
    System.arraycopy( fieldTrimType, 0, retval.fieldTrimType, 0, nrfields );

    return retval;
  }

  public void allocate( int nrfields ) {
    fieldName = new String[nrfields];
    fieldType = new int[nrfields];
    fieldFormat = new String[nrfields];
    fieldGroup = new String[nrfields];
    fieldDecimal = new String[nrfields];
    fieldCurrency = new String[nrfields];
    fieldLength = new int[nrfields];
    fieldPrecision = new int[nrfields];
    fieldNullIf = new String[nrfields];
    fieldIfNull = new String[nrfields];
    fieldTrimType = new int[nrfields];
  }

  public String getScript() {
    return script;
  }

  public String getRegexOptions() {
    StringBuilder options = new StringBuilder();

    if ( isCaseInsensitiveFlagSet() ) {
      options.append( "(?i)" );
    }
    if ( isCommentFlagSet() ) {
      options.append( "(?x)" );
    }
    if ( isDotAllFlagSet() ) {
      options.append( "(?s)" );
    }
    if ( isMultilineFlagSet() ) {
      options.append( "(?m)" );
    }
    if ( isUnicodeFlagSet() ) {
      options.append( "(?u)" );
    }
    if ( isUnixLineEndingsFlagSet() ) {
      options.append( "(?d)" );
    }
    return options.toString();
  }

  public void setScript( String script ) {
    this.script = script;
  }

  public String getMatcher() {
    return matcher;
  }

  public void setMatcher( String matcher ) {
    this.matcher = matcher;
  }

  public String getResultFieldName() {
    return resultfieldname;
  }

  public void setResultFieldName( String resultfieldname ) {
    this.resultfieldname = resultfieldname;
  }

  public boolean isUseVariableInterpolationFlagSet() {
    return usevar;
  }

  public void setUseVariableInterpolationFlag( boolean usevar ) {
    this.usevar = usevar;
  }

  public boolean isAllowCaptureGroupsFlagSet() {
    return allowcapturegroups;
  }

  public void setAllowCaptureGroupsFlag( boolean allowcapturegroups ) {
    this.allowcapturegroups = allowcapturegroups;
  }

  public boolean isReplacefields() {
    return replacefields;
  }

  public void setReplacefields( boolean replacefields ) {
    this.replacefields = replacefields;
  }

  public boolean isCanonicalEqualityFlagSet() {
    return canoneq;
  }

  public void setCanonicalEqualityFlag( boolean canoneq ) {
    this.canoneq = canoneq;
  }

  public boolean isCaseInsensitiveFlagSet() {
    return caseinsensitive;
  }

  public void setCaseInsensitiveFlag( boolean caseinsensitive ) {
    this.caseinsensitive = caseinsensitive;
  }

  public boolean isCommentFlagSet() {
    return comment;
  }

  public void setCommentFlag( boolean comment ) {
    this.comment = comment;
  }

  public boolean isDotAllFlagSet() {
    return dotall;
  }

  public void setDotAllFlag( boolean dotall ) {
    this.dotall = dotall;
  }

  public boolean isMultilineFlagSet() {
    return multiline;
  }

  public void setMultilineFlag( boolean multiline ) {
    this.multiline = multiline;
  }

  public boolean isUnicodeFlagSet() {
    return unicode;
  }

  public void setUnicodeFlag( boolean unicode ) {
    this.unicode = unicode;
  }

  public boolean isUnixLineEndingsFlagSet() {
    return unix;
  }

  public void setUnixLineEndingsFlag( boolean unix ) {
    this.unix = unix;
  }

  public String[] getFieldName() {
    return fieldName;
  }

  public void setFieldName( String[] value ) {
    this.fieldName = value;
  }

  public int[] getFieldType() {
    return fieldType;
  }

  public void setFieldType( int[] fieldType ) {
    this.fieldType = fieldType;
  }

  public String[] getFieldFormat() {
    return fieldFormat;
  }

  public void setFieldFormat( String[] fieldFormat ) {
    this.fieldFormat = fieldFormat;
  }

  public String[] getFieldGroup() {
    return fieldGroup;
  }

  public void setFieldGroup( String[] fieldGroup ) {
    this.fieldGroup = fieldGroup;
  }

  public String[] getFieldDecimal() {
    return fieldDecimal;
  }

  public void setFieldDecimal( String[] fieldDecimal ) {
    this.fieldDecimal = fieldDecimal;
  }

  public String[] getFieldCurrency() {
    return fieldCurrency;
  }

  public void setFieldCurrency( String[] fieldCurrency ) {
    this.fieldCurrency = fieldCurrency;
  }

  public int[] getFieldLength() {
    return fieldLength;
  }

  public void setFieldLength( int[] fieldLength ) {
    this.fieldLength = fieldLength;
  }

  public int[] getFieldPrecision() {
    return fieldPrecision;
  }

  public void setFieldPrecision( int[] fieldPrecision ) {
    this.fieldPrecision = fieldPrecision;
  }

  public String[] getFieldNullIf() {
    return fieldNullIf;
  }

  public void setFieldNullIf( final String[] fieldNullIf ) {
    this.fieldNullIf = fieldNullIf;
  }

  public String[] getFieldIfNull() {
    return fieldIfNull;
  }

  public void setFieldIfNull( final String[] fieldIfNull ) {
    this.fieldIfNull = fieldIfNull;
  }

  public int[] getFieldTrimType() {
    return fieldTrimType;
  }

  public void setFieldTrimType( final int[] fieldTrimType ) {
    this.fieldTrimType = fieldTrimType;
  }

  private void readData( Node transform) throws HopXmlException {
    try {
      script = XmlHandler.getTagValue(transform, "script" );
      matcher = XmlHandler.getTagValue(transform, "matcher" );
      resultfieldname = XmlHandler.getTagValue(transform, "resultfieldname" );
      usevar = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "usevar" ) );
      allowcapturegroups = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "allowcapturegroups" ) );
      replacefields = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "replacefields" ) );
      canoneq = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "canoneq" ) );
      caseinsensitive = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "caseinsensitive" ) );
      comment = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "comment" ) );
      dotall = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "dotall" ) );
      multiline = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "multiline" ) );
      unicode = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "unicode" ) );
      unix = "Y".equalsIgnoreCase( XmlHandler.getTagValue(transform, "unix" ) );

      Node fields = XmlHandler.getSubNode(transform, "fields" );
      int nrfields = XmlHandler.countNodes( fields, "field" );

      allocate( nrfields );

      for ( int i = 0; i < nrfields; i++ ) {
        Node fnode = XmlHandler.getSubNodeByNr( fields, "field", i );

        fieldName[i] = XmlHandler.getTagValue( fnode, "name" );
        final String stype = XmlHandler.getTagValue( fnode, "type" );
        fieldFormat[i] = XmlHandler.getTagValue( fnode, "format" );
        fieldGroup[i] = XmlHandler.getTagValue( fnode, "group" );
        fieldDecimal[i] = XmlHandler.getTagValue( fnode, "decimal" );
        fieldCurrency[i] = XmlHandler.getTagValue( fnode, "currency" );
        final String slen = XmlHandler.getTagValue( fnode, "length" );
        final String sprc = XmlHandler.getTagValue( fnode, "precision" );
        fieldNullIf[i] = XmlHandler.getTagValue( fnode, "nullif" );
        fieldIfNull[i] = XmlHandler.getTagValue( fnode, "ifnull" );
        final String trim = XmlHandler.getTagValue( fnode, "trimtype" );
        fieldType[i] = ValueMetaFactory.getIdForValueMeta( stype );
        fieldLength[i] = Const.toInt( slen, -1 );
        fieldPrecision[i] = Const.toInt( sprc, -1 );
        fieldTrimType[i] = ValueMetaString.getTrimTypeByCode( trim );
      }
    } catch ( Exception e ) {
      throw new HopXmlException( BaseMessages.getString(
        PKG, "RegexEvalMeta.Exception.UnableToLoadStepInfoFromXML" ), e );
    }
  }

  public void setDefault() {
    script = "";
    matcher = "";
    resultfieldname = "result";
    usevar = false;
    allowcapturegroups = false;
    replacefields = true;
    canoneq = false;
    caseinsensitive = false;
    comment = false;
    dotall = false;
    multiline = false;
    unicode = false;
    unix = false;

    allocate( 0 );
  }
  @Override
    public void getFields( IRowMeta inputRowMeta, String name, IRowMeta[] info, TransformMeta nextTransform,
                           IVariables variables, IHopMetadataProvider metadataProvider ) throws HopTransformException{
    try {
      if ( !Utils.isEmpty( resultfieldname ) ) {
        if ( replacefields ) {
          int replaceIndex = inputRowMeta.indexOfValue( resultfieldname );
          if ( replaceIndex < 0 ) {
            IValueMeta v =
              new ValueMetaBoolean( variables.environmentSubstitute( resultfieldname ) );
            v.setOrigin( name );
            inputRowMeta.addValueMeta( v );
          } else {
            IValueMeta valueMeta = inputRowMeta.getValueMeta( replaceIndex );
            IValueMeta replaceMeta =
              ValueMetaFactory.cloneValueMeta( valueMeta, IValueMeta.TYPE_BOOLEAN );
            replaceMeta.setOrigin( name );
            inputRowMeta.setValueMeta( replaceIndex, replaceMeta );
          }
        } else {
          IValueMeta v =
            new ValueMetaBoolean( variables.environmentSubstitute( resultfieldname ) );
          v.setOrigin( name );
          inputRowMeta.addValueMeta( v );
        }
      }

      if ( allowcapturegroups == true ) {
        for ( int i = 0; i < fieldName.length; i++ ) {
          if ( Utils.isEmpty( fieldName[i] ) ) {
            continue;
          }

          if ( replacefields ) {
            int replaceIndex = inputRowMeta.indexOfValue( fieldName[i] );
            if ( replaceIndex < 0 ) {
              inputRowMeta.addValueMeta( constructValueMeta( null, fieldName[i], i, name ) );
            } else {
              IValueMeta valueMeta = inputRowMeta.getValueMeta( replaceIndex );
              IValueMeta replaceMeta = constructValueMeta( valueMeta, fieldName[i], i, name );
              inputRowMeta.setValueMeta( replaceIndex, replaceMeta );
            }
          } else {
            inputRowMeta.addValueMeta( constructValueMeta( null, fieldName[i], i, name ) );
          }
        }
      }
    } catch ( Exception e ) {
      throw new HopTransformException( e );
    }
  }

  private IValueMeta constructValueMeta( IValueMeta sourceValueMeta, String fieldName, int i,
    String name ) throws HopPluginException {
    int type = fieldType[i];
    if ( type == IValueMeta.TYPE_NONE ) {
      type = IValueMeta.TYPE_STRING;
    }
    IValueMeta v;
    if ( sourceValueMeta == null ) {
      v = ValueMetaFactory.createValueMeta( fieldName, type );
    } else {
      v = ValueMetaFactory.cloneValueMeta( sourceValueMeta, type );
    }
    v.setLength( fieldLength[i] );
    v.setPrecision( fieldPrecision[i] );
    v.setOrigin( name );
    v.setConversionMask( fieldFormat[i] );
    v.setDecimalSymbol( fieldDecimal[i] );
    v.setGroupingSymbol( fieldGroup[i] );
    v.setCurrencySymbol( fieldCurrency[i] );
    v.setTrimType( fieldTrimType[i] );

    return v;
  }

  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws HopXMLException {
    readData( stepnode, databases );
  }

  public String getXML() {
    StringBuilder retval = new StringBuilder();

    retval.append( "    "
      + XMLHandler.addTagValue( "script", script ) );
    retval.append( "    " + XMLHandler.addTagValue( "matcher", matcher ) );
    retval.append( "    " + XMLHandler.addTagValue( "resultfieldname", resultfieldname ) );
    retval.append( "    " + XMLHandler.addTagValue( "usevar", usevar ) );
    retval.append( "    " + XMLHandler.addTagValue( "allowcapturegroups", allowcapturegroups ) );
    retval.append( "    " + XMLHandler.addTagValue( "replacefields", replacefields ) );
    retval.append( "    " + XMLHandler.addTagValue( "canoneq", canoneq ) );
    retval.append( "    " + XMLHandler.addTagValue( "caseinsensitive", caseinsensitive ) );
    retval.append( "    " + XMLHandler.addTagValue( "comment", comment ) );
    retval.append( "    " + XMLHandler.addTagValue( "dotall", dotall ) );
    retval.append( "    " + XMLHandler.addTagValue( "multiline", multiline ) );
    retval.append( "    " + XMLHandler.addTagValue( "unicode", unicode ) );
    retval.append( "    " + XMLHandler.addTagValue( "unix", unix ) );

    retval.append( "    <fields>" ).append( Const.CR );
    for ( int i = 0; i < fieldName.length; i++ ) {
      if ( fieldName[i] != null && fieldName[i].length() != 0 ) {
        retval.append( "      <field>" ).append( Const.CR );
        retval.append( "        " ).append( XMLHandler.addTagValue( "name", fieldName[i] ) );
        retval
          .append( "        " ).append( XMLHandler.addTagValue( "type",
            ValueMetaFactory.getValueMetaName( fieldType[i] ) ) );
        retval.append( "        " ).append( XMLHandler.addTagValue( "format", fieldFormat[i] ) );
        retval.append( "        " ).append( XMLHandler.addTagValue( "group", fieldGroup[i] ) );
        retval.append( "        " ).append( XMLHandler.addTagValue( "decimal", fieldDecimal[i] ) );
        retval.append( "        " ).append( XMLHandler.addTagValue( "length", fieldLength[i] ) );
        retval.append( "        " ).append( XMLHandler.addTagValue( "precision", fieldPrecision[i] ) );
        retval.append( "        " ).append( XMLHandler.addTagValue( "nullif", fieldNullIf[i] ) );
        retval.append( "        " ).append( XMLHandler.addTagValue( "ifnull", fieldIfNull[i] ) );
        retval.append( "        " ).append(
          XMLHandler.addTagValue( "trimtype", ValueMetaString.getTrimTypeCode( fieldTrimType[i] ) ) );
        retval.append( "        " ).append( XMLHandler.addTagValue( "currency", fieldCurrency[i] ) );
        retval.append( "      </field>" ).append( Const.CR );
      }
    }
    retval.append( "    </fields>" ).append( Const.CR );

    return retval.toString();
  }

  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases ) throws HopException {
    try {
      script = rep.getStepAttributeString( id_step, "script" );
      matcher = rep.getStepAttributeString( id_step, "matcher" );
      resultfieldname = rep.getStepAttributeString( id_step, "resultfieldname" );
      usevar = rep.getStepAttributeBoolean( id_step, "usevar" );
      allowcapturegroups = rep.getStepAttributeBoolean( id_step, "allowcapturegroups" );
      replacefields = rep.getStepAttributeBoolean( id_step, "replacefields" );
      canoneq = rep.getStepAttributeBoolean( id_step, "canoneq" );
      caseinsensitive = rep.getStepAttributeBoolean( id_step, "caseinsensitive" );
      comment = rep.getStepAttributeBoolean( id_step, "comment" );
      multiline = rep.getStepAttributeBoolean( id_step, "multiline" );
      dotall = rep.getStepAttributeBoolean( id_step, "dotall" );
      unicode = rep.getStepAttributeBoolean( id_step, "unicode" );
      unix = rep.getStepAttributeBoolean( id_step, "unix" );

      int nrfields = rep.countNrStepAttributes( id_step, "field_name" );

      allocate( nrfields );

      for ( int i = 0; i < nrfields; i++ ) {
        fieldName[i] = rep.getStepAttributeString( id_step, i, "field_name" );
        fieldType[i] = ValueMetaFactory.getIdForValueMeta( rep.getStepAttributeString( id_step, i, "field_type" ) );

        fieldFormat[i] = rep.getStepAttributeString( id_step, i, "field_format" );
        fieldGroup[i] = rep.getStepAttributeString( id_step, i, "field_group" );
        fieldDecimal[i] = rep.getStepAttributeString( id_step, i, "field_decimal" );
        fieldLength[i] = (int) rep.getStepAttributeInteger( id_step, i, "field_length" );
        fieldPrecision[i] = (int) rep.getStepAttributeInteger( id_step, i, "field_precision" );
        fieldNullIf[i] = rep.getStepAttributeString( id_step, i, "field_nullif" );
        fieldIfNull[i] = rep.getStepAttributeString( id_step, i, "field_ifnull" );
        fieldCurrency[i] = rep.getStepAttributeString( id_step, i, "field_currency" );
        fieldTrimType[i] =
          ValueMetaString.getTrimTypeByCode( rep.getStepAttributeString( id_step, i, "field_trimtype" ) );
      }
    } catch ( Exception e ) {
      throw new HopException( BaseMessages.getString(
        PKG, "RegexEvalMeta.Exception.UnexpectedErrorInReadingStepInfo" ), e );
    }
  }

  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step ) throws HopException {
    try {
      rep.saveStepAttribute( id_transformation, id_step, "script", script );
      for ( int i = 0; i < fieldName.length; i++ ) {
        if ( fieldName[i] != null && fieldName[i].length() != 0 ) {
          rep.saveStepAttribute( id_transformation, id_step, i, "field_name", fieldName[i] );
          rep
            .saveStepAttribute( id_transformation, id_step, i, "field_type",
              ValueMetaFactory.getValueMetaName( fieldType[i] ) );
          rep.saveStepAttribute( id_transformation, id_step, i, "field_format", fieldFormat[i] );
          rep.saveStepAttribute( id_transformation, id_step, i, "field_group", fieldGroup[i] );
          rep.saveStepAttribute( id_transformation, id_step, i, "field_decimal", fieldDecimal[i] );
          rep.saveStepAttribute( id_transformation, id_step, i, "field_length", fieldLength[i] );
          rep.saveStepAttribute( id_transformation, id_step, i, "field_precision", fieldPrecision[i] );
          rep.saveStepAttribute( id_transformation, id_step, i, "field_nullif", fieldNullIf[i] );
          rep.saveStepAttribute( id_transformation, id_step, i, "field_ifnull", fieldIfNull[i] );
          rep.saveStepAttribute( id_transformation, id_step, i, "field_currency", fieldCurrency[i] );
          rep.saveStepAttribute( id_transformation, id_step, i, "field_trimtype", ValueMetaString
            .getTrimTypeCode( fieldTrimType[i] ) );
        }
      }

      rep.saveStepAttribute( id_transformation, id_step, "resultfieldname", resultfieldname );
      rep.saveStepAttribute( id_transformation, id_step, "usevar", usevar );
      rep.saveStepAttribute( id_transformation, id_step, "allowcapturegroups", allowcapturegroups );
      rep.saveStepAttribute( id_transformation, id_step, "replacefields", replacefields );
      rep.saveStepAttribute( id_transformation, id_step, "canoneq", canoneq );
      rep.saveStepAttribute( id_transformation, id_step, "caseinsensitive", caseinsensitive );
      rep.saveStepAttribute( id_transformation, id_step, "comment", comment );
      rep.saveStepAttribute( id_transformation, id_step, "dotall", dotall );
      rep.saveStepAttribute( id_transformation, id_step, "multiline", multiline );
      rep.saveStepAttribute( id_transformation, id_step, "unicode", unicode );
      rep.saveStepAttribute( id_transformation, id_step, "unix", unix );
      rep.saveStepAttribute( id_transformation, id_step, "matcher", matcher );
    } catch ( Exception e ) {
      throw new HopException( BaseMessages.getString( PKG, "RegexEvalMeta.Exception.UnableToSaveStepInfo" )
        + id_step, e );
    }
  }

  public void check( List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta,
    RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info, VariableSpace space,
    Repository repository, IMetaStore metaStore ) {

    CheckResult cr;

    if ( prev != null && prev.size() > 0 ) {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "RegexEvalMeta.CheckResult.ConnectedStepOK", String.valueOf( prev.size() ) ), stepMeta );
      remarks.add( cr );
    } else {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "RegexEvalMeta.CheckResult.NoInputReceived" ), stepMeta );
      remarks.add( cr );
    }

    // Check Field to evaluate
    if ( !Utils.isEmpty( matcher ) ) {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "RegexEvalMeta.CheckResult.MatcherOK" ), stepMeta );
      remarks.add( cr );
    } else {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "RegexEvalMeta.CheckResult.NoMatcher" ), stepMeta );
      remarks.add( cr );

    }

    // Check Result Field name
    if ( !Utils.isEmpty( resultfieldname ) ) {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "RegexEvalMeta.CheckResult.ResultFieldnameOK" ), stepMeta );
      remarks.add( cr );
    } else {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "RegexEvalMeta.CheckResult.NoResultFieldname" ), stepMeta );
      remarks.add( cr );
    }

  }

  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr,
    TransMeta transMeta, Trans trans ) {
    return new RegexEval( stepMeta, stepDataInterface, cnr, transMeta, trans );
  }

  public StepDataInterface getStepData() {
    return new RegexEvalData();
  }

  public boolean supportsErrorHandling() {
    return true;
  }
}
