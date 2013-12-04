rhq-client-ejb: Remote JAVA API example - EJB Client
======================================================
Author: Libor Zoubek  
Level: Beginner  
Technologies: EJB, JNDI, JBoss ON  
Summary: Demonstrates how can be interacted with JBoss ON using remote EJB 
Target Product: JBoss ON  
Product Versions: JBoss ON 3.2.0  
Source: <https://github.com/jboss-developer/jboss-on-quickstarts/>  

What is it?
-----------

This quickstart shows how to interract with JBoss ON server via remote EJB calls.

System requirements
-------------------

The application this project produces is designed to be run with Red Hat JBoss Operations Network 3.2.0 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

You may need to setup EJB authentication if you run this quickstart on remote JON Server. You can either  [completely disable] 
(https://community.jboss.org/wiki/JBossAS71-DisableRemoteEJBSecurityRealm) EJB remote security or create new user in `ApplicationRealm` on JON server and setup credetials for client (see [EJBClientConfiguration.java] (src/main/java/org/rhq/remoting/ejbclient/examples/EjbClientConfiguration.java))

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-eap-quickstarts#configure-maven) 
before testing the quickstarts.


Start the JBoss ON Server
-------------------------

You can skip this step in case you already have JBoss ON instance running on 

1. Open a command line and navigate to the root of the JBoss ON server directory.
2. The following shows the command line to start the server:

        For Linux:   RHQ_HOME/bin/rhqctl start --server
        For Windows: RHQ_HOME\bin\rhqctl.bat start --server


Build and the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven 
setting arguments on the command line. See [Configure Maven](https://github.com/jboss-developer/jboss-eap-quickstarts#configure-maven) for complete instructions and additional options._

_NOTE: **rhq.server.host** system property is optional, if you don't supply it `localhost` is used instead._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build quickstart:

        mvn clean install -DskipTests

4. Type this command to run quickstart        
        
        mvn exec:java -Drhq.server.host=<your JON server host> 

Run tests
---------------------

Running tests runs almost the same things as if you run the quickstart

        mvn test -Drhq.server.host=<your JON server host>
