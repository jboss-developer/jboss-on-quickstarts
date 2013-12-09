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
import org.rhq.core.domain.measurement.Availability;
import org.rhq.core.domain.measurement.MeasurementData;
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.remoting.cli.examples.ResourceDiscovery;
import org.rhq.remoting.cli.examples.ResourceMonitoring;

import java.util.Set;

public class ResourceMonitoringTest {

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
    public void getCurrentAvailability() {
        Resource[] resources = new ResourceDiscovery(this.client).findResources("Linux");
        Assert.assertTrue(resources.length > 0);
        Availability availability = new ResourceMonitoring(client).getCurrentAvailability(resources[0]);
        Assert.assertNotNull(availability);
        Assert.assertTrue(new ResourceMonitoring(client).isAvailable(resources[0]));
    }

    @Test
    public void getLiveMetricData() {
        Resource[] resources = new ResourceDiscovery(this.client).findResources("Linux");
        Assert.assertTrue(resources.length > 0);
        Set<MeasurementData> data = new ResourceMonitoring(client).getLiveMetricData(resources[0], "Free Memory");
        Assert.assertNotNull(data);
        Assert.assertTrue(data.size() > 0);
    }

}
