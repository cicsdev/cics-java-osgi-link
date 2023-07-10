package com.ibm.cicsdev.java.osgi.link.cicsprogram;

import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;

public class ChannelTargetCICSProgram
{
    static final String PROGRAM_NAME = "CDEVCPCT";

    private final Task task;

    public ChannelTargetCICSProgram()
    {
        this(Task.getTask());
    }

    ChannelTargetCICSProgram(Task task)
    {
        this.task = task;
    }

    @CICSProgram(PROGRAM_NAME)
    public void run()
    {
        task.getOut().println(PROGRAM_NAME + ": Started. Called by: " + task.getInvokingProgramName());

        // TODO Interact with the channel

        task.getOut().println(PROGRAM_NAME + ": Complete");
    }
}
