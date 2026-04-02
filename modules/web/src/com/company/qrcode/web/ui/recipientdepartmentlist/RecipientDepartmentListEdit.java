/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.recipientdepartmentlist;

import com.company.qrcode.entity.RecipientIndividualList;
import com.company.qrcode.entity.RecipientListAccessType;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.RecipientDepartmentList;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("qrcode$RecipientDepartmentList.edit")
@UiDescriptor("recipient-department-list-edit.xml")
@EditedEntityContainer("recipientDepartmentListDc")
@LoadDataBeforeShow
public class RecipientDepartmentListEdit extends StandardEditor<RecipientDepartmentList> {
    @Inject
    private UserSession userSession;
    @Inject
    private GroupBoxLayout shareBox;
    @Subscribe
    public void onInit(InitEntityEvent<RecipientDepartmentList> event) {
        event.getEntity().setOwner(userSession.getUser());
        event.getEntity().setAccessType(RecipientListAccessType.PRIVATE);
    }
    @Subscribe("accessTypeField")
    public void showShareWindow(HasValue.ValueChangeEvent<RecipientListAccessType> event){
        RecipientListAccessType selectedType = event.getValue();
        boolean isPrivate = RecipientListAccessType.PRIVATE.equals(selectedType);
        shareBox.setVisible(isPrivate);
    }
}