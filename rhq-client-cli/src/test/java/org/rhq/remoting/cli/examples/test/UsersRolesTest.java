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
import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Role;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.remoting.cli.examples.UsersRoles;

import java.util.Date;


public class UsersRolesTest {

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
    public void createSubjectWithRole() {
        // create a new role
        Role role = new UsersRoles(client).createRole("testrole" + new Date().getTime());
        Assert.assertNotNull(role);

        // create a new subject with given role
        String subjName = "testsubject" + new Date().getTime();
        Subject subject = new UsersRoles(client).createSubject(subjName, "secure", role);
        Assert.assertNotNull(subject);

        // try to login using new subject
        RemoteClient cl = TestUtil.createClient(subjName, "secure");
        Assert.assertNotNull(cl);

    }
}
