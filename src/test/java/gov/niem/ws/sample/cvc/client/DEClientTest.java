package gov.niem.ws.sample.cvc.client;

import junit.framework.Assert;
import org.junit.Test;

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

public class DEClientTest {

    /*================================================================================================================*/
    /* LOGGER */
    /*================================================================================================================*/
    private static final Logger logger = Logger.getLogger(DEClient.class.getName());

    /*================================================================================================================*/
    /* UNIT Test */
    /*================================================================================================================*/
    @Test
    public void testDEClient() throws Exception {
        logger.log( Level.INFO, "-------------------- RUNNING TEST CLIENT --------------------");
        DEClient deClient = new DEClient();
        Assert.assertTrue(deClient != null);
//        deClient.execute();
        logger.log( Level.INFO, "-------------------- FINISHED RUNNING TEST CLIENT --------------------");
    }
}
