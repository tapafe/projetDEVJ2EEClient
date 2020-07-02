/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exia.textebusiness.ejb;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/**
 *
 * @author Arnaud RIGAUT
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/textQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})

public class textProcessor implements MessageListener {

    public textProcessor(){
        
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            //on extrait le paiment du corps du message. - getBody est une méthode JMS 2.0
            String textMessage = message.getBody(String.class);
            Document doc = convertStringToXMLDocument(textMessage);
            String text = doc.getElementsByTagName("data").item(0).getTextContent();
            DBConnect db = new DBConnect();
            System.out.println(confiance(text));
            
        } catch (JMSException ex) {
            Logger.getLogger(textProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Document convertStringToXMLDocument(String xmlString) 
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    
    private static double confiance(String text){
        
        //DBConnect db = new DBConnect();
        
        ArrayList<String> word = new ArrayList<>();
        ArrayList<String> dico = new ArrayList<>();
        //dico = db.request();

        Scanner scanner = new Scanner(text);
        while (scanner.hasNext() == true) {
            String s = scanner.next();
            word.add(s);
            dico.add(s);
        }

        word = new ArrayList<>(new HashSet<>(word));
        dico = new ArrayList<>(new HashSet<>(dico));
            
        return calc(word, dico);
    }
    
    public static double calc(ArrayList<String> text, ArrayList<String> dico){

            int conf = 0;

            for (String word:text) {
                if (dico.contains(word)){
                    conf += 1;
                }
            }

            int size = text.size();
            double temp = (double) conf/size;     
            
            return (temp * 100);
        }

        private static String deleteAll(String strValue, String charToRemove) {
            return strValue.replaceAll(charToRemove, "");

        }
}