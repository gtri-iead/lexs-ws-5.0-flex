package gov.niem.ws.sample.cvc.client;

import gov.justice.lexs.meta._5.MessageMetadataElement;
import gov.lexs.ws.flex.de.jaxb.msg.DomainActionElement;
import gov.lexs.ws.flex.de.jaxb.msg.DomainRequestElement;
import gov.lexs.ws.flex.jaxws.DEWebService;
import gov.lexs.ws.flex.jaxws.DEWebServicePortType;
import gov.niem.ws.sample.cvc.service.util.ServiceUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

/*
 * Copyright 2016 Georgia Tech Research Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class DEClient {

    /*================================================================================================================*/
    /* LOGGER */
    /*================================================================================================================*/
    private static final Logger logger = Logger.getLogger(DEClient.class.getName());

    /*================================================================================================================*/
    /* CONSTANTS */
    /*================================================================================================================*/
    public static String wsdlUrl = "http://localhost:8080/lexs-ws-jaxws-service/services/de?wsdl";

    /*================================================================================================================*/
    /* VARIABLES */
    /*================================================================================================================*/
    public gov.lexs.ws.flex.de.jaxb.msg.ObjectFactory deObjectFactory;
    public gov.justice.lexs.meta._5.ObjectFactory lexsMetaObjectFactory;

    /*================================================================================================================*/
    /* PUBLIC METHODS */
    /*================================================================================================================*/
    public void execute() throws MalformedURLException, Exception {
        deObjectFactory = new gov.lexs.ws.flex.de.jaxb.msg.ObjectFactory();
        lexsMetaObjectFactory = new gov.justice.lexs.meta._5.ObjectFactory();

        logger.log(Level.INFO, "Executing DEClient");
        DEWebServicePortType dePort;
        DEWebService deWebService;

        logger.log(Level.INFO, "Configuring WebService");
        deWebService = new DEWebService(new URL(wsdlUrl),
                       new QName("http://lexs.justice.gov/domainexchange/5.0/ws",
                        "DEWebService"));

        dePort = deWebService.getDEWebServicePort();

        //create message to send across
        logger.log(Level.INFO, "Create DomainAction message");
        JAXBContext deContext = JAXBContext.newInstance("gov.lexs.ws.flex.de.jaxb.msg");
        Unmarshaller unmarshaller = deContext.createUnmarshaller();

        //create domain action
        DomainActionElement deDomainActionType = (DomainActionElement) JAXBIntrospector.getValue( unmarshaller.unmarshal(ServiceUtil.getResponseFileInputStream("samples/domainAction.xml")));

        //create domain exchange request
        DomainRequestElement deRequestType = (DomainRequestElement)JAXBIntrospector.getValue( unmarshaller.unmarshal(ServiceUtil.getResponseFileInputStream("samples/domainRequest.xml")));

        //create message metadata
        logger.log(Level.INFO, "Configuring LEXS Message Metadata");
        JAXBContext deMetaContent = JAXBContext.newInstance(gov.justice.lexs.meta._5.ObjectFactory.class);
        Unmarshaller metaUnmarshaller = deMetaContent.createUnmarshaller();
        MessageMetadataElement messageMetadataType = (MessageMetadataElement) JAXBIntrospector.getValue(metaUnmarshaller.unmarshal(ServiceUtil.getResponseFileInputStream("samples/lexsMetadata.xml")));

        //send the message
        logger.log(Level.INFO, "Sending Domain Action");
        dePort.domainAction(deDomainActionType);//deDomainActionType
        logger.log(Level.INFO, "Finished Sending Domain Action");

        logger.log(Level.INFO, "Sending Domain Exchange");
        Holder<MessageMetadataElement> metaHolder = new Holder<>(messageMetadataType);
        dePort.domainExchange(deRequestType);
        logger.log(Level.INFO, "Finished Sending Domain Exchange");

        logger.log(Level.INFO, "Finished Executing DEClient");
    }

    public static void main(String[] args){
        DEClient deClient = new DEClient();
        try {
            deClient.execute();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
