/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.recipientuniversallist;

import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.RecipientUniversalList;

@UiController("qrcode$RecipientUniversalList.browse")
@UiDescriptor("recipient-universal-list-browse.xml")
@LookupComponent("recipientUniversalListsTable")
@LoadDataBeforeShow
public class RecipientUniversalListBrowse extends StandardLookup<RecipientUniversalList> {
}