package si.nib.mbp.akvarij.poc.servlet.listener;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
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

/**
 * @author Peter
 */
public class MerilnaNapravaListener extends DaoDbConnection implements ServletContextListener {
    final String TOMCAT_ENV_CONTEXT = "java:comp/env";
    final String ENV_NAME = "arduinoDataUrl";

    private String urlNaslov = "";

    public MerilnaNapravaListener() {
        try {
            Context ctx = new InitialContext();
            urlNaslov = (String) ctx.lookup(TOMCAT_ENV_CONTEXT + "/" + ENV_NAME);  // from Tomcat's context.xml
        } catch (Exception e) {
            urlNaslov = "http://localhost";
            logger.log(Level.SEVERE, "Cannot find Tomcat ENV variable '" + ENV_NAME + "'", e);
        }
        logger.log(Level.INFO, "Arduino URL = '" + urlNaslov + "'");
    }

    public String getUrlNaslov() {
        return urlNaslov;
    }

    public static final Logger logger = Logger.getLogger(MerilnaNapravaListener.class.getName());

    @Override
    public void contextDestroyed(ServletContextEvent arg) {
        logger.log(Level.INFO, "MerilnaNapravaListener je uničen..");
    }

    private Connection getConnection() {
        if (!connect()) {
            logger.log(Level.WARNING, "Cannot get connection");
        }
        return connection;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.log(Level.INFO, "MerilnaNapravaListener je inicializiran..");
        TimerTask vodTimer = new VodTimerTask();
        Timer timer = new Timer();
        timer.schedule(vodTimer, 1000, (60 * 1000));

    }

    class VodTimerTask extends TimerTask {

        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document doc;
        XPathFactory xpathFactory;
        Connection con;
        PreparedStatement stmt;

        @Override
        public void run() {

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
                con = getConnection();

                stmt = con.prepareCall("insert into met_meritve (temp,sal,freq_cond,freq_temp,cond,dat_vno) values (?,?,?,?,?,?)");
                stmt.setBigDecimal(1, new BigDecimal(temperatura));
                stmt.setBigDecimal(2, new BigDecimal(slanost));
                stmt.setBigDecimal(3, new BigDecimal(frekvencaPrevodnost));
                stmt.setBigDecimal(4, new BigDecimal(frekvencaTemperatura));
                stmt.setBigDecimal(5, new BigDecimal(prevodnost));
                stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                stmt.execute();
                stmt.close();
                con.commit();
                //con.close();

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(MerilnaNapravaListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(MerilnaNapravaListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MerilnaNapravaListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(MerilnaNapravaListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XPathExpressionException ex) {
                Logger.getLogger(MerilnaNapravaListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(MerilnaNapravaListener.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(MerilnaNapravaListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (con != null) {
                    try {
                        con.commit();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(MerilnaNapravaListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }

}