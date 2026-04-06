/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.texttemplate;

import com.company.qrcode.entity.TemplateCategories;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.sys.ShowInfoAction;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.TextTemplate;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.global.UserSession;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.v7.ui.Table;

@UiController("qrcode$TextTemplate.browse")
@UiDescriptor("text-template-browse.xml")
@LookupComponent("textTemplatesTable")
public class TextTemplateBrowse extends StandardLookup<TextTemplate> {
    TemplateCategories selectCategory;
    private Set<TextTemplate> selectedTemplates;
    @Inject
    private GroupTable<TextTemplate> textTemplatesTable;
    @Inject
    private Notifications notifications;
    @Inject
    private UserSettingService userSettingService;
    @Inject
    private CollectionLoader<TemplateCategories> categoriesTemplatesDl;
    @Inject
    private UserSession userSession;
    @Inject
    private CollectionLoader<TextTemplate> textTemplatesDl;
    @Inject
    private GroupTable<TemplateCategories> textCategoriesTable;
    @Inject
    private Button createBtn2;
    public void setTemplates(){
        selectedTemplates = textTemplatesTable.getSelected();
    }
    public Set<TextTemplate> getTemplates(){
        return selectedTemplates;
    }
    @Named("textTemplatesTable.create")
    private Action createAction;
    private void updateActionCreate(TemplateCategories categories){
        boolean canUse = false;
        if(categories!=null){
            canUse = true;
        }
        createAction.setEnabled(canUse);
    }

    @Subscribe
    public void onInit(InitEvent event) {
        TemplateCategories templateCategories = userSession.getAttribute("LastCategory");
        updateActionCreate(templateCategories);
        if(templateCategories!=null){
            textTemplatesDl.setParameter("category", templateCategories);
            selectCategory = templateCategories;
            textTemplatesDl.load();
        }
        Table vTable = textTemplatesTable.unwrap(Table.class);
        textTemplatesTable.setItemClickAction(new BaseAction("nullAction"){
            @Override
            public void actionPerform(Component component){
            }
        });
        vTable.addItemClickListener(clickEvent->{
            if(clickEvent.getMouseEventDetails().isDoubleClick()){
                if(!clickEvent.getMouseEventDetails().isCtrlKey()&&!clickEvent.getMouseEventDetails().isMetaKey()){
                    setTemplates();
                    close(StandardOutcome.SELECT);
                }
            }
        });
    }
    @Subscribe("insertTemplates")
    public void onInsertTemplatesClick(Button.ClickEvent event) {
        setTemplates();
        close(StandardOutcome.SELECT);
    }
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        categoriesTemplatesDl.setParameter("id", userSession.getUser().getUuid());
        categoriesTemplatesDl.load();
    }
    @Subscribe("selectCategory")
    public void onSelectCategoryClick(Button.ClickEvent event) {
        TemplateCategories selectedCategory = textCategoriesTable.getSingleSelected();
        if(selectedCategory!=null){
            userSettingService.saveSetting("selectedCategoryName", selectedCategory.getName());
            UUID id = selectedCategory.getUuid();
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json_templates = mapper.writeValueAsString(id);
                userSettingService.saveSetting("selectedCategory", json_templates);
            }
            catch (JsonProcessingException e){
                throw new RuntimeException("Ошибка сериализации UUID текстовых шаблонов в JSON", e);
            }
        }
        else{
            notifications.create().withCaption("Не выбрана ни одна категория!").show();
        }
    }
    @Subscribe(id = "textCategoriesDc", target = Target.DATA_CONTAINER)
    public void onTextCategoriesDcItemChange(InstanceContainer.ItemChangeEvent<TemplateCategories> event) {
        selectCategory = event.getItem();
        updateActionCreate(selectCategory);
        if(selectCategory!=null){
            textTemplatesDl.setParameter("category", selectCategory);
            textTemplatesDl.load();
        }
        else{
            textTemplatesDl.setParameter("category", null);
            textTemplatesDl.load();
        }
    }
    @Install(to = "textTemplatesTable.create", subject = "initializer")
    protected void textTemplatesTableCreateInitializer(TextTemplate entity){
        entity.setCategory(selectCategory);
    }
}