/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.texttemplate;

import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.TextTemplate;

@UiController("qrcode$TextTemplate.browse")
@UiDescriptor("text-template-browse.xml")
@LookupComponent("textTemplatesTable")
@LoadDataBeforeShow
public class TextTemplateBrowse extends StandardLookup<TextTemplate> {
}