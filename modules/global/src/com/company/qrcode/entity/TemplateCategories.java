/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "QRCODE_TEMPLATE_CATEGORIES")
@Entity(name = "qrcode$TemplateCategories")
@NamePattern("%s|name")
public class TemplateCategories extends StandardEntity {
    private static final long serialVersionUID = 2475458751829051992L;

    @Column(name = "NAME", length = 30)
    protected String name;

    @Column(name = "ACTIVE")
    protected Boolean active;

    @Column(name = "ACCESS_TYPE")
    protected String accessType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OWNER_ID")
    @NotNull
    protected User owner;

    @JoinTable(name = "QRCODE_TEMPLATE_CATEGORIES_USER_LINK",
            joinColumns = @JoinColumn(name = "TEMPLATE_CATEGORIES_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @ManyToMany
    protected List<User> share;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public AccessType getAccessType() {
        return accessType == null ? null : AccessType.fromId(accessType);
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType == null ? null : accessType.getId();
    }

    public List<User> getShare() {
        return share;
    }

    public void setShare(List<User> share) {
        this.share = share;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}