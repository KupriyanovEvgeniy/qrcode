package com.company.qrcode.web.ui;

import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.server.StreamResource;

import java.io.ByteArrayInputStream;

@UiController("qrcode_Qrcodedialog")
@UiDescriptor("QrCodeDialog.xml")
public class Qrcodedialog extends Screen {

    public void setQrCode(byte[] qrBytes) {
        Image cubaImage = (Image) getWindow().getComponent("qrImage");

        if (cubaImage == null || qrBytes == null) {
            return;
        }

        StreamResource resource = new StreamResource(
                () -> new ByteArrayInputStream(qrBytes),
                "qr.png"
        );
        resource.setCacheTime(0);


        com.vaadin.ui.Image vaadinImage =
                (com.vaadin.ui.Image) WebComponentsHelper.unwrap(cubaImage);

        vaadinImage.setSource(resource);
    }
}
