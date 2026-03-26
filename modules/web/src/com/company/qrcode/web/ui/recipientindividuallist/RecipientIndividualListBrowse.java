/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.recipientindividuallist;

import com.company.qrcode.entity.RecipientUserList;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.RecipientIndividualList;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("qrcode$RecipientIndividualList.browse")
@UiDescriptor("recipient-individual-list-browse.xml")
@LookupComponent("recipientIndividualListsTable")
@LoadDataBeforeShow
public class RecipientIndividualListBrowse extends StandardLookup<RecipientIndividualList> {
    @Inject
    private CollectionLoader<RecipientIndividualList> recipientIndividualListsDl;
    @Inject
    private UserSession userSession;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        recipientIndividualListsDl.setParameter("id", userSession.getUser().getId());
    }
}