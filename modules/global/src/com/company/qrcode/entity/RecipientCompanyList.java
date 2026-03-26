/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.thesis.core.entity.Company;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "QRCODE_RECIPIENT_COMPANY_LIST")
@Entity(name = "qrcode$RecipientCompanyList")
public class RecipientCompanyList extends StandardEntity {
    private static final long serialVersionUID = -6341521932260851804L;
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

    @JoinTable(name = "QRCODE_RECIPIENT_LIST_COMPANY_LINK", joinColumns = @JoinColumn(name = "RECIPIENT_LIST_ID"), inverseJoinColumns = @JoinColumn(name = "COMPANY_ID"))
    @ManyToMany
    @NotNull
    protected List<Company> recipients;

    @JoinTable(name = "QRCODE_RECIPIENT_LIST_COMPANY_SHARE_LINK", joinColumns = @JoinColumn(name = "RECIPIENT_LIST_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @ManyToMany
    protected List<User> share;

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public RecipientListAccessType getAccessType() {
        return accessType == null ? null : RecipientListAccessType.fromId(accessType);
    }

    public void setAccessType(RecipientListAccessType accessType) {
        this.accessType = accessType == null ? null : accessType.getId();
    }

    public List<User> getShare() {
        return share;
    }

    public void setShare(List<User> share) {
        this.share = share;
    }

    public User getOwner() {
        return owner;
    }

    public List<Company> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Company> recipients) {
        this.recipients = recipients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}