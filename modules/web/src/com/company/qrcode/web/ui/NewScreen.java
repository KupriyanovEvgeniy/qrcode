package com.company.qrcode.web.ui;

import com.company.qrcode.entity.TemplateCategories;
import com.company.qrcode.entity.TextTemplate;
import com.company.qrcode.service.ConstructionService;
import com.company.qrcode.web.ui.texttemplate.SelectTextTemplateBrowse;
import com.company.qrcode.web.ui.texttemplate.TextTemplateBrowse;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.vaadin.ui.AbstractComponent;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@UiController("screen")
@UiDescriptor("new-screen.xml")
public class NewScreen extends Screen {
    @Inject
    private UserSession userSession;
    @Inject
    private TextArea<String> myTextArea;
    @Inject
    private Screens screens;
    public void setText(Set<TextTemplate> textTemplate){
        com.vaadin.ui.TextArea vTextArea = myTextArea.unwrap(com.vaadin.ui.TextArea.class);
        int cursorPos = vTextArea.getCursorPosition();
        String currentText = myTextArea.getValue();
        if(currentText==null){
            currentText="";
        }
        StringBuilder insertText = new StringBuilder();
        TemplateCategories selectCategory = null;
        for(TextTemplate insertTemplates : textTemplate){
            if(selectCategory==null){
                userSession.setAttribute("LastCategory", insertTemplates.getCategory());
            }
            insertText.append(insertTemplates.getContent());
            insertText.append(" ");
        }
        String result = currentText.substring(0, cursorPos) + insertText + currentText.substring(cursorPos);
        myTextArea.setValue(result);
    }
    @Subscribe("openTemplateLists")
    public void onOpenTemplateLists(Action.ActionPerformedEvent event){
        TextTemplateBrowse screenTemplates = screens.create(TextTemplateBrowse.class, OpenMode.DIALOG);
        screenTemplates.addAfterCloseListener(afterCloseEvent -> {
            Set<TextTemplate> selectedForInsertTemplates = screenTemplates.getTemplates();
            if(selectedForInsertTemplates!=null&&!selectedForInsertTemplates.isEmpty()){
                setText(selectedForInsertTemplates);
            }
        });
        screenTemplates.show();
    }
    @Subscribe("openSelectTemplateLists")
    public void onOpenSelectTemplateLists(Action.ActionPerformedEvent event){
        SelectTextTemplateBrowse screenTemplates = screens.create(SelectTextTemplateBrowse.class, OpenMode.DIALOG);
        screenTemplates.addAfterCloseListener(afterCloseEvent -> {
            Set<TextTemplate> selectedForInsertTemplates = screenTemplates.getTemplates();
            if(selectedForInsertTemplates!=null&&!selectedForInsertTemplates.isEmpty()){
                setText(selectedForInsertTemplates);
            }
        });
        screenTemplates.show();
    }
}