package com.company.qrcode;

import com.haulmont.thesis.testsupport.ThesisTestContainer;
import org.junit.jupiter.api.extension.ExtensionContext;

public class QrcodeTestContainer extends ThesisTestContainer {

    public QrcodeTestContainer() {
        super();
        // project and special properties for test environment.
        appPropertiesFiles.add("qrcode-app.properties");
        appPropertiesFiles.add("qrcode-test-app.properties");
        autoConfigureDataSource();
    }

    public static class Common extends QrcodeTestContainer {

        // A common singleton instance of the test container which is initialized once for all tests
        public static final QrcodeTestContainer.Common INSTANCE = new QrcodeTestContainer.Common();

        private static volatile boolean initialized;

        private Common() {}

        @Override
        public void beforeAll(ExtensionContext extensionContext) throws Exception {
            if (!initialized) {
                super.beforeAll(extensionContext);
                initialized = true;
            }
            setupContext();
        }

        @SuppressWarnings("RedundantThrows")
        @Override
        public void afterAll(ExtensionContext extensionContext) throws Exception {
            cleanupContext();
           // never stops - do not call super
        }
    }
}