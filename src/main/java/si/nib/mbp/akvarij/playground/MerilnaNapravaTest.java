/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.nib.mbp.akvarij.playground;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author Peter
 */
public class MerilnaNapravaTest {

    static String urlNaslov = "http://192.168.1.150";

    public static void main(String[] args) {

        String temperatura;
        String slanost;
        String frekvencaTemperatura;
        String frekvencaSlansot;

        URL url;
        URLConnection con;
        HttpURLConnection http;
        BufferedReader in;

        try {
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(urlNaslov).openStream());
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            
            XPathExpression expr = xpath.compile("/root/temperature/value/text()");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            temperatura = (String)nodes.item(0).getNodeValue().trim();
            
            expr = xpath.compile("/root/temperature/freq/text()");
            nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            frekvencaTemperatura = (String)nodes.item(0).getNodeValue().trim();
            
            expr = xpath.compile("/root/salinity/value/text()");
            nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            slanost = (String)nodes.item(0).getNodeValue().trim();
            
            expr = xpath.compile("/root/salinity/freq/text()");
            nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            frekvencaSlansot = (String)nodes.item(0).getNodeValue().trim();
            
            System.out.println(temperatura);
            System.out.println(frekvencaTemperatura);
            System.out.println(slanost);
            System.out.println(frekvencaSlansot);
            

        } catch (MalformedURLException ex) {
            Logger.getLogger(MerilnaNapravaTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MerilnaNapravaTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MerilnaNapravaTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MerilnaNapravaTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(MerilnaNapravaTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
    }
}
