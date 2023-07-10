package com.ibm.cicsdev.java.osgi.link.cicsprogram;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;

public class ChannelLinkCICSProgram
{
    private static final String PROGRAM_NAME = "CDEVCPCL";
    private static final String TARGET_PROGRAM = ChannelTargetCICSProgram.PROGRAM_NAME;
    private static final String CHANNEL_NAME = "MYCHAN";

    private final Task task;
    private final Program program;

    public ChannelLinkCICSProgram()
    {
        this(Task.getTask(), new Program(TARGET_PROGRAM));
    }

    ChannelLinkCICSProgram(Task task, Program program)
    {
        this.task = task;
        this.program = program;
    }

    @CICSProgram(PROGRAM_NAME)
    public void run() throws CicsException
    {
        task.getOut().println(PROGRAM_NAME + ": Started.");
        Channel channel = task.createChannel(CHANNEL_NAME);

        // Link to the target program
        task.getOut().println(PROGRAM_NAME + ": Linking to program " + this.program.getName());
        this.program.link(channel);
        task.getOut().println(PROGRAM_NAME + ": Returned from program " + this.program.getName());

        // TODO Interact with the channel.

        task.getOut().println(PROGRAM_NAME + ": Completed.");
    }
}
