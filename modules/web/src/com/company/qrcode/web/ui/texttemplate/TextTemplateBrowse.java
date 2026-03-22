/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.texttemplate;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.GroupTableItems;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.TextTemplate;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@UiController("qrcode$TextTemplate.browse")
@UiDescriptor("text-template-browse.xml")
@LookupComponent("textTemplatesTable")
@LoadDataBeforeShow
public class TextTemplateBrowse extends StandardLookup<TextTemplate> {
    @Inject
    private CollectionLoader<TextTemplate> textTemplatesDl;
    @Inject
    private GroupTable<TextTemplate> textTemplatesTable;
    @Inject
    private UserSession userSession;
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        textTemplatesDl.setParameter("id", userSession.getUser().getUuid());
    }
    @Inject
    private Notifications notifications;
    @Inject
    private UserSessionSource userSessionSource;
    @Subscribe("selectCategory")
    public void onSelectCategoryClick(Button.ClickEvent event) {
        Set<TextTemplate> selected = textTemplatesTable.getSelected();
        if(!selected.isEmpty()){
            List<UUID> ids = selected.stream().map(BaseUuidEntity::getUuid).collect(Collectors.toCollection(ArrayList::new));
            userSessionSource.getUserSession().setAttribute("selectedTemplates", (Serializable) ids);
        }
        else{
            notifications.create().withCaption("Не выбрана ни одна запись!").show();
        }
    }
    
}