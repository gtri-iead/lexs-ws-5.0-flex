package gov.niem.ws.sample.cvc.service.util;


import gov.justice.lexs.meta._5.MessageMetadataElement;
import gov.lexs.ws.flex.de.jaxb.msg.DomainActionElement;
import gov.lexs.ws.flex.sr.jaxb.msg.DoSearchResponseElement;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import static junit.framework.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
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
public class TestServiceUtil {

    /*========================================================================*/
    /* CONSTANTS */
    /*========================================================================*/
    private String TEST_FILE_PATH = "samples/textSearchResponse.xml";
    private static final String META_OBJECT_FACTORY_PACKAGE = "gov.justice.lexs.meta._5";
    private static final String SR_OBJECT_FACTORY_PACKAGE = "gov.lexs.ws.flex.sr.jaxb.msg";

    /*========================================================================*/
    /* STATIC VARIABLES */
    /*========================================================================*/
    private static final Logger logger = Logger.getLogger(TestServiceUtil.class.getName());

    /*========================================================================*/
    /* TESTS */
    /*========================================================================*/

    @Test
    public void testCreateDate() throws Exception{
        XMLGregorianCalendar calendar = ServiceUtil.createDate();
        assertNotNull(calendar);
    }

    @Test
    public void testCreateAndConfigureMarshaller() throws Exception{
        JAXBContext jaxbContext = jaxbContext = JAXBContext.newInstance("gov.lexs.ws.flex.sr.jaxb.msg");
        Marshaller m = ServiceUtil.createAndConfigureMarshaller(jaxbContext);
        assertNotNull(m);
    }

    @Test
    public void testGetMetadataAsString() throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance("gov.lexs.ws.flex.sr.jaxb.msg");
        Unmarshaller metaUnmarshaller = jaxbContext.createUnmarshaller();
        ServiceUtil.createAndConfigureMarshaller(jaxbContext);
        DoSearchResponseElement doSearchResponseElement = ServiceUtil.unmarshalXml(DoSearchResponseElement.class, jaxbContext, ServiceUtil.getResponseFileInputStream(TEST_FILE_PATH));
        String metaString = ServiceUtil.getMetadataAsString(SR_OBJECT_FACTORY_PACKAGE, "Search Response", doSearchResponseElement );
        assertNotNull(metaString);
        assertTrue(metaString.startsWith("<lexsmeta:MessageMetadata"));
    }

    @Test
    public void testGetObjectAsString() throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance("gov.lexs.ws.flex.sr.jaxb.msg");
        Unmarshaller metaUnmarshaller = jaxbContext.createUnmarshaller();
        ServiceUtil.createAndConfigureMarshaller(jaxbContext);
        DoSearchResponseElement doSearchResponseElement = ServiceUtil.unmarshalXml(DoSearchResponseElement.class, jaxbContext, ServiceUtil.getResponseFileInputStream(TEST_FILE_PATH));
        String elementString = ServiceUtil.getObjectAsString(SR_OBJECT_FACTORY_PACKAGE, "Search Response", doSearchResponseElement);
        assertTrue(elementString.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><doSearchResponse"));
    }

    @Test
    public void testGetResponseFile() throws Exception{
        File testFile = ServiceUtil.getResponseFile(TEST_FILE_PATH);
        assertNotNull(testFile);
    }

    @Test
    public void testGetResponseFileInputStream() throws Exception{
        FileInputStream testFileIs = ServiceUtil.getResponseFileInputStream(TEST_FILE_PATH);
        assertNotNull(testFileIs);
    }

    @Test
    public void testGetResponseFileWithMetadata() throws Exception{
        JAXBContext deMetaContent = JAXBContext.newInstance(gov.justice.lexs.meta._5.ObjectFactory.class);
        Unmarshaller metaUnmarshaller = deMetaContent.createUnmarshaller();
        MessageMetadataElement messageMetadataType = (MessageMetadataElement) JAXBIntrospector.getValue(metaUnmarshaller.unmarshal(ServiceUtil.getResponseFileInputStream("samples/lexsMetadata.xml")));
        String metaString = ServiceUtil.getMetadataAsString(META_OBJECT_FACTORY_PACKAGE, "Message Metadata", messageMetadataType );
        InputStream responseInputStream = ServiceUtil.getResponseFileWithMetadata(TEST_FILE_PATH, "lexs:SearchResponseMessage", metaString);
        assertNotNull(responseInputStream);
    }

    @Test
    public void testNamespaceGen() throws Exception{
        logger.log(Level.INFO, "Testing Namespace Gen");
        Boolean didError = false;
        //create a jaxbcontext
        JAXBContext jaxbContext = jaxbContext = JAXBContext.newInstance("gov.lexs.ws.flex.sr.jaxb.msg");

        //unmarshall to an object
        logger.log(Level.INFO, "Unmarshalling File");
        DoSearchResponseElement doSearchResponseElement = null;
        try {
            InputStream is = ServiceUtil.getResponseFileInputStream(TEST_FILE_PATH);
            doSearchResponseElement = ServiceUtil.unmarshalXml(DoSearchResponseElement.class, jaxbContext, is );
        }
        catch (Exception e) {
            e.printStackTrace();
            didError = true;
        }
        assertFalse(didError);
        assertNotNull(doSearchResponseElement);

        logger.log(Level.INFO, "Marshalling back to text");
        Marshaller marshaller = ServiceUtil.createAndConfigureMarshaller(jaxbContext);
        //now marshall
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(doSearchResponseElement, stringWriter);
        String marshalledXML = stringWriter.toString();

        logger.log(Level.INFO, "Testing marshalled XML");
        assertFalse( marshalledXML.isEmpty());
        assertTrue(marshalledXML.contains("<lexssr:"));
        assertTrue(marshalledXML.contains("<lexs:"));
        assertTrue(marshalledXML.contains("<nc:"));
        assertTrue(marshalledXML.contains("<j:"));

        logger.log(Level.INFO, "Finished Testing Namespace Gen");
    }


}
