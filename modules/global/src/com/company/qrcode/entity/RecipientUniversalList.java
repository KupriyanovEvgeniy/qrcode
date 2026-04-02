/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "QRCODE_RECIPIENT_UNIVERSAL_LIST")
@Entity(name = "qrcode$RecipientUniversalList")
@NamePattern("%s|name")
public class RecipientUniversalList extends StandardEntity {
    private static final long serialVersionUID = 6145439279549951397L;
    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OWNER_ID")
    protected User owner;
    @NotNull
    @Column(name = "ACCESS_TYPE", nullable = false)
    protected String accessType;
    @Composition
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    protected List<RecipientUniversalItem> recipients;
    @JoinTable(name = "QRCODE_RECIPIENT_UNIVERSAL_LIST_USER_LINK",
            joinColumns = @JoinColumn(name = "RECIPIENT_UNIVERSAL_LIST_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @ManyToMany
    protected List<User> share;

    public List<RecipientUniversalItem> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<RecipientUniversalItem> recipients) {
        this.recipients = recipients;
    }

    public List<User> getShare() {
        return share;
    }

    public void setShare(List<User> share) {
        this.share = share;
    }

    public RecipientListAccessType getAccessType() {
        return accessType == null ? null : RecipientListAccessType.fromId(accessType);
    }

    public void setAccessType(RecipientListAccessType accessType) {
        this.accessType = accessType == null ? null : accessType.getId();
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}