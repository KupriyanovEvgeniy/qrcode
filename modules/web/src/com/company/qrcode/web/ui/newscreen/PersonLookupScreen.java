/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.newscreen;

import com.company.qrcode.entity.support.UniquePerson;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.thesis.core.entity.Company;
import com.haulmont.thesis.core.entity.Individual;

import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@UiController("qrcode_PersonLookupScreen")
@UiDescriptor("person-lookup-screen.xml")
public class PersonLookupScreen extends Screen implements LookupScreen {
    @Inject
    private DataManager dataManager;

    @Inject
    private KeyValueCollectionContainer personsDc;

    private Consumer<Collection> selectHandler;

    @Inject
    private Table<KeyValueEntity> personsTable;

    private Predicate<Collection> selectValidator;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        personsDc.getMutableItems().clear();
        loadUsers();
        loadPersons();
        loadCompanies();
    }

    private void addRow(Entity entity, String type) {

        KeyValueEntity row = new KeyValueEntity();

        row.setValue("person", new UniquePerson(entity));
        row.setValue("name", entity.getInstanceName());
        row.setValue("type", type);

        personsDc.getMutableItems().add(row);
    }

    private void loadUsers() {
        List<User> users = dataManager.load(User.class)
                .query("select u from sec$User u")
                .list();

        for (User user : users) {
            addRow(user, "Сотрудник");
        }
    }

    private void loadPersons() {
        List<Individual> persons = dataManager.load(Individual.class)
                .query("select i from df$Individual i")
                .list();

        for (Individual person : persons) {
            addRow(person, "Физ лицо");
        }
    }

    private void loadCompanies() {
        List<Company> companies = dataManager.load(Company.class)
                .query("select c from df$Company c")
                .list();

        for (Company company : companies) {
            addRow(company, "Юр лицо");
        }
    }

    @Subscribe
    public void onInit(InitEvent event) {

//        personsTable.addItemClickListener(clickEvent -> {
//
//            if (!clickEvent.isDoubleClick())
//                return;
//
//            if (selectHandler != null) {
//
//                KeyValueEntity item = (KeyValueEntity) clickEvent.getItem();
//
//                List<KeyValueEntity> selected = List.of(item);
//
//                if (selectValidator == null || selectValidator.test(selected)) {
//                    selectHandler.accept(selected);
//                    close(StandardOutcome.SELECT);
//                }
//            }
//        });
    }

    @Override
    public Consumer<Collection> getSelectHandler() {
        return selectHandler;
    }

    @Override
    public void setSelectHandler(@Nullable Consumer handler) {
        this.selectHandler = handler;
    }

    @Override
    public Predicate<ValidationContext> getSelectValidator() {
        return ctx -> selectValidator == null || selectValidator.test(ctx.getSelectedItems());
    }

    @Override
    public void setSelectValidator(Predicate validator) {
        this.selectValidator = validator;
    }
}