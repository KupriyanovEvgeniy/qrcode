/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.texttemplate;

import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.TextTemplate;

@UiController("qrcode$TextTemplate.edit")
@UiDescriptor("text-template-edit.xml")
@EditedEntityContainer("textTemplateDc")
@LoadDataBeforeShow
public class TextTemplateEdit extends StandardEditor<TextTemplate> {
}