package com.company.qrcode.web.ui;

import com.company.qrcode.entity.TextTemplate;
import com.company.qrcode.service.ConstructionService;
import com.company.qrcode.web.ui.texttemplate.TextTemplateBrowse;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.vaadin.ui.AbstractComponent;

import javax.inject.Inject;
import java.util.List;

@UiController("screen")
@UiDescriptor("new-screen.xml")
public class NewScreen extends Screen {
    @Inject
    private ConstructionService constructionService;
    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        List<TextTemplate> templates = constructionService.getActiveConstructions();
        applyAutoComplete(getWindow(), templates);
    }
    private void applyAutoComplete(ComponentContainer container, List<TextTemplate> templates){
        for(Component c: container.getOwnComponents()){
            if(c instanceof TextField || c instanceof TextArea){
                AbstractComponent vComponent = c.unwrap(AbstractComponent.class);
                AutocompleteExtension existing = vComponent
                        .getExtensions()
                        .stream()
                        .filter(e->e instanceof AutocompleteExtension)
                        .map(e->(AutocompleteExtension)e)
                        .findFirst()
                        .orElse(null);
                if(existing!=null){
                    existing.setTemplates(templates);
                }
                else{
                    new AutocompleteExtension(vComponent, templates);
                }
            }
            else if(c instanceof ComponentContainer) applyAutoComplete((ComponentContainer) c, templates);
        }
    }

    @Inject
    private Screens screens;
    @Subscribe("openTemplateLists")
    public void onOpenTemplateLists(Action.ActionPerformedEvent event){
        TextTemplateBrowse screenTemplates = screens.create(TextTemplateBrowse.class, OpenMode.DIALOG);
        screenTemplates.addAfterCloseListener(afterCloseEvent -> {
            List<TextTemplate> templates = constructionService.getActiveConstructions();
            applyAutoComplete(getWindow(), templates);
        });
        screenTemplates.show();
    }
}