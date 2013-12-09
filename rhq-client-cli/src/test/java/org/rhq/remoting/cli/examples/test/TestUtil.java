/** 
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rhq.remoting.cli.examples.test;

import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.remoting.cli.examples.Login;

public class TestUtil {

    /**
     * a helper method that returns connected and authenticated client.
     * RHQ defaults are used, RHQ/JBoss ON host is <b>rhq.server.host</b> system property
     * or <b>localhost</b> by default
     *
     * @return
     */
    public static RemoteClient createClient() {
        return createClient("rhqadmin", "rhqadmin");
    }

    /**
     * a helper method that returns connected and authenticated client.
     * RHQ defaults are used, RHQ/JBoss ON host is <b>rhq.server.host</b> system property
     * or <b>localhost</b> by default
     *
     * @return
     */
    public static RemoteClient createClient(String username, String password) {
        RemoteClient client = new Login().login(System.getProperty("rhq.server.host", "localhost"), 7080, username, password);
        if (client == null) {
            throw new RuntimeException("RemoteClient could not be initialized, did you pass correct \'rhq.server.host\' system property?");
        }
        return client;
    }
}
