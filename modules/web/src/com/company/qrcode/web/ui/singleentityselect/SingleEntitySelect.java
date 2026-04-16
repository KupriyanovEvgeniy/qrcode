/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.singleentityselect;

import com.company.qrcode.entity.*;
import com.company.qrcode.web.ui.recipientuniversallist.RecipientUniversalListEdit;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.thesis.core.entity.Company;
import com.haulmont.thesis.core.entity.Department;
import com.haulmont.thesis.core.entity.Individual;
import com.haulmont.cuba.core.entity.Entity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UiController("qrcode_SingleEntitySelect")
@UiDescriptor("single-entity-select.xml")
public class SingleEntitySelect extends Screen {
    @Inject
    private TabSheet tabs;
    @Inject
    private Table<User> usersTable;
    @Inject
    private Table<Individual> individualsTable;
    @Inject
    private Table<Company> companiesTable;
    @Inject
    private Table<Department> departmentsTable;
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private CollectionLoader<Department> departmentsDl;
    @Inject
    private CollectionLoader<Individual> individualsDl;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private CollectionLoader<User> usersDl;

    private List<Entity> result = new ArrayList<>();

    public Collection<Entity> getSelectedEntities() {
        return result;
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        getScreenData().loadAll();
    }

    @Subscribe("selectBtn")
    public void onSelectBtnClick(Button.ClickEvent event) {

        String tabId = tabs.getSelectedTab().getName();

        switch (tabId) {
            case "usersTab":
                result.addAll(usersTable.getSelected());
                break;

            case "individualsTab":
                result.addAll(individualsTable.getSelected());
                break;

            case "companiesTab":
                result.addAll(companiesTable.getSelected());
                break;

            case "departmentsTab":
                result.addAll(departmentsTable.getSelected());
                break;
        }

        close(StandardOutcome.SELECT);
    }

    @Subscribe("cancelBtn")
    public void onCancelBtnClick(Button.ClickEvent event) {
        close(StandardOutcome.CLOSE);
    }

    @Subscribe("createBtn")
    public void onCreateBtnClick(Button.ClickEvent event) {
        String tabId = tabs.getSelectedTab().getName();

        Screen screen = null;

        switch (tabId) {

            case "usersTab":
                screen = screenBuilders.editor(User.class, this)
                        .newEntity()
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "individualsTab":
                screen = screenBuilders.editor(Individual.class, this)
                        .newEntity()
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "companiesTab":
                screen = screenBuilders.editor(Company.class, this)
                        .newEntity()
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;

            case "departmentsTab":
                screen = screenBuilders.editor(Department.class, this)
                        .newEntity()
                        .withOpenMode(OpenMode.DIALOG)
                        .build();
                break;
        }


        screen.addAfterCloseListener(e -> {
            if (e.closedWith(StandardOutcome.COMMIT)) {
                reloadLists();
            }
        });

        screen.show();

    }

    private void reloadLists() {
        usersDl.load();
        individualsDl.load();
        companiesDl.load();
        departmentsDl.load();
    }
}