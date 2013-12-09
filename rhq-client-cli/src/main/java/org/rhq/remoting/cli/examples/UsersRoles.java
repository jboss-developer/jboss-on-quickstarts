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

import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Permission;
import org.rhq.core.domain.authz.Role;
import org.rhq.core.domain.criteria.SubjectCriteria;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.enterprise.server.auth.SubjectManagerRemote;
import org.rhq.enterprise.server.authz.RoleManagerRemote;

/**
 * this class shows several examples about creating roles and subjects (users)
 *
 * @author lzoubek@redhat.com
 */
public class UsersRoles {

    private final RemoteClient client;
    private final SubjectManagerRemote subjectManager;
    private final RoleManagerRemote roleManager;

    public UsersRoles(RemoteClient client) {
        this.client = client;
        subjectManager = client.getProxy(SubjectManagerRemote.class);
        roleManager = client.getProxy(RoleManagerRemote.class);
    }

    /**
     * creates a role of given name, adds some permissions
     *
     * @param name
     * @return
     */
    public Role createRole(String name) {
        Role role = new Role(name);
        role.addPermission(Permission.MANAGE_INVENTORY);
        role.addPermission(Permission.VIEW_USERS);
        return roleManager.createRole(client.getSubject(), role);
    }

    /**
     * creates a new subject with given name and password, given Role is assigned
     * to subject
     *
     * @param name     of subject (login name)
     * @param password
     * @param role
     * @return
     */
    public Subject createSubject(String name, String password, Role role) {
        //create the new user entry
        Subject subject = new Subject();
        subject.setName(name);
        subject.setEmailAddress(name + "@redhat.com");
        subject.setFactive(true);
        subject.setFsystem(false);
        subject = subjectManager.createSubject(client.getSubject(), subject);

        //create the login principal for the user
        subjectManager.createPrincipal(client.getSubject(), subject.getName(), password);

        // assign a role
        roleManager.addRolesToSubject(client.getSubject(), subject.getId(), new int[]{role.getId()});

        // return new subject retrieved from server, so it is the most fresh instance
        SubjectCriteria criteria = new SubjectCriteria();
        criteria.addFilterId(subject.getId());
        return subjectManager.findSubjectsByCriteria(client.getSubject(), criteria).get(0);
    }
}
