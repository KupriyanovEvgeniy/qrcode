/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.recipientlist;

import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.RecipientUserList;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("qrcode$RecipientList.browse")
@UiDescriptor("recipient-list-browse.xml")
@LookupComponent("recipientListsTable")
@LoadDataBeforeShow
public class RecipientListBrowse extends StandardLookup<RecipientUserList> {
    @Inject
    private CollectionLoader<RecipientUserList> recipientListsDl;
    @Inject
    private UserSession userSession;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        recipientListsDl.setParameter("id", userSession.getUser().getUuid());
    }
    
}