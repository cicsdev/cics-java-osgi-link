package com.ibm.cicsdev.java.osgi.link.cicsprogram;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;
import com.ibm.cicsdev.java.osgi.link.data.ProgramData;

public class ChannelLinkCICSProgram
{
    private static final Logger logger = Logger.getLogger(ChannelLinkCICSProgram.class.getName());

    static
    {
        logger.setLevel(Level.INFO);
    }

    private static final String PROGRAM_NAME = "CDEVCPCL";
    private static final String TARGET_PROGRAM = ChannelTargetCICSProgram.PROGRAM_NAME;
    private static final String CHANNEL_NAME = "MYCHAN";

    private static final String BIT_CONTAINER_NAME = "BitContainer";
    private static final String CHAR_CONTAINER_NAME = "CharContainer";
    private static final String RESPONSE_CONTAINER_NAME = "Response";

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
        logger.info(() -> "Program " + task.getProgramName() + " running on transaction " + task.getTransactionName());
        Channel channel = task.createChannel(CHANNEL_NAME);

        // Write data to the channels
        createBitContainer(channel);
        createCharContainer(channel);

        // Link to the target program
        logger.info(() -> "Linking to program: " + program.getName() + " with channel: " + channel.getName());
        this.program.link(channel);
        logger.info(() -> "Returned from program: " + program.getName());

        // Create the response from the program
        Container responseContainer = channel.getContainer(RESPONSE_CONTAINER_NAME);
        String response = responseContainer.getString();
        if(!"OK".equals(response))
        {
            logger.severe(() -> "Program " + program.getName() + " returned a non-OK repsonse: " + response);
            return;
        }
    }

    private void createBitContainer(Channel channel) throws CicsException
    {
        ProgramData data = new ProgramData(654321, 'y', 1.75f);

        logger.info(() -> "Creating bit container: " + BIT_CONTAINER_NAME + " in channel: " + channel.getName());
        Container bitContainer = channel.createContainer(BIT_CONTAINER_NAME);
        bitContainer.put(data.getBytes());
    }

    private void createCharContainer(Channel channel) throws CicsException
    {
        String data = "Hello CICS!";

        logger.info(() -> "Creating char container: " + CHAR_CONTAINER_NAME + " in channel: " + channel.getName());
        Container charContainer = channel.createContainer(CHAR_CONTAINER_NAME);
        charContainer.putString(data);
    }
}
