package com.ibm.cicsdev.java.osgi.link.cicsprogram;

import java.util.List;
import java.util.stream.Collectors;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;
import com.ibm.cicsdev.java.osgi.link.data.ProgramData;

/**
 * Demonstrates how an OSGi {@link CICSProgram} defined program can be linked to
 * with a channel.
 */
public class ChannelTargetCICSProgram
{
    static final String PROGRAM_NAME = "CDEVCPCT";
    private static final String BIT_CONTAINER_NAME = "BitContainer";
    private static final String CHAR_CONTAINER_NAME = "CharContainer";
    private static final String RESPONSE_CONTAINER_NAME = "Response";

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
    public void run() throws CicsException
    {
        task.getOut().println();

        Channel channel = this.task.getCurrentChannel();

        // Print all the containers in the channel
        printContainers(channel);

        // Print the bit and char containers
        printBitContainer(channel);
        printCharContainer(channel);

        // Create and populate the response container
        createResponseContainer(channel);
    }

    /**
     * Print all the containers in the channel.
     * 
     * @param channel
     *            The channel.
     * @throws CicsException
     *             If listing the containers fails.
     */
    private void printContainers(Channel channel) throws CicsException
    {
        //At V6.1 ContainerIterator is deprecated so getContainerNames() used to list all containers in a channel
        List<String> containers = channel.getContainerNames();

        String containerNamesStr = containers.stream().map(String::trim).collect(Collectors.joining(", "));

        task.getOut().println(PROGRAM_NAME + ": Containers: " + containerNamesStr);
    }

    /**
     * Prints the information in the bit container.
     * 
     * @param channel
     *            The channel to get the container from
     * @throws CicsException
     *             If reading the container fails.
     */
    private void printBitContainer(Channel channel) throws CicsException
    {
        Container bitContainer = channel.getContainer(BIT_CONTAINER_NAME);
        ProgramData data = ProgramData.fromBytes(bitContainer.get());
        task.getOut().println(PROGRAM_NAME + ": Bit data - int: " + data.getInteger() + ", char: " + data.getCharacter()
                + ", decimal: " + data.getDecimal());
    }

    /**
     * Prints the information in the char container.
     * 
     * @param channel
     *            The channel to get the container from
     * @throws CicsException
     *             If reading the container fails.
     */
    private void printCharContainer(Channel channel) throws CicsException
    {
        Container charContainer = channel.getContainer(CHAR_CONTAINER_NAME);
        String charData = charContainer.getString();
        task.getOut().println(PROGRAM_NAME + ": Char data - " + charData);
    }

    /**
     * Creates the response container and populates it with data.
     * 
     * @param channel
     *            The channel to get the container from
     * @throws CicsException
     *             If reading the container fails.
     */
    private void createResponseContainer(Channel channel) throws CicsException
    {
        Container responseContainer = channel.createContainer(RESPONSE_CONTAINER_NAME);
        responseContainer.putString("OK");
    }
}
