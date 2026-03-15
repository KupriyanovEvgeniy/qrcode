/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.sys;

import com.company.qrcode.entity.TextTemplate;
import com.company.qrcode.service.ConstructionService;
import com.company.qrcode.web.ui.AutocompleteExtension;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.components.TextField;
import org.springframework.context.event.EventListener;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ComponentContainer;

import javax.inject.Inject;
import java.util.List;

@org.springframework.stereotype.Component("qrcode_ScreenEventListener")
public class ScreenEventListener {
    @Inject
    private ConstructionService constructionService;

    @EventListener
    public void onScreenAfterInit(Screen.AfterInitEvent event){
        System.out.println("!!!!!!!!!!!!!_ВЫВОД1_!!!!!!!!!!!!!!");
        System.out.println(event.getSource().getId());
        System.out.println("!!!!!!!!!!!!!_ВЫВОД1_!!!!!!!!!!!!!!");
        Screen screen = event.getSource();
        List<TextTemplate> templates = constructionService.getActiveConstructions();
        applyAutocomplete(screen.getWindow(), templates);
    }

    private void applyAutocomplete(ComponentContainer container, List<TextTemplate> templates){
        for(Component c: container.getOwnComponents()){
            if (c instanceof TextField || c instanceof TextArea){
                System.out.println("!!!!!!!!!!!!!_ВЫВОД2_!!!!!!!!!!!!!!");
                System.out.println(c.getId());
                System.out.println("!!!!!!!!!!!!!_ВЫВОД2_!!!!!!!!!!!!!!");
                if(c instanceof WebAbstractComponent){
                    com.vaadin.ui.AbstractComponent vComponent = ((WebAbstractComponent<?>) c)
                            .unwrap(com.vaadin.ui.AbstractComponent.class);
                    new AutocompleteExtension(vComponent, templates);
                }
            }
            else if(c instanceof ComponentContainer){
                applyAutocomplete((ComponentContainer) c, templates);
            }
        }
    }
}
