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
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.remoting.cli.examples.ResourceDiscovery;

public class ResourceDiscoveryTest {

    RemoteClient client;

    @Before
    public void initClient() {
        client = TestUtil.createClient();
    }

    @After
    public void logoutClient() {
        client.logout();
    }

    @Test
    public void discoveryQueue() {
        new ResourceDiscovery(this.client).discoveryQueue();
    }

    @Test
    public void importResources() {
        Resource[] resources = new ResourceDiscovery(this.client).discoveryQueue();
        new ResourceDiscovery(this.client).importResources(resources);
        Assert.assertTrue(new ResourceDiscovery(this.client).discoveryQueue().length == 0);

    }

    @Test
    public void importAllResources() {
        new ResourceDiscovery(this.client).importAllResources();
        Assert.assertTrue(new ResourceDiscovery(this.client).discoveryQueue().length == 0);
    }

    @Test
    public void findResourcesByResourceTypeName() {
        Assert.assertTrue(new ResourceDiscovery(this.client).findResources("RHQ Agent").length > 0);
    }

    @Test
    public void findChildResources() {
        Resource[] resources = new ResourceDiscovery(this.client).findResources("Linux");
        Assert.assertTrue(resources.length > 0);
        Resource[] children = new ResourceDiscovery(this.client).findChildResources(resources[0]);
        Assert.assertTrue(children.length > 0);
        // we know that one of child resources is called RHQ Agent
        for (Resource child : children) {
            if ("RHQ Agent".equals(child.getName())) {
                return;
            }
        }
        Assert.fail("RHQ Agent child resource was not found on linux platform");
    }
}
