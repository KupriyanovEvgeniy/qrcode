/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.eventrequest;

import com.company.qrcode.entity.EventParticipant;
import com.company.qrcode.entity.EventRequest;
import com.company.qrcode.service.EventCodeService;
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

import com.company.qrcode.entity.EventParticipant;
import com.company.qrcode.entity.EventRequest;
import com.company.qrcode.service.EventCodeService;
import com.company.qrcode.service.EventEmailService;
import com.company.qrcode.service.EventQrCodeService;
import com.company.qrcode.web.ui.Qrcodedialog;
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
import com.vaadin.annotations.JavaScript;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@UiController("qrcode$EventRequest.edit")
@UiDescriptor("event-request-edit.xml")
@EditedEntityContainer("eventRequestDc")
@LoadDataBeforeShow
@JavaScript({"scripts/qr-scanner.js",
        "https://unpkg.com/@zxing/browser@latest"})
public class EventRequestEdit extends StandardEditor<EventRequest> {

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

        // Генерируем QR-коды для всех участников
        for (EventParticipant participant : participantsDc.getItems()) {
            if (participant.getQrCode() == null || participant.getQrCode().length == 0) {
                try {
                    // Используем ваш сервис
                    byte[] qrCode = eventQrCodeService.generateForParticipant(
                            getEditedEntity(),
                            participant.getUser()
                    );
                    participant.setQrCode(qrCode);
                    generatedCount++;

                    // Для отладки
                    System.out.println("Generated QR code for " +
                            (participant.getUser() != null ?
                                    participant.getUser().getLastName() : "unknown") +
                            ", size: " + (qrCode != null ? qrCode.length : 0) + " bytes");

                } catch (Exception e) {
                    e.printStackTrace(); // Для отладки
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

        // Открываем QR-код в диалоговом окне
        openQrCodeDialog(selected);
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        // Убеждаемся, что все участники привязаны к мероприятию
        EventRequest eventRequest = getEditedEntity();
        for (EventParticipant participant : participantsDc.getItems()) {
            participant.setEventRequest(eventRequest);
        }
    }

    @Subscribe
    public void onAfterCommitChanges(AfterCommitChangesEvent event) {
        // После сохранения перезагружаем участников
        EventRequest eventRequest = getEditedEntity();
        if (eventRequest.getId() != null) {
            participantsDl.setParameter("eventId", eventRequest.getId());
            participantsDl.load();
        }

        // Загружаем обновленное мероприятие
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

        // Скачиваем QR-код
        downloadQrCode(selected);
    }

    private void downloadQrCode(EventParticipant participant) {
        try {
            byte[] qrBytes = participant.getQrCode();

            // Создаем имя файла
            User user = participant.getUser();
            String fileName = "qr_code.png";
            if (user != null) {
                StringBuilder name = new StringBuilder("qr_");
                if (user.getLastName() != null) {
                    name.append(user.getLastName());
                }
                if (user.getFirstName() != null) {
                    if (user.getLastName() != null) name.append("_");
                    name.append(user.getFirstName());
                }
                fileName = name.toString() + ".png";
            }

            // Скачиваем через JavaScript
            downloadViaJavaScript(qrBytes, fileName);

        } catch (Exception e) {
            e.printStackTrace();
            notifications.create()
                    .withCaption("Ошибка скачивания QR-кода")
                    .withDescription(e.getMessage())
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }
    }

    /**
     * Скачивание через JavaScript (самый простой способ)
     */
    private void downloadViaJavaScript(byte[] qrBytes, String fileName) {
        try {
            // Конвертируем в base64
            String base64 = java.util.Base64.getEncoder().encodeToString(qrBytes);

            // Сохраняем оригинальное имя файла для уведомления
            final String originalFileName = fileName;

            // Экранируем кавычки в имени файла для JavaScript
            String escapedFileName = fileName.replace("'", "\\'");

            // Создаем JavaScript код для скачивания
            String jsCode =
                    "(function() {" +
                            "  var link = document.createElement('a');" +
                            "  link.href = 'data:image/png;base64," + base64 + "';" +
                            "  link.download = '" + escapedFileName + "';" +
                            "  document.body.appendChild(link);" +
                            "  link.click();" +
                            "  document.body.removeChild(link);" +
                            "})()";

            // Получаем доступ к JavaScript интерфейсу Vaadin
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

    /**
     * Открывает диалоговое окно с QR-кодом
     */
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
            byte[] bytes = qrFileUpload.getBytes(); // загруженный файл в байтах
            String qrText = decodeQrFromBytes(bytes);
            processQrText(qrText);

        } catch (Exception e) {
            e.printStackTrace();
            notifications.create()
                    .withCaption("Ошибка")
                    .withDescription(e.getMessage())
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }
    }

    // Декодирование QR из байтов
    private String decodeQrFromBytes(byte[] bytes) throws Exception {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);
        System.out.println(result.getText());
        return result.getText();
    }

    private void processQrText(String qrText) {

        // 1️⃣ Парсим USER_ID из QR
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
                    .withDescription("Не удалось определить пользователя")
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
            return;
        }

        // 2️⃣ Ищем пользователя в БД
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

        // 3️⃣ Проверяем участие в мероприятии
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

        // 4️⃣ УСПЕХ
        notifications.create()
                .withCaption("Участник найден")
                .withDescription(user.getLastName() + " " + user.getFirstName())
                .show();

        // 🔜 Здесь потом:
        // participant.setVisited(true);
        // dataContext.merge(participant);
    }

    private UUID extractUserId(String qrText) {
        // Ожидаем формат: USER_ID=uuid
        for (String line : qrText.split("\n")) {
            if (line.startsWith("UUID пользователя:")) {
                return UUID.fromString(line.substring("UUID пользователя:".length()).trim());
            }
        }
        throw new IllegalArgumentException("USER_ID not found in QR");
    }

    private String extractCodMero(String qrText) {
        // Ожидаем формат: USER_ID=uuid
        for (String line : qrText.split("\n")) {
            if (line.startsWith("Код мероприятия:")) {
                return line.substring("Код мероприятия:".length()).trim();
            }
        }
        throw new IllegalArgumentException("Код мероприятия not found in QR");
    }
}