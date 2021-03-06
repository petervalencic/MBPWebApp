package si.nib.mbp.akvarij.poc.servlet.listener;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import si.nib.mbp.akvarij.poc.dao.DaoDbConnection;

public class MerilnaNapravaListener implements ServletContextListener {

    final String TOMCAT_ENV_CONTEXT = "java:comp/env";
    final String ENV_NAME = "arduinoDataUrl";
    final int INTERVAL = 10 * 60 * 1000;
    final int INTERVAL_SERVIS = 10 * 1000;

    ServletContext servletContext;

    private String urlNaslov = "";

    public MerilnaNapravaListener() {
        //this.dao = new DaoDbConnection();
        try {
            Context ctx = new InitialContext();
            urlNaslov = (String) ctx.lookup(TOMCAT_ENV_CONTEXT + "/" + ENV_NAME);  // from Tomcat's context.xml
        } catch (Exception e) {
            urlNaslov = "http://localhost";
            logger.log(Level.SEVERE, "Cannot find Tomcat ENV variable '" + ENV_NAME + "'", e);
        }
        logger.log(Level.INFO, "Arduino URL = ''{0}''", urlNaslov);
    }

    public String getUrlNaslov() {
        return urlNaslov;
    }

    public static final Logger logger = Logger.getLogger(MerilnaNapravaListener.class.getName());

    @Override
    public void contextDestroyed(ServletContextEvent arg) {
        logger.log(Level.INFO, "MerilnaNapravaListener je uničen..");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.log(Level.INFO, "MerilnaNapravaListener je inicializiran..");

        servletContext = servletContextEvent.getServletContext();

        TimerTask podatkiTimer = new ZapisPodatkovTimerTask();
        Timer timer = new Timer();
        timer.schedule(podatkiTimer, 1000, INTERVAL);

        TimerTask cachePodatkiTimer = new CacheServisTask();
        Timer timerCache = new Timer();
        timerCache.schedule(cachePodatkiTimer, 1000, INTERVAL_SERVIS);

    }

    private String preberiPodatkeURL(String reqURL) throws IOException {
        try (Scanner scanner = new Scanner(new URL(reqURL).openStream(),
                StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    class CacheServisTask extends TimerTask {

        @Override
        public void run() {
            logger.log(Level.INFO, "CacheServisTask start:{0}", getUrlNaslov());
            if (servletContext == null) {
                return;
            }
            String podatki;
            try {
                podatki = preberiPodatkeURL(urlNaslov);
            } catch (Exception ex) {
                podatki = "<xml><root><temp><value>---</value><freq>0</freq></temp><sal><value>---</value><freq>0</freq></sal><cond><value>---</value><freq>0</freq></cond></root></xml>";
            }
            servletContext.setAttribute("ARDUINO_DATA", podatki);
        }
    }

    class ZapisPodatkovTimerTask extends TimerTask {

        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc;
        XPathFactory xpathFactory;

        @Override
        public void run() {
            logger.log(Level.INFO, "ZapisPodatkovTimerTask start");
            Connection connection = null;
            PreparedStatement stmt = null;
            String temperatura;
            String slanost;
            String prevodnost;
            String frekvencaTemperatura;
            String frekvencaPrevodnost;

            logger.log(Level.INFO, "Klic merilne naprave na IP:{0}", getUrlNaslov());
            try {

                dbf = DocumentBuilderFactory.newInstance();
                db = dbf.newDocumentBuilder();
                doc = db.parse(new URL(getUrlNaslov()).openStream());
                xpathFactory = XPathFactory.newInstance();
                XPath xpath = xpathFactory.newXPath();

                XPathExpression expr = xpath.compile("/root/temp/value/text()");
                NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                temperatura = nodes.item(0).getNodeValue().trim();

                expr = xpath.compile("/root/temp/freq/text()");
                nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                frekvencaTemperatura = nodes.item(0).getNodeValue().trim();

                expr = xpath.compile("/root/cond/value/text()");
                nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                prevodnost = nodes.item(0).getNodeValue().trim();

                expr = xpath.compile("/root/cond/freq/text()");
                nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                frekvencaPrevodnost = nodes.item(0).getNodeValue().trim();

                expr = xpath.compile("/root/sal/value/text()");
                nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                slanost = nodes.item(0).getNodeValue().trim();

                logger.log(Level.INFO, "Temperatura: {0}", temperatura);
                logger.log(Level.INFO, "Slanost: {0}", slanost);
                logger.log(Level.INFO, "Frek. temp: {0}", frekvencaTemperatura);
                logger.log(Level.INFO, "Frek. prevodnost: {0}", frekvencaPrevodnost);
                logger.log(Level.INFO, "Prevodnost: {0}", prevodnost);

                //podatke zapišemo v bazo..
                DaoDbConnection dao = new DaoDbConnection();
                connection = dao.geConnection();
                connection.setAutoCommit(true);
                stmt = connection.prepareCall("insert into met_meritve (temp,sal,freq_cond,freq_temp,cond,dat_vno) values (?,?,?,?,?,?)");
                stmt.setBigDecimal(1, new BigDecimal(temperatura));
                stmt.setBigDecimal(2, new BigDecimal(slanost));
                stmt.setBigDecimal(3, new BigDecimal(frekvencaPrevodnost));
                stmt.setBigDecimal(4, new BigDecimal(frekvencaTemperatura));
                stmt.setBigDecimal(5, new BigDecimal(prevodnost));
                stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                stmt.execute();
                stmt.close();
                stmt = null;
                connection.close();
                connection = null;

            } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException | SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                    stmt = null;
                }

                if (connection != null) {
                    try {
                        connection.close();

                    } catch (SQLException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                    connection = null;
                }
            }

        }
    }

}
