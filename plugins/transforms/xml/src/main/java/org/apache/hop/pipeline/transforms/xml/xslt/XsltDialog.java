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

package org.apache.hop.pipeline.transforms.xml.xslt;

import org.apache.hop.core.Const;
import org.apache.hop.core.Props;
import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.row.IRowMeta;
import org.apache.hop.core.util.Utils;
import org.apache.hop.i18n.BaseMessages;
import org.apache.hop.pipeline.PipelineMeta;
import org.apache.hop.pipeline.transform.BaseTransformMeta;
import org.apache.hop.pipeline.transform.ITransformDialog;
import org.apache.hop.pipeline.transform.TransformMeta;
import org.apache.hop.ui.core.dialog.ErrorDialog;
import org.apache.hop.ui.core.widget.ColumnInfo;
import org.apache.hop.ui.core.widget.LabelTextVar;
import org.apache.hop.ui.core.widget.TableView;
import org.apache.hop.ui.core.widget.TextVar;
import org.apache.hop.ui.pipeline.transform.BaseTransformDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;

import java.util.List;
import java.util.*;

public class XsltDialog extends BaseTransformDialog implements ITransformDialog {
  private static Class<?> PKG = XsltMeta.class; // for i18n purposes, needed by Translator2!!

  private LabelTextVar wResultField;
  private CCombo wField, wXSLField;
  private FormData fdlXSLFileField, fdXSLFileField;

  private Label wlField, wlFilename, wlXSLField, wlXSLFileField;

  private Button wbbFilename, wXSLFileField;

  private XsltMeta input;

  private Group wOutputField, wXSLFileGroup;
  private FormData fdOutputField, fdXSLFileGroup;

  private TextVar wXSLFilename;

  private Label wlXSLFieldIsAFile;
  private FormData fdlXSLFieldIsAFile;
  private Button wXSLFieldIsAFile;
  private FormData fdXSLFieldIsAFile;

  private CTabFolder wTabFolder;

  private CTabItem wGeneralTab;
  private Composite wGeneralComp;
  private FormData fdGeneralComp;

  private CTabItem wAdditionalTab;

  private Composite wAdditionalComp;
  private FormData fdAdditionalComp;

  private Label wlXSLTFactory;
  private CCombo wXSLTFactory;
  private FormData fdlXSLTFactory, fdXSLTFactory;

  private Label wlFields;
  private TableView wFields;
  private FormData fdlFields, fdFields;

  private Label wlOutputProperties;
  private FormData fdlOutputProperties;

  private TableView wOutputProperties;
  private FormData fdOutputProperties;

  private ColumnInfo[] colinf;

  private Map<String, Integer> inputFields;

  public XsltDialog(Shell parent, Object in, PipelineMeta pipelineMeta, String sname ) {
    super( parent, (BaseTransformMeta) in, pipelineMeta, sname );
    input = (XsltMeta) in;
    inputFields = new HashMap<String, Integer>();
  }

