# CICS OSGi program link application

This sample demonstrates how to use the JCICS Program class to peform CICS LINK operations between CICS Java programs using either a COMMAREA or a channel interface within a CICS OSGi JVM server. 
Java programs for both the calling and target programs are provided.

Sample DFHCSDUP input is provided to build the CICS resource definitions for the transaction IDs, programs and bundles, 
along with Gradle and Maven build files to build the Java applications and package into CICS bundle project ZIP files for deployment. 
Branches are provided for both CICS TS V5 and CICS TS V6 highlighting changes in JCICS v2 at CICS TS V6. 
The V5 branch uses the OSGi CICSMainClass header to denote the program entry point, and the CICS TS V6 branch uses the 
new `@CICSProgram` annotation for the channel based programs and the CICSMainClass header for the COMMAREA based prgorams.

## Versions
| CICS TS for z/OS Version | Branch                                 | Minimum Java Version | Build Status |
|--------------------------|----------------------------------------|----------------------|--------------|
| 5.5, 5.6                 | [cicsts/v5.5](/../../tree/cicsts/v5.5) | 8                    | [![Build](https://github.com/cicsdev/cics-java-osgi-linkactions/workflows/java.yaml/badge.svg?branch=cicsts%2Fv5.5)](https://github.com/cicsdev/cics-java-osgi-link/actions/workflows/java.yaml) |
| 6.1                      | [cicsts/v6.1](/../../tree/cicsts/v6.1) | 8                    | [![Build](https://github.com/cicsdev/cics-java-osgi-link/actions/workflows/java.yaml/badge.svg?branch=cicsts%2Fv6.1)](https://github.com/cicsdev/cics-java-osgi-link/actions/workflows/java.yaml) |

## License
This project is licensed under [Eclipse Public License - v 2.0](LICENSE).
