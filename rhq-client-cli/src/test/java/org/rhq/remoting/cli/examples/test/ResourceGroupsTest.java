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
import org.rhq.core.domain.resource.group.GroupCategory;
import org.rhq.core.domain.resource.group.ResourceGroup;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.remoting.cli.examples.ResourceDiscovery;
import org.rhq.remoting.cli.examples.ResourceGroups;

public class ResourceGroupsTest {

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
    public void createEmptyGroup() {
        new ResourceGroups(client).deleteGroup("empty");
        new ResourceGroups(client).createGroup("empty", new Resource[0], false);
    }

    @Test
    public void createCompatibleGroup() {
        new ResourceGroups(client).deleteGroup("agents");
        Resource[] resources = new ResourceDiscovery(this.client).findResources("RHQ Agent");
        Assert.assertTrue(resources.length > 0);
        ResourceGroup group = new ResourceGroups(client).createGroup("agents", resources, false);
        Assert.assertEquals(GroupCategory.COMPATIBLE, group.getGroupCategory());
        Resource[] groupResources = new ResourceDiscovery(this.client).findResourcesForGroup(group);
        Assert.assertEquals(resources.length, groupResources.length);

    }

    @Test
    public void createMixedGroup() {
        new ResourceGroups(client).deleteGroup("mixed");
        Resource[] agents = new ResourceDiscovery(this.client).findResources("RHQ Agent");
        Assert.assertTrue(agents.length > 0);

        Resource[] platforms = new ResourceDiscovery(this.client).findResources("Linux");
        Assert.assertTrue(platforms.length > 0);

        Resource[] resources = new Resource[agents.length + platforms.length];
        int index = 0;
        for (int i = 0; i < agents.length; i++) {
            resources[i] = agents[i];
            index++;
        }
        for (int i = 0; i < platforms.length; i++) {
            resources[index + i] = platforms[i];
        }

        ResourceGroup group = new ResourceGroups(client).createGroup("mixed", resources, false);
        Assert.assertEquals(GroupCategory.MIXED, group.getGroupCategory());
        Resource[] groupResources = new ResourceDiscovery(this.client).findResourcesForGroup(group);
        Assert.assertEquals(resources.length, groupResources.length);
    }

}
