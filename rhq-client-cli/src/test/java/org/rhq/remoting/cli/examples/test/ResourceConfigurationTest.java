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


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rhq.core.domain.configuration.ConfigurationUpdateStatus;
import org.rhq.core.domain.configuration.ResourceConfigurationUpdate;
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.remoting.cli.examples.ResourceConfiguration;
import org.rhq.remoting.cli.examples.ResourceDiscovery;

public class ResourceConfigurationTest {

    RemoteClient client;

    @Before
    public void initClient() {
        client = TestUtil.createClient();
        new ResourceDiscovery(this.client).importAllResources();
    }

    @After
    public void logoutClient() {
        client.logout();
    }

    @Test
    public void printAgentConfigurationDefinition() {
        Resource[] resources = new ResourceDiscovery(this.client).findResources("RHQ Agent");
        Assert.assertTrue(resources.length > 0);
        new ResourceConfiguration(this.client).printConfigurationDefinition(resources[0]);
    }

    @Test
    public void printAgentConfiguration() {
        Resource[] resources = new ResourceDiscovery(this.client).findResources("RHQ Agent");
        Assert.assertTrue(resources.length > 0);
        new ResourceConfiguration(this.client).printConfiguration(resources[0]);
    }

    @Test
    public void updateAgentConfiguration() throws Exception {
        Resource[] resources = new ResourceDiscovery(this.client).findResources("RHQ Agent");
        Assert.assertTrue(resources.length > 0);

        // let's update a configuration - we assue that RHQ Client has default configuration
        // (rhq.agent.server.alias != 'test')
        ResourceConfigurationUpdate result = new ResourceConfiguration(this.client)
                .updateResourceConfiguration(resources[0], "rhq.agent.server.alias", "test");
        Assert.assertEquals(ConfigurationUpdateStatus.SUCCESS, result.getStatus());

        // let's try to update again, this time our property has already 
        // a value 'test' this means no update should happen
        // and result must be null
        result = new ResourceConfiguration(this.client)
                .updateResourceConfiguration(resources[0], "rhq.agent.server.alias", "test");
        Assert.assertNull(result);

        // put configuration back to default value
        result = new ResourceConfiguration(this.client)
                .updateResourceConfiguration(resources[0], "rhq.agent.server.alias", "rhqserver");
        Assert.assertEquals(ConfigurationUpdateStatus.SUCCESS, result.getStatus());
    }
}
