/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.templatecategories;

import com.company.qrcode.entity.AccessType;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.TemplateCategories;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("qrcode$TemplateCategories.edit")
@UiDescriptor("template-categories-edit.xml")
@EditedEntityContainer("templateCategoriesDc")
@LoadDataBeforeShow
public class TemplateCategoriesEdit extends StandardEditor<TemplateCategories> {
    @Inject
    private UserSession userSession;
    @Inject
    private GroupBoxLayout shareBox;
    @Subscribe
    public void onInit(InitEntityEvent<TemplateCategories> event) {
        event.getEntity().setOwner(userSession.getUser());
        event.getEntity().setAccessType(AccessType.PRIVATE);
        event.getEntity().setActive(Boolean.TRUE);
    }
    @Subscribe("accessTypeField")
    public void showShareWindow(HasValue.ValueChangeEvent<AccessType> event){
        AccessType selectedType = event.getValue();
        boolean isPrivate = AccessType.PRIVATE.equals(selectedType);
        shareBox.setVisible(isPrivate);
    }
}