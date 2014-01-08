Red Hat JBoss Operations Network (JBoss ON) quickstarts
====================
Summary: The quickstarts demonstrate various ways of interaction with JBoss ON server. They provide small, specific, working 
examples that can be used as a reference for your own project.

Introduction
------------

These quickstarts demonstrate various ways of interaction with JBoss ON server. They provide small, specific, working 
examples that can be used as a reference for your own project.

Be sure to read this entire document before you attempt to work with the quickstarts. It contains the following information:

* [Available Quickstarts](#available-quickstarts): List of the available quickstarts and details about each one.

* [System Requirements](#system-requirements): List of software required to run the quickstarts.

* [Configure Maven](#configure-maven): How to configure the Maven repository for use by the quickstarts.

* [Run the Quickstarts](#run-the-quickstarts): General instructions for building, deploying, and running the quickstarts.


Available Quickstarts
---------------------

* [Remote JAVA API example - CLI Client](rhq-client-cli/README.md)
* [Remote JAVA API example - EJB Client](rhq-client-ejb/README.md)


System Requirements
-------------------

The applications these projects produce are designed to be run on Red Hat JBoss Operations Network 3.2 or later. 

To run these quickstarts with the provided build scripts, you need the following:

1. Java 1.6, to run JBoss AS and Maven. You can choose from the following:
    * OpenJDK
    * Oracle Java SE
    * Oracle JRockit

2. Maven 3.0.0 or newer, to build and deploy the examples
    * If you have not yet installed Maven, see the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/index.html) for details.
    * If you have installed Maven, you can check the version by typing the following in a command prompt:

            mvn --version 

3. JBoss Operations Network (JON) installed and configured.
    * For information on how to install and run JBoss ON, refer to the product documentation at <https://access.redhat.com/site/documentation/en-US/Red_Hat_JBoss_Operations_Network/>.


Configure Maven
---------------

### Configure Maven to Build and Deploy the Quickstarts

The quickstarts use artifacts located in the JBoss GA and Early Access repositories. You must configure Maven to use these repositories before you build and deploy the quickstarts. 

1. Locate the Maven install directory for your operating system. It is usually installed in `${user.home}/.m2/`. 

            For Linux or Mac:   ~/.m2/
            For Windows: "\Documents and Settings\USER_NAME\.m2\"  -or-  "\Users\USER_NAME\.m2\"

2. If you have an existing `settings.xml` file, rename it so you can restore it later.

            For Linux or Mac:  mv ~/.m2/settings.xml ~/.m2/settings-backup.xml
            For Windows: ren "\Documents and Settings\USER_NAME\.m2\settings.xml" settings-backup.xml
                    -or- ren "\Users\USER_NAME\.m2\settings.xml" settings-backup.xml
                   
3. If you have an existing `repository/` directory, rename it so you can restore it later. For example

            For Linux or Mac:  mv ~/.m2/repository/ ~/.m2/repository-backup/
            For Windows: ren "\Documents and Settings\USER_NAME\.m2\repository\" repository-backup
                    -or- ren "\Users\USER_NAME\.m2\repository\" repository-backup
4. Copy the `settings.xml` file from the root of the quickstarts directory to your Maven install directory.
 
            For Linux or Mac:  cp QUICKSTART_HOME/settings.xml  ~/.m2/settings.xml
            For Windows: copy QUICKSTART_HOME/settings.xml "\Documents and Settings\USER_NAME\.m2\settings.xml"
                    -or- copy QUICKSTART_HOME/settings.xml "\Users\USER_NAME\.m2\settings.xml"

_Note:_ If you do not wish to configure the Maven settings, you must pass the configuration setting on every Maven command as follows: ` -s QUICKSTART_HOME/settings.xml`

### Restore Your Maven Configuration When You Finish Testing the Quickstarts

1. Locate the Maven install directory for your operating system. It is usually installed in `${user.home}/.m2/`. 

            For Linux or Mac:   ~/.m2/
            For Windows: "\Documents and Settings\USER_NAME\.m2\"  -or-  "\Users\USER_NAME\.m2\"

2. Restore the `settings.xml` file/

            For Linux or Mac:  mv ~/.m2/settings-backup.xml ~/.m2/settings.xml
            For Windows: ren "\Documents and Settings\USER_NAME\.m2\settings-backup.xml" settings.xml
                    -or- ren "\Users\USER_NAME\.m2\settings-backup.xml" settings.xml
                   
3. Restore the `repository/` directory

            For Linux or Mac:  mv ~/.m2/repository-backup/ ~/.m2/repository/
            For Windows: ren "\Documents and Settings\USER_NAME\.m2\repository-backup\" repository
                    -or- ren "\Users\USER_NAME\.m2\repository-backup\" repository
            

Run the Quickstarts
-------------------

See the individual quickstart README files for information on how to run the quickstarts.

