/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.texttemplate;

import com.company.qrcode.entity.TextTemplate;
import com.company.qrcode.service.ConstructionService;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.table.ContainerGroupTableItems;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import javax.inject.Inject;
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