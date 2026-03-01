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

@Table(name = "QRCODE_REQUEST")
@Entity(name = "qrcode$Request")
@NamePattern("%s|name")
public class Request extends StandardEntity {
    private static final long serialVersionUID = 426150667533252197L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;

    @JoinTable(name = "QRCODE_REQUEST_USER_LINK",
            joinColumns = @JoinColumn(name = "REQUEST_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @ManyToMany
    protected List<User> recipients;

    public List<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<User> recipients) {
        this.recipients = recipients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}