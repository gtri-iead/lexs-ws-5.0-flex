package gov.niem.ws.sample.cvc.service;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import java.util.HashMap;
import java.util.Map;
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

public class LexsNamespacePrefixMapper extends NamespacePrefixMapper {

    /*========================================================================*/
    /* STATIC VARIABLES */
    /*========================================================================*/
    private static final Logger logger = Logger.getLogger(LexsNamespacePrefixMapper.class.getName());

    /*========================================================================*/
    /* PRIVATE VARIABLES */
    /*========================================================================*/
    private Map<String, String> namespaceMap = new HashMap<String, String>();

    /*========================================================================*/
    /* PUBLIC METHODS */
    /*========================================================================*/
    public LexsNamespacePrefixMapper(){
        logger.log(Level.INFO, "Configuring Namespace Mapper");

        //NIEM prefixes
        logger.log(Level.INFO, "Adding NIEM namespaces");
        namespaceMap.put("http://release.niem.gov/niem/codes/atf/3.0", "atf");
        namespaceMap.put("http://release.niem.gov/niem/codes/core_misc/3.0/", "core_misc");
        namespaceMap.put("http://release.niem.gov/niem/domains/cyfs/3.2/", "cyfs");
        namespaceMap.put("http://release.niem.gov/niem/codes/dea_ctlsub/3.0/", "dea");
        namespaceMap.put("http://release.niem.gov/niem/codes/dol_soc/3.0/", "dol");
        namespaceMap.put("http://release.niem.gov/niem/domains/emergencyManagement/3.2/", "em");
        namespaceMap.put("http://release.niem.gov/niem/codes/fips_10-4/3.0/", "fips_10-4");
        namespaceMap.put("http://release.niem.gov/niem/codes/dot_hazmat/3.0/", "hazmat");
        namespaceMap.put("http://publication.niem.gov/niem/codes/dot_hazmat/3.0/1/", "hazmat-3.0.1");
        namespaceMap.put("http://release.niem.gov/niem/appinfo/3.0", "i");
        namespaceMap.put("http://release.niem.gov/niem/domains/immigration/3.2/", "im");
        namespaceMap.put("http://release.niem.gov/niem/domains/intelligence/3.1/", "intel");
        namespaceMap.put("http://release.niem.gov/niem/codes/iso_4217/3.0/", "iso_4217");
        namespaceMap.put("http://release.niem.gov/niem/codes/iso_639-3/3.0/", "iso_639-3");
        namespaceMap.put("http://release.niem.gov/niem/domains/jxdm/5.2/", "j");
        namespaceMap.put("http://release.niem.gov/niem/niem-core/3.0/", "nc");
        namespaceMap.put("http://publication.niem.gov/niem/niem-core/3.0/2/", "nc-3.0.2");
        namespaceMap.put("http://release.niem.gov/niem/codes/fbi_ncic/3.2/", "ncic");
        namespaceMap.put("http://release.niem.gov/niem/codes/fbi_ndex/3.1/", "ndex");
        namespaceMap.put("http://release.niem.gov/niem/proxy/xsd/3.0/", "niem-xs");
        namespaceMap.put("http://release.niem.gov/niem/structures/3.0/", "s");
        namespaceMap.put("http://release.niem.gov/niem/domains/screening/3.2/", "scr");
        namespaceMap.put("http://release.niem.gov/niem/codes/fbi_ucr/3.2/", "ucr");
        namespaceMap.put("http://release.niem.gov/niem/codes/unece_rec20/3.0/", "unece");

        //lexs prefixes
        logger.log(Level.INFO, "Adding LEXS namespaces");
        namespaceMap.put("http://lexs.justice.gov/lexs/5.0", "lexs");
        namespaceMap.put("http://lexs.justice.gov/codes/5.0", "lexscodes");
        namespaceMap.put("http://lexs.justice.gov/digest/5.0", "lexsdigest");
        namespaceMap.put("http://lexs.justice.gov/faults/5.0", "lexsfaults");
        namespaceMap.put("http://lexs.justice.gov/library/5.0", "lexslib");
        namespaceMap.put("http://lexs.justice.gov/meta/5.0", "lexsmeta");
        namespaceMap.put("http://lexs.justice.gov/query/5.0", "lexsquery");
        namespaceMap.put("http://lexs.justice.gov/domainexchange/5.0", "lexsde");
        namespaceMap.put("http://lexs.justice.gov/publishdiscover/5.0", "lexspd");
        namespaceMap.put("http://lexs.justice.gov/searchretrieve/5.0", "lexssr");
        namespaceMap.put("http://lexs.justice.gov/subscribenotify/5.0", "lexssn");

        //w3c prefixes
        logger.log(Level.INFO, "Adding W3C namespaces");
        namespaceMap.put("http://www.w3.org/2005/08/addressing", "wsa");
        namespaceMap.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        namespaceMap.put("http://docs.oasis-open.org/wsn/b-2", "wsnt");
        namespaceMap.put("http://docs.oasis-open.org/wsrf/bf-2", "wsrf-bf");
        namespaceMap.put("http://docs.oasis-open.org/wsn/t-1", "wstop");
        namespaceMap.put("urn:oasis:names:tc:SAML:2.0:assertion", "saml");
        namespaceMap.put("urn:oasis:names:tc:SAML:2.0:metadata", "md");
        namespaceMap.put("http://schemas.xmlsoap.org/soap/envelope/", "soap");

        logger.log(Level.INFO, "Finished Configuring Namespace Mapper");
    }

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        logger.log(Level.INFO, "Looking for prefix for URI " + namespaceUri + ", suggestion " + suggestion + ", requiredPrefix " + requirePrefix);
        String prefix = namespaceMap.getOrDefault(namespaceUri, suggestion);
        logger.log(Level.INFO, "Returning " + prefix);
        return prefix;
    }
}
