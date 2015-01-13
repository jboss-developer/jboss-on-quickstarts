Remote JAVA API example - CLI Client
======================================================
Author: Libor Zoubek  
Level: Intermediate  
Technologies: JBoss ON  
Summary: Demonstrates how can be interacted with JBoss ON using RemoteClient  
Target Product: JBoss ON  
Product Versions: JBoss ON 3.2.0  
Source: <https://github.com/jboss-developer/jboss-on-quickstarts/>  

What is it?
-----------

This quickstart shows how to interract with JBoss ON server using RemoteClient. There are several examples demonstrating 
following areas:

 * Creating users and roles
 * Importing resources from discovery queue
 * Creating resource groups
 * Running resource operation
 * Applying resource configuration change
 * Uploading and deploying a bundle
 * Retrieving resource availability and metric data

_NOTE: Please do not run this quickstart on your production JBoss ON server. It can change state of your resources or inventory._

System requirements
-------------------

The application this project produces is designed to be run with Red Hat JBoss Operations Network 3.2.0 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.

Start the JBoss ON Server
-------------------------

You can skip this step in case you already have JBoss ON instance running on 

1. Open a command line and navigate to the root of the JBoss ON server directory.
2. The following shows the command line to start the server:

        For Linux:   RHQ_HOME/bin/rhqctl start --server
        For Windows: RHQ_HOME\bin\rhqctl.bat start --server


Build and run the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven 
setting arguments on the command line. See [Configure Maven](../README.md#configure-maven) for complete instructions and additional options._

_NOTE: **rhq.server.host** system property is optional, if you don't supply it, `localhost` is used instead._

1. Make sure you have started the JBoss ON Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build quickstart:

        mvn clean install -DskipTests

4. Type this command to run quickstart:
        
        mvn exec:java -Drhq.server.host=<your JON server host> 

_NOTE: You may encounter ERROR messages about server and client version incompatibility being printed in console output. To 
workaround it, create **lib** folder in the root of directory of this quickstart, download CLI from your JON Server, unzip it and copy all .jar files from **lib** directory to your new *lib* directory. Then edit pom.xml in the root directory of this quickstart, comment out **remote-client-deps** dependency and uncomment **addjars-maven-plugin**._

Run the tests
---------------------

These tests automate many of the tasks performed when your run the quickstart manually. 

        mvn test -Drhq.server.host=<your JON server host>
