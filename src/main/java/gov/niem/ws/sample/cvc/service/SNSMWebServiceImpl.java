package gov.niem.ws.sample.cvc.service;

import gov.lexs.ws.flex.jaxws.SubscriptionManager;
import gov.lexs.ws.flex.jaxws.UnableToDestroySubscriptionFault;
import gov.lexs.ws.flex.jaxws.UnacceptableTerminationTimeFault;
import gov.lexs.ws.flex.oasis.wsn.b_2.RenewElement;
import gov.lexs.ws.flex.oasis.wsn.b_2.RenewResponseElement;
import gov.lexs.ws.flex.oasis.wsn.b_2.UnsubscribeElement;
import gov.lexs.ws.flex.oasis.wsn.b_2.UnsubscribeResponseElement;
import gov.niem.ws.sample.cvc.service.util.ServiceUtil;

import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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

@WebService(serviceName = "SNSMWebService",
portName = "SNSMWebServicePort",
endpointInterface = "gov.lexs.ws.flex.jaxws.SubscriptionManager",
targetNamespace = "http://docs.oasis-open.org/wsn/bw-2",
wsdlLocation = "WEB-INF/wsdl/lexsSNSMwebserviceImpl.wsdl")
public class SNSMWebServiceImpl implements SubscriptionManager {

    /*========================================================================*/
    /* CONSTANTS */
    /*========================================================================*/
    private static final String OBJECT_FACTORY_PACKAGE = "gov.lexs.ws.flex.oasis.wsn.b_2";
    private static final String RESPONSE_PATH = "samples/renewResponse.xml";

    /*========================================================================*/
    /* STATIC VARIABLES */
    /*========================================================================*/
    private static final Logger logger = Logger.getLogger(SNSMWebServiceImpl.class.getName());

    /*========================================================================*/
    /* PRIVATE VARIABLES */
    /*========================================================================*/
    private JAXBContext jaxbContext;

    /*========================================================================*/
    /* CONSTRUCTOR */
    /*========================================================================*/
    public SNSMWebServiceImpl() {
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
    public RenewResponseElement renew(RenewElement renewRequest) throws UnacceptableTerminationTimeFault {
        logger.log(Level.INFO, "Received Renew");
        //TODO process message here
        //for now send back canned response.
        RenewResponseElement renewResponseElement = null;
        try {
            renewResponseElement = ServiceUtil.unmarshalXml(RenewResponseElement.class, jaxbContext, ServiceUtil.getResponseFileInputStream(RESPONSE_PATH));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Finished Renew");
        return renewResponseElement;
    }

    @Override
    public UnsubscribeResponseElement unsubscribe(UnsubscribeElement unsubscribeRequest) throws UnableToDestroySubscriptionFault {
        return null;
    }
}
