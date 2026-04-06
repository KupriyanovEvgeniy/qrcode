/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.texttemplate;

import com.company.qrcode.entity.TemplateCategories;
import com.company.qrcode.entity.TextTemplate;
import com.company.qrcode.service.ConstructionService;
import com.company.qrcode.web.ui.templatecategories.TemplateCategoriesEdit;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.table.ContainerGroupTableItems;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.global.UserSession;
import com.vaadin.v7.ui.Table;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@UiController("qrcode$SelectTextTemplate.browse")
@UiDescriptor("select-text-template-browse.xml")
@LookupComponent("textTemplatesTable")
@LoadDataBeforeShow
public class SelectTextTemplateBrowse extends StandardLookup<TextTemplate> {
    private Set<TextTemplate> insertSelectedTemplates;
    @Inject
    private ConstructionService constructionService;
    @Inject
    private CollectionContainer<TextTemplate> selectedTextTemplatesDc;
    @Inject
    private GroupTable<TextTemplate> selectedTemplates;
    @Inject
    private Screens screens;
    @Inject
    private Metadata metadata;
    @Inject
    private UserSession userSession;
    @Inject
    private DataManager dataManager;
    @Inject
    private MessageBundle messageBundle;
    @Inject
    private UserSettingService userSettingService;
    public void setTemplate(){
        insertSelectedTemplates = selectedTemplates.getSelected();
    }
    public Set<TextTemplate> getTemplates(){
        return insertSelectedTemplates;
    }
    @Subscribe
    public void onInit(InitEvent event) {
        String baseCaption = messageBundle.getMessage("selectTextTemplateBrowse.caption");
        String dynamicPart = userSettingService.loadSetting("selectedCategoryName");
        getWindow().setCaption(baseCaption + ": "  + dynamicPart);
        com.vaadin.v7.ui.Table vTable = selectedTemplates.unwrap(Table.class);
        vTable.addItemClickListener(clickEvent->{
            if(clickEvent.getMouseEventDetails().isDoubleClick()){
                if(!clickEvent.getMouseEventDetails().isCtrlKey()&&!clickEvent.getMouseEventDetails().isMetaKey()){
                    setTemplate();
                    close(StandardOutcome.SELECT);
                }
            }
        });
    }
    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        selectedTextTemplatesDc.setItems(constructionService.getActiveConstructions());
        selectedTemplates.setItems(new ContainerGroupTableItems<>(selectedTextTemplatesDc));
    }
    @Subscribe("insertTemplates")
    public void onInsertTemplatesClick(Button.ClickEvent event) {
        setTemplate();
        close(StandardOutcome.SELECT);
    }
}