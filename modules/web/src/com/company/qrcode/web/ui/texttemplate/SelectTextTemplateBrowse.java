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
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@UiController("qrcode$SelectTextTemplate.browse")
@UiDescriptor("select-text-template-browse.xml")
@LookupComponent("textTemplatesTable")
@LoadDataBeforeShow
public class SelectTextTemplateBrowse extends StandardLookup<TextTemplate> {
    private TextTemplate selectedTemplate;
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
    private MetadataTools metadataTools;
    @Inject
    private UserSession userSession;
    @Inject
    private DataManager dataManager;

    @Subscribe
    public void onInit(InitEvent event) {
        selectedTemplates.setItemClickAction(new BaseAction("doubleClick"){
            @Override
            public void actionPerform(Component component){
                int counter = selectedTemplates.getSelected().size();
                if(counter==1){
                    setTemplate();
                    close(StandardOutcome.CLOSE);
                }
            }
        });
    }
        @Subscribe("createNewTemplateList")
        public void onCreateNewTemplateListClick(Button.ClickEvent event) {
            Set<TextTemplate> selectedListTemplates = selectedTemplates.getSelected();
            TemplateCategoriesEdit templateCategoriesEditScreen = screens.create(TemplateCategoriesEdit.class, OpenMode.DIALOG);
            templateCategoriesEditScreen.addAfterCloseListener(afterCloseEvent -> {
                TemplateCategories savedCategory = templateCategoriesEditScreen.getEditedEntity();
                if(selectedListTemplates.isEmpty()){
                    return;
                }
                CommitContext context = new CommitContext();
                for(TextTemplate original: selectedListTemplates){
                    TextTemplate new_entity = metadata.create(TextTemplate.class);
                    new_entity.setCode(original.getCode());
                    new_entity.setContent(original.getContent());
                    new_entity.setOwner(userSession.getUser());
                    new_entity.setCategory(savedCategory);
                    context.addInstanceToCommit(new_entity);
                }
                dataManager.commit(context);
            });
            TemplateCategories newCategory = metadata.create(TemplateCategories.class);
            templateCategoriesEditScreen.setEntityToEdit(newCategory);
            templateCategoriesEditScreen.show();
        }
    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        selectedTextTemplatesDc.setItems(constructionService.getActiveConstructions());
        selectedTemplates.setItems(new ContainerGroupTableItems<>(selectedTextTemplatesDc));
    }
    public void setTemplate(){
        selectedTemplate = selectedTemplates.getSingleSelected();
    }
    public TextTemplate getTemplate(){
        return selectedTemplate;
    }
}