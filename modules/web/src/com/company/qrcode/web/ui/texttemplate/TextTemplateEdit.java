/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.texttemplate;

import com.company.qrcode.entity.AccessType;
import com.company.qrcode.entity.RecipientList;
import com.company.qrcode.entity.TemplateCategories;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.TextTemplate;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.UUID;

@UiController("qrcode$TextTemplate.edit")
@UiDescriptor("text-template-edit.xml")
@EditedEntityContainer("textTemplateDc")
@LoadDataBeforeShow
public class TextTemplateEdit extends StandardEditor<TextTemplate> {
    @Inject
    private UserSettingService userSettingService;
    @Inject
    private UserSession userSession;
    @Inject
    private DataManager dataManager;

    @Subscribe
    public void onInit(InitEntityEvent<TextTemplate> event) {
        String lastCategoryId = userSettingService.loadSetting("LAST_CATEGORY_CHOISE");
        if(lastCategoryId!=null){
            UUID categoryId = UUID.fromString(lastCategoryId);
            TemplateCategories lastCatEntity = dataManager.load(TemplateCategories.class).id(categoryId).one();
            event.getEntity().setCategory(lastCatEntity);
        }
        event.getEntity().setOwner(userSession.getUser());
    }
    @Subscribe("categoryPickerField")
    public void onCategoryPickerField(HasValue.ValueChangeEvent<TemplateCategories> event){
        if(event.getValue()!=null){
            userSettingService.saveSetting("LAST_CATEGORY_CHOISE", event.getValue().getId().toString());
        }
    }
}