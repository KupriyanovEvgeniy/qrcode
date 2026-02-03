package com.company.qrcode.service;

import com.company.qrcode.entity.EventParticipant;
import com.company.qrcode.entity.EventRequest;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailInfo;
import org.springframework.stereotype.Service;

@Service(EventEmailService.NAME)
public class EventEmailServiceBean implements EventEmailService {

    @Override
    public void sendEmailsToParticipants(EventRequest eventRequest) {

        EmailService emailService = AppBeans.get(EmailService.class);

        if (eventRequest.getParticipants() == null || eventRequest.getParticipants().isEmpty()) {
            return;
        }

        for (EventParticipant participant : eventRequest.getParticipants()) {

            if (participant.getUser() == null || participant.getUser().getEmail() == null) {
                continue;
            }

            String emailTo = participant.getUser().getEmail();
            String subject = "Ваша заявка на событие: " + eventRequest.getEventName();

            // HTML текст письма
            String body = "<html><body>" +
                    "<p>Здравствуйте, " + participant.getUser().getFirstName() + "!</p>" +
                    "<p>Ваша заявка на событие <b>" + eventRequest.getEventName() + "</b> успешно создана.</p>" +
                    "<p>Дата: " + eventRequest.getEventDate() + "</p>" +
                    "<p>Пожалуйста, используйте прикреплённый QR-код для участия.</p>" +
                    "<p>Спасибо!</p>" +
                    "</body></html>";

            // Вложения
            EmailAttachment[] attachments = null;
            if (participant.getQrCode() != null) {
                attachments = new EmailAttachment[]{
                        new EmailAttachment(participant.getQrCode(), "qr-code.png", "image/png")
                };
            }

            // Создаём EmailInfo
            EmailInfo emailInfo = new EmailInfo(emailTo, subject, body);

            // Добавляем вложения, если есть
            if (attachments != null) {
                emailInfo.setAttachments(attachments);
            }

            // Отправляем асинхронно
            emailService.sendEmailAsync(emailInfo);
        }
    }
}
