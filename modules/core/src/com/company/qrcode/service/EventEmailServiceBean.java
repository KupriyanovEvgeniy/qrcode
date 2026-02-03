package com.company.qrcode.service;

import com.company.qrcode.entity.EventParticipant;
import com.company.qrcode.entity.EventRequest;
import com.company.qrcode.service.EventQrCodeService;
import com.haulmont.cuba.core.app.Emailer;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EmailInfo;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Base64;

@Service(EventEmailService.NAME)
public class EventEmailServiceBean implements EventEmailService {

    @Inject
    private Emailer emailer;

    @Inject
    private EventQrCodeService eventQrCodeService;

    @Inject
    private DataManager dataManager;

    @Override
    public void sendEmailsToParticipants(EventRequest event) {
        for (EventParticipant participant : event.getParticipants()) {
            if (participant.getUser() == null || participant.getUser().getEmail() == null) {
                continue;
            }

            try {
                // Генерируем QR-код, если нет
                if (participant.getQrCode() == null || participant.getQrCode().length == 0) {
                    byte[] qrBytes = eventQrCodeService.generateForParticipant(event, participant.getUser());
                    participant.setQrCode(qrBytes);
                    dataManager.commit(participant);
                }

                String base64Qr = Base64.getEncoder().encodeToString(participant.getQrCode());

                String htmlBody = "<h3>Приглашение на мероприятие</h3>"
                        + "<p>Название: " + event.getEventName() + "</p>"
                        + "<p>Дата: " + event.getEventDate() + "</p>"
                        + "<p>Время: " + event.getTimeStart() + " - " + event.getTimeEnd() + "</p>"
                        + "<p>Зал: " + (event.getEventHall() != null ? event.getEventHall().getName() : "") + "</p>"
                        + "<p>Ваш QR-код:</p>"
                        + "<img src='data:image/png;base64," + base64Qr + "' alt='QR-код'/>";

                EmailInfo emailInfo = new EmailInfo(
                        participant.getUser().getEmail(),
                        "Приглашение на " + event.getEventName(),
                        htmlBody
                );

                // Отправка письма (HTML-письмо будет автоматически распознано)
                emailer.sendEmail(emailInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
