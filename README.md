CONNECT
=======

CONNECT is an open source software solution that supports health information exchange - both locally and at the national level. CONNECT uses Nationwide Health Information Network standards and governance to make sure that health information exchanges are compatible with other exchanges being set up throughout the country.

This software solution was initially developed by federal agencies to support their health-related missions, but it is now available to all organizations and can be used to help set up health information exchanges and share data using nationally-recognized interoperability standards.

Uses
----
CONNECT can be used to:

* Set up a health information exchange within an organization
* Tie a health information exchange into a regional network of health information exchanges using Nationwide Health Information Network standards
* Send and receive Direct messages, see: [Setting up CONNECT as a Direct HISP](https://github.com/CONNECT-Solution/CONNECT/blob/CONNECT_integration/Product/Production/Services/DirectCore/README.md)

By advancing the adoption of interoperable health IT systems and health information exchanges, the country will better be able to achieve the goal of making sure all citizens have electronic health records by 2014. Health data will be able to follow a patient across the street or across the country.

Solution
--------
Three primary elements make up the CONNECT solution:

* The Core Services Gateway provides the ability to locate patients at other organizations, request and receive documents associated with the patient, and record these transactions for subsequent auditing by patients and others. Other features include mechanisms for authenticating network participants, formulating and evaluating authorizations for the release of medical information, and honoring consumer preferences for sharing their information. The Nationwide Health Information Network Interface specifications are implemented within this component.

* The Enterprise Service Components provide default implementations of many critical enterprise components required to support electronic health information exchange, including a Master Patient Index (MPI), XDS.b Document Registry and Repository, Authorization Policy Engine, Consumer Preferences Manager, HIPAA-compliant Audit Log and others. Implementers of CONNECT are free to adopt the components or use their own existing software for these purposes.

* The Universal Client Framework contains a set of applications that can be adapted to quickly create an edge system, and be used as a reference system, and/or can be used as a test and demonstration system for the gateway solution. This makes it possible to innovate on top of the existing CONNECT platform.

History
-------
* 4.4 released December 2014
* 4.3 released March 2014
* 4.2 released August 2013
* 4.1 released April 2013
* 4.0 released February 2013
* 3.3 released March 2012

For more information about CONNECT's history, see [HISTORY.md](./HISTORY.md)

Getting Started
---------------
###Prerequisites
Before you get started, you'll need the following installed and set up:
* [Java (JDK) 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html)
  * Install under $JAVA_HOME/jre/lib/security
* [Maven 3.0.5+](http://maven.apache.org/download.html)    See [installation instructions](http://maven.apache.org/download.html#Installation).
* [MySQL 5.1.x](http://dev.mysql.com/downloads/mysql/5.1.html#downloads)
* Application Server (Below are Supported Servers)
  * [GlassFish v3.1.2.2]
(https://glassfish.java.net/downloads/3.1.2.2-final.html)
  * [WebSphere Enterprise 8.5.5.3](http://www.ibm.com/developerworks/downloads/ws/wasdevelopers/)
  * [WebLogic 12c (12.1.1)](http://www.oracle.com/technetwork/middleware/ias/downloads/wls-main-097127.html)
  * [JBoss 7.1.1 Final](http://jbossas.jboss.org/downloads/)
* [Apache Ant v1.7.1](http://ant.apache.org/)


###Building from source
To build all CONNECT modules from source, run:

Windows Users:

        $ set MAVEN_OPTS='-Xmx5000m -XX:MaxPermSize=1024m'

OSX / Linux Users:

        $ export MAVEN_OPTS='-Xmx5000m -XX:MaxPermSize=1024m'

Everyone:

        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install

####Building an ear
All services profiles are active by default, so to build a GlassFish ear containing all services, except for Direct, X12 just execute:

        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install

If you want to exclude a service, in this case Patient Discovery, you can turn off the profile by adding a "!" to the name of the service profile you'd like to exclude (needs to be escaped with "\" char on *NIX) platforms:

        $ mvn clean install -P \!PD

Available service profiles which can be excluded from the generated ear (use value within parentheses):
* Admin Distribution (AD)
* Patient Discovery (PD)
* Document Query (DQ)
* Document Submission (DS)
* Document Retrieve (DR)
* Direct(Direct)
* CONNECTUniversalClientGUI, CONNECTConsumerPreferencesProfileGUI, CONNECTDeferredQueueManagerGUI (GUI)

After CONNECT has been built from the clone directory, you can alter the composition of the CONNECT.ear at any time by turning off any combination of the available profiles as a comma-separated list

        $ cd <CONNECT_CLONE_DIR>
        $ mvn clean install -P \!PD,\!DQ,\!DR -f Product/Production/Deploy/pom.xml

OR

        $ cd Product/Production/Deploy/
        $ mvn clean install -P \!PD,\!DQ,\!DR

You can also specify explicitly what services are included in the ear by passing in the individual profiles.  For example, if you only want to include PD:

        $ cd Product/Production/Deploy/
        $ mvn clean install -P PD

You can find more details about build at: [Build the Source](https://connectopensource.atlassian.net/wiki/display/CONNECT4/Building+CONNECT+4.4+from+Source)
        
######Altering targeted application server
For some application server deployments the generated .ear needs different dependencies. Follow the wiki links below for CONNECT supported application server build and deployment steps.

  * GlassFish: [Glassfish Install](https://connectopensource.atlassian.net/wiki/display/CONNECT4/Building+CONNECT+4.4+from+Source) 
  * WebSphere: [WebSphere Setup](https://connectopensource.atlassian.net/wiki/pages/viewpage.action?pageId=17203313)
  * JBoss7: [JBoss7 Setup](https://connectopensource.atlassian.net/wiki/pages/viewpage.action?pageId=26214665)
  * WebLogic: [WebLogic Setup](https://connectopensource.atlassian.net/wiki/pages/viewpage.action?pageId=17203304)

####Chain of Trust Certificates
Unlike testing with the embedded glassfish and chain of trust certificates, 'ant install' generates self signed certificates at deploy time. In order to switch over to chain of trust certs follow these directions (it is recommended to back up your self signed certs first):

        $ asadmin stop-domain domain1
        $ cp <CONNECT_CLONE_DIR>/Product/SoapUI_Test/ValidationSuite/src/test/resources/chaincerts/*.jks <GLASSFISH_HOME>/domains/domain1/config/
        $ asadmin start-domain domain1
        
The certificates that come in the CONNECT source do not include a Certificate Revocation List, therefore the following `domain.xml` properties need to be set to false:

        <jvm-options>-Dcom.sun.net.ssl.checkRevocation=false</jvm-options>
        <jvm-options>-Dcom.sun.security.enableCRLDP=false</jvm-options>

Testing
-------
###Run the Validation Suite as part of install
At the end of the mvn install process, an embedded GlassFish instance will start and the Validation Suite will run against it. The maven scripts automatically stand up the embedded glassfish using trust chain certificates:

        $ cd <CONNECT_CLONE_DIR>/Product/SoapUI_Test/ValidationSuite at: 
        $ mvn clean install

###Run the Validation Suite via Maven sript
The Validation Suite can be run via a Maven script against a standalone installation of the application server:

        $ cd <CONNECT_CLONE_DIR>/Product/SoapUI_Test/ValidationSuite
        $ mvn verify -Dstandalone -Dproperties.dir=<application server configuration dir>

Several properties can be passed for mvn verify:

        -Dstandalone -- must be passed in for standalone testing
        -Dproperties.dir=<gateway config dir> -- for GlassFish this is <GlassFish home>/domains/domain1/config/nhin; there is an equivalent in WebSphere 
        -Ddb.host=<machine name or IP address of the MySQL server> --  defaults to localhost
        -Ddb.port=<####> -- defaults to 3306
        -Ddb.user=<database user name> -- defaults to nhincuser
        -Ddb.password=<database password> -- defaults to nhincpass
        -Dtest.suite=<g0 or g1>
        -Dtest.case=<test case name> -- one of "Document Submission Deferred Req", "Document Submission Deferred Resp", "Document Submission", "Patient Discovery Deferred Req", "Patient Discovery Deferred Resp", "Patient Discovery", "Document Query", "Document Retrieve", "Subscribe", "Notify", "Unsubscribe", "Admin Distribution"
		
Alternatively, any of these properties can be set in your maven settings.xml file, and they will be propagated to all your builds.  Here is an example showing the mysql.root.password property set to a non-default value:

        <properties>
            <mysql.root.password>f00B4r</mysql.root.password>
            ...
        </properties>

###Run the Validation Suite via SoapUI
The Validation Suite can be run with SoapUI. First, follow the instructions "Setting up SoapUI" below.

Set the property "GatewayPropDir" in ConnectValidation-soapui-project.properties in the Validation Suite directory. This should be set to the gateway configuration directory. For GlassFish this is <GlassFish home>/domains/domain1/config/nhin; there is an equivalent in WebSphere, JBoss and WebLogic.

Run the Validation Suite project file ConnectValidation-soapui-project.xml via SoapUI's command line runner testrunner.sh (or testrunner.bat in Windows).

You can find more details at: [Connect Validation Suite](https://connectopensource.atlassian.net/wiki/display/CONNECT4/Validating+CONNECT+Installation)

##Setting up SoapUI
Install SoapUI v4.5.1.

Copy the MySQL jdbc driver mysql-connector-java-5.1.10.jar from the Maven repository directory .m2/repository/mysql/mysql-connector-java/5.1.10 to {$SoapUI_home}/bin/ext.

Copy the file FileUtils-4.0.0-SNAPSHOT.jar (or similarly named) to {$SoapUI_home}/bin/ext.


Documentation
-------------

###Generate & View
You can generate the project's site information by performing the following: 

        $ mvn -P\!embedded-testing site:site site:stage -DstagingSiteURL=/tmp/fullsite

Then open your browser and view [file:///tmp/fullsite/index.html]

Contributing
------------
1. Fork it
2. Clone the repo (`git clone --recursive <REPO_URL>/CONNECT.git`)
3. If you already have the repo and need to update to the latest, use `git pull`
4. Create a branch (`git checkout -b my_feature`)
5. Commit your changes (`git commit -am "Added new feature"`)
6. Push to the branch (`git push origin my_feature`)
7. Open a [Pull Request][]

[Pull Request]: https://github.com/CONNECT-Solution/CONNECT/pulls
[Download Maven]: http://maventest.apache.org/download.html
[Install Maven]: http://maventest.apache.org/download.html#Installation
[Eclipse]: http://www.eclipse.org/downloads/
[ant 1.7.1]: http://archive.apache.org/dist/ant/binaries/apache-ant-1.7.1-bin.zip
[egit plugin]: http://www.eclipse.org/egit/download/
[m2eclipse plugin]: http://eclipse.org/m2e/
[MySQL 5.1.x]: http://dev.mysql.com/downloads/mysql/5.1.html

