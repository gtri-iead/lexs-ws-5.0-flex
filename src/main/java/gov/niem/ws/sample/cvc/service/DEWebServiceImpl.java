package gov.niem.ws.sample.cvc.service;

import gov.justice.lexs.meta._5.MessageMetadataElement;
import gov.justice.lexs.meta._5.ObjectFactory;
import gov.lexs.ws.flex.de.jaxb.msg.DomainActionElement;
import gov.lexs.ws.flex.de.jaxb.msg.DomainRequestElement;
import gov.lexs.ws.flex.de.jaxb.msg.DomainResponseElement;
import gov.lexs.ws.flex.jaxws.*;
import gov.niem.ws.sample.cvc.service.util.ServiceUtil;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.bind.*;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

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

@WebService(serviceName = "DEWebService",
portName = "DEWebServicePort",
endpointInterface = "gov.lexs.ws.flex.jaxws.DEWebServicePortType",
targetNamespace = "http://lexs.justice.gov/domainexchange/5.0/ws",
wsdlLocation = "WEB-INF/wsdl/lexsDEwebserviceImpl.wsdl")
public class DEWebServiceImpl implements DEWebServicePortType {

    /*========================================================================*/
    /* CONSTANTS */
    /*========================================================================*/
    private static final String META_OBJECT_FACTORY_PACKAGE = "gov.justice.lexs.meta._5";
    private static final String OBJECT_FACTORY_PACKAGE = "gov.lexs.ws.flex.de.jaxb.msg";
    private static final String RESPONSE_PATH = "samples/domainResponse.xml";

    /*========================================================================*/
    /* STATIC VARIABLES */
    /*========================================================================*/
    private static final Logger logger = Logger.getLogger(DEWebServiceImpl.class.getName());

    /*========================================================================*/
    /* INJECTED VARIABLES */
    /*========================================================================*/
    @Resource
    private WebServiceContext context;

    /*========================================================================*/
    /* PRIVATE VARIABLES */
    /*========================================================================*/
    private JAXBContext jaxbContext;

    /*========================================================================*/
    /* CONSTRUCTOR */
    /*========================================================================*/
    public DEWebServiceImpl() {
        try {
            jaxbContext = JAXBContext.newInstance(OBJECT_FACTORY_PACKAGE);
            ServiceUtil.createAndConfigureMarshaller(jaxbContext);
        }
        catch(JAXBException e){
            e.printStackTrace();
        }
    }

    /*========================================================================*/
    /* PUBLIC METHODS */
    /*========================================================================*/
    @Override
    public void domainAction(DomainActionElement parameters) {
        logger.log(Level.INFO, "Executing domainAction Service");
        String incomingMetadata = ServiceUtil.getMetadataAsString(OBJECT_FACTORY_PACKAGE, "Request LEXS Metadata", parameters);
        logger.log(Level.INFO, "Incoming Metadata " + incomingMetadata);
        //TODO process message here
        ServiceUtil.printMessagePart(OBJECT_FACTORY_PACKAGE, "Domain Action", parameters, System.out);
    }

    /**
     *
     * @param parameters the DomainRequest to process
     * @return the response message
     * @throws BusinessRuleNotMetFault
     * @throws InvalidRequestFault
     * @throws NetworkFailureFault
     * @throws OtherErrorFault
     * @throws TimeoutFault
     */
    @Override
    public DomainResponseElement domainExchange(DomainRequestElement parameters) throws BusinessRuleNotMetFault, InvalidRequestFault, NetworkFailureFault, OtherErrorFault, TimeoutFault {
        logger.log(Level.INFO, "Executing domainExchange Service");
        String incomingMetadata = ServiceUtil.getMetadataAsString(OBJECT_FACTORY_PACKAGE, "Request LEXS Metadata", parameters);
        logger.log(Level.INFO, "Incoming Metadata [" + incomingMetadata + "]");
        ServiceUtil.printMessagePart(OBJECT_FACTORY_PACKAGE, "Domain Request", parameters, System.out);

        //TODO process message here

        gov.lexs.ws.flex.de.jaxb.msg.ObjectFactory deObjFactory = new gov.lexs.ws.flex.de.jaxb.msg.ObjectFactory();
        DomainResponseElement domainResponseElement = null;
        logger.log(Level.INFO, "Generating the response");
        try {
            InputStream responseInputStream = ServiceUtil.getResponseFileInputStream(RESPONSE_PATH);
            domainResponseElement = ServiceUtil.unmarshalXml(DomainResponseElement.class, jaxbContext, responseInputStream);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Finished Executing domainExchange Service");
        return domainResponseElement;
    }
}
