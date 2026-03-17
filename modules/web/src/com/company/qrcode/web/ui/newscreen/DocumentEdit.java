/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.newscreen;

import com.company.qrcode.entity.support.UniquePerson;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.core.entity.Entity;

import javax.inject.Inject;
import java.awt.*;
import java.util.Collection;

@UiController("qrcode_DocumentEdit")
@UiDescriptor("document-edit.xml")
public class DocumentEdit extends Screen {
    @Inject
    private PickerField<Entity> addresseeField;

    @Inject
    private ScreenBuilders screenBuilders;

    @Inject
    private Screens screens;

    @Subscribe("addresseeField.lookup")
    public void onAddresseeLookup(Action.ActionPerformedEvent event) {

        Screen screen = screens.create("qrcode_AddresseeBrowse", OpenMode.DIALOG);
        screen.show();

    }

//    @Subscribe("addresseeField.lookup")
//    public void onAddresseeLookup(Action.ActionPerformedEvent event) {
//
//        screenBuilders.lookup(addresseeField)
//                .withScreenClass(PersonLookupScreen.class)
//                .withSelectHandler(selected -> {
//
//                    Collection<?> items = (Collection<?>) selected;
//
//                    if (items == null || items.isEmpty())
//                        return;
//
//                    KeyValueEntity row = (KeyValueEntity) items.iterator().next();
//
//                    UniquePerson person = row.getValue("person");
//
//                    addresseeField.setValue(person.getEntity());
//
//                })
//                .build()
//                .show();
//    }
}