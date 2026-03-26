/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.newscreen;

import com.company.qrcode.entity.support.UniquePerson;
import com.company.qrcode.web.ui.correspond.ToChoiseCorrespondent;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.TokenList;
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
    private ScreenBuilders screenBuilders;

    @Inject
    private Screens screens;
    @Inject
    private TokenList addresseeField;

    @Subscribe("selectBtn")
    public void onSelectBtnClick(Button.ClickEvent event) {

        screenBuilders.screen(this)
                .withScreenClass(ToChoiseCorrespondent.class)
                .withLaunchMode(OpenMode.NEW_TAB)
                .build()
                .show()
                .addAfterCloseListener(closeEvent -> {

                    if (closeEvent.closedWith(StandardOutcome.SELECT)) {

                        ToChoiseCorrespondent screen =
                                (ToChoiseCorrespondent) closeEvent.getSource();

                        Collection<Entity> selected = screen.getSelectedEntities();

                        if (selected != null) {
                            addresseeField.setValue(selected);
                        }
                    }
                });
    }
    
    
}