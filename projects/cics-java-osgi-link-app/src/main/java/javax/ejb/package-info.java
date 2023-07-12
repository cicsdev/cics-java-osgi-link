
/**
 * This package is included to work around issues with Maven bnd adding the
 * javax.ejb import package for
 * {@link com.ibm.cics.server.invocation.CICSProgram} generated proxies.
 */
@Export
package javax.ejb;

import org.osgi.annotation.bundle.Export;
