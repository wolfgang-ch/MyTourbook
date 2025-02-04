/*******************************************************************************
 * Copyright (C) 2005, 2024 Wolfgang Schramm and Contributors
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 *******************************************************************************/
package net.tourbook.photo.internal.preferences;

import net.tourbook.photo.IPhotoPreferences;
import net.tourbook.photo.PhotoActivator;
import net.tourbook.photo.internal.Messages;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PrefPagePhotoSystem extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

   public static final String     ID         = "net.tourbook.preferences.PrefPagePhotoSystemID"; //$NON-NLS-1$

   private final IPreferenceStore _prefStore = PhotoActivator.getPrefStore();

   private boolean                _isImageQualityModified;

   private SelectionAdapter       _imageQualitySelectionListener;

   /*
    * UI controls
    */
   private Button _chkImageAutoRotate;

   @Override
   protected void createFieldEditors() {

      initUI();
      createUI();
   }

   private void createUI() {

      final Composite parent = getFieldEditorParent();
      GridDataFactory.fillDefaults().grab(true, false).applyTo(parent);
      GridLayoutFactory.fillDefaults().numColumns(2).applyTo(parent);
//		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
      {
         createUI_10_ThumbPreview(parent);
      }
   }

   private void createUI_10_ThumbPreview(final Composite parent) {

      final Composite container = new Composite(parent, SWT.NONE);
      GridDataFactory.fillDefaults().span(2, 1).applyTo(container);
      GridLayoutFactory.fillDefaults().applyTo(container);
      {
         // checkbox: rotate images automatically
         _chkImageAutoRotate = new Button(container, SWT.CHECK);
         _chkImageAutoRotate.setText(Messages.PrefPage_Photo_System_Checkbox_IsRotateImageAutomatically);
         _chkImageAutoRotate.setToolTipText(Messages.PrefPage_Photo_System_Checkbox_IsRotateImageAutomatically_Tooltip);
         _chkImageAutoRotate.addSelectionListener(_imageQualitySelectionListener);
      }

   }

   private void enableControls() {

   }

   @Override
   public void init(final IWorkbench workbench) {
      setPreferenceStore(_prefStore);
   }

   @Override
   protected void initialize() {

      super.initialize();

      restoreState();

      enableControls();
   }

   private void initUI() {

      _imageQualitySelectionListener = new SelectionAdapter() {
         @Override
         public void widgetSelected(final SelectionEvent e) {
            _isImageQualityModified = true;
         }
      };
   }

   @Override
   public boolean okToLeave() {

      if (_isImageQualityModified) {
         saveState();
      }

      return super.okToLeave();
   }

   @Override
   protected void performDefaults() {

      _isImageQualityModified = true;

      _chkImageAutoRotate.setSelection(_prefStore.getDefaultBoolean(IPhotoPreferences.PHOTO_SYSTEM_IS_ROTATE_IMAGE_AUTOMATICALLY));

      enableControls();
   }

   @Override
   public boolean performOk() {

      saveState();

      return true;
   }

   private void restoreState() {

      _chkImageAutoRotate.setSelection(_prefStore.getBoolean(IPhotoPreferences.PHOTO_SYSTEM_IS_ROTATE_IMAGE_AUTOMATICALLY));
   }

   private void saveState() {

      _prefStore.setValue(IPhotoPreferences.PHOTO_SYSTEM_IS_ROTATE_IMAGE_AUTOMATICALLY, _chkImageAutoRotate.getSelection());

      if (_isImageQualityModified) {

         _isImageQualityModified = false;

         getPreferenceStore().setValue(
               IPhotoPreferences.PHOTO_VIEWER_PREF_EVENT_IMAGE_QUALITY_IS_MODIFIED,
               Math.random());
      }
   }
}
