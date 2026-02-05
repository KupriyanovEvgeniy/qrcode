/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@UiController("qrcode_QrScanner")
@UiDescriptor("qr-scanner.xml")
public class QrScanner extends Screen {

    private static final Logger log = LoggerFactory.getLogger(QrScanner.class);

    @Inject
    private Button startBtn;
    @Inject
    private Button stopBtn;
    @Inject
    private Button scanBtn;
    @Inject
    private Button copyBtn;
    @Inject
    private Button clearBtn;
    @Inject
    private Button closeBtn;
    @Inject
    private TextArea<String> resultText;
    @Inject
    private Label<String> scanInfo;
    @Inject
    private Notifications notifications;

    private boolean cameraActive = false;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        updateUIState();

        // Инициализируем обработчик сообщений от JavaScript
        initJsMessageHandler();
    }

    @Subscribe("startBtn")
    public void onStartBtnClick(Button.ClickEvent event) {
        startCamera();
    }

    @Subscribe("stopBtn")
    public void onStopBtnClick(Button.ClickEvent event) {
        stopCamera();
    }

    @Subscribe("scanBtn")
    public void onScanBtnClick(Button.ClickEvent event) {
        captureAndDecode();
    }

    @Subscribe("copyBtn")
    public void onCopyBtnClick(Button.ClickEvent event) {
        copyToClipboard();
    }

    @Subscribe("clearBtn")
    public void onClearBtnClick(Button.ClickEvent event) {
        resultText.setValue("");
        scanInfo.setValue("Ожидание QR-кода...");
    }

    @Subscribe("closeBtn")
    public void onCloseBtnClick(Button.ClickEvent event) {
        stopCamera();
        closeWithDefaultAction();
    }

    /**
     * Инициализация обработчика сообщений от JavaScript
     */
    private void initJsMessageHandler() {
        String js = ""
                + "// Создаем глобальную функцию для получения данных от QR-сканера"
                + "window.receiveQrData = function(data) {"
                + "  console.log('QR данные получены:', data);"
                + "  // Можно добавить визуальную обратную связь"
                + "  if (window.cameraVideo) {"
                + "    window.cameraVideo.style.border = '3px solid #4CAF50';"
                + "    setTimeout(() => {"
                + "      if (window.cameraVideo) {"
                + "        window.cameraVideo.style.border = '3px solid #2196F3';"
                + "      }"
                + "    }, 500);"
                + "  }"
                + "};";

        executeJavaScript(js);
    }

    /**
     * Запуск камеры через JavaScript
     */
    private void startCamera() {
        String js = createCameraJs();

        executeJavaScript(js);
        cameraActive = true;
        updateUIState();
        scanInfo.setValue("Сканирование QR-кодов... Наведите камеру на QR-код");

        notifications.create(Notifications.NotificationType.TRAY)
                .withCaption("Камера запущена")
                .withDescription("Разрешите доступ к камере")
                .show();
    }

    /**
     * Создание JavaScript для камеры с автоматическим сканированием
     */
    private String createCameraJs() {
        return ""
                + "// Останавливаем предыдущую камеру"
                + "if (window.cameraStream) {"
                + "  window.cameraStream.getTracks().forEach(track => track.stop());"
                + "}"

                + "// Удаляем старый overlay"
                + "let oldOverlay = document.getElementById('cameraOverlay');"
                + "if (oldOverlay) {"
                + "  document.body.removeChild(oldOverlay);"
                + "}"

                + "// Создаем overlay для камеры"
                + "let overlay = document.createElement('div');"
                + "overlay.id = 'cameraOverlay';"
                + "overlay.style.position = 'fixed';"
                + "overlay.style.top = '0';"
                + "overlay.style.left = '0';"
                + "overlay.style.width = '100%';"
                + "overlay.style.height = '100%';"
                + "overlay.style.background = 'rgba(0,0,0,0.95)';"
                + "overlay.style.display = 'flex';"
                + "overlay.style.flexDirection = 'column';"
                + "overlay.style.justifyContent = 'center';"
                + "overlay.style.alignItems = 'center';"
                + "overlay.style.zIndex = '9999';"
                + "document.body.appendChild(overlay);"

                + "// Заголовок"
                + "let title = document.createElement('div');"
                + "title.innerText = 'Сканирование QR-кода';"
                + "title.style.color = 'white';"
                + "title.style.fontSize = '24px';"
                + "title.style.marginBottom = '20px';"
                + "overlay.appendChild(title);"

                + "// Контейнер для видео"
                + "let videoContainer = document.createElement('div');"
                + "videoContainer.style.position = 'relative';"
                + "videoContainer.style.width = '640px';"
                + "videoContainer.style.maxWidth = '90%';"
                + "videoContainer.style.height = '480px';"
                + "videoContainer.style.maxHeight = '60vh';"
                + "overlay.appendChild(videoContainer);"

                + "// Элемент video"
                + "let video = document.createElement('video');"
                + "video.id = 'cameraVideo';"
                + "window.cameraVideo = video;"
                + "video.style.width = '100%';"
                + "video.style.height = '100%';"
                + "video.style.border = '3px solid #2196F3';"
                + "video.style.borderRadius = '5px';"
                + "video.autoplay = true;"
                + "video.playsInline = true;"
                + "videoContainer.appendChild(video);"

                + "// Индикатор сканирования"
                + "let scanBox = document.createElement('div');"
                + "scanBox.style.position = 'absolute';"
                + "scanBox.style.top = '50%';"
                + "scanBox.style.left = '50%';"
                + "scanBox.style.transform = 'translate(-50%, -50%)';"
                + "scanBox.style.width = '200px';"
                + "scanBox.style.height = '200px';"
                + "scanBox.style.border = '2px dashed #FF9800';"
                + "scanBox.style.pointerEvents = 'none';"
                + "videoContainer.appendChild(scanBox);"

                + "// Кнопка закрытия"
                + "let closeBtn = document.createElement('button');"
                + "closeBtn.innerText = 'Закрыть камеру';"
                + "closeBtn.style.marginTop = '20px';"
                + "closeBtn.style.padding = '10px 30px';"
                + "closeBtn.style.fontSize = '16px';"
                + "closeBtn.style.background = '#F44336';"
                + "closeBtn.style.color = 'white';"
                + "closeBtn.style.border = 'none';"
                + "closeBtn.style.borderRadius = '3px';"
                + "closeBtn.style.cursor = 'pointer';"
                + "closeBtn.onclick = function() {"
                + "  if (window.cameraStream) {"
                + "    window.cameraStream.getTracks().forEach(track => track.stop());"
                + "    window.cameraStream = null;"
                + "  }"
                + "  document.body.removeChild(overlay);"
                + "  window.cameraVideo = null;"
                + "  if (window.scanningInterval) clearInterval(window.scanningInterval);"
                + "  // Вызываем функцию остановки в CUBA"
                + "  if (window.cubaStopCamera) window.cubaStopCamera();"
                + "};"
                + "overlay.appendChild(closeBtn);"

                + "// Подключаем камеру"
                + "navigator.mediaDevices.getUserMedia({ "
                + "  video: { "
                + "    facingMode: 'environment',"
                + "    width: { ideal: 1280 },"
                + "    height: { ideal: 720 }"
                + "  },"
                + "  audio: false"
                + "})"
                + ".then(function(stream) {"
                + "  window.cameraStream = stream;"
                + "  video.srcObject = stream;"
                + "  "
                + "  // Запускаем сканирование"
                + "  startQrScanning(video);"
                + "})"
                + ".catch(function(err) {"
                + "  alert('Ошибка камеры: ' + err.message);"
                + "  document.body.removeChild(overlay);"
                + "});"

                + "// Функция сканирования QR-кодов"
                + "function startQrScanning(video) {"
                + "  // Загружаем jsQR если нужно"
                + "  if (!window.jsQR) {"
                + "    let script = document.createElement('script');"
                + "    script.src = 'https://cdn.jsdelivr.net/npm/jsqr@1.4.0/dist/jsQR.min.js';"
                + "    script.onload = function() {"
                + "      startScanLoop(video);"
                + "    };"
                + "    document.head.appendChild(script);"
                + "  } else {"
                + "    startScanLoop(video);"
                + "  }"
                + "}"
                + ""
                + "function startScanLoop(video) {"
                + "  let canvas = document.createElement('canvas');"
                + "  canvas.style.display = 'none';"
                + "  document.body.appendChild(canvas);"
                + "  let ctx = canvas.getContext('2d');"
                + "  "
                + "  function scan() {"
                + "    if (!video.videoWidth) {"
                + "      setTimeout(scan, 100);"
                + "      return;"
                + "    }"
                + "    "
                + "    canvas.width = video.videoWidth;"
                + "    canvas.height = video.videoHeight;"
                + "    ctx.drawImage(video, 0, 0, canvas.width, canvas.height);"
                + "    "
                + "    let imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);"
                + "    let code = window.jsQR(imageData.data, imageData.width, imageData.height, {"
                + "      inversionAttempts: 'dontInvert'"
                + "    });"
                + "    "
                + "    if (code) {"
                + "      console.log('QR найден:', code.data);"
                + "      // Отправляем данные в CUBA"
                + "      sendQrToCuba(code.data);"
                + "      video.style.border = '3px solid #4CAF50';"
                + "    } else {"
                + "      video.style.border = '3px solid #2196F3';"
                + "    }"
                + "    "
                + "    requestAnimationFrame(scan);"
                + "  }"
                + "  "
                + "  scan();"
                + "}"
                + ""
                + "// Функция отправки данных в CUBA через Vaadin Push"
                + "function sendQrToCuba(data) {"
                + "  // Способ 1: Через глобальную функцию (проще)"
                + "  if (window.receiveQrData) {"
                + "    window.receiveQrData(data);"
                + "  }"
                + "  "
                + "  // Способ 2: Через обработку на стороне клиента"
                + "  try {"
                + "    // Получаем доступ к Vaadin"
                + "    if (window.parent && window.parent.Vaadin && window.parent.Vaadin.connection) {"
                + "      window.parent.Vaadin.connection.send({"
                + "        'qrData': data"
                + "      });"
                + "    }"
                + "  } catch(e) {"
                + "    console.log('Не удалось отправить через Vaadin:', e);"
                + "  }"
                + "}";
    }

    /**
     * Остановка камеры
     */
    private void stopCamera() {
        String js = ""
                + "// Останавливаем камеру"
                + "if (window.cameraStream) {"
                + "  window.cameraStream.getTracks().forEach(track => track.stop());"
                + "  window.cameraStream = null;"
                + "}"
                + "// Убираем overlay"
                + "let overlay = document.getElementById('cameraOverlay');"
                + "if (overlay) {"
                + "  document.body.removeChild(overlay);"
                + "}"
                + "// Очищаем переменные"
                + "window.cameraVideo = null;";

        executeJavaScript(js);
        cameraActive = false;
        updateUIState();
        scanInfo.setValue("Камера выключена");

        notifications.create(Notifications.NotificationType.TRAY)
                .withCaption("Камера остановлена")
                .show();
    }

    /**
     * Сделать снимок и декодировать
     */
    private void captureAndDecode() {
        if (!cameraActive) {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption("Сначала включите камеру")
                    .show();
            return;
        }

        String js = ""
                + "if (!window.cameraStream) { return 'ERROR: Камера не активна'; }"
                + "let video = document.getElementById('cameraVideo');"
                + "if (!video) { return 'ERROR: Видео не найдено'; }"
                + "let canvas = document.createElement('canvas');"
                + "canvas.width = video.videoWidth;"
                + "canvas.height = video.videoHeight;"
                + "let ctx = canvas.getContext('2d');"
                + "ctx.drawImage(video, 0, 0);"
                + "// Пробуем декодировать QR"
                + "try {"
                + "  let imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);"
                + "  if (window.jsQR) {"
                + "    let code = window.jsQR(imageData.data, imageData.width, imageData.height);"
                + "    if (code) {"
                + "      return code.data;"
                + "    } else {"
                + "      return 'INFO: QR-код не найден на снимке';"
                + "    }"
                + "  } else {"
                + "    return 'ERROR: Библиотека jsQR не загружена';"
                + "  }"
                + "} catch(e) {"
                + "  return 'ERROR: ' + e.message;"
                + "}";

        // В реальном проекте нужно использовать callback или Promise
        // Здесь просто выполним код
        executeJavaScript(js);

        notifications.create(Notifications.NotificationType.TRAY)
                .withCaption("Снимок сделан")
                .withDescription("Проверяю QR-код...")
                .show();
    }

    /**
     * Копирование в буфер обмена
     */
    private void copyToClipboard() {
        String text = resultText.getValue();
        if (text != null && !text.isEmpty()) {
            // Используем JavaScript для копирования
            String escapedText = text.replace("'", "\\'").replace("\n", "\\n");
            String js = ""
                    + "let text = '" + escapedText + "';"
                    + "navigator.clipboard.writeText(text).then(function() {"
                    + "  console.log('Скопировано в буфер');"
                    + "  alert('Текст скопирован в буфер обмена');"
                    + "}, function(err) {"
                    + "  console.error('Ошибка копирования:', err);"
                    + "  // Fallback для старых браузеров"
                    + "  let textarea = document.createElement('textarea');"
                    + "  textarea.value = text;"
                    + "  document.body.appendChild(textarea);"
                    + "  textarea.select();"
                    + "  document.execCommand('copy');"
                    + "  document.body.removeChild(textarea);"
                    + "  alert('Текст скопирован (fallback)');"
                    + "});";

            executeJavaScript(js);

            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption("Текст скопирован")
                    .show();
        } else {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption("Нет текста для копирования")
                    .show();
        }
    }

    /**
     * Обновление состояния UI
     */
    private void updateUIState() {
        startBtn.setEnabled(!cameraActive);
        stopBtn.setEnabled(cameraActive);
        scanBtn.setEnabled(cameraActive);
    }

    /**
     * Выполнение JavaScript кода
     */
    private void executeJavaScript(String js) {
        com.haulmont.cuba.web.AppUI ui = com.haulmont.cuba.web.AppUI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                try {
                    com.vaadin.ui.JavaScript.getCurrent().execute(js);
                } catch (Exception e) {
                    log.error("Ошибка выполнения JavaScript", e);
                }
            });
        }
    }

    /**
     * Получение QR данных из JavaScript
     * Этот метод вызывается из JavaScript через window.receiveQrData
     */
    public void receiveQrDataFromJs(String qrData) {
        // Этот метод будет вызываться из JavaScript
        // Обновляем UI в UI потоке
        com.haulmont.cuba.web.AppUI ui = com.haulmont.cuba.web.AppUI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                resultText.setValue(qrData);
                scanInfo.setValue("QR-код распознан: " +
                        (qrData.length() > 30 ? qrData.substring(0, 30) + "..." : qrData));

                notifications.create(Notifications.NotificationType.HUMANIZED)
                        .withCaption("QR-код распознан")
                        .show();

                // Автоматически останавливаем камеру после успешного сканирования
                stopCamera();

                // Здесь можно добавить логику для работы с QR-кодом
                // Например, поиск участника по QR-коду
                processScannedQrCode(qrData);
            });
        }
    }

    /**
     * Обработка распознанного QR-кода
     */
    private void processScannedQrCode(String qrData) {
        // Пример: если QR-код содержит ID участника
        if (qrData.contains("Участник:")) {
            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption("Найден участник")
                    .withDescription("Обработка данных участника...")
                    .show();
        }

        // TODO: Добавить вашу логику обработки QR-кодов
        // Например, поиск в базе данных, обновление статуса и т.д.
    }
}