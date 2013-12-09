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

import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.criteria.ResourceOperationHistoryCriteria;
import org.rhq.core.domain.operation.OperationRequestStatus;
import org.rhq.core.domain.operation.ResourceOperationHistory;
import org.rhq.core.domain.operation.bean.ResourceOperationSchedule;
import org.rhq.core.domain.resource.Resource;
import org.rhq.core.domain.util.PageList;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.enterprise.server.operation.OperationManagerRemote;

/**
 * this class shows how to schedule resource operation
 *
 * @author lzoubek@redhat.com
 */
public class ResourceOperation {

    private final RemoteClient client;
    private final OperationManagerRemote operationManager;

    public ResourceOperation(RemoteClient client) {
        this.client = client;
        this.operationManager = client.getProxy(OperationManagerRemote.class);
    }

    /**
     * runs operation on given resource and wait's until it finishes
     *
     * @param resource      to perform operation
     * @param operationName
     * @param input         - configuration with input parameters (may or may not be required - it depends on operation)
     * @return operation history object which contains operation status and also operation outcome (if any)
     */
    public ResourceOperationHistory runResourceOperation(Resource resource,
                                                         String operationName, Configuration input) {
        ResourceOperationSchedule schedule = operationManager
                .scheduleResourceOperation(client.getSubject(),
                        resource.getId(), operationName, 0, 0, 0, 0, input,
                        null);
        return waitForUpdateFinishes(schedule);
    }

    /**
     * runs command on given agent resource
     *
     * @param rhqAgent resource - must be RHQ Agent
     * @param command  to be executed
     * @return output configuration (which contains results)
     */
    public Configuration runRHQAgentCommand(Resource rhqAgent, String command) {
        if (!rhqAgent.getResourceType().getName().equalsIgnoreCase("RHQ Agent")) {
            throw new IllegalArgumentException("given resource must be RHQ Agent type, but was " + rhqAgent.getResourceType().getName());
        }
        Configuration input = new Configuration();
        input.put(new PropertySimple("command", command));
        ResourceOperationHistory result = runResourceOperation(rhqAgent, "executePromptCommand", input);
        return result.getResults();
    }

    /**
     * waits until an operation defined by given schedule finishes (i.e. is not INPROGRESS)
     *
     * @param schedule
     * @return operation history instance with fetched results
     */
    private ResourceOperationHistory waitForUpdateFinishes(
            ResourceOperationSchedule schedule) {
        while (true) {
            try {
                Thread.currentThread().join(3 * 1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ResourceOperationHistoryCriteria criteria = new ResourceOperationHistoryCriteria();
            criteria.addFilterJobId(schedule.getJobId());
            criteria.fetchResults(true);
            PageList<ResourceOperationHistory> list = operationManager
                    .findResourceOperationHistoriesByCriteria(
                            client.getSubject(), criteria);
            if (list.size() > 0) {
                if (!OperationRequestStatus.INPROGRESS.equals(list.get(0)
                        .getStatus())) {
                    return list.get(0);
                }
            }
        }
    }

}
