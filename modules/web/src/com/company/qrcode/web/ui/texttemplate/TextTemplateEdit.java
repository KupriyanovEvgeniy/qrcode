/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.texttemplate;

import com.company.qrcode.entity.AccessType;
import com.company.qrcode.entity.RecipientList;
import com.company.qrcode.entity.TemplateCategories;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.TextTemplate;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("qrcode$TextTemplate.edit")
@UiDescriptor("text-template-edit.xml")
@EditedEntityContainer("textTemplateDc")
@LoadDataBeforeShow
public class TextTemplateEdit extends StandardEditor<TextTemplate> {
    @Inject
    private UserSession userSession;
    @Inject
    private GroupBoxLayout shareBox;
    @Subscribe
    public void onInit(InitEntityEvent<TextTemplate> event) {
        event.getEntity().setOwner(userSession.getUser());
        event.getEntity().setAccessType(AccessType.PRIVATE);
        event.getEntity().setActive(true);
    }
    @Subscribe("accessTypeField")
    public void showShareWindow(HasValue.ValueChangeEvent<AccessType> event){
        AccessType selectedType = event.getValue();
        boolean isPrivate = AccessType.PRIVATE.equals(selectedType);
        shareBox.setVisible(isPrivate);
    }
}