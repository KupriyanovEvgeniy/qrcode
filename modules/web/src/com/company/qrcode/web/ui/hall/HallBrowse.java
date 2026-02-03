/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.hall;

import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.Hall;

@UiController("qrcode$Hall.browse")
@UiDescriptor("hall-browse.xml")
@LookupComponent("hallsTable")
@LoadDataBeforeShow
public class HallBrowse extends StandardLookup<Hall> {
}