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

import org.rhq.core.domain.bundle.Bundle;
import org.rhq.core.domain.bundle.BundleDeployment;
import org.rhq.core.domain.bundle.BundleDeploymentStatus;
import org.rhq.core.domain.bundle.BundleDestination;
import org.rhq.core.domain.bundle.BundleVersion;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.criteria.BundleCriteria;
import org.rhq.core.domain.criteria.BundleDeploymentCriteria;
import org.rhq.core.domain.resource.group.ResourceGroup;
import org.rhq.core.domain.util.PageList;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.enterprise.server.bundle.BundleManagerRemote;

import java.io.File;
import java.io.FileInputStream;

/**
 * this class shows several examples about how to deploy a bundle to JON server.
 * <p/>
 * In order to deploy a bundle to several agents you need to 1. upload your
 * bundle to server 2. create a bundle destination - this destination exists on
 * top of COMPATIBLE resource group 3. deploy bundle of specific version to
 * given destination
 * 
 * @author lzoubek@redhat.com
 */
public class DeployBundle {

    private final RemoteClient client;
    private final BundleManagerRemote bundleManager;

    public DeployBundle(RemoteClient client) {
        this.client = client;

        bundleManager = client.getProxy(BundleManagerRemote.class);
    }

    /**
     * deploys a bundle
     * 
     * @param input
     *            bundle distribution ZIP file
     * @param group
     *            to be deployed to (a BundleDestination is created on top of
     *            given group), group must be compatible and it's resources must
     *            support bundle deployment
     * @param config
     *            input configuration for bundle (for passing input-parameter
     *            values)
     * @param destinationName
     *            - name for new destination being created
     * @param baseDirName
     *            - baseDir for deployment - this must match to resourceType
     *            contained in given group
     * @param deployDir
     *            - directory to deploy to - relative path based on baseDir
     * @return bundleDeployment where deployment has finished (either failed or
     *         success)
     * @throws Exception
     */
    public BundleDeployment deployBundle(File input, ResourceGroup group, Configuration config, String destinationName, String baseDirName, String deployDir) throws Exception {
        BundleVersion version = createBundleVersion(input);
        BundleDestination destination = bundleManager.createBundleDestination(client.getSubject(), version.getBundle().getId(), destinationName, "", baseDirName, deployDir, group.getId());

        BundleDeployment deployment = bundleManager.createBundleDeployment(client.getSubject(), version.getId(), destination.getId(), "", config);
        deployment = bundleManager.scheduleBundleDeployment(client.getSubject(), deployment.getId(), false);
        return waitForBundleDeployment(deployment);
    }
    /**
     * removes bundle from server (if found)
     * @param name name of bundle
     */
    public void removeBundle(String name) {
        BundleCriteria criteria = new BundleCriteria();
        criteria.addFilterName(name);
        PageList<Bundle> bundles = bundleManager.findBundlesByCriteria(client.getSubject(), criteria);
        if (!bundles.isEmpty()) {
            try {
                bundleManager.deleteBundle(client.getSubject(),bundles.get(0).getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * creates a bundleVersion on server. This is done by uploading given
     * BundleDistributionFile (ZIP) to server. Server than read's deploy.xml
     * from ZIP file and creates appropriate Bundle in version found in ZIP
     * 
     * @param input
     *            BundleDistribution ZIP file
     * @return
     * @throws Exception
     *             when input file cannot be read or Server fails creating
     *             BundleVersion
     */
    private BundleVersion createBundleVersion(File input) throws Exception {
        byte[] array;
        FileInputStream is = null;
        try {
            is = new FileInputStream(input);
            int length = (int) input.length();
            array = new byte[length];
            for (int numRead = 0, offset = 0; ((numRead >= 0) && (offset < array.length)); offset += numRead) {
                numRead = is.read(array, offset, array.length - offset);
            }
            is.close();
            return bundleManager.createBundleVersionViaByteArray(client.getSubject(), array);

        } catch (Exception ex) {
            if (is != null) {
                is.close();
            }
            throw ex;
        }

    }

    /**
     * waits until given BundleDeployment is not in PENDING or IN_PROGRESS
     * state, then returns BundleDeployment instance, which is going to be
     * either SUCCESS or FAILURE
     * 
     * @param deployment
     * @return
     */
    private BundleDeployment waitForBundleDeployment(BundleDeployment deployment) {
        while (deployment.getStatus().equals(BundleDeploymentStatus.IN_PROGRESS) || deployment.getStatus().equals(BundleDeploymentStatus.PENDING)) {
            try {
                Thread.currentThread().join(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BundleDeploymentCriteria criteria = new BundleDeploymentCriteria();
            criteria.addFilterBundleId(deployment.getId());
            PageList<BundleDeployment> found = bundleManager.findBundleDeploymentsByCriteria(client.getSubject(), criteria);
            if (!found.isEmpty()) {
                deployment = found.get(0);
            }
        }
        return deployment;
    }
}
