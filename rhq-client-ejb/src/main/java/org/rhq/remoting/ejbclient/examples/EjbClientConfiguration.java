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
package org.rhq.remoting.ejbclient.examples;

import java.util.Properties;

import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

/**
 * This is an alternative way to configure EJB3 Client. By default it is enough
 * to define all these properties in jboss-ejb-client.properties file and place
 * it on class-path. But as we need to define RHQ/JBoss ON hostname from based
 * on system property, we use this approach.
 * 
 * @author lzoubek@redhat.com
 * 
 */
public class EjbClientConfiguration {

    public static void configure() {
        String rhqServer = System.getProperty("rhq.server.host", "localhost");
        Properties p = new Properties();
        {
            p.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
            p.put("remote.connections", "default");
            p.put("remote.connection.default.host", rhqServer);
            p.put("remote.connection.default.port", "3447");
            p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
            // p.put("remote.connection.default.username", "test");
            // p.put("remote.connection.default.password", "test");
            // p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS",
            // "JBOSS-LOCAL-USER");
            p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        }
        EJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(p);
        ContextSelector<EJBClientContext> selector = new ConfigBasedEJBClientContextSelector(cc);
        EJBClientContext.setSelector(selector);
    }
}
