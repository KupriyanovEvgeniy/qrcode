/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.recipientdepartmentlist;

import com.company.qrcode.entity.RecipientIndividualList;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.RecipientDepartmentList;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("qrcode$RecipientDepartmentList.browse")
@UiDescriptor("recipient-department-list-browse.xml")
@LookupComponent("recipientDepartmentListsTable")
@LoadDataBeforeShow
public class RecipientDepartmentListBrowse extends StandardLookup<RecipientDepartmentList> {
    @Inject
    private CollectionLoader<RecipientDepartmentList> recipientDepartmentListsDl;
    @Inject
    private UserSession userSession;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        recipientDepartmentListsDl.setParameter("id", userSession.getUser().getId());
    }
}