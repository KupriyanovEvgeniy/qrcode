/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.eventrequest;

import com.company.qrcode.entity.*;
import com.company.qrcode.service.EventCodeService;
import com.company.qrcode.service.EventEmailService;
import com.company.qrcode.service.EventQrCodeService;
import com.company.qrcode.web.ui.Qrcodedialog;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.UUID;

@UiController("qrcode$EventRequest.edit")
@UiDescriptor("event-request-edit.xml")
@EditedEntityContainer("eventRequestDc")
@LoadDataBeforeShow
public class EventRequestEdit extends StandardEditor<EventRequest> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EventRequestEdit.class);

    @Inject
    private EventEmailService eventEmailService;

    @Inject
    private DataManager dataManager;

    @Inject
    private EventCodeService eventCodeService;

    @Inject
    private EventQrCodeService eventQrCodeService;

    @Inject
    private InstanceContainer<EventRequest> eventRequestDc;

    @Inject
    private CollectionContainer<EventParticipant> participantsDc;

    @Inject
    private CollectionLoader<EventParticipant> participantsDl;

    @Inject
    private CollectionLoader<User> allUsersDl;

    @Inject
    private LookupPickerField<User> userPicker;

    @Inject
    private Table<EventParticipant> participantsTable;

    @Inject
    private Button addParticipantBtn;

    @Inject
    private Button removeParticipantBtn;

    @Inject
    private Button generateQrBtn;

    @Inject
    private Button showQrBtn;

    @Inject
    private Notifications notifications;

    @Inject
    private DataContext dataContext;

    @Inject
    private ScreenBuilders screenBuilders;

    @Inject
    private FileUploadField qrFileUpload;

    @Inject
    private CollectionContainer<EventExternalParticipant> externalParticipantsDc;
    @Inject
    private CollectionLoader<EventExternalParticipant> externalParticipantsDl;
    @Inject
    private CollectionLoader<ExternalGuest> allGuestsDl;
    @Inject
    private LookupPickerField<ExternalGuest> guestPicker;
    @Inject
    private Table<EventExternalParticipant> externalParticipantsTable;
    @Inject
    private Button addExternalGuestBtn;
    @Inject
    private Button removeExternalGuestBtn;
    @Inject
    private Button generateExternalQrBtn;
    @Inject
    private Button showExternalQrBtn;
    @Inject
    private Button downloadExternalQrBtn;
    @Inject
    private Button sendExternalEmailBtn;

    @Inject
    private FileUploadField externalQrFileUpload;

    @Inject
    private Button processExternalQrBtn;


    @Subscribe
    public void onInitEntity(InitEntityEvent<EventRequest> event) {
        EventRequest req = event.getEntity();

        req.setRequestDate(LocalDate.now());
        req.setNumber(dataManager.loadValue(
                "select coalesce(max(e.number), 0) + 1 from qrcode$EventRequest e",
                Integer.class
        ).one());

        req.setEventCode(eventCodeService.generateEventCode(req));
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        // Загружаем всех пользователей
        allUsersDl.load();

        // Загружаем участников для текущего мероприятия
        EventRequest eventRequest = getEditedEntity();
        if (eventRequest.getId() != null) {
            participantsDl.setParameter("eventId", eventRequest.getId());
            participantsDl.load();
        }

        // Настраиваем таблицу
        participantsTable.addSelectionListener(e -> {
            boolean hasSelection = !participantsTable.getSelected().isEmpty();
            removeParticipantBtn.setEnabled(hasSelection);
            showQrBtn.setEnabled(hasSelection);
        });

        // Загружаем внешних гостей
        allGuestsDl.load();

        if (eventRequest.getId() != null) {
            externalParticipantsDl.setParameter("eventId", eventRequest.getId());
            externalParticipantsDl.load();
        }

        // Настраиваем таблицу внешних участников
        externalParticipantsTable.addSelectionListener(e -> {
            boolean hasSelection = !externalParticipantsTable.getSelected().isEmpty();
            removeExternalGuestBtn.setEnabled(hasSelection);
            showExternalQrBtn.setEnabled(hasSelection);
            downloadExternalQrBtn.setEnabled(hasSelection);
            sendExternalEmailBtn.setEnabled(hasSelection);
        });
    }

    @Subscribe("addExternalGuestBtn")
    public void onAddExternalGuestBtnClick(Button.ClickEvent event) {
        ExternalGuest selectedGuest = guestPicker.getValue();
        if (selectedGuest == null) {
            notifications.create()
                    .withCaption("Выберите гостя")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        // Проверяем, не добавлен ли уже этот гость
        boolean alreadyAdded = externalParticipantsDc.getItems().stream()
                .anyMatch(p -> p.getGuest() != null &&
                        p.getGuest().getId().equals(selectedGuest.getId()));

        if (alreadyAdded) {
            notifications.create()
                    .withCaption("Этот гость уже добавлен")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        // Создаем нового внешнего участника
        EventExternalParticipant participant = dataContext.create(EventExternalParticipant.class);
        participant.setEventRequest(getEditedEntity());
        participant.setGuest(selectedGuest);

        // Добавляем в DataContainer
        externalParticipantsDc.getMutableItems().add(participant);

        notifications.create()
                .withCaption("Гость добавлен")
                .show();

        guestPicker.setValue(null);
    }

    @Subscribe("removeExternalGuestBtn")
    public void onRemoveExternalGuestBtnClick(Button.ClickEvent event) {
        EventExternalParticipant selected = externalParticipantsTable.getSingleSelected();
        if (selected == null) return;

        externalParticipantsDc.getMutableItems().remove(selected);

        if (selected.getId() != null) {
            dataContext.remove(selected);
        }

        notifications.create()
                .withCaption("Гость удален")
                .show();
    }

    @Subscribe("generateExternalQrBtn")
    public void onGenerateExternalQrBtnClick(Button.ClickEvent event) {
        if (externalParticipantsDc.getItems().isEmpty()) {
            notifications.create()
                    .withCaption("Нет гостей для генерации QR-кодов")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        int generatedCount = 0;
        EventRequest eventRequest = getEditedEntity();

        for (EventExternalParticipant participant : externalParticipantsDc.getItems()) {
            if (participant.getQrCode() == null || participant.getQrCode().length == 0) {
                try {
                    byte[] qrCode = eventQrCodeService.generateForExternalGuest(
                            eventRequest,
                            participant.getGuest()
                    );
                    participant.setQrCode(qrCode);
                    generatedCount++;
                } catch (Exception e) {
                    log.error("Ошибка генерации QR-кода для гостя", e);
                }
            }
        }

        notifications.create()
                .withCaption("QR-коды сгенерированы")
                .withDescription("Создано: " + generatedCount)
                .show();
    }


    @Subscribe("addParticipantBtn")
    public void onAddParticipantBtnClick(Button.ClickEvent event) {
        User selectedUser = userPicker.getValue();
        if (selectedUser == null) {
            notifications.create()
                    .withCaption("Выберите пользователя")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        // Проверяем, не добавлен ли уже этот пользователь
        boolean alreadyAdded = participantsDc.getItems().stream()
                .anyMatch(p -> p.getUser() != null && p.getUser().getId().equals(selectedUser.getId()));

        if (alreadyAdded) {
            notifications.create()
                    .withCaption("Этот пользователь уже добавлен")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        // Создаем нового участника через DataContext
        EventParticipant participant = dataContext.create(EventParticipant.class);
        participant.setEventRequest(getEditedEntity());
        participant.setUser(selectedUser);

        // Добавляем в DataContainer для отображения в таблице
        participantsDc.getMutableItems().add(participant);

        notifications.create()
                .withCaption("Пользователь добавлен")
                .show();

        // Очищаем поле выбора
        userPicker.setValue(null);
    }

    @Subscribe("removeParticipantBtn")
    public void onRemoveParticipantBtnClick(Button.ClickEvent event) {
        EventParticipant selected = participantsTable.getSingleSelected();
        if (selected == null) {
            return;
        }

        // Удаляем из таблицы
        participantsDc.getMutableItems().remove(selected);

        // Помечаем для удаления в DataContext
        if (selected.getId() != null) {
            dataContext.remove(selected);
        }

        notifications.create()
                .withCaption("Участник удален")
                .show();
    }

    @Subscribe("generateQrBtn")
    public void onGenerateQrBtnClick(Button.ClickEvent event) {
        if (participantsDc.getItems().isEmpty()) {
            notifications.create()
                    .withCaption("Нет участников для генерации QR-кодов")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        int generatedCount = 0;

        for (EventParticipant participant : participantsDc.getItems()) {
            if (participant.getQrCode() == null || participant.getQrCode().length == 0) {
                try {
                    byte[] qrCode = eventQrCodeService.generateForParticipant(
                            getEditedEntity(),
                            participant.getUser()
                    );
                    participant.setQrCode(qrCode);
                    generatedCount++;
                } catch (Exception e) {
                    log.error("Ошибка генерации QR-кода", e);
                    notifications.create()
                            .withCaption("Ошибка генерации QR-кода")
                            .withDescription(e.getMessage())
                            .withType(Notifications.NotificationType.ERROR)
                            .show();
                }
            }
        }

        if (generatedCount > 0) {
            notifications.create()
                    .withCaption("QR-коды сгенерированы")
                    .withDescription("Создано QR-кодов: " + generatedCount)
                    .show();
        } else {
            notifications.create()
                    .withCaption("Все QR-коды уже сгенерированы")
                    .show();
        }
    }

    @Subscribe("showQrBtn")
    public void onShowQrBtnClick(Button.ClickEvent event) {
        EventParticipant selected = participantsTable.getSingleSelected();
        if (selected == null) {
            return;
        }

        if (selected.getQrCode() == null || selected.getQrCode().length == 0) {
            notifications.create()
                    .withCaption("QR-код еще не сгенерирован")
                    .withDescription("Нажмите 'Сгенерировать QR' для создания QR-кода")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        openQrCodeDialog(selected);
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        EventRequest eventRequest = getEditedEntity();
        for (EventParticipant participant : participantsDc.getItems()) {
            participant.setEventRequest(eventRequest);
        }
    }

    @Subscribe
    public void onAfterCommitChanges(AfterCommitChangesEvent event) {
        EventRequest eventRequest = getEditedEntity();
        if (eventRequest.getId() != null) {
            participantsDl.setParameter("eventId", eventRequest.getId());
            participantsDl.load();
        }
        getScreenData().loadAll();
    }

    @Subscribe("downloadQrBtn")
    public void onDownloadQrBtnClick(Button.ClickEvent event) {
        EventParticipant selected = participantsTable.getSingleSelected();
        if (selected == null) {
            notifications.create()
                    .withCaption("Выберите участника")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        if (selected.getQrCode() == null || selected.getQrCode().length == 0) {
            notifications.create()
                    .withCaption("QR-код еще не сгенерирован")
                    .withDescription("Нажмите 'Сгенерировать QR' для создания QR-кода")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        downloadQrCode(selected);
    }

    private void downloadQrCode(EventParticipant participant) {
        try {
            byte[] qrBytes = participant.getQrCode();
            User user = participant.getUser();
            String fileName = "qr_code.png";
            if (user != null) {
                StringBuilder name = new StringBuilder("qr_");
                if (user.getLastName() != null) name.append(user.getLastName());
                if (user.getFirstName() != null) {
                    if (user.getLastName() != null) name.append("_");
                    name.append(user.getFirstName());
                }
                fileName = name.toString() + ".png";
            }
            downloadViaJavaScript(qrBytes, fileName);
        } catch (Exception e) {
            log.error("Ошибка скачивания QR-кода", e);
            notifications.create()
                    .withCaption("Ошибка скачивания QR-кода")
                    .withDescription(e.getMessage())
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }
    }

    private void downloadViaJavaScript(byte[] qrBytes, String fileName) {
        try {
            String base64 = java.util.Base64.getEncoder().encodeToString(qrBytes);
            final String originalFileName = fileName;
            String escapedFileName = fileName.replace("'", "\\'");
            String jsCode = "(function() {" +
                    "  var link = document.createElement('a');" +
                    "  link.href = 'data:image/png;base64," + base64 + "';" +
                    "  link.download = '" + escapedFileName + "';" +
                    "  document.body.appendChild(link);" +
                    "  link.click();" +
                    "  document.body.removeChild(link);" +
                    "})()";

            com.haulmont.cuba.web.AppUI ui = com.haulmont.cuba.web.AppUI.getCurrent();
            if (ui != null) {
                ui.access(() -> {
                    com.vaadin.ui.JavaScript javascript = com.vaadin.ui.JavaScript.getCurrent();
                    if (javascript != null) {
                        javascript.execute(jsCode);
                        notifications.create()
                                .withCaption("QR-код готов к скачиванию")
                                .withDescription("Файл: " + originalFileName)
                                .show();
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка скачивания через JavaScript: " + e.getMessage(), e);
        }
    }

    private void openQrCodeDialog(EventParticipant participant) {
        byte[] qrBytes = participant.getQrCode();
        Qrcodedialog dialog = screenBuilders.screen(this)
                .withScreenClass(Qrcodedialog.class)
                .withOpenMode(OpenMode.DIALOG)
                .build();

        User user = participant.getUser();
        String caption = "QR-код участника";
        if (user != null) {
            caption = "QR-код: " + user.getLastName() + " " + user.getFirstName();
        }
        dialog.getWindow().setCaption(caption);
        dialog.addAfterShowListener(e -> dialog.setQrCode(qrBytes));
        dialog.show();
    }

    @Subscribe("sendEmailBtn")
    public void onSendEmailBtnClick(Button.ClickEvent event) {
        EventRequest eventRequest = getEditedEntity();

        if (eventRequest.getParticipants() == null || eventRequest.getParticipants().isEmpty()) {
            notifications.create()
                    .withCaption("Нет участников для отправки email")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        boolean missingQr = eventRequest.getParticipants().stream()
                .anyMatch(p -> p.getQrCode() == null || p.getQrCode().length == 0);

        if (missingQr) {
            notifications.create()
                    .withCaption("Не все QR-коды сгенерированы")
                    .withDescription("Сначала нажмите 'Сгенерировать QR'")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        try {
            eventEmailService.sendEmailsToParticipants(eventRequest);

            notifications.create()
                    .withCaption("Письма отправлены")
                    .withDescription("Email отправлен всем участникам")
                    .show();

        } catch (Exception e) {
            notifications.create()
                    .withCaption("Ошибка при отправке email")
                    .withDescription(e.getMessage())
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }
    }

    @Subscribe("processQrBtn")
    public void onProcessQrBtnClick(Button.ClickEvent event) {
        if (qrFileUpload.getValue() == null) {
            notifications.create()
                    .withCaption("Ошибка")
                    .withDescription("Выберите файл с QR-кодом")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        try {
            byte[] bytes = qrFileUpload.getBytes();
            String qrText = decodeQrFromBytes(bytes);
            processQrText(qrText);
        } catch (Exception e) {
            log.error("Ошибка обработки QR-кода", e);
            notifications.create()
                    .withCaption("Ошибка")
                    .withDescription(e.getMessage())
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }
    }

    private String decodeQrFromBytes(byte[] bytes) throws Exception {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    private void processQrText(String qrText) {
        UUID userId;
        try {
            userId = extractUserId(qrText);
        } catch (Exception e) {
            notifications.create()
                    .withCaption("Ошибка QR-кода")
                    .withDescription("Не удалось определить пользователя")
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
            return;
        }

        String codMero;
        try {
            codMero = extractCodMero(qrText);
        } catch (Exception e) {
            notifications.create()
                    .withCaption("Ошибка QR-кода")
                    .withDescription("Не удалось определить мероприятие")
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
            return;
        }

        User user = dataManager.load(User.class)
                .id(userId)
                .optional()
                .orElse(null);

        if (user == null) {
            notifications.create()
                    .withCaption("Пользователь не найден")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        EventRequest eventRequest = getEditedEntity();

        EventParticipant participant = participantsDc.getItems().stream()
                .filter(p -> p.getUser() != null && p.getUser().getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (!codMero.equals(eventRequest.getEventCode())) {
            notifications.create()
                    .withCaption("Неверное мероприятие")
                    .withDescription("Этот QR-код относится к другому мероприятию")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        if (participant == null) {
            notifications.create()
                    .withCaption("Пользователь не является участником")
                    .withDescription(user.getLastName() + " " + user.getFirstName())
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        notifications.create()
                .withCaption("Участник найден")
                .withDescription(user.getLastName() + " " + user.getFirstName())
                .show();
    }

    private UUID extractUserId(String qrText) {
        for (String line : qrText.split("\n")) {
            if (line.startsWith("UUID пользователя:")) {
                return UUID.fromString(line.substring("UUID пользователя:".length()).trim());
            }
        }
        throw new IllegalArgumentException("USER_ID not found in QR");
    }

    private String extractCodMero(String qrText) {
        for (String line : qrText.split("\n")) {
            if (line.startsWith("Код мероприятия:")) {
                return line.substring("Код мероприятия:".length()).trim();
            }
        }
        throw new IllegalArgumentException("Код мероприятия not found in QR");
    }

    @Subscribe("showExternalQrBtn")
    public void onShowExternalQrBtnClick(Button.ClickEvent event) {
        EventExternalParticipant selected = externalParticipantsTable.getSingleSelected();
        if (selected == null) {
            return;
        }

        if (selected.getQrCode() == null || selected.getQrCode().length == 0) {
            notifications.create()
                    .withCaption("QR-код еще не сгенерирован")
                    .withDescription("Нажмите 'Сгенерировать QR' для создания QR-кода")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        openExternalQrCodeDialog(selected);
    }

    private void openExternalQrCodeDialog(EventExternalParticipant participant) {
        byte[] qrBytes = participant.getQrCode();
        Qrcodedialog dialog = screenBuilders.screen(this)
                .withScreenClass(Qrcodedialog.class)
                .withOpenMode(OpenMode.DIALOG)
                .build();

        ExternalGuest guest = participant.getGuest();
        String caption = "QR-код гостя";
        if (guest != null) {
            caption = "QR-код: " + guest.getLastName() + " " + guest.getFirstName();
        }
        dialog.getWindow().setCaption(caption);
        dialog.addAfterShowListener(e -> dialog.setQrCode(qrBytes));
        dialog.show();
    }

    @Subscribe("downloadExternalQrBtn")
    public void onDownloadExternalQrBtnClick(Button.ClickEvent event) {
        EventExternalParticipant selected = externalParticipantsTable.getSingleSelected();
        if (selected == null) {
            notifications.create()
                    .withCaption("Выберите гостя")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        if (selected.getQrCode() == null || selected.getQrCode().length == 0) {
            notifications.create()
                    .withCaption("QR-код еще не сгенерирован")
                    .withDescription("Нажмите 'Сгенерировать QR' для создания QR-кода")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        downloadExternalQrCode(selected);
    }

    private void downloadExternalQrCode(EventExternalParticipant participant) {
        try {
            byte[] qrBytes = participant.getQrCode();
            ExternalGuest guest = participant.getGuest();
            String fileName = "qr_code.png";
            if (guest != null) {
                StringBuilder name = new StringBuilder("qr_guest_");
                if (guest.getLastName() != null) name.append(guest.getLastName());
                if (guest.getFirstName() != null) {
                    if (guest.getLastName() != null) name.append("_");
                    name.append(guest.getFirstName());
                }
                fileName = name.toString() + ".png";
            }
            downloadViaJavaScript(qrBytes, fileName);
        } catch (Exception e) {
            log.error("Ошибка скачивания QR-кода гостя", e);
            notifications.create()
                    .withCaption("Ошибка скачивания QR-кода")
                    .withDescription(e.getMessage())
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }
    }

    @Subscribe("processExternalQrBtn")
    public void onProcessExternalQrBtnClick(Button.ClickEvent event) {
        if (externalQrFileUpload.getValue() == null) {
            notifications.create()
                    .withCaption("Ошибка")
                    .withDescription("Выберите файл с QR-кодом")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        try {
            byte[] bytes = externalQrFileUpload.getBytes();
            String qrText = decodeQrFromBytes(bytes);
            processExternalQrText(qrText);
        } catch (Exception e) {
            log.error("Ошибка обработки QR-кода гостя", e);
            notifications.create()
                    .withCaption("Ошибка")
                    .withDescription(e.getMessage())
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }
    }

    private void processExternalQrText(String qrText) {
        UUID guestId;
        try {
            guestId = extractGuestId(qrText);
        } catch (Exception e) {
            notifications.create()
                    .withCaption("Ошибка QR-кода")
                    .withDescription("Не удалось определить гостя")
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
            return;
        }

        String eventCode;
        try {
            eventCode = extractExternalEventCode(qrText);
        } catch (Exception e) {
            notifications.create()
                    .withCaption("Ошибка QR-кода")
                    .withDescription("Не удалось определить мероприятие")
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
            return;
        }

        ExternalGuest guest = dataManager.load(ExternalGuest.class)
                .id(guestId)
                .optional()
                .orElse(null);

        if (guest == null) {
            notifications.create()
                    .withCaption("Гость не найден")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        EventRequest eventRequest = getEditedEntity();

        EventExternalParticipant participant = externalParticipantsDc.getItems().stream()
                .filter(p -> p.getGuest() != null && p.getGuest().getId().equals(guestId))
                .findFirst()
                .orElse(null);

        if (!eventCode.equals(eventRequest.getEventCode())) {
            notifications.create()
                    .withCaption("Неверное мероприятие")
                    .withDescription("Этот QR-код относится к другому мероприятию")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        if (participant == null) {
            notifications.create()
                    .withCaption("Гость не является участником")
                    .withDescription(guest.getLastName() + " " + guest.getFirstName())
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }

        notifications.create()
                .withCaption("Гость найден")
                .withDescription(guest.getLastName() + " " + guest.getFirstName())
                .show();
    }

    private UUID extractGuestId(String qrText) {
        for (String line : qrText.split("\n")) {
            if (line.startsWith("UUID гостя:")) {
                return UUID.fromString(line.substring("UUID гостя:".length()).trim());
            }
        }
        throw new IllegalArgumentException("Guest ID not found in QR");
    }

    private String extractExternalEventCode(String qrText) {
        for (String line : qrText.split("\n")) {
            if (line.startsWith("Код мероприятия:")) {
                return line.substring("Код мероприятия:".length()).trim();
            }
        }
        throw new IllegalArgumentException("Код мероприятия not found in QR");
    }
}