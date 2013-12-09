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

import org.rhq.core.domain.criteria.ResourceGroupCriteria;
import org.rhq.core.domain.resource.Resource;
import org.rhq.core.domain.resource.ResourceType;
import org.rhq.core.domain.resource.group.ResourceGroup;
import org.rhq.core.domain.util.PageList;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.enterprise.server.resource.group.ResourceGroupDeleteException;
import org.rhq.enterprise.server.resource.group.ResourceGroupManagerRemote;
import org.rhq.enterprise.server.resource.group.ResourceGroupNotFoundException;

/**
 * this class shows how to create and delete resource groups
 *
 * @author lzoubek@redhat.com
 */
public class ResourceGroups {

    private final RemoteClient client;
    private final ResourceGroupManagerRemote resourceGroupManager;

    public ResourceGroups(RemoteClient client) {
        this.client = client;
        this.resourceGroupManager = client.getProxy(ResourceGroupManagerRemote.class);
    }

    /**
     * creates a new resource group
     *
     * @param name      of new group
     * @param children  resources to be contained
     * @param recursive - whether this group will contain child resources as implicit resources
     * @return
     */
    public ResourceGroup createGroup(String name, Resource[] children, boolean recursive) {
        ResourceGroup group = new ResourceGroup(name);
        group.setRecursive(recursive);
        // create a group on server
        group = resourceGroupManager.createResourceGroup(client.getSubject(), group);

        // check whether all resources are same type, if so, we'll create COMPATIBLE group
        // at the same time we fill array of resource IDs to tell server our group contains given resources
        int[] ids = new int[children.length];
        ResourceType type = null;
        boolean compatible = true;
        // find a first resource type, which would be used in case of compatible group
        if (children.length > 0) {
            type = children[0].getResourceType();
        }
        for (int i = 0; i < children.length; i++) {
            ids[i] = children[i].getId();
            if (type != null) {
                if (!type.equals(children[i].getResourceType())) {
                    compatible = false;
                }
            }
        }
        if (type != null && compatible) {
            group.setResourceType(type); // this makes group COMPATIBLE
        }

        // assign resources to new group
        resourceGroupManager.addResourcesToGroup(client.getSubject(), group.getId(), ids);
        return group;
    }

    /**
     * deletes group by given name
     *
     * @param name of group
     * @return true if given group was deleted
     */
    public boolean deleteGroup(String name) {
        // let's find given group
        ResourceGroupCriteria criteria = new ResourceGroupCriteria();
        criteria.setStrict(true); // we need to be strict otherwise if name is foo server could return foo, foo2 etc
        criteria.addFilterName(name);
        PageList<ResourceGroup> groups = resourceGroupManager.findResourceGroupsByCriteria(client.getSubject(), criteria);
        // we expect exactly 1 group, on RHQ server group name must be unique
        if (groups.size() == 1) {
            try {
                resourceGroupManager.deleteResourceGroup(client.getSubject(), groups.get(0).getId());
            } catch (ResourceGroupNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (ResourceGroupDeleteException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }
}
