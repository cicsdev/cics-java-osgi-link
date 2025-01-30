/** 
 * Copyright IBM Corp. 2025
 */
package com.ibm.cicsdev.java.osgi.link.cicsprogram;

import java.nio.ByteBuffer;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;

/**
 * Demonstrates how an OSGi {@link CICSProgram} defined program can link to a
 * CICS program with a channel.
 * <p>
 * The channel is populated with two containers:
 * <ol>
 * <li>A bit container that contains integer data -
 * {@value #BIT_CONTAINER_NAME}</li>
 * <li>A char container that contains string data -
 * {@value #CHAR_CONTAINER_NAME}</li>
 * </ol>
 * The target program, {@value #TARGET_PROGRAM}, is linked to with this channel
 * and runs the class {@link ChannelTargetProgram}. This should write the string
 * {@value #RESPONSE_OK} into the container {@value #RESPONSE_CONTAINER_NAME}.
 * <p>
 * <b>Note:</b> Because this is defined using the CICSProgram annotation, a
 * program definition is automatically created when this class is installed into
 * the JVM server.
 */
public class ChannelLinkCICSProgram
{

    /** The name of this program */
    private static final String PROGRAM_NAME = "CDEVMCLC";

    /** The name of the target program */
    private static final String TARGET_PROGRAM = ChannelTargetCICSProgram.PROGRAM_NAME;

    /** The channel name */
    private static final String CHANNEL_NAME = "CICSDEV";

    /** Bit container name */
    private static final String BIT_CONTAINER_NAME = "BIT_CONT";

    /** Char container name */
    private static final String CHAR_CONTAINER_NAME = "CHAR_CONT";

    /** Char container data */
    private static final String STRING_INPUT = "Hello from CICS Program";

    /** binary container data */
    private static final int INT_INPUT = 123456;

    /** Response container name */
    private static final String RESPONSE_CONTAINER_NAME = "RESPONSE_CONT"; 
    
    /** Expected data in response container */
    private static final String RESPONSE_OK = "OK";

    /** The current CICS task */
    private final Task task;

    /** The current CICS program */
    private final Program program;

    /**
     * Creates a new instance of the channel link program.
     * <p>
     * A new instance must be created per link request as JCICS objects cannot be
     * shared across threads. For more information, see
     * {@link com.ibm.cics.server.API}.
     */
    public ChannelLinkCICSProgram()
    {
        this.task = Task.getTask();        
        this.program = new Program(TARGET_PROGRAM);        
    }

    /**
     * Runs the business logic.
     * <ol>
     * <li>Creates a BIT container and populates it with data.</li>
     * <li>Creates a CHAR container and populates it with data.</li>
     * <li>Links to the program {@value #TARGET_PROGRAM}.</li>
     * <li>Gets the response container and checks the value is
     * {@value #RESPONSE_OK}.</li>
     * </ol>
     * 
     * @throws CicsException
     *             If interating with any of the containers fails.
     */
    @CICSProgram(PROGRAM_NAME)
    public void run() throws CicsException
    {
        Channel channel = task.createChannel(CHANNEL_NAME);

        // Write data to the channels
        createBitContainer(channel);
        createCharContainer(channel);

        // Link to the target program
        this.program.link(channel);

        // Get the response from the linked program
        Container responseContainer = channel.getContainer(RESPONSE_CONTAINER_NAME);
        String response = responseContainer.getString();
        // Abend the task with no dump if response not OK
        if (!RESPONSE_OK.equals(response))
        {
            task.abend("INVD", false);
        }
    }

    /**
     * Creates the BIT container and populates it with numeric data.
     * 
     * @param channel
     *            The channel to create the container in.
     * @throws CicsException
     *             If creating and writing the container fails.
     */
    private void createBitContainer(Channel channel) throws CicsException
    {
        int bufferSize = Integer.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

        // Write the int data to the byte buffer
        buffer.putInt(INT_INPUT);

        Container bitContainer = channel.createContainer(BIT_CONTAINER_NAME);
        bitContainer.put(buffer.array());
    }

    /**
     * Creates the CHAR container and populates it with character data.
     * 
     * @param channel
     *            The channel to create the container in.
     * @throws CicsException
     *             If creating and writing the container fails.
     */
    private void createCharContainer(Channel channel) throws CicsException
    {
        Container charContainer = channel.createContainer(CHAR_CONTAINER_NAME);
        charContainer.putString(STRING_INPUT);
    }
}
