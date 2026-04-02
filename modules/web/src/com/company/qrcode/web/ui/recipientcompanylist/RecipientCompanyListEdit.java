/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.recipientcompanylist;

import com.company.qrcode.entity.RecipientIndividualList;
import com.company.qrcode.entity.RecipientListAccessType;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.RecipientCompanyList;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("qrcode$RecipientCompanyList.edit")
@UiDescriptor("recipient-company-list-edit.xml")
@EditedEntityContainer("recipientCompanyListDc")
@LoadDataBeforeShow
public class RecipientCompanyListEdit extends StandardEditor<RecipientCompanyList> {
    @Inject
    private UserSession userSession;
    @Inject
    private GroupBoxLayout shareBox;
    @Subscribe
    public void onInit(InitEntityEvent<RecipientCompanyList> event) {
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