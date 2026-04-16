/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.correspond;

import com.company.qrcode.entity.*;
import com.company.qrcode.web.ui.recipientuniversallist.RecipientUniversalListEdit;
import com.company.qrcode.web.ui.singleentityselect.SingleEntitySelect;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
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

import java.util.*;

import javax.inject.Inject;

@UiController("qrcode_ToChoiseCorrespondent")
@UiDescriptor("to-choise-correspondent.xml")
@LookupComponent("selectedTable")
public class ToChoiseCorrespondent extends StandardLookup<User> {
    @Inject
    private CollectionLoader<RecipientUniversalList> recipientUniversalListsDl;
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
    @Inject
    ScreenBuilders screenBuilders = new ScreenBuilders();
    @Inject
    private Table<RecipientUniversalList> universalListsTable;
    @Inject
    private DataManager dataManager;
    @Inject
    private Metadata metadata;
    @Inject
    private CollectionLoader<RecipientDepartmentList> recipientDepartmentsListsDl;
    @Inject
    private CollectionLoader<RecipientCompanyList> recipientCompaniesListsDl;
    @Inject
    private Table<RecipientUniversalItem> universalItemsTable;
    @Inject
    private UiComponents uiComponents;

    @Subscribe
    public void onInit(InitEvent event) {
        mainBox.setExpandRatio(choiseUsersBox, 1.0f);
        mainBox.setExpandRatio(choiseListsBox, 1.0f);

        selectedTable.addGeneratedColumn("type", entity -> {
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

        universalItemsTable.addGeneratedColumn("entityType", entity -> {
            Label<String> label = uiComponents.create(Label.TYPE_STRING);

            String metaType = entity.getEntityType();
            String type = getTypeName(metaType);

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
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        recipientUserListsDl.setParameter("id", userSession.getUser().getUuid());
        recipientUserListsDl.load();
        recipientsIndividualListsDl.setParameter("id", userSession.getUser().getId());
        recipientsIndividualListsDl.load();
        recipientUniversalListsDl.setParameter("id", userSession.getUser().getId());
        recipientUniversalListsDl.load();
        recipientCompaniesListsDl.setParameter("id", userSession.getUser().getId());
        recipientCompaniesListsDl.load();
        recipientDepartmentsListsDl.setParameter("id", userSession.getUser().getId());
        recipientDepartmentsListsDl.load();
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

            case "universalTab":
                addUniversal();
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
    private Table<RecipientDepartmentList> departmentsListTable;

    private void addDepartments() {
        RecipientDepartmentList list = departmentsListTable.getSingleSelected();

        if (list == null) {
            showSelectError();
            return;
        }

        addEntities(list.getRecipients(), "Подразделение");
    }

    @Inject
    private Table<RecipientCompanyList> companiesListTable;

    private void addCompanies() {
        RecipientCompanyList list = companiesListTable.getSingleSelected();

        if (list == null) {
            showSelectError();
            return;
        }

        addEntities(list.getRecipients(), "Юр лицо");
    }

    private void addUniversal() {

        RecipientUniversalList list = universalListsTable.getSingleSelected();

        if (list == null) {
            showSelectError();
            return;
        }

        for (RecipientUniversalItem item : list.getRecipients()) {

            if (item.getEntityId() == null)
                continue;

            Entity entity = loadEntity(item);

            if (entity != null) {
                addToSelected(entity, getTypeName(item.getEntityType()));
            }
        }
    }

    private Entity loadEntity(RecipientUniversalItem item) {

        MetaClass metaClass = metadata.getSession().getClass(item.getEntityType());

        if (metaClass == null) {
            return null;
        }

        Class<?> javaClass = metaClass.getJavaClass();

        return (Entity) dataManager.load(
                Id.of(item.getEntityId(), (Class) javaClass)
        ).view("_minimal").optional().orElse(null);
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

    @Subscribe("addOneBtn")
    public void onAddOneBtnClick(Button.ClickEvent event) {

        Screen screen = screenBuilders.screen(this)
                .withScreenClass(SingleEntitySelect.class)
                .withOpenMode(OpenMode.DIALOG)
                .build();

        screen.addAfterCloseListener(e -> {
            if (e.closedWith(StandardOutcome.SELECT) && screen instanceof SingleEntitySelect) {

                SingleEntitySelect selectScreen = (SingleEntitySelect) screen;
                Collection<Entity> selected = selectScreen.getSelectedEntities();

                if (selected != null) {
                    for (Entity entity : selected) {
                        if (entity != null) {
                            addToSelected(entity, getTypeName(entity.getMetaClass().getName()));
                        }
                    }
                }
            }
        });

        screen.show();
    }

    @Subscribe("createListBtn")
    public void onCreateListBtnClick(Button.ClickEvent event) {

        String tabId = entityTabs.getSelectedTab().getName();

        Screen screen = null;

        switch (tabId) {

            case "usersTab":
                screen = screenBuilders.editor(RecipientUserList.class, this)
                        .newEntity()
                        .withOpenMode(OpenMode.DIALOG)

                        .build();
                break;

            case "individualsTab":
                screen = screenBuilders.editor(RecipientIndividualList.class, this)
                        .newEntity()
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "companiesTab":
                screen = screenBuilders.editor(RecipientCompanyList.class, this)
                        .newEntity()
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "departmentsTab":
                screen = screenBuilders.editor(RecipientDepartmentList.class, this)
                        .newEntity()
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "universalTab":
                screen = screenBuilders.editor(RecipientUniversalList.class, this)
                        .newEntity()
                        .withOpenMode(OpenMode.DIALOG)
                        .build();

                Screen finalScreen = screen;
                screen.addAfterShowListener(e -> {
                    if (finalScreen instanceof RecipientUniversalListEdit) {

                        RecipientUniversalListEdit editScreen = (RecipientUniversalListEdit) finalScreen;

                        List<Entity> entities = new ArrayList<>();

                        for (KeyValueEntity kv : selectedItemsDc.getItems()) {
                            entities.add(kv.getValue("entity"));
                        }

                        editScreen.initEntities(entities);
                    }
                });
                break;

            default:
                notifications.create()
                        .withCaption("Неизвестный тип списка")
                        .show();
                return;
        }

        screen.addAfterCloseListener(e -> {
            if (e.closedWith(StandardOutcome.COMMIT)) {
                reloadLists();
            }
        });

        screen.show();
    }

    private void reloadLists() {
        recipientUserListsDl.load();
        recipientsIndividualListsDl.load();
        recipientUniversalListsDl.load();
        recipientCompaniesListsDl.load();
        recipientDepartmentsListsDl.load();
    }

    private String getTypeName(String metaName) {
        switch (metaName) {
            case "tm$User":
                return "Сотрудник";
            case "df$Individual":
                return "Физ лицо";
            case "df$Company":
                return "Юр лицо";
            case "df$Department":
                return "Подразделение";
            default:
                return "Неизвестно";
        }
    }

    @Subscribe("editListBtn")
    public void onEditListBtnClick(Button.ClickEvent event) {

        String tabId = entityTabs.getSelectedTab().getName();

        Screen screen = null;

        switch (tabId) {

            case "usersTab":
                RecipientUserList userList = usersListsTable.getSingleSelected();
                if (userList == null) {
                    showSelectError();
                    return;
                }

                screen = screenBuilders.editor(RecipientUserList.class, this)
                        .editEntity(userList)
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "individualsTab":
                RecipientIndividualList individualList = individualsListTable.getSingleSelected();
                if (individualList == null) {
                    showSelectError();
                    return;
                }

                screen = screenBuilders.editor(RecipientIndividualList.class, this)
                        .editEntity(individualList)
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "companiesTab":
                RecipientCompanyList companyList = companiesListTable.getSingleSelected();
                if (companyList == null) {
                    showSelectError();
                    return;
                }

                screen = screenBuilders.editor(RecipientCompanyList.class, this)
                        .editEntity(companyList)
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "departmentsTab":
                RecipientDepartmentList departmentList = departmentsListTable.getSingleSelected();
                if (departmentList == null) {
                    showSelectError();
                    return;
                }

                screen = screenBuilders.editor(RecipientDepartmentList.class, this)
                        .editEntity(departmentList)
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "universalTab":
                RecipientUniversalList universalList = universalListsTable.getSingleSelected();
                if (universalList == null) {
                    showSelectError();
                    return;
                }

                screen = screenBuilders.editor(RecipientUniversalList.class, this)
                        .editEntity(universalList)
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            default:
                notifications.create()
                        .withCaption("Неизвестный тип списка")
                        .show();
                return;

        }

        screen.addAfterCloseListener(e -> {
            if (e.closedWith(StandardOutcome.COMMIT)) {
                reloadLists();
            }
        });

        screen.show();

    }
}