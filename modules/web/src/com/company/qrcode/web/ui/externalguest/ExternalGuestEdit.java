/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.externalguest;

import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.ExternalGuest;

@UiController("qrcode$ExternalGuest.edit")
@UiDescriptor("external-guest-edit.xml")
@EditedEntityContainer("externalGuestDc")
@LoadDataBeforeShow
public class ExternalGuestEdit extends StandardEditor<ExternalGuest> {
}