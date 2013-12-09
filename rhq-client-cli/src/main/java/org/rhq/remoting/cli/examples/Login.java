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
package org.rhq.remoting.cli.examples;

import org.rhq.enterprise.clientapi.RemoteClient;

public class Login {


    /**
     * an example method that logs in to remote RHQ/JBoss ON server
     *
     * @param host     RHQ/JBoss ON server host or IP
     * @param port
     * @param username
     * @param password
     * @return connected and authenticated remote client or null on error
     */
    public RemoteClient login(String host, int port, String username, String password) {
        RemoteClient client = new RemoteClient(host, port);
        try {
            client.login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return client;
    }
}
