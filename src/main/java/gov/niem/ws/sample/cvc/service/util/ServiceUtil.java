package gov.niem.ws.sample.cvc.service.util;

import gov.lexs.ws.flex.de.jaxb.msg.DomainResponseElement;
import gov.niem.ws.sample.cvc.service.LexsNamespacePrefixMapper;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class ServiceUtil {

    /*========================================================================*/
    /* CONSTANTS */
    /*========================================================================*/
//    private static final String LEXS_META_REGEX = "<lexsmeta:MessageMetadata>[\\w\\W]*</lexsmeta:MessageMetadata>";
    private static final String LEXS_META_REGEX = "<lexsmeta:MessageMetadata[\\w0-9:=\\-_\\s/\"\\.]*>[\\w0-9!-=\\s:/<>]*</lexsmeta:MessageMetadata>";

    /*========================================================================*/
    /* STATIC VARIABLES */
    /*========================================================================*/
    private static final Logger logger = Logger.getLogger(ServiceUtil.class.getName());

    /*========================================================================*/
    /* PUBLIC METHODS */
    /*========================================================================*/

    /**
     * Creates a new Marshaller
     * @param context
     * @return
     * @throws JAXBException
     */
    public static Marshaller createAndConfigureMarshaller(JAXBContext context) throws JAXBException{
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new LexsNamespacePrefixMapper());
        return marshaller;
    }

    /**
     * Creates a date
     * @return {@link XMLGregorianCalendar} for the current date
     */
    public static XMLGregorianCalendar createDate(){
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'kk:mm:ss'Z'");
        XMLGregorianCalendar calendar = null;
        try{
            calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(new Date()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * Used to get a String representation of the LEXS Metadata contained in an JAXB Object.
     * @param classPackage the package to use for the JAXBContext
     * @param elementName the name of the element being printed
     * @param message the JAXB Object to marshal
     */
    public static String getMetadataAsString(String classPackage, String elementName, Object message){
        String objectString = getObjectAsString(classPackage, elementName, message);
        //now use regex to get out the metadata
        Pattern p = Pattern.compile(LEXS_META_REGEX);
        Matcher m = p.matcher(objectString);
        String result = (m.find()) ? m.group() : "";
        return result;
    }

    /**
     * Used to get the Marshalled XML content
     * of a JAXB Object as a String.
     * @param classPackage the package to use for the JAXBContext
     * @param elementName the name of the element being printed
     * @param message the JAXB Object to marshal
     */
    public static String getObjectAsString(String classPackage, String elementName, Object message){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        printMessagePart(classPackage, elementName, message, baos);
        return baos.toString();
    }

    /**
     * Retrieves a file from the classpath
     * @param path
     * @return
     */
    public static File getResponseFile(String path){
        ClassLoader classLoader = ServiceUtil.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        return file;
    }

    /**
     * Retrieves a file inputstream from the classpath
     * @param path
     * @return
     */
    public static FileInputStream getResponseFileInputStream(String path) throws FileNotFoundException{
        File file = getResponseFile(path);
        FileInputStream fis = new FileInputStream(file);
        return fis;
    }

    /**
     * Inserts LEXS metadata into a LEXS xml file.
     * @param filePath the path to the LEXS xml file to use
     * @param metadataParentElementQName the QName of the parent element the LEXS metadata should be inserted under
     * @param metadataXml the String containing the xml for the lexsmeta:MessageMetadata
     * @return a ByteArrayInputStream that contains the xml for the file located at filePath, with the new lexsmeta:MessageMetadata inserted into it.
     * @throws IOException thrown if the file at filePath cannot be located.
     */
    public static InputStream getResponseFileWithMetadata(String filePath, String metadataParentElementQName, String metadataXml) throws IOException{
        File file = getResponseFile(filePath);
        String messageContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        String parentRegEx= "<" + metadataParentElementQName + ">";
        String response = messageContent.replace(parentRegEx, parentRegEx + metadataXml);
        ByteArrayInputStream bais = new ByteArrayInputStream(response.getBytes());
        return bais;
    }

    /**
     * Used to print out the Marshalled XML content of a JAXB Object
     * @param classPackage the package to use for the JAXBContext
     * @param elementName the name of the element being printed
     * @param message the JAXB Object to marshal
     * @param outputStream the OutputStream to use
     */
    public static void printMessagePart(String classPackage, String elementName, Object message, OutputStream outputStream){
        logger.log(Level.INFO, "Printing " + elementName);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classPackage);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(message, outputStream);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Finished Printing " + elementName);
    }

    /**
     * Used to unmarshal an input stream with the XML content of a file to a JAXB Object
     * @param returnType the JAXB Object to return
     * @param xmlStream the input stream that contains teh XML to Unmarshall
     * @param <T> the Type of JAXB object to return.
     * @return a JAXB Object that contains the unmarshalled XML.
     * @throws Exception
     */
    public static <T> T unmarshalXml( Class<T> returnType, JAXBContext context, InputStream xmlStream ) throws JAXBException{
        Unmarshaller unmarshaller = context.createUnmarshaller();
//        Object obj = JAXBIntrospector.getValue(unmarshaller.unmarshal(getResponseFileInputStream(responseFilePath)));
        Object obj = JAXBIntrospector.getValue(unmarshaller.unmarshal(xmlStream));
        return returnType.cast(obj);
    }

}
