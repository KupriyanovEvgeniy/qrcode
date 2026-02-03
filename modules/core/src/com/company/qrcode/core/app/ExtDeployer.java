package com.company.qrcode.core.app;

import com.google.common.collect.Lists;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.Config;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.app.Authenticated;
import com.haulmont.cuba.security.app.EntityLogAPI;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportScreen;
import com.haulmont.thesis.core.app.*;
import com.haulmont.thesis.core.app.filter.FilterDeploymentTools;
import com.haulmont.workflow.core.entity.Proc;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

@Component("qrcode_ExtDeployer")
public class ExtDeployer extends AbstractDeployer implements ExtDeployerMBean {

    public static final String DB_SCRIPTS_MODULE_ALIAS = "50-qrcode";

    protected Log log = LogFactory.getLog(ExtDeployer.class);
    @Inject
    protected EntityLogAPI entityLogAPI;
    @Inject
    protected FilterDeploymentTools filterDeploymentTools;

    public ExtDeployer() {
        createAppContextListener();
    }

    protected void createAppContextListener() {
        AppContext.addListener(new AppContext.Listener() {
            public void applicationStarted() {
                checkForFirstInit();
            }

            public void applicationStopped() {
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    protected void checkForFirstInit() {
        authentication.begin();
        try {
            String initDefaultInFirstStart = persistence.callInTransaction(em -> em.createQuery(
                    "select c.value from sys$Config c " +
                            "where c.name='qrcode.initDefaultInFirstStart'",
                    String.class).getFirstResult());

            if (initDefaultInFirstStart == null || Boolean.parseBoolean(initDefaultInFirstStart)) {
                String edition = AppContext.getProperty("thesis.initDefaultEdition");
                if (StringUtils.isNotBlank(edition) && Integer.parseInt(edition) > 1) {
                    initDefault("init");
                    persistence.runInTransaction(em -> {
                        if (initDefaultInFirstStart == null) {
                            Config config = metadata.create(Config.class);
                            config.setName("qrcode.initDefaultInFirstStart");
                            config.setValue("false");
                            em.persist(config);
                        } else {
                            em.createQuery("update sys$Config c set c.value='false' where c.name='qrcode.initDefaultInFirstStart'",
                                        Config.class).executeUpdate();
                        }
                    });
                }
            }
        } catch (Exception e) {
            log.error("Exception during defaults initialization", e);
        } finally {
            authentication.end();
        }
    }

    @Override
    @Authenticated
    public String initDefault(String password) {
        if (password != null && initDefaultPassword.equals(DigestUtils.md5Hex(password))) {
            try {
                log.info("Init default");
                executeInitScripts();
                initExtensionDocumentsFunctionality();
                initStandardFilters();
                entityLogAPI.invalidateCache();
                initDefaultsForAdditionalStores();
                executeGroovyInitScripts();
                return INIT_SUCESS;
            } catch (Exception e) {
                log.error("Exception during 'initDefault' execution", e);
                throw e;
            }
        }
        return "Error password";
    }

    protected void initDefaultsForAdditionalStores() {}

    protected void executeGroovyInitScripts() {
        try {
            log.info("executeGroovyInitScripts");
            Stores.getAll().forEach(this::executeGroovyInitScripts);
        } catch (Exception e) {
            log.error("Exception during 'executeGroovyInitScripts'", e);
            throw e;
        }
    }

    protected void executeGroovyInitScripts(String storeName) {
        GroovyDbInitializer
                .create()
                .setModuleAlias(DB_SCRIPTS_MODULE_ALIAS)
                .setStoreName(storeName)
                .initialize();
    }

    protected void executeInitScripts() {
        log.info("Execute init scripts");
        String dbDirPath = configuration.getConfig(ServerConfig.class).getDbDir();
        String dbmsType = AppContext.getProperty("cuba.dbmsType");
        File folderDB = new File(dbDirPath + "/" + DB_SCRIPTS_MODULE_ALIAS + "/init/" + dbmsType);
        File[] scripts = folderDB.listFiles();
        if (ArrayUtils.isNotEmpty(scripts)) {
            for (File script : scripts) {
                if (script.getName().contains("init")) {
                    executeSqlScript(script);
                }
            }
        } else {
            log.info("There is no init scripts found");
        }
    }

    public void initExtensionDocumentsFunctionality() {
        log.info("Init extension documents functionality");
        ArrayList<String> procCodes = Lists.newArrayList("Endorsement", "Resolution", "Acquaintance", "Registration");
        ArrayList<String> reportCodes = Lists.newArrayList("EndorsementList");

        String extensionDocuments = AppContext.getProperty("ext.extensionDocuments");
        if (StringUtils.isEmpty(extensionDocuments)) {
            log.info("There is no extension documents found");
            return;
        }

        String[] extDocMetaClasses = StringUtils.split(extensionDocuments, ", \t\n\f\r");
        try(Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            List<Proc> procs = em
                    .createQuery("select p from wf$Proc p where p.code in :procCodes", Proc.class)
                    .setParameter("procCodes", procCodes)
                    .getResultList();

            for (Proc proc : procs) {
                StringBuilder cardTypes = new StringBuilder(proc.getCardTypes());
                for (String metaClass : extDocMetaClasses)
                    if (cardTypes.indexOf(metaClass) == -1)
                        cardTypes.append(metaClass).append(",");

                proc.setCardTypes(cardTypes.toString());
            }

            List<Report> reports = em
                    .createQuery("select r from report$Report r where r.code in :reportCodes", Report.class)
                    .setParameter("reportCodes", reportCodes)
                    .getResultList();

            for (Report report : reports) {
                Report reportFromXml = reportingApi.convertToReport(report.getXml());
                List<ReportScreen> reportScreens = reportFromXml.getReportScreens();

                for (String metaClass : extDocMetaClasses) {
                    final String screenId = metaClass + ".edit";
                    Optional<ReportScreen> oReportScreen = reportScreens
                            .stream()
                            .filter(r -> screenId.equals(r.getScreenId()))
                            .findFirst();

                    if (!oReportScreen.isPresent()) {
                        ReportScreen reportScreen = metadata.create(ReportScreen.class);
                        reportScreen.setReport(reportFromXml);
                        reportScreen.setScreenId(screenId);
                        reportScreens.add(reportScreen);
                    }
                }

                reportFromXml.setReportScreens(reportScreens);
                report.setXml(reportingApi.convertToString(reportFromXml));
            }
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error during initialization of extension documents functionality", e);
        }
        log.info("Extension documents functionality was added to " + extensionDocuments);
    }

    public void initStandardFilters() {
        log.info("Init standard filters");
        String filtersDirPath = AppContext.getProperty("ext.filtersDir");
        if (StringUtils.isEmpty(filtersDirPath)) {
            log.info("'ext.filtersDir' property is not set");
            return;
        }
        File filtersDir = new File(filtersDirPath);
        if (!filtersDir.exists()) {
            log.info("Folders dir " + filtersDirPath + " not found");
            return;
        }
        File[] filters = filtersDir.listFiles((dir, name) -> name.endsWith(".xml"));
        if (!ArrayUtils.isEmpty(filters)) {
            StringBuilder resultMessage = new StringBuilder();
            Arrays.sort(filters);
            createFilterEntities(filters, resultMessage);
            log.info(resultMessage);
        }
    }

    protected void createFilterEntities(File[] filters, StringBuilder resultMessage) {
        try(Transaction tx = persistence.createTransaction()) {
            authentication.begin();
            for (File filterXml : filters) {
                filterDeploymentTools.deployFiltersFromFile(filterXml, resultMessage);
            }
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error during initialization of standard filter", e);
        } finally {
            authentication.end();
        }
    }
}