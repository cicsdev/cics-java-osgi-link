package com.ibm.cicsdev.java.osgi.link;

import com.ibm.cics.server.Program;

/**
 * Utilities for working with CICS programs
 */
public class LinkUtils
{
    /**
     * Creates a {@link Program} object.
     * 
     * @param name
     *            The name of the program
     * @return The program object.
     */
    public static Program createProgram(String name)
    {
        Program program = new Program();
        program.setName(name);
        return program;
    }
}
