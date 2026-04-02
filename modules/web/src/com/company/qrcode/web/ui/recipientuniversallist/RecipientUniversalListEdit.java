/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.recipientuniversallist;

import com.company.qrcode.entity.RecipientIndividualList;
import com.company.qrcode.entity.RecipientListAccessType;
import com.company.qrcode.entity.RecipientUniversalItem;
import com.company.qrcode.web.ui.correspond.ToChoiseCorrespondent;
import com.company.qrcode.web.ui.singleentityselect.SingleEntitySelect;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.RecipientUniversalList;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Collection;
import java.util.UUID;

@UiController("qrcode$RecipientUniversalList.edit")
@UiDescriptor("recipient-universal-list-edit.xml")
@EditedEntityContainer("recipientUniversalListDc")
@LoadDataBeforeShow
public class RecipientUniversalListEdit extends StandardEditor<RecipientUniversalList> {
    @Inject
    private UserSession userSession;
    @Inject
    private GroupBoxLayout shareBox;
    @Inject
    private CollectionContainer<RecipientUniversalItem> recipientsDc;
    @Inject
    private Metadata metadata;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<RecipientUniversalItem> recipientsTable;
    @Subscribe
    public void onInit(InitEntityEvent<RecipientUniversalList> event) {
        event.getEntity().setOwner(userSession.getUser());
        event.getEntity().setAccessType(RecipientListAccessType.PRIVATE);
    }
    @Subscribe("accessTypeField")
    public void showShareWindow(HasValue.ValueChangeEvent<RecipientListAccessType> event){
        RecipientListAccessType selectedType = event.getValue();
        boolean isPrivate = RecipientListAccessType.PRIVATE.equals(selectedType);
        shareBox.setVisible(isPrivate);
    }

    @Subscribe("addBtn")
    public void onAddBtnClick(Button.ClickEvent event) {
        screenBuilders.screen(this)
                .withScreenClass(SingleEntitySelect.class)
                .withOpenMode(OpenMode.DIALOG)
                .withAfterCloseListener(closeEvent -> {

                    if (closeEvent.closedWith(StandardOutcome.SELECT)) {

                        SingleEntitySelect screen =
                                (SingleEntitySelect) closeEvent.getSource();

                        Collection<Entity> selected = screen.getSelectedEntities();

                        addEntities(selected);
                    }
                })
                .build()
                .show();
    }

    private void addEntities(Collection<Entity> entities) {

        if (entities == null || entities.isEmpty())
            return;

        for (Entity entity : entities) {

            if (alreadyExists(entity))
                continue;

            RecipientUniversalItem item = metadata.create(RecipientUniversalItem.class);

            item.setList(getEditedEntity());
            item.setEntityId((UUID) entity.getId());
            item.setEntityType(entity.getMetaClass().getName());
            item.setEntityName(entity.getInstanceName());

            recipientsDc.getMutableItems().add(item);
        }
    }

    private boolean alreadyExists(Entity entity) {
        return recipientsDc.getItems().stream()
                .anyMatch(i ->
                        entity.getId().equals(i.getEntityId())
                                && entity.getMetaClass().getName().equals(i.getEntityType())
                );
    }

    @Subscribe("removeBtn")
    public void onRemoveBtnClick(Button.ClickEvent event) {

        RecipientUniversalItem selected = recipientsTable.getSingleSelected();

        if (selected != null) {
            recipientsDc.getMutableItems().remove(selected);
        }
    }
}