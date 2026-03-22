/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.templatecategories;

import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.TemplateCategories;

@UiController("qrcode$TemplateCategories.edit")
@UiDescriptor("template-categories-edit.xml")
@EditedEntityContainer("templateCategoriesDc")
@LoadDataBeforeShow
public class TemplateCategoriesEdit extends StandardEditor<TemplateCategories> {
}