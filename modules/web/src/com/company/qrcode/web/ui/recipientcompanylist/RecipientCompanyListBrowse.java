/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.recipientcompanylist;

import com.company.qrcode.entity.RecipientIndividualList;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.RecipientCompanyList;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("qrcode$RecipientCompanyList.browse")
@UiDescriptor("recipient-company-list-browse.xml")
@LookupComponent("recipientCompanyListsTable")
@LoadDataBeforeShow
public class RecipientCompanyListBrowse extends StandardLookup<RecipientCompanyList> {
    @Inject
    private CollectionLoader<RecipientCompanyList> recipientCompanyListsDl;
    @Inject
    private UserSession userSession;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        recipientCompanyListsDl.setParameter("id", userSession.getUser().getId());
    }
}