/** 
 * Copyright IBM Corp. 2025
 */
package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import java.nio.ByteBuffer;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.ContainerIterator;
import com.ibm.cics.server.Task;


/**
 * Demonstrates how an OSGi CICS-MainClass program can be targeted by an EXEC
 * CICS LINK with a channel.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public class ChannelTargetProgram
{
    /** The name of the container containing integer data */
    private static final String BIT_CONTAINER_NAME = "BitContainer";

    /** The name of the container container string data */
    private static final String CHAR_CONTAINER_NAME = "CharContainer";

    /** The response container name */
    private static final String RESPONSE_CONTAINER_NAME = "Response";

    /**
     * Entry point to the CICS program.
     * 
     * @param args
     *            Not used.
     */
    public static void main(String[] args) throws CicsException
    {
        Task task = Task.getTask();

        // Run the business logic
        ChannelTargetProgram program = new ChannelTargetProgram(task);
        program.run();
    }

    /** The current CICS task */
    private final Task task;

    /** The current program name */
    private final String programName;

    /**
     * Creates a new instance of the channel target program.
     * <p>
     * A new instance must be created per link request as JCICS object cannot be
     * shared across threads. For more information, see
     * {@link com.ibm.cics.server.API}.
     * 
     * @param task
     *            The current task
     */
    ChannelTargetProgram(Task task)
    {
        this.task = task;
        this.programName = task.getProgramName();
    }

    /**
     * Runs the business logic:
     * <ol>
     * <li>Prints the containers in the current channel</li>
     * <li>Prints the data in the bit container</li>
     * <li>Prints the data in the char container</li>
     * <li>Creates and writes the response container</li>
     * </ol>
     * 
     * @throws CicsException
     */
    public void run() throws CicsException
    {
        task.out.println();

        // Print the containers in the current channel
        Channel channel = this.task.getCurrentChannel();
        printContainers(channel);

        // Print the bit container
        printBitContainer(channel);

        // Print the char container
        printCharContainer(channel);

        // Create and write the response container
        createResponseContainer(channel);
    }

    /**
     * Prints the names of all the containers in a channel.
     * 
     * @param channel
     *            The channel the print the channels of.
     * @throws CicsException
     *             If getting the channel names fails.
     */
    private void printContainers(Channel channel) throws CicsException
    {
        ContainerIterator iterator = channel.containerIterator();
        task.out.print(programName + ": Containers: ");
        while (iterator.hasNext())
        {
            Container container = iterator.next();
            String containerName = container.getName().trim();
            task.out.print(containerName);

            if (iterator.hasNext())
            {
                task.out.print(", ");
            }
        }
        task.out.println();
    }

    /**
     * Prints the data in the bit container.
     * 
     * @param channel
     *            The channel the container is in.
     * 
     * @throws CicsException
     *             If reading the container fails.
     */
    private void printBitContainer(Channel channel) throws CicsException
    {
        // Get byte array from the container, with existence checking set to true
        Container bitContainer = channel.getContainer(BIT_CONTAINER_NAME); 

        // If the container actually exists then get the data
        if (bitContainer != null) 
        {       
            ByteBuffer bb = ByteBuffer.wrap(bitContainer.get());               

            // Print contents of byte buffer as integer to task stdout stream
            task.out.println(this.programName + ": BIT  data - int: " + bb.getInt()); 
        }

        // If the container does not exist the container object will be null
        else {
            task.out.println(this.programName + ": BIT  data - " + "ERROR - INVALID CONTAINER");
        }
    }

    /**
     * Prints the data in the char container.
     * 
     * @param channel
     *            The channel the container is in.
     * 
     * @throws CicsException
     *             If reading the container fails.
     */
    private void printCharContainer(Channel channel) throws CicsException
    {
        Container charContainer = channel.getContainer(CHAR_CONTAINER_NAME);
        String charData = charContainer.getString();
        task.out.println(programName + ": CHAR data - " + charData);
    }

    /**
     * Creates the response container and populates it with data.
     * 
     * @param channel
     *            The channel to create the container is in.
     * 
     * @throws CicsException
     *             If reading the container fails.
     */
    private void createResponseContainer(Channel channel) throws CicsException
    {
        Container responseContainer = channel.createContainer(RESPONSE_CONTAINER_NAME);
        responseContainer.putString("OK");
    }
}
