/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.newscreen;

import com.company.qrcode.entity.support.UniquePerson;
import com.company.qrcode.web.ui.correspond.ToChoiseCorrespondent;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.awt.*;
import java.util.Collection;

@UiController("qrcode_DocumentEdit")
@UiDescriptor("document-edit.xml")
public class DocumentEdit extends Screen {

    @Inject
    private KeyValueCollectionContainer addresseesDc;

    @Inject
    private Table<KeyValueEntity> addresseesTable;

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Messages messages;
    @Inject
    private UiComponents uiComponents;

    @Subscribe
    public void onInit(InitEvent event) {

        addresseesTable.addGeneratedColumn("type", entity -> {
            Label<String> label = uiComponents.create(Label.TYPE_STRING);

            String type = entity.getValue("type");

            switch (type) {
                case "Сотрудник":
                    label.setIcon("font-icon:USER");
                    break;
                case "Физ лицо":
                    label.setIcon("font-icon:USER_O");
                    break;
                case "Юр лицо":
                    label.setIcon("font-icon:BUILDING");
                    break;
                case "Подразделение":
                    label.setIcon("font-icon:SITEMAP");
                    break;
                default:
                    label.setIcon("font-icon:QUESTION");
            }

            label.setValue(type);
            return label;
        });
    }

    @Subscribe("addBtn")
    public void onAddBtnClick(Button.ClickEvent event) {

        Screen screen = screenBuilders.screen(this)
                .withScreenClass(ToChoiseCorrespondent.class)
                .withLaunchMode(OpenMode.NEW_TAB)
                .build();

        screen.addAfterCloseListener(closeEvent -> {

            if (closeEvent.closedWith(StandardOutcome.SELECT)) {

                ToChoiseCorrespondent selectScreen = (ToChoiseCorrespondent) screen;

                Collection<Entity> selected = selectScreen.getSelectedEntities();

                if (selected != null) {
                    for (Entity entity : selected) {
                        addToTable(entity);
                    }
                }
            }
        });

        screen.show();
    }

    private void addToTable(Entity entity) {

        if (alreadyExists(entity))
            return;

        KeyValueEntity row = new KeyValueEntity();

        row.setValue("entity", entity);
        row.setValue("name", entity.getInstanceName());
        row.setValue("type", getTypeName(entity));

        addresseesDc.getMutableItems().add(row);
    }

    @Subscribe("removeBtn")
    public void onRemoveBtnClick(Button.ClickEvent event) {

        if (!addresseesTable.getSelected().isEmpty()) {
            addresseesDc.getMutableItems()
                    .removeAll(addresseesTable.getSelected());
        }
    }

    private boolean alreadyExists(Entity entity) {
        return addresseesDc.getItems().stream()
                .anyMatch(e -> entity.equals(e.getValue("entity")));
    }

    private String getTypeName(Entity entity) {

        String metaName = entity.getMetaClass().getName();

        switch (metaName) {
            case "tm$User":
                return messages.getMessage(getClass(), "type.user");

            case "df$Individual":
                return messages.getMessage(getClass(), "type.individual");

            case "df$Company":
                return messages.getMessage(getClass(), "type.company");

            case "df$Department":
                return messages.getMessage(getClass(), "type.department");

            default:
                return messages.getMessage(getClass(), "type.unknown");
        }
    }
}