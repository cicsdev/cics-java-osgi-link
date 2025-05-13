# CICS OSGi Program LINK sample application

Demonstrates how OSGi CICS Java programs link to other CICS programs sending a COMMAREA or channel, or are linked to receiving a COMMAREA or channel.



## Samples overview
### com.ibm.cicsdev.java.osgi.link.cicsmainclass
* [`CommareaLinkProgram.java`](cics-java-osgi-link-app/src/main/java/com/ibm/cicsdev/java/osgi/link/cicsmainclass/CommareaLinkProgram.java) - OSGi CICS-MainClass program that issues a link with a COMMAREA
* [`CommareaTargetProgram.java`](cics-java-osgi-link-app/src/main/java/com/ibm/cicsdev/java/osgi/link/cicsmainclass/CommareaTargetProgram.java) - OSGi CICS-MainClass program linked to with a COMMAREA

### com.ibm.cicsdev.java.osgi.link.cicsprogram
* [`ChannelLinkCICSProgram.java`](cics-java-osgi-link-app/src/main/java/com/ibm/cicsdev/java/osgi/link/cicsprogram/ChannelLinkCICSProgram.java) - OSGi `@CICSProgram` defined program that issues a link with a channel
* [`ChannelTargetCICSProgram.java`](cics-java-osgi-link-app/src/main/java/com/ibm/cicsdev/java/osgi/link/cicsprogram/ChannelTargetCICSProgram.java) - OSGi `@CICSProgram` defined program linked to with a channel

## Downloading

- Clone the repository using your IDEs support, such as the Eclipse Git plugin
- **or**, download the sample as a [ZIP](https://github.com/cicsdev/cics-java-osgi-link/archive/cicsts/v6.1.zip) and unzip onto the workstation

> [!TIP]
> Eclipse Git provides an 'Import existing Projects' check-box when cloning a repository.

---

## Repository structure
* [`cics-java-osgi-link-app/`](cics-java-osgi-link-app) - Java application source
* [`cics-java-osgi-link-bundle/`](cics-java-osgi-link-bundle/) - Build files for CICS bundle project
* [`etc/eclipse_projects/`](etc/eclipse_projects) - Static CICS Bundle Project 


## Building
The sample includes an Eclipse project configuration, a Gradle build, a Maven POM, and Gradle/Maven Wrappers offering a wide range of build options with the tooling and IDE of your choice.

Choose from the following approach:
* Use the built-in Eclipse and CICS Explorer SDK capability
* Use Eclipse with Buildship (Gradle), or m2e (Maven) to drive Gradle, or Maven.
* Use the command line, or IDE terminal, to drive Gradle or Apache Maven (if installed on your workstation)
* Use the command line, or IDE terminal, or IDE support for Wrappers, to drive the supplied Gradle or Apache Maven Wrappers (with no requirement for Gradle, Maven, Eclipse, or CICS Explorer SDK to be installed)


** Note: ** If you import the project to your IDE, you might experience local project compile errors. To resolve these errors follow the relevant build section below.


### Option 1 - Building with Eclipse

The sample comes pre-configured for use with a standard JDK 1.8 and the CICS TS V5.5 Java EE 6/7 Target Platform. When you initially import the project to your IDE, if your IDE is not configured for a JDK 1.8, or does not have CICS Explorer SDK installed with the correct 'target platform' set, you might experience local project compile errors. 

To resolve issues:
* ensure you have the CICS Explorer SDK plug-in installed
* configure the Project's build-path, and Application Project settings to use your preferred JDK and Java compiler settings
* set the CICS TS Target Platform to your intended CICS target (Hint: Window | Preferences | Plug-in Development | Target Platform | Add | Template | Other...) 


### Option 2 - Building with Gradle

You don't necessarily need to fix the local errors, but to do so, you can run a tooling refresh on the cics-java-osgi-link-app project. For example, in Eclipse: right-click on "Project", select "Gradle | Refresh Gradle Project".

The CICS JVM server name should be modified in the  `cics.jvmserver` property in the gradle build [cics-java-osgi-link-bundle/build.gradle](cics-java-osgi-link-bundle/build.gradle) to match the required CICS JVMSERVER resource name, or alternatively can be set on the command line (see below).

If you have the Gradle buildship plug-in available, use the right-click **Run As...** menu on the cics-java-osgi-link project to configure and run the `clean` and `build` tasks. Otherwise choose from the command-line approaches.

**Gradle Wrapper (Linux/Mac):**
```shell
./gradlew clean build
```

**Gradle Wrapper (Windows):**
```shell
gradle.bat clean build
```

**Gradle (command-line):**
```shell
gradle clean build
```

**Gradle (command-line & setting jvmserver):**
```shell
gradle clean build -Pcics.jvmserver=MYJVM
```

A JAR file is created inside the `cics-java-osgi-link-app/build/libs/` directory and a CICS bundle ZIP file inside the `cics-java-osgi-link-bundle/build/distributions` directory.


### Option 3 - Building with Apache Maven

You don't necessarily need to fix the local errors, but to do so, you can run a tooling refresh on the cics-java-osgi-link-app project. For example, in Eclipse: right-click on "Project", select "Maven -> Update Project...".

> [!TIP]
> In Eclipse, Gradle (buildship) is able to fully refresh and resolve the local classpath even if the project was previously updated by Maven. However, Maven (m2e) does not currently reciprocate that capability. If you previously refreshed the project with Gradle or with the CICS Explorer SDK Java Libraries, you'll need to manually remove the 'Project Dependencies' entry on the Java build-path of your Project Properties to avoid duplication errors when performing a Maven Project Update.

The CICS JVM server name should be modified in the `<cics.jvmserver>` property in the [`pom.xml`](pom.xml) to match the required CICS JVMSERVER resource name, or alternatively can be set on the command line (see below).

If you have the Maven m2e plug-in available, use the right-click **Run As...** menu on the cics-java-osgi-link project to configure and run the `clean` and `verify` tasks. Otherwise choose from the command-line approaches.

**Maven Wrapper (Linux/Mac):**
```shell
./mvnw clean verify
```

**Maven Wrapper (Windows):**
```shell
mvnw.cmd clean verify
```

**Maven (command-line):**
```shell
mvn clean verify
```

**Maven (command-line & setting jvmserver):**
```shell
mvn clean verify -Dcics.jvmserver=MYJVM
```

A JAR file is created inside the `cics-java-osgi-link-app/target` directory and a CICS bundle ZIP file inside the `cics-java-osgi-link-bundle/target` directory.

---

## Deploying to CICS
1. Configure an OSGi JVMSERVER. 

2. CICS resource definitions for the bundle, programs, transactions and a JVM server are supplied in a group CDEVSAMP as a DFHCSDUP sample input stream supplied in [`DFHCSD.txt`](etc/DFHCSD.txt). Alternatively they can be installed using the bundle parts supplied with the cics-java-osgi-link-bundle project.
Note that the resource definitions for the CICS programs `CDEVMCLC` and `CDEVMCTC` for the Channel sample are not supplied as they will be auto-installed by the processing of @CICSProgram annotation.


### Option 1 - Deploying using CICS Explorer SDK and the provided CICS bundle project
1. Deploy the CICS bundle project 'com.ibm.cics.server.examples.osgi.link.bundle' from CICS Explorer using the **Export Bundle Project to z/OS UNIX File System** wizard. This CICS bundle includes the osgi bundlepart, the MCLC & MCLM transactions and the CDEVMCLM & CDEVMCTM programs to run the sample.


### Option 2 - Deploying using CICS Explorer SDK with own CICS bundle project
1. Copy and paste the built JAR from your *projects/cics-java-osgi-link-app/target* or *projects/cics-java-osgi-link-app/build/libs* directory into a new Eclipse CICS bundle project.
2. Create a new OSGi bundlepart that references the JAR (OSGi bundle) file. 
3. Optionally customise the CICS bundle contents, perhaps adding a TRANDEF of your choice
4. Right click using the ** Export Bundle Project to z/OS UNIX File System ** wizard.


### Option 3 - Deploying using CICS Explorer (Remote System Explorer) and CICS Bundle ZIP
1. Connect to USS on the host system
2. Create the bundle directory for the project.
3. Copy & paste the built CICS bundle ZIP file from your *projects/cics-java-osgi-link-bundle/target* or *projects/cics-java-osgi-link-bundle/build/distributions* directory to z/FS on the host system into the bundle directory.
4. Extract the ZIP by right-clicking on the ZIP file > User Action > unjar...
5. Refresh the bundle directory


### Option 4 - Deploying using command line tools
1. Upload the built CICS bundle ZIP file from your *projects/cics-java-osgi-link-bundle/target* or *projects/cics-java-osgi-link-bundle/build/distributions* directory to z/FS on the host system (e.g. FTP).
2. Connect to USS on the host system (e.g. SSH).
3. Create the bundle directory for the project.
4. Move the CICS bundle ZIP file into the bundle directory.
5. Change directory into the bundle directoy.
6. Extract the CICS bundle ZIP file. This can be done using the `jar` command. For example:
   ```shell
   jar xf file.zip
   ```

---

## Installing the CICS bundle
### Installing the CICS bundle from a CICS terminal
1. Create a new bundle definition, setting the bundle directory to the deployed bundle directory:
   ```
   BUNDLE(CDEVCJOL) GROUP(CDEVCJOL) BUNDLEDIR(/path/to/bundle/directory)
   ```
2. Install the bundle


### Installing the CICS bundle with CICS Explorer
1. Definitions > Bundle Definitions
2. Right-click > New...
3. Fill in the Bundle and Group names as `CDEVJODB`
4. Fill in the Bundle Directory to point to the directory you expanded the ZIP
5. Install the bundle

---


## Running
There are two types of link this sample demonstrates.
* To run the channel link example, run the `MCLC` transaction. This runs the `CDEVMCLC` program, which links to the `CDEVMCTC` program with channels and containers.
* To run the COMMAREA link example, run the `MCLM` transaction. This runs the `CDEVMCLM` program, which links to the `CDEVMCTM` program with a COMMAREA.

## License
This project is licensed under [Eclipse Public License - v 2.0](LICENSE).

## Usage terms
By downloading, installing, and/or using this sample, you acknowledge that separate license terms may apply to any dependencies that might be required as part of the installation and/or execution and/or automated build of the sample, including the following IBM license terms for relevant IBM components:

• IBM CICS development components terms: https://www.ibm.com/support/customer/csol/terms/?id=L-ACRR-BBZLGX