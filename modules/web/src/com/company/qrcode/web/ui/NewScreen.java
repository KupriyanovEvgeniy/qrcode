package com.company.qrcode.web.ui;

import com.company.qrcode.entity.TextTemplate;
import com.company.qrcode.service.ConstructionService;
import com.company.qrcode.web.ui.texttemplate.SelectTextTemplateBrowse;
import com.company.qrcode.web.ui.texttemplate.TextTemplateBrowse;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
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
    @Inject
    private TextArea<String> myTextArea;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        List<TextTemplate> templates = constructionService.getActiveConstructions();
    }
    @Inject
    private Screens screens;
    @Subscribe("openTemplateLists")
    public void onOpenTemplateLists(Action.ActionPerformedEvent event){
        TextTemplateBrowse screenTemplates = screens.create(TextTemplateBrowse.class, OpenMode.DIALOG);
        screenTemplates.addAfterCloseListener(afterCloseEvent -> {
            List<TextTemplate> templates = constructionService.getActiveConstructions();
            TextTemplate myTemplate = screenTemplates.getTemplate();
            if(myTemplate!=null){
                setText(myTemplate.getContent());
            }
        });
        screenTemplates.show();
    }
    @Subscribe("openSelectTemplateLists")
    public void onOpenSelectTemplateLists(Action.ActionPerformedEvent event){
        SelectTextTemplateBrowse screenTemplates = screens.create(SelectTextTemplateBrowse.class, OpenMode.DIALOG);
        screenTemplates.addAfterCloseListener(afterCloseEvent -> {
            TextTemplate myTemplate = screenTemplates.getTemplate();
            if(myTemplate!=null){
                setText(myTemplate.getContent());
            }
        });
        screenTemplates.show();
    }
    public void setText(String textTemplate){
        com.vaadin.ui.TextArea vTextArea = myTextArea.unwrap(com.vaadin.ui.TextArea.class);
        int cursorPos = vTextArea.getCursorPosition();
        String currentText = myTextArea.getValue();
        if(currentText==null){
            currentText="";
        }
        String textToInsert = textTemplate;
        String result = currentText.substring(0, cursorPos)+textToInsert+currentText.substring(cursorPos);
        myTextArea.setValue(result);
    }
}