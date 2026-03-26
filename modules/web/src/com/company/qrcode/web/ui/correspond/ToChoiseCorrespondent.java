/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.correspond;

import com.company.qrcode.entity.RecipientIndividualList;
import com.company.qrcode.entity.RecipientUserList;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.core.entity.Entity;
import java.util.Collection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UiController("qrcode_ToChoiseCorrespondent")
@UiDescriptor("to-choise-correspondent.xml")
@LookupComponent("selectedTable")
public class ToChoiseCorrespondent extends StandardLookup<User> {
    @Inject
    private GroupBoxLayout choiseListsBox;
    @Inject
    private GroupBoxLayout choiseUsersBox;
    @Inject
    private VBoxLayout mainBox;
    @Inject
    private Table<RecipientUserList> usersListsTable;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionLoader<RecipientUserList> recipientUserListsDl;
    @Inject
    private UserSession userSession;
    @Inject
    private KeyValueCollectionContainer selectedItemsDc;
    @Inject
    private Table<KeyValueEntity> selectedTable;
    @Inject
    private CollectionLoader<RecipientIndividualList> recipientsIndividualListsDl;

    @Subscribe
    public void onInit(InitEvent event) {
        mainBox.setExpandRatio(choiseUsersBox, 1.0f);
        mainBox.setExpandRatio(choiseListsBox, 1.0f);
    }
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        recipientUserListsDl.setParameter("id", userSession.getUser().getUuid());
        recipientUserListsDl.load();
        recipientsIndividualListsDl.setParameter("id", userSession.getUser().getId());
        recipientsIndividualListsDl.load();
    }
    @Subscribe("addList")
    public void onAddListClick(Button.ClickEvent event) {
        RecipientUserList selectedList = usersListsTable.getSingleSelected();
        if (selectedList != null) {

            List<User> users = selectedList.getRecipients();

            if (users == null || users.isEmpty()) {
                notifications.create()
                        .withCaption("В списке нет ни одного пользователя")
                        .show();
                return;
            }

            for (User user : users) {
                addToSelected(user, "Сотрудник");
            }

        } else {
            notifications.create()
                    .withCaption("Выберите хоть один список!")
                    .show();
        }
    }
    @Subscribe("removeList")
    public void onRemoveListClick(Button.ClickEvent event) {

        Set<KeyValueEntity> selected = selectedTable.getSelected();

        if (selected != null && !selected.isEmpty()) {
            selectedItemsDc.getMutableItems().removeAll(selected);
        } else {
            notifications.create()
                    .withCaption("Выберите хоть одного пользователя")
                    .show();
        }
    }

    private boolean alreadyExists(Entity entity) {
        return selectedItemsDc.getItems().stream()
                .anyMatch(e -> entity.equals(e.getValue("entity")));
    }

    private void addToSelected(Entity entity, String type) {

        if (alreadyExists(entity))
            return;

        KeyValueEntity row = new KeyValueEntity();

        row.setValue("entity", entity);
        row.setValue("name", entity.getInstanceName());
        row.setValue("type", type);

        selectedItemsDc.getMutableItems().add(row);
    }

    public Collection<Entity> getSelectedEntities() {

        List<Entity> result = new ArrayList<>();

        for (KeyValueEntity item: selectedItemsDc.getItems()) {
            result.add(item.getValue("entity"));
        }

        return result;
    }

    @Subscribe("selectBtn")
    public void onSelectBtnClick(Button.ClickEvent event) {
        close(StandardOutcome.SELECT);
    }

    @Subscribe("cancelBtn")
    public void onCancelBtnClick(Button.ClickEvent event) {
        close(StandardOutcome.CLOSE);
    }
}