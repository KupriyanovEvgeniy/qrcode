/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Table(name = "QRCODE_EVENT_REQUEST")
@Entity(name = "qrcode$EventRequest")
@NamePattern("%s|firstName")
public class EventRequest extends StandardEntity {
    private static final long serialVersionUID = -7261740379357396212L;
    @Column(name = "EVENT_CODE")
    protected String eventCode;
    @Column(name = "EVENT_NAME")
    protected String eventName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_HALL_ID")
    protected Hall eventHall;
    @NotNull
    @Column(name = "EVENT_DATE", nullable = false)
    protected LocalDate eventDate;
    @NotNull
    @Column(name = "TIME_START", nullable = false)
    protected LocalTime timeStart;
    @NotNull
    @Column(name = "TIME_END", nullable = false)
    protected LocalTime timeEnd;
    @Column(name = "FIRST_NAME")
    protected String firstName;
    @Column(name = "LAST_NAME")
    protected String lastName;
    @Column(name = "MIDDLE_NAME")
    protected String middleName;
    @Column(name = "ORGANIZATION")
    protected String organization;
    @Column(name = "QR_CODE")
    protected byte[] qrCode;
    @OneToMany(mappedBy = "eventRequest")
    protected List<EventParticipant> participants;
    @Column(name = "REQUEST_DATE")
    protected LocalDate requestDate;
    @Column(name = "NUMBER_")
    protected Integer number;


    // Внешние гости (физические лица) - новое поле
    @OneToMany(mappedBy = "eventRequest")
    protected List<EventExternalParticipant> externalParticipants;


    public List<EventExternalParticipant> getExternalParticipants() {
        return externalParticipants;
    }

    public void setExternalParticipants(List<EventExternalParticipant> externalParticipants) {
        this.externalParticipants = externalParticipants;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public List<EventParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<EventParticipant> participants) {
        this.participants = participants;
    }

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

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

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Hall getEventHall() {
        return eventHall;
    }

    public void setEventHall(Hall eventHall) {
        this.eventHall = eventHall;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }
}