/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui;

import com.company.qrcode.entity.TextTemplate;
import com.company.qrcode.service.ConstructionService;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
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
//        String basePath = "./VAADIN/tribute/";
//        String jsCode =
//                "if (!window.tributeLoaded) {" +
//                        "  var scriptTribute = document.createElement('script'); scriptTribute.src='" + basePath + "tribute.js'; document.head.appendChild(scriptTribute);" +
//                        "  var scriptConnector = document.createElement('script'); scriptConnector.src='" + basePath + "tribute-connector.js'; document.head.appendChild(scriptConnector);" +
//                        "  var linkStyles = document.createElement('link'); linkStyles.rel='stylesheet'; linkStyles.href='" + basePath + "tribute.css'; document.head.appendChild(linkStyles);" +
//                        "  window.tributeLoaded = true;" +
//                        "}";
//        com.vaadin.server.Page.getCurrent().getJavaScript().execute(jsCode);
        List<TextTemplate> templates = constructionService.getActiveConstructions();
        System.out.println("!!! ТЕСТ ЗАПУЩЕН !!!");
        applyAutoComplete(getWindow(), templates);
    }
    private void applyAutoComplete(ComponentContainer container, List<TextTemplate> templates){
        System.out.println("!!! ТЕСТ ЗАПУЩЕН1 !!!");
        for(Component c: container.getOwnComponents()){
            System.out.println("!!! ТЕСТ ЗАПУЩЕН2 !!!");
            if(c instanceof TextField || c instanceof TextArea){
                System.out.println("!!! ТЕСТ ЗАПУЩЕН3 !!!");
                if(c instanceof WebAbstractComponent){
                    System.out.println("!!! ТЕСТ ЗАПУЩЕН4 !!!");
                    AbstractComponent vComponent = c.unwrap(AbstractComponent.class);
                    AutocompleteExtension extension = new AutocompleteExtension(vComponent, templates);
                    System.out.println("Расширение создано для: " + c.getId());
                }
                else if(c instanceof ComponentContainer){
                    System.out.println("!!! ТЕСТ ЗАПУЩЕН5 !!!");
                    applyAutoComplete((ComponentContainer) c, templates);
                }
            }
        }
    }
}