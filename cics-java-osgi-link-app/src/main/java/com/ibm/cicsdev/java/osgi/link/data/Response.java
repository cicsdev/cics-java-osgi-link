/** 
 * Copyright IBM Corp. 2025
 */
package com.ibm.cicsdev.java.osgi.link.data;

/** Response from a target program */
public enum Response
{
    /** OK Response */
    OK, 
    /** Invalid data response */
    INVALID;

    /**
     * @param name The name of the response
     * @return The {@link Response}.
     */
    public static Response fromString(String name)
    {
        switch (name)
        {
        case "OK":
            return OK;
        case "INVALID":
            return INVALID;
        default:
            return INVALID;
        }
    }
}
