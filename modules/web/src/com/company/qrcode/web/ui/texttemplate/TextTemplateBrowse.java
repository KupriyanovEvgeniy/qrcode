/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.texttemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
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
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.global.UserSession;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

@UiController("qrcode$TextTemplate.browse")
@UiDescriptor("text-template-browse.xml")
@LookupComponent("textTemplatesTable")
@LoadDataBeforeShow
public class TextTemplateBrowse extends StandardLookup<TextTemplate> {
    private TextTemplate selectedTemplate;
    @Inject
    private CollectionLoader<TextTemplate> textTemplatesDl;
    @Inject
    private GroupTable<TextTemplate> textTemplatesTable;
    @Inject
    private UserSession userSession;
    @Inject
    private Notifications notifications;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private UserSettingService userSettingService;

    @Subscribe
    public void onInit(InitEvent event) {
        textTemplatesTable.setItemClickAction(new BaseAction("doubleClick"){
            @Override
            public void actionPerform(Component component){
                int counter = textTemplatesTable.getSelected().size();
                if(counter==1){
                    setTemplate();
                    close(StandardOutcome.CLOSE);
                }
            }
        });
    }
    
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        textTemplatesDl.setParameter("id", userSession.getUser().getUuid());
    }
    @Subscribe("selectCategory")
    public void onSelectCategoryClick(Button.ClickEvent event) {
        Set<TextTemplate> selected = textTemplatesTable.getSelected();
        if(!selected.isEmpty()){
            List<UUID> ids = selected.stream().map(BaseUuidEntity::getUuid).collect(Collectors.toList());
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json_templates = mapper.writeValueAsString(ids);
                userSettingService.saveSetting("selectedTemplates", json_templates);
            }
            catch (JsonProcessingException e){
                throw new RuntimeException("Ошибка сериализации UUID текстовых шаблонов в JSON", e);
            }
        }
        else{
            notifications.create().withCaption("Не выбрана ни одна запись!").show();
        }
    }
    public void setTemplate(){
        selectedTemplate = textTemplatesTable.getSingleSelected();
    }
    public TextTemplate getTemplate(){
        return selectedTemplate;
    }
}