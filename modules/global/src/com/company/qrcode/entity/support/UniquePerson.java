/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity.support;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;

public class UniquePerson {

    private Entity entity;
    private MetaClass metaClass;

    public UniquePerson(Entity entity) {
        this.entity = entity;
        this.metaClass = entity.getMetaClass();
    }

    public Entity getEntity() {
        return entity;
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    public String getType() {
        return metaClass.getName();
    }
}
