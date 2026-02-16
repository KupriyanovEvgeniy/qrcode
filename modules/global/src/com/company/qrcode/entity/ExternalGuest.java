/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Table(name = "QRCODE_EXTERNAL_GUEST")
@Entity(name = "qrcode$ExternalGuest")
@NamePattern("%s %s|lastName,firstName")
public class ExternalGuest extends StandardEntity {
    private static final long serialVersionUID = 8266078364891302299L;

    @NotNull
    @Column(name = "LAST_NAME", nullable = false)
    protected String lastName;

    @NotNull
    @Column(name = "FIRST_NAME", nullable = false)
    protected String firstName;

    @Column(name = "MIDDLE_NAME")
    protected String middleName;

    @Email
    @Column(name = "EMAIL")
    protected String email;

    @Column(name = "PHONE")
    protected String phone;

    @Column(name = "ORGANIZATION")
    protected String organization;

    @Column(name = "POSITION_")
    protected String position;

    @Column(name = "PASSPORT_DATA")
    protected String passportData;

    @Column(name = "COMMENT_")
    protected String comment;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPassportData() {
        return passportData;
    }

    public void setPassportData(String passportData) {
        this.passportData = passportData;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (lastName != null) {
            sb.append(lastName);
        }
        if (firstName != null) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(firstName);
        }
        if (middleName != null) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(middleName);
        }
        return sb.toString();
    }
}