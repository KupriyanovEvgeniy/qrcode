/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.templatecategories;

import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.TemplateCategories;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("qrcode$TemplateCategories.browse")
@UiDescriptor("template-categories-browse.xml")
@LookupComponent("templateCategoriesesTable")
@LoadDataBeforeShow
public class TemplateCategoriesBrowse extends StandardLookup<TemplateCategories> {
    @Inject
    private CollectionLoader<TemplateCategories> templateCategoriesesDl;
    @Inject
    private UserSession userSession;
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        templateCategoriesesDl.setParameter("id", userSession.getUser().getUuid());
        templateCategoriesesDl.load();
    }
}