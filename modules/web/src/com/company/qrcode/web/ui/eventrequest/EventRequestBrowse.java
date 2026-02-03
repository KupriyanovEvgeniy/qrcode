/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.eventrequest;

import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.EventRequest;

@UiController("qrcode$EventRequest.browse")
@UiDescriptor("event-request-browse.xml")
@LookupComponent("eventRequestsTable")
@LoadDataBeforeShow
public class EventRequestBrowse extends StandardLookup<EventRequest> {
}