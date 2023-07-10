# CICS OSGi program control application
Demonstrates how OSGi CICS programs link to other programs sending a commarea or channel, or are linked to receiving a commarea or channel.

## Building
The sample can be built using Apache Maven.

### Building with Apache Maven
1. Change into the `projects/` directory.
2. Run the following command.
   ```sh
   mvn clean package
   ```
3. The CICS bundle file will be stored in `cics-java-osgi-program-control-bundle/targets/cics-java-osgi-program-control-bundle-1.0.0-SNAPSHOT.zip`.

## Deploying
Ensure the prerequisite library has been installed for the programs DFH$LCCC and DFH$LCCA. (*TODO - instrutctions/link here*).

1. Copy the JVM profile in `config/jvmprofiles/DFHOSGI.jvmprofile` to the JVM profiles directory of the CICS region on z/FS.
2. Create the JVM server definition OSGIJVMS with the fullowing attributes.
   ```
   JVMSERVER(OSGIJVMS) GROUP(CDEVSAMP) JVMPROFILE(DFHOSGI) DESCRIPTION(CICS JVM server to run OSGi samples)
   ```
3. Install the JVM server OSGIJVMS.
4. Deploy the CICS bundle to z/FS.
   1. Via CICS explorer "Export to UNIX filesystem".
   2. Via the bundle deploy endpoint (create the bundle definition in step 5 first).
   3. Via FTP to z/FS, and extract the ZIP file.
5. Create a bundle definition CDEVJPC with the following attributes.
   ```
   BUNDLE(CDEVJPC) GROUP(CDEVSAMP) BUNDLEDIR(/path/to/deployed/bundle/)
   ```
6. Install the bundle CDEVJPC.


## Running

* To run the channel link example, run the `JPC3` transaction.
* To run the commarea link example, run the `JPC1` transaction.
