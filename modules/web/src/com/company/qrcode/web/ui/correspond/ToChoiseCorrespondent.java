/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.correspond;

import com.company.qrcode.entity.RecipientList;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UiController("qrcode_ToChoiseCorrespondent")
@UiDescriptor("to-choise-correspondent.xml")
@LookupComponent("usersTable")
public class ToChoiseCorrespondent extends StandardLookup<User> {
    @Inject
    private GroupBoxLayout choiseListsBox;
    @Inject
    private GroupBoxLayout choiseUsersBox;
    @Inject
    private VBoxLayout mainBox;
    @Inject
    private CollectionContainer<User> selectedUsersDc;
    @Inject
    private Table<RecipientList> listsTable;
    @Inject
    private Table<User> usersTable;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionContainer<RecipientList> recipientListsDc;
    @Inject
    private CollectionLoader<RecipientList> recipientListsDl;

    @Subscribe
    public void onInit(InitEvent event) {
        mainBox.setExpandRatio(choiseUsersBox, 1.0f);
        mainBox.setExpandRatio(choiseListsBox, 1.0f);
    }
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        recipientListsDl.load();
    }
    @Subscribe("addList")
    public void onAddListClick(Button.ClickEvent event) {
        RecipientList selectedList = listsTable.getSingleSelected();
        if(selectedList != null){
            if(selectedUsersDc.getItems().isEmpty()){
               List<User> selectedUsersFromTable = selectedList.getRecipients();
               if(selectedUsersFromTable != null){
                   selectedUsersDc.getMutableItems().addAll(selectedUsersFromTable);
               }
               else{
                   notifications.create().withCaption("В списке нет ни одного пользователя").show();
               }
            }
            else{
                Collection<User> currentUsersInSelectedTable = selectedUsersDc.getItems();
                List<User> selectedUsersFromTable = selectedList.getRecipients();
                List<User> newUsersToAdd = new ArrayList<>();
                for(User user: selectedUsersFromTable){
                    if(!currentUsersInSelectedTable.contains(user)){
                        newUsersToAdd.add(user);
                    }
                }
                selectedUsersDc.getMutableItems().addAll(newUsersToAdd);
            }
        }
        else{
            notifications.create().withCaption("Выберите хоть один список!").show();
        }
    }
    @Subscribe("removeList")
    public void onRemoveListClick(Button.ClickEvent event) {
    }
}