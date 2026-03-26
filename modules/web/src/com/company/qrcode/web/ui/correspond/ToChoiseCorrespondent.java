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
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.thesis.core.entity.Company;
import com.haulmont.thesis.core.entity.Department;

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
    @Inject
    private TabSheet entityTabs;

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

        String tabId = entityTabs.getSelectedTab().getName();

        switch (tabId) {

            case "usersTab":
                addUsers();
                break;

            case "individualsTab":
                addIndividuals();
                break;

            case "companiesTab":
                addCompanies();
                break;

            case "departmentsTab":
                addDepartments();
                break;

            default:
                notifications.create()
                        .withCaption("Неизвестный тип вкладки")
                        .show();
        }
    }

    private void addEntities(Collection<? extends Entity> entities, String type) {

        if (entities == null || entities.isEmpty()) {
            notifications.create()
                    .withCaption("Нет данных для добавления")
                    .show();
            return;
        }

        for (Entity entity : entities) {
            addToSelected(entity, type);
        }
    }

    private void addUsers() {
        RecipientUserList list = usersListsTable.getSingleSelected();

        if (list == null) {
            showSelectError();
            return;
        }

        addEntities(list.getRecipients(), "Сотрудник");
    }

    @Inject
    private Table<RecipientIndividualList> individualsListTable;

    private void addIndividuals() {
        RecipientIndividualList list = individualsListTable.getSingleSelected();

        if (list == null) {
            showSelectError();
            return;
        }

        addEntities(list.getRecipients(), "Физ лицо");
    }

    @Inject
    private Table<Department> departmentsListTable;

    private void addDepartments() {
        Set<Department> selected = departmentsListTable.getSelected();

        if (selected == null || selected.isEmpty()) {
            showSelectError();
            return;
        }

        addEntities(selected, "Подразделение");
    }

    @Inject
    private Table<Company> companiesListTable;

    private void addCompanies() {
        Set<Company> selected = companiesListTable.getSelected();

        if (selected == null || selected.isEmpty()) {
            showSelectError();
            return;
        }

        addEntities(selected, "Юр лицо");
    }

    private void showSelectError() {
        notifications.create()
                .withCaption("Выберите элемент")
                .show();
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