package gov.niem.ws.sample.cvc.service;

import gov.lexs.ws.flex.jaxws.*;
import gov.lexs.ws.flex.sr.jaxb.msg.*;
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

@WebService(serviceName = "SRWebService",
portName = "SRWebServicePort",
endpointInterface = "gov.lexs.ws.flex.jaxws.SRWebServicePortType",
targetNamespace = "http://lexs.justice.gov/searchretrieve/5.0/ws",
wsdlLocation = "WEB-INF/wsdl/lexsSRwebserviceImpl.wsdl")
public class SRWebServiceImpl implements SRWebServicePortType {
    /*========================================================================*/
    /* CONSTANTS */
    /*========================================================================*/
    private static final String OBJECT_FACTORY_PACKAGE = "gov.lexs.ws.flex.sr.jaxb.msg";
    private static final String META_OBJECT_FACTORY_PACKAGE = "gov.justice.lexs.meta._5";
    private static final String RESPONSE_PATH = "samples/textSearchResponse.xml";

    /*========================================================================*/
    /* STATIC VARIABLES */
    /*========================================================================*/
    private static final Logger logger = Logger.getLogger(SRWebServiceImpl.class.getName());

    /*========================================================================*/
    /* PRIVATE VARIABLES */
    /*========================================================================*/
    private JAXBContext jaxbContext;

    /*========================================================================*/
    /* CONSTRUCTOR */
    /*========================================================================*/
    public SRWebServiceImpl() {
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
    public DoSearchResponseElement doAttachmentSearch(DoAttachmentSearchRequestElement parameters) throws BusinessRuleNotMetFault, InvalidRequestFault, NetworkFailureFault, NextPreviousNotSupportedFault, OtherErrorFault, TimeoutFault {
        return null;
    }

    @Override
    public DoSearchResponseElement doDataItemMetadataSearch(DoDataItemMetadataSearchRequestElement parameters) throws BusinessRuleNotMetFault, InvalidRequestFault, NetworkFailureFault, NextPreviousNotSupportedFault, OtherErrorFault, TimeoutFault {
        return null;
    }

    @Override
    public DoSearchResponseElement doStructuredSearch(DoStructuredSearchRequestElement parameters) throws BusinessRuleNotMetFault, FuzzyMatchNotSupportedFault, InvalidRequestFault, MultipleValuesNotSupportedFault, NetworkFailureFault, NextPreviousNotSupportedFault, OtherErrorFault, QueryFieldNotSupportedFault, QueryObjectNotSupportedFault, QueryOperatorNotSupportedFault, StructuredSearchNotSupportedFault, TimeoutFault, WildcardNotSupportedFault {
        return generateSearchResponse("doStructuredSearch", parameters);
    }

    @Override
    public DoSearchResponseElement doTextSearch(DoTextSearchRequestElement parameters) throws BusinessRuleNotMetFault, InvalidRequestFault, LogicalOperatorsNotSupportedFault, NetworkFailureFault, NextPreviousNotSupportedFault, OtherErrorFault, PhrasesNotSupportedFault, TimeoutFault, UnstructuredSearchNotSupportedFault, WildcardNotSupportedFault {
        return generateSearchResponse("doTextSearch", parameters);
    }

    @Override
    public GetDataItemResponseElement getDataItem(GetDataItemRequestType parameters) throws BusinessRuleNotMetFault, InvalidDataItemRequestedFault, InvalidRequestFault, NetworkFailureFault, OtherErrorFault, TimeoutFault {
        return null;
    }

    @Override
    public GetDataItemResponseElement getEntity(GetEntityRequestType parameters) throws BusinessRuleNotMetFault, InvalidEntityRequestedFault, InvalidRequestFault, NetworkFailureFault, OtherErrorFault, TimeoutFault {
        return null;
    }

    @Override
    public GetDataItemWithAttachmentsResponseElement getDataItemWithAttachments(GetDataItemRequestType parameters) throws BusinessRuleNotMetFault, InvalidDataItemRequestedFault, InvalidRequestFault, NetworkFailureFault, OtherErrorFault, TimeoutFault {
        return null;
    }

    @Override
    public GetAttachmentResponseElement getRenderedDataItem(GetDataItemRequestType parameters) throws BusinessRuleNotMetFault, InvalidDataItemRequestedFault, InvalidRequestFault, NetworkFailureFault, OtherErrorFault, TimeoutFault {
        return null;
    }

    @Override
    public GetAttachmentResponseElement getRenderedEntity(GetEntityRequestType parameters) throws BusinessRuleNotMetFault, InvalidEntityRequestedFault, InvalidRequestFault, NetworkFailureFault, OtherErrorFault, TimeoutFault {
        return null;
    }

    @Override
    public GetAttachmentResponseElement getAttachment(GetAttachmentRequestElement parameters) throws BusinessRuleNotMetFault, InvalidAttachmentRequestedFault, InvalidRequestFault, NetworkFailureFault, OtherErrorFault, TimeoutFault {
        return null;
    }

    /*========================================================================*/
    /* PUBLIC METHODS */
    /*========================================================================*/

    /**
     * Handles generating a response for text and structured search. At the moment it returns a
     * canned response. It should be modified to process the incoming query and return an
     * appropriate response.
     * @param messageName
     * @param parameters
     * @return
     */
    private DoSearchResponseElement generateSearchResponse(String messageName, Object parameters){
        logger.log(Level.INFO, "Receieved " + messageName);
        String incomingMetadata = ServiceUtil.getMetadataAsString(OBJECT_FACTORY_PACKAGE, "Request LEXS Metadata", parameters);
        System.out.println("Incoming Metadata " + incomingMetadata);
        ServiceUtil.printMessagePart(OBJECT_FACTORY_PACKAGE, messageName, parameters, System.out);
        //TODO: Do some processing on incoming message
        //for now just return a canned response.
        DoSearchResponseElement doSearchResponseElement = null;
        try{
            doSearchResponseElement = ServiceUtil.unmarshalXml(DoSearchResponseElement.class, jaxbContext, ServiceUtil.getResponseFileInputStream(RESPONSE_PATH));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Finished " + messageName);
        return doSearchResponseElement;
    }
}
