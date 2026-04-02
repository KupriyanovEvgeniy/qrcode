/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "QRCODE_RECIPIENT_UNIVERSAL_ITEM")
@Entity(name = "qrcode$RecipientUniversalItem")
public class RecipientUniversalItem extends StandardEntity {
    private static final long serialVersionUID = 8620640734175263181L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIST_ID")
    protected RecipientUniversalList list;
    @Column(name = "ENTITY_ID")
    protected UUID entityId;
    @Column(name = "ENTITY_NAME")
    protected String entityName;
    @Column(name = "ENTITY_TYPE")
    protected String entityType;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public RecipientUniversalList getList() {
        return list;
    }

    public void setList(RecipientUniversalList list) {
        this.list = list;
    }
}