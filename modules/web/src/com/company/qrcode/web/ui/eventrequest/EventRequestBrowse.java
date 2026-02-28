/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui.eventrequest;

import com.company.qrcode.entity.QrCode;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.screen.*;
import com.company.qrcode.entity.EventRequest;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.UUID;

@UiController("qrcode$EventRequest.browse")
@UiDescriptor("event-request-browse.xml")
@LookupComponent("eventRequestsTable")
@LoadDataBeforeShow
public class EventRequestBrowse extends StandardLookup<EventRequest> {
    @Inject
    private Button searchBtn;

    @Inject
    private FileUploadField uploadField;

    @Inject
    private Notifications notifications;

    @Inject
    private DataManager dataManager;

    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe("searchBtn")
    public void onSearchBtnClick(Button.ClickEvent event) {
        if (uploadField.getValue() == null) {
            notifications.create()
                    .withCaption("Ошибка")
                    .withDescription("Выберите файл с QR-кодом")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }
        try {
            byte[] bytes = uploadField.getBytes();
            String qrText = decodeQrFromBytes(bytes);
            UUID qrCodeId;

            try {
                qrCodeId = UUID.fromString(qrText.trim());
            } catch (Exception e) {
                notifications.create().withCaption("Ошибка QR-кода")
                        .withDescription("Не удалось расшифровать QR-код")
                        .withType(Notifications.NotificationType.ERROR)
                        .show();
                return;
            }

            QrCode qrCode = dataManager.load(QrCode.class)
                    .id(qrCodeId)
                    .optional()
                    .orElse(null);

            if (qrCode == null) {
                notifications.create().withCaption("Ошибка QR-кода")
                        .withDescription("Данный QR-код не найден")
                        .withType(Notifications.NotificationType.ERROR)
                        .show();
                return;
            }

            EventRequest eventRequest = dataManager.load(EventRequest.class)
                    .id(qrCode.getEventId())
                    .one();

            screenBuilders.editor(EventRequest.class, this)
                    .editEntity(eventRequest)
                    .show();
        }
        catch (Exception e)
        {
            notifications.create()
                    .withCaption("Ошибка генерации QR-кода")
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
}