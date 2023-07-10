package com.ibm.cicsdev.java.osgi.program.control;

import java.util.Optional;

import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.TerminalPrincipalFacility;

class LinkUtils
{
    static Program getProgram(String name)
    {
        Program program = new Program();
        program.setName(name);
        return program;
    }

    static Optional<TerminalPrincipalFacility> getTerminal(Task task)
    {
        Object facility = task.getPrincipalFacility();
        if (facility instanceof TerminalPrincipalFacility)
        {
            return Optional.of((TerminalPrincipalFacility) facility);
        }
        return Optional.empty();
    }

}