  public String open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN );
    props.setLook( shell );
    setShellImage( shell, input );

    ModifyListener lsMod = new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        input.setChanged();
      }
    };
    changed = input.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout( formLayout );
    shell.setText( BaseMessages.getString( PKG, "XsltDialog.Shell.Title" ) );

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Filename line
    wlTransformName = new Label( shell, SWT.RIGHT );
    wlTransformName.setText( BaseMessages.getString( PKG, "XsltDialog.Stepname.Label" ) );
    props.setLook( wlTransformName );
    fdlTransformName = new FormData();
    fdlTransformName.left = new FormAttachment( 0, 0 );
    fdlTransformName.right = new FormAttachment( middle, -margin );
    fdlTransformName.top = new FormAttachment( 0, margin );
    wlTransformName.setLayoutData( fdlTransformName );
    wTransformName = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wTransformName.setText( transformName );
    props.setLook( wTransformName );
    wTransformName.addModifyListener( lsMod );
    fdTransformName = new FormData();
    fdTransformName.left = new FormAttachment( middle, 0 );
    fdTransformName.top = new FormAttachment( 0, margin );
    fdTransformName.right = new FormAttachment( 100, 0 );
    wTransformName.setLayoutData( fdTransformName );

    wTabFolder = new CTabFolder( shell, SWT.BORDER );
    props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );

    // ////////////////////////
    // START OF GENERAL TAB ///
    // ////////////////////////

    wGeneralTab = new CTabItem( wTabFolder, SWT.NONE );
    wGeneralTab.setText( BaseMessages.getString( PKG, "XsltDialog.GeneralTab.TabTitle" ) );

    wGeneralComp = new Composite( wTabFolder, SWT.NONE );
    props.setLook( wGeneralComp );

    FormLayout generalLayout = new FormLayout();
    generalLayout.marginWidth = 3;
    generalLayout.marginHeight = 3;
    wGeneralComp.setLayout( generalLayout );

    // FieldName to evaluate
    wlField = new Label( wGeneralComp, SWT.RIGHT );
    wlField.setText( BaseMessages.getString( PKG, "XsltDialog.Field.Label" ) );
    props.setLook( wlField );
    FormData fdlField = new FormData();
    fdlField.left = new FormAttachment( 0, 0 );
    fdlField.top = new FormAttachment( wTransformName, 2 * margin );
    fdlField.right = new FormAttachment( middle, -margin );
    wlField.setLayoutData( fdlField );
    wField = new CCombo( wGeneralComp, SWT.BORDER | SWT.READ_ONLY );
    wField.setEditable( true );
    props.setLook( wField );
    wField.addModifyListener( lsMod );
    FormData fdField = new FormData();
    fdField.left = new FormAttachment( middle, margin );
    fdField.top = new FormAttachment( wTransformName, 2 * margin );
    fdField.right = new FormAttachment( 100, -margin );
    wField.setLayoutData( fdField );
    wField.addFocusListener( new FocusListener() {
      public void focusLost( org.eclipse.swt.events.FocusEvent e ) {
      }

      public void focusGained( org.eclipse.swt.events.FocusEvent e ) {
        Cursor busy = new Cursor( shell.getDisplay(), SWT.CURSOR_WAIT );
        shell.setCursor( busy );
        PopulateFields( wField );
        shell.setCursor( null );
        busy.dispose();
      }
    } );

    // Step Ouput field grouping?
    // ////////////////////////
    // START OF Output Field GROUP
    //

    wOutputField = new Group( wGeneralComp, SWT.SHADOW_NONE );
    props.setLook( wOutputField );
    wOutputField.setText( BaseMessages.getString( PKG, "XsltDialog.ResultField.Group.Label" ) );

    FormLayout outputfieldgroupLayout = new FormLayout();
    outputfieldgroupLayout.marginWidth = 10;
    outputfieldgroupLayout.marginHeight = 10;
    wOutputField.setLayout( outputfieldgroupLayout );

    // Output Fieldame
    wResultField =
        new LabelTextVar( pipelineMeta, wOutputField, BaseMessages.getString( PKG, "XsltDialog.ResultField.Label" ),
            BaseMessages.getString( PKG, "XsltDialog.ResultField.Tooltip" ) );
    props.setLook( wResultField );
    wResultField.addModifyListener( lsMod );
    FormData fdResultField = new FormData();
    fdResultField.left = new FormAttachment( 0, 0 );
    fdResultField.top = new FormAttachment( wField, margin );
    fdResultField.right = new FormAttachment( 100, 0 );
    wResultField.setLayoutData( fdResultField );

    fdOutputField = new FormData();
    fdOutputField.left = new FormAttachment( 0, margin );
    fdOutputField.top = new FormAttachment( wField, margin );
    fdOutputField.right = new FormAttachment( 100, -margin );
    wOutputField.setLayoutData( fdOutputField );

    // ///////////////////////////////////////////////////////////
    // / END OF Output Field GROUP
    // ///////////////////////////////////////////////////////////

    // XSL File grouping
    // ////////////////////////
    // START OF XSL File GROUP
    //

    wXSLFileGroup = new Group( wGeneralComp, SWT.SHADOW_NONE );
    props.setLook( wXSLFileGroup );
    wXSLFileGroup.setText( BaseMessages.getString( PKG, "XsltDialog.XSL.Group.Label" ) );

    FormLayout XSLFileGroupLayout = new FormLayout();
    XSLFileGroupLayout.marginWidth = 10;
    XSLFileGroupLayout.marginHeight = 10;
    wXSLFileGroup.setLayout( XSLFileGroupLayout );

    // Is XSL source defined in a Field?
    wlXSLFileField = new Label( wXSLFileGroup, SWT.RIGHT );
    wlXSLFileField.setText( BaseMessages.getString( PKG, "XsltDialog.XSLFilenameFileField.Label" ) );
    props.setLook( wlXSLFileField );
    fdlXSLFileField = new FormData();
    fdlXSLFileField.left = new FormAttachment( 0, 0 );
    fdlXSLFileField.top = new FormAttachment( wResultField, margin );
    fdlXSLFileField.right = new FormAttachment( middle, -margin );
    wlXSLFileField.setLayoutData( fdlXSLFileField );
    wXSLFileField = new Button( wXSLFileGroup, SWT.CHECK );
    props.setLook( wXSLFileField );
    wXSLFileField.setToolTipText( BaseMessages.getString( PKG, "XsltDialog.XSLFilenameFileField.Tooltip" ) );
    fdXSLFileField = new FormData();
    fdXSLFileField.left = new FormAttachment( middle, margin );
    fdXSLFileField.top = new FormAttachment( wResultField, margin );
    wXSLFileField.setLayoutData( fdXSLFileField );

    SelectionAdapter lsXslFile = new SelectionAdapter() {
      public void widgetSelected( SelectionEvent arg0 ) {
        ActivewlXSLField();
        input.setChanged();
      }
    };
    wXSLFileField.addSelectionListener( lsXslFile );

    // If XSL File name defined in a Field
    wlXSLField = new Label( wXSLFileGroup, SWT.RIGHT );
    wlXSLField.setText( BaseMessages.getString( PKG, "XsltDialog.XSLFilenameField.Label" ) );
    props.setLook( wlXSLField );
    FormData fdlXSLField = new FormData();
    fdlXSLField.left = new FormAttachment( 0, 0 );
    fdlXSLField.top = new FormAttachment( wXSLFileField, margin );
    fdlXSLField.right = new FormAttachment( middle, -margin );
    wlXSLField.setLayoutData( fdlXSLField );
    wXSLField = new CCombo( wXSLFileGroup, SWT.BORDER | SWT.READ_ONLY );
    wXSLField.setEditable( true );
    props.setLook( wXSLField );
    wXSLField.addModifyListener( lsMod );
    FormData fdXSLField = new FormData();
    fdXSLField.left = new FormAttachment( middle, margin );
    fdXSLField.top = new FormAttachment( wXSLFileField, margin );
    fdXSLField.right = new FormAttachment( 100, -margin );
    wXSLField.setLayoutData( fdXSLField );
    wXSLField.addFocusListener( new FocusListener() {
      public void focusLost( org.eclipse.swt.events.FocusEvent e ) {
      }

      public void focusGained( org.eclipse.swt.events.FocusEvent e ) {
        Cursor busy = new Cursor( shell.getDisplay(), SWT.CURSOR_WAIT );
        shell.setCursor( busy );
        PopulateFields( wXSLField );
        shell.setCursor( null );
        busy.dispose();
      }
    } );

    // Is XSL field defined in a Field is a file?
    wlXSLFieldIsAFile = new Label( wXSLFileGroup, SWT.RIGHT );
    wlXSLFieldIsAFile.setText( BaseMessages.getString( PKG, "XsltDialog.XSLFieldIsAFile.Label" ) );
    props.setLook( wlXSLFieldIsAFile );
    fdlXSLFieldIsAFile = new FormData();
    fdlXSLFieldIsAFile.left = new FormAttachment( 0, 0 );
    fdlXSLFieldIsAFile.top = new FormAttachment( wXSLField, margin );
    fdlXSLFieldIsAFile.right = new FormAttachment( middle, -margin );
    wlXSLFieldIsAFile.setLayoutData( fdlXSLFieldIsAFile );
    wXSLFieldIsAFile = new Button( wXSLFileGroup, SWT.CHECK );
    props.setLook( wXSLFieldIsAFile );
    wXSLFieldIsAFile.setToolTipText( BaseMessages.getString( PKG, "XsltDialog.XSLFieldIsAFile.Tooltip" ) );
    fdXSLFieldIsAFile = new FormData();
    fdXSLFieldIsAFile.left = new FormAttachment( middle, margin );
    fdXSLFieldIsAFile.top = new FormAttachment( wXSLField, margin );
    wXSLFieldIsAFile.setLayoutData( fdXSLFieldIsAFile );
    wXSLFieldIsAFile.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent arg0 ) {
        input.setChanged();
      }
    } );

    // XSL Filename
    wlFilename = new Label( wXSLFileGroup, SWT.RIGHT );
    wlFilename.setText( BaseMessages.getString( PKG, "XsltDialog.XSLFilename.Label" ) );
    props.setLook( wlFilename );
    FormData fdlXSLFilename = new FormData();
    fdlXSLFilename.left = new FormAttachment( 0, 0 );
    fdlXSLFilename.top = new FormAttachment( wXSLFieldIsAFile, 2 * margin );
    fdlXSLFilename.right = new FormAttachment( middle, -margin );
    wlFilename.setLayoutData( fdlXSLFilename );

    wbbFilename = new Button( wXSLFileGroup, SWT.PUSH | SWT.CENTER );
    props.setLook( wbbFilename );
    wbbFilename.setText( BaseMessages.getString( PKG, "XsltDialog.FilenameBrowse.Button" ) );
    wbbFilename.setToolTipText( BaseMessages.getString( PKG, "System.Tooltip.BrowseForFileOrDirAndAdd" ) );
    FormData fdbXSLFilename = new FormData();
    fdbXSLFilename.right = new FormAttachment( 100, 0 );
    fdbXSLFilename.top = new FormAttachment( wXSLFieldIsAFile, 2 * margin );
    wbbFilename.setLayoutData( fdbXSLFilename );

    wXSLFilename = new TextVar( pipelineMeta, wXSLFileGroup, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wXSLFilename );
    wXSLFilename.addModifyListener( lsMod );
    FormData fdXSLFilename = new FormData();
    fdXSLFilename.left = new FormAttachment( middle, margin );
    fdXSLFilename.right = new FormAttachment( wbbFilename, -margin );
    fdXSLFilename.top = new FormAttachment( wXSLFieldIsAFile, 2 * margin );
    wXSLFilename.setLayoutData( fdXSLFilename );

    // XSLTFactory
    wlXSLTFactory = new Label( wXSLFileGroup, SWT.RIGHT );
    wlXSLTFactory.setText( BaseMessages.getString( PKG, "XsltDialog.XSLTFactory.Label" ) );
    props.setLook( wlXSLTFactory );
    fdlXSLTFactory = new FormData();
    fdlXSLTFactory.left = new FormAttachment( 0, 0 );
    fdlXSLTFactory.top = new FormAttachment( wXSLFilename, 2 * margin );
    fdlXSLTFactory.right = new FormAttachment( middle, -margin );
    wlXSLTFactory.setLayoutData( fdlXSLTFactory );
    wXSLTFactory = new CCombo( wXSLFileGroup, SWT.BORDER | SWT.READ_ONLY );
    wXSLTFactory.setEditable( true );
    props.setLook( wXSLTFactory );
    wXSLTFactory.addModifyListener( lsMod );
    fdXSLTFactory = new FormData();
    fdXSLTFactory.left = new FormAttachment( middle, margin );
    fdXSLTFactory.top = new FormAttachment( wXSLFilename, 2 * margin );
    fdXSLTFactory.right = new FormAttachment( 100, 0 );
    wXSLTFactory.setLayoutData( fdXSLTFactory );
    wXSLTFactory.add( "JAXP" );
    wXSLTFactory.add( "SAXON" );

    fdXSLFileGroup = new FormData();
    fdXSLFileGroup.left = new FormAttachment( 0, margin );
    fdXSLFileGroup.top = new FormAttachment( wOutputField, margin );
    fdXSLFileGroup.right = new FormAttachment( 100, -margin );
    wXSLFileGroup.setLayoutData( fdXSLFileGroup );

    // ///////////////////////////////////////////////////////////
    // / END OF XSL File GROUP
    // ///////////////////////////////////////////////////////////

    fdGeneralComp = new FormData();
    fdGeneralComp.left = new FormAttachment( 0, 0 );
    fdGeneralComp.top = new FormAttachment( wField, 0 );
    fdGeneralComp.right = new FormAttachment( 100, 0 );
    fdGeneralComp.bottom = new FormAttachment( 100, 0 );
    wGeneralComp.setLayoutData( fdGeneralComp );

    wGeneralComp.layout();
    wGeneralTab.setControl( wGeneralComp );
    props.setLook( wGeneralComp );

    // ///////////////////////////////////////////////////////////
    // / END OF GENERAL TAB
    // ///////////////////////////////////////////////////////////

    // Additional tab...
    //
    wAdditionalTab = new CTabItem( wTabFolder, SWT.NONE );
    wAdditionalTab.setText( BaseMessages.getString( PKG, "XsltDialog.AdvancedTab.Title" ) );

    FormLayout addLayout = new FormLayout();
    addLayout.marginWidth = Const.FORM_MARGIN;
    addLayout.marginHeight = Const.FORM_MARGIN;

    wAdditionalComp = new Composite( wTabFolder, SWT.NONE );
    wAdditionalComp.setLayout( addLayout );
    props.setLook( wAdditionalComp );

    // Output properties
    wlOutputProperties = new Label( wAdditionalComp, SWT.NONE );
    wlOutputProperties.setText( BaseMessages.getString( PKG, "XsltDialog.OutputProperties.Label" ) );
    props.setLook( wlOutputProperties );
    fdlOutputProperties = new FormData();
    fdlOutputProperties.left = new FormAttachment( 0, 0 );
    fdlOutputProperties.top = new FormAttachment( 0, margin );
    wlOutputProperties.setLayoutData( fdlOutputProperties );

    final int OutputPropertiesRows = input.getOutputPropertyName().length;

    colinf =
        new ColumnInfo[] {
          new ColumnInfo( BaseMessages.getString( PKG, "XsltDialog.ColumnInfo.OutputProperties.Name" ),
              ColumnInfo.COLUMN_TYPE_CCOMBO, new String[] { "" }, false ),
          new ColumnInfo( BaseMessages.getString( PKG, "XsltDialog.ColumnInfo.OutputProperties.Value" ),
              ColumnInfo.COLUMN_TYPE_TEXT, false ), };
    colinf[0].setComboValues( XsltMeta.outputProperties );
    colinf[1].setUsingVariables( true );

    wOutputProperties =
        new TableView( pipelineMeta, wAdditionalComp, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, colinf,
            OutputPropertiesRows, lsMod, props );
    fdOutputProperties = new FormData();
    fdOutputProperties.left = new FormAttachment( 0, 0 );
    fdOutputProperties.top = new FormAttachment( wlOutputProperties, margin );
    fdOutputProperties.right = new FormAttachment( 100, -margin );
    fdOutputProperties.bottom = new FormAttachment( wlOutputProperties, 200 );
    wOutputProperties.setLayoutData( fdOutputProperties );

    // Parameters

    wlFields = new Label( wAdditionalComp, SWT.NONE );
    wlFields.setText( BaseMessages.getString( PKG, "XsltDialog.Parameters.Label" ) );
    props.setLook( wlFields );
    fdlFields = new FormData();
    fdlFields.left = new FormAttachment( 0, 0 );
    fdlFields.top = new FormAttachment( wOutputProperties, 2 * margin );
    wlFields.setLayoutData( fdlFields );

    wGet = new Button( wAdditionalComp, SWT.PUSH );
    wGet.setText( BaseMessages.getString( PKG, "XsltDialog.GetFields.Button" ) );
    FormData fdGet = new FormData();
    fdGet.top = new FormAttachment( wlFields, margin );
    fdGet.right = new FormAttachment( 100, 0 );
    wGet.setLayoutData( fdGet );

    final int FieldsRows = input.getParameterField().length;

    colinf =
        new ColumnInfo[] {
          new ColumnInfo( BaseMessages.getString( PKG, "XsltDialog.ColumnInfo.Name" ), ColumnInfo.COLUMN_TYPE_CCOMBO,
              new String[] { "" }, false ),
          new ColumnInfo( BaseMessages.getString( PKG, "XsltDialog.ColumnInfo.Parameter" ),
              ColumnInfo.COLUMN_TYPE_TEXT, false ), };
    colinf[1].setUsingVariables( true );

    wFields =
        new TableView( pipelineMeta, wAdditionalComp, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, colinf, FieldsRows,
            lsMod, props );
    fdFields = new FormData();
    fdFields.left = new FormAttachment( 0, 0 );
    fdFields.top = new FormAttachment( wlFields, margin );
    fdFields.right = new FormAttachment( wGet, -margin );
    fdFields.bottom = new FormAttachment( 100, -margin );
    wFields.setLayoutData( fdFields );

    // Search the fields in the background

    final Runnable runnable = new Runnable() {
      public void run() {
        TransformMeta stepMeta = pipelineMeta.findTransform( transformName );
        if ( stepMeta != null ) {
          try {
            IRowMeta row = pipelineMeta.getPrevTransformFields( stepMeta );

            // Remember these fields...
            for ( int i = 0; i < row.size(); i++ ) {
              inputFields.put( row.getValueMeta( i ).getName(), Integer.valueOf( i ) );
            }
            setComboBoxes();
          } catch ( HopException e ) {
            logError( BaseMessages.getString( PKG, "System.Dialog.GetFieldsFailed.Message" ) );
          }
        }
      }
    };
    new Thread( runnable ).start();

    fdAdditionalComp = new FormData();
    fdAdditionalComp.left = new FormAttachment( 0, 0 );
    fdAdditionalComp.top = new FormAttachment( wTransformName, margin );
    fdAdditionalComp.right = new FormAttachment( 100, 0 );
    fdAdditionalComp.bottom = new FormAttachment( 100, 0 );
    wAdditionalComp.setLayoutData( fdAdditionalComp );

    wAdditionalComp.layout();
    wAdditionalTab.setControl( wAdditionalComp );
    // ////// END of Additional Tab

    FormData fdTabFolder = new FormData();
    fdTabFolder.left = new FormAttachment( 0, 0 );
    fdTabFolder.top = new FormAttachment( wTransformName, margin );
    fdTabFolder.right = new FormAttachment( 100, 0 );
    fdTabFolder.bottom = new FormAttachment( 100, -50 );
    wTabFolder.setLayoutData( fdTabFolder );

    wOk = new Button( shell, SWT.PUSH );
    wOk.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );

    wCancel = new Button( shell, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

    setButtonPositions( new Button[] { wOk, wCancel }, margin, wTabFolder );

    // Add listeners
    lsCancel = new Listener() {
      public void handleEvent( Event e ) {
        cancel();
      }
    };
    lsGet = new Listener() {
      public void handleEvent( Event e ) {
        get();
      }
    };
    lsOk = new Listener() {
      public void handleEvent( Event e ) {
        ok();
      }
    };

    wCancel.addListener( SWT.Selection, lsCancel );
    wGet.addListener( SWT.Selection, lsGet );

    wOk.addListener( SWT.Selection, lsOk );

    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected( SelectionEvent e ) {
        ok();
      }
    };

    wTransformName.addSelectionListener( lsDef );

    // Whenever something changes, set the tooltip to the expanded version
    // of the filename:
    wXSLFilename.addModifyListener( new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        wXSLFilename.setToolTipText( pipelineMeta.environmentSubstitute( wXSLFilename.getText() ) );
      }
    } );

    // Listen to the Browse... button
    wbbFilename.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {

        FileDialog dialog = new FileDialog( shell, SWT.OPEN );
        dialog.setFilterExtensions( new String[] { "*.xsl;*.XSL", "*.xslt;.*XSLT", "*" } );
        if ( wXSLFilename.getText() != null ) {
          String fname = pipelineMeta.environmentSubstitute( wXSLFilename.getText() );
          dialog.setFileName( fname );
        }

        dialog.setFilterNames( new String[] { BaseMessages.getString( PKG, "XsltDialog.FileType" ),
          BaseMessages.getString( PKG, "System.FileType.AllFiles" ) } );

        if ( dialog.open() != null ) {
          String str = dialog.getFilterPath() + System.getProperty( "file.separator" ) + dialog.getFileName();
          wXSLFilename.setText( str );
        }
      }
    } );

    wTabFolder.setSelection( 0 );

    // Set the shell size, based upon previous time...
    setSize();

    getData();
    ActivewlXSLField();

    input.setChanged( changed );

    shell.open();
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return transformName;
  }

  private void ActivewlXSLField() {

    wXSLField.setEnabled( wXSLFileField.getSelection() );
    wlXSLField.setEnabled( wXSLFileField.getSelection() );

    wXSLFilename.setEnabled( !wXSLFileField.getSelection() );
    wlFilename.setEnabled( !wXSLFileField.getSelection() );
    wbbFilename.setEnabled( !wXSLFileField.getSelection() );
    wlXSLFieldIsAFile.setEnabled( wXSLFileField.getSelection() );
    wXSLFieldIsAFile.setEnabled( wXSLFileField.getSelection() );
    if ( !wXSLFileField.getSelection() ) {
      wXSLFieldIsAFile.setSelection( false );
    }

  }

  private void PopulateFields( CCombo cc ) {
    if ( cc.isDisposed() ) {
      return;
    }
    try {
      String initValue = cc.getText();
      cc.removeAll();
      IRowMeta r = pipelineMeta.getPrevTransformFields( transformName );
      if ( r != null ) {
        cc.setItems( r.getFieldNames() );
      }
      if ( !Utils.isEmpty( initValue ) ) {
        cc.setText( initValue );
      }
    } catch ( HopException ke ) {
      new ErrorDialog( shell, BaseMessages.getString( PKG, "XsltDialog.FailedToGetFields.DialogTitle" ), BaseMessages
          .getString( PKG, "XsltDialog.FailedToGetFields.DialogMessage" ), ke );
    }

  }

  /**
   * Copy information from the meta-data input to the dialog fields.
   */
  public void getData() {

    if ( input.getXslFilename() != null ) {
      wXSLFilename.setText( input.getXslFilename() );
    }
    if ( input.getResultfieldname() != null ) {
      wResultField.setText( input.getResultfieldname() );
    }
    if ( input.getFieldname() != null ) {
      wField.setText( input.getFieldname() );
    }

    if ( input.getXSLFileField() != null ) {
      wXSLField.setText( input.getXSLFileField() );
    }

    wXSLFileField.setSelection( input.useXSLField() );
    wXSLFieldIsAFile.setSelection( input.isXSLFieldIsAFile() );

    if ( input.getXSLFactory() != null ) {
      wXSLTFactory.setText( input.getXSLFactory() );
    } else {
      wXSLTFactory.setText( "JAXP" );
    }

    if ( input.getParameterName() != null ) {
      for ( int i = 0; i < input.getParameterName().length; i++ ) {
        TableItem item = wFields.table.getItem( i );
        item.setText( 1, Const.NVL( input.getParameterField()[i], "" ) );
        item.setText( 2, Const.NVL( input.getParameterName()[i], "" ) );
      }
    }

    if ( input.getOutputPropertyName() != null ) {
      for ( int i = 0; i < input.getOutputPropertyName().length; i++ ) {
        TableItem item = wOutputProperties.table.getItem( i );
        item.setText( 1, Const.NVL( input.getOutputPropertyName()[i], "" ) );
        item.setText( 2, Const.NVL( input.getOutputPropertyValue()[i], "" ) );
      }
    }

    wTransformName.selectAll();
    wTransformName.setFocus();
  }

  private void cancel() {
    transformName = null;
    input.setChanged( changed );
    dispose();
  }

  private void ok() {
    transformName = wTransformName.getText(); // return value

    input.setXslFilename( wXSLFilename.getText() );
    input.setResultfieldname( wResultField.getText() );
    input.setFieldname( wField.getText() );
    input.setXSLFileField( wXSLField.getText() );
    input.setXSLFactory( wXSLTFactory.getText() );

    input.setXSLField( wXSLFileField.getSelection() );
    input.setXSLFieldIsAFile( wXSLFieldIsAFile.getSelection() );
    int nrparams = wFields.nrNonEmpty();
    int nroutputprops = wOutputProperties.nrNonEmpty();
    input.allocate( nrparams, nroutputprops );

    if ( isDebug() ) {
      logDebug( BaseMessages.getString( PKG, "HTTPDialog.Log.FoundArguments", String.valueOf( nrparams ) ) );
    }
    // CHECKSTYLE:Indentation:OFF
    for ( int i = 0; i < nrparams; i++ ) {
      TableItem item = wFields.getNonEmpty( i );
      input.getParameterField()[i] = item.getText( 1 );
      input.getParameterName()[i] = item.getText( 2 );
    }
    // CHECKSTYLE:Indentation:OFF
    for ( int i = 0; i < nroutputprops; i++ ) {
      TableItem item = wOutputProperties.getNonEmpty( i );
      input.getOutputPropertyName()[i] = item.getText( 1 );
      input.getOutputPropertyValue()[i] = item.getText( 2 );
    }
    dispose();
  }

  protected void setComboBoxes() {
    // Something was changed in the row.
    //
    final Map<String, Integer> fields = new HashMap<String, Integer>();

    // Add the currentMeta fields...
    fields.putAll( inputFields );

    Set<String> keySet = fields.keySet();
    List<String> entries = new ArrayList<String>( keySet );

    String[] fieldNames = entries.toArray( new String[entries.size()] );

    Const.sortStrings( fieldNames );
    colinf[0].setComboValues( fieldNames );
    // colinfHeaders[0].setComboValues(fieldNames);
  }

  private void get() {
    try {
      IRowMeta r = pipelineMeta.getPrevTransformFields( transformName );
      if ( r != null && !r.isEmpty() ) {
        BaseTransformDialog.getFieldsFromPrevious( r, wFields, 1, new int[] { 1, 2 }, new int[] { 3 }, -1, -1, null );
      }
    } catch ( HopException ke ) {
      new ErrorDialog( shell, BaseMessages.getString( PKG, "XsltDialog.FailedToGetFields.DialogTitle" ), BaseMessages
          .getString( PKG, "XsltDialog.FailedToGetFields.DialogMessage" ), ke );
    }
  }
}
