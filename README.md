# CICS OSGi Program LINK sample application

Demonstrates how OSGi CICS Java programs link to other CICS programs sending a COMMAREA or channel, or are linked to receiving a COMMAREA or channel.



## Samples overview
### com.ibm.cicsdev.java.osgi.link.cicsmainclass
* [`CommareaLinkProgram.java`](cics-java-osgi-link-app/src/main/java/com/ibm/cicsdev/java/osgi/link/cicsmainclass/CommareaLinkProgram.java) - OSGi CICS-MainClass program that issues a link with a COMMAREA
* [`CommareaTargetProgram.java`](cics-java-osgi-link-app/src/main/java/com/ibm/cicsdev/java/osgi/link/cicsmainclass/CommareaTargetProgram.java) - OSGi CICS-MainClass program linked to with a COMMAREA

### com.ibm.cicsdev.java.osgi.link.cicsprogram
* [`ChannelLinkCICSProgram.java`](cics-java-osgi-link-app/src/main/java/com/ibm/cicsdev/java/osgi/link/cicsprogram/ChannelLinkCICSProgram.java) - OSGi {@link CICSProgram} defined program that issues a link with a channel
* [`ChannelLinkCICSProgram.java`](cics-java-osgi-link-app/src/main/java/com/ibm/cicsdev/java/osgi/link/cicsprogram/ChannelLinkCICSProgram.java) - OSGi {@link CICSProgram} defined program linked to with a channel


## Repository structure
* [`cics-java-osgi-link-app/`](cics-java-osgi-link-app) - Java application source
* [`cics-java-osgi-link-bundle/`](cics-java-osgi-link-bundle/) - Build files for CICS bundle project
* [`etc/`](etc) - Supporting materials 


## Building
The sample can be built using the supplied Gradle or Maven build files to produce an OSGi bundle JAR file and optionally a CICS Bundle archive.

### Gradle (command line)

1. Run the following in a local command prompt:

```sh
gradle clean build
```

This creates a CICS bundle folder in `cics-java-osgi-link-app/build/libs` directory and a CICS bundle ZIP file inside the `cics-java-osgi-link-app/build/distributions` directory.

2. If using the CICS bundle ZIP, the CICS JVM server name for the OSGi bundle part should be modified in the `cics.jvmserver` property in the gradle build properties file to match the required CICS JVMSERVER resource name, or alternatively can be set on the command line as follows.

```sh
gradle clean build -Pjvmserver=MYJVM
```

### Building with Apache Maven
1.Run the following in a local command prompt which will create a JAR file for deployment.

```sh
mvn clean verify
```

This creates a CICS bundle folder in the target directory.

2. If building a CICS bundle ZIP the CICS bundle plugin is driven using the maven verify phase. The CICS JVM server name for the OSGi bundle part should be modified in the `cics.jvmserver` property in the pom.xml to match the required CICS JVMSERVER resource name, or alternatively can be set on the command line as follows.

```sh
mvn clean verify -Dcics.jvmserver=MYJVM
```

The CICS bundle file will be stored in `cics-java-osgi-link-bundle/target/cics-java-osgi-link-bundle-1.0.0.zip`.

## Deploying to CICS

1. CICS resource definitions for the bundle, programs, transactions and a JVM server are supplied in a group CDEVSAMP as a DFHCSDUP sample input stream supplied in [`DFHCSD.txt`](etc/DFHCSD.txt). Alternatively they can be installed using the bundle parts supplied with the cics-java-osgi-link-bundle project.
Note that the resource definitions for the CICS programs `CDEVMCLC` and `CDEVMCTC` for the Channel sample are not supplied as they will be autoinstalled by the processing of @CICSProgram annotation.

1. First deploy the CICS bundle project to z/FS.
   1. Via CICS explorer "Export to UNIX filesystem".
   2. Via the bundle deploy endpoint (create the bundle definition in step 5 first).
   3. Via FTP to z/FS, and extract the ZIP file.

1. Update the BUNDLEDIR attribute on the BUNDLE resource to match the zFS location of the deployed CICS bundle project. 

1. Install the sample group CDEVCJOL 


## Running
There are two types of link this sample demonstrates.
* To run the channel link example, run the `MCLC` transaction. This runs the `CDEVMCLC` program, which links to the `CDEVMCTC` program with channels and containers.
* To run the COMMAREA link example, run the `MCLM` transaction. This runs the `CDEVMCLM` program, which links to the `CDEVMCTM` program with a COMMAREA.

## License
This project is licensed under [Eclipse Public License - v 2.0](LICENSE).
