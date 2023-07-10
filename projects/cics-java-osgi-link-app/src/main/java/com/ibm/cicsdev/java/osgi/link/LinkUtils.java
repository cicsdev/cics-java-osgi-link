package com.ibm.cicsdev.java.osgi.link;

import java.util.Optional;

import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.TerminalPrincipalFacility;

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

    /**
     * Gets the current terminal, if present.
     * 
     * @param task
     *            The current task.
     * @return An {@link Optional} that either contains a
     *         {@value TerminalPrincipalFacility} if the current pricipal
     *         facility is a terminal, or is empty.
     */
    public static Optional<TerminalPrincipalFacility> getTerminal(Task task)
    {
        Object facility = task.getPrincipalFacility();
        if (facility instanceof TerminalPrincipalFacility)
        {
            return Optional.of((TerminalPrincipalFacility) facility);
        }
        return Optional.empty();
    }

}
