/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.singleentityselect;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.components.Table;
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

    private List<Entity> result = new ArrayList<>();

    public Collection<Entity> getSelectedEntities() {
        return result;
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        // загружаем данные
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
}