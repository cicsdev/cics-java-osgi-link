package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.java.osgi.link.data.ProgramData;

/**
 * Demonstrates how an OSGi CICS-MainClass program can link to a CICS program
 * with a channel.
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
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public class ChannelLinkProgram
{
    /** The target program to link to */
    private static final String TARGET_PROGRAM = "CDEVMCTC";

    /** The channel name */
    private static final String CHANNEL_NAME = "CICSDEV";

    /** Bit container name */
    private static final String BIT_CONTAINER_NAME = "BitContainer";

    /** Char container name */
    private static final String CHAR_CONTAINER_NAME = "CharContainer";
    /** Char container data */
    private static final String STRING_INPUT = "Hello CICS Program";

    /** Response container name */
    private static final String RESPONSE_CONTAINER_NAME = "Response";
    /** Expected data in response container */
    private static final String RESPONSE_OK = "OK";

    /**
     * Entry point to the CICS program.
     * 
     * @param args
     *            Not used.
     */
    public static void main(String[] args) throws CicsException
    {
        Task task = Task.getTask();

        Program target = new Program();
        target.setName(TARGET_PROGRAM);
        ChannelLinkProgram program = new ChannelLinkProgram(task, target);

        program.run();
    }

    /** The current task */
    private final Task task;

    /** The target program */
    private final Program program;

    /**
     * Creates a new instance of the channel link program.
     * <p>
     * A new instance must be created per link request as JCICS object cannot be
     * shared across threads. For more information, see
     * {@link com.ibm.cics.server.API}.
     * 
     * @param task
     *            The current task
     * @param program
     *            The target program
     * @param channel
     *            The current channel
     */
    ChannelLinkProgram(Task task, Program program)
    {
        this.task = task;
        this.program = program;
    }

    /**
     * Runs the business logic of the program:
     * <ol>
     * <li>Create the bit container {@value #BIT_CONTAINER_NAME} and populate it
     * with data.</li>
     * <li>Create the char chatainer {@value #CHAR_CONTAINER_NAME} and
     * populate it with the data {@value #STRING_INPUT}.</li>
     * <li>Link to the target program {@value #TARGET_PROGRAM}.</li>
     * <li>Get the container {@value #RESPONSE_CONTAINER_NAME} and check the
     * data is {@value #RESPONSE_OK}.</li>
     * </ol>
     * 
     * @throws CicsException
     *             If creating the containers, linking to the program, or
     *             reading the response container fails.
     */
    public void run() throws CicsException
    {
        Channel channel = this.task.createChannel(CHANNEL_NAME);

        // Create and populate the containers
        createBitContainer(channel);
        createCharContainer(channel);

        // Link to the target program with the channel.
        program.link(channel);

        // Get the container "Response" and check the data is "OK".
        String response = readResponseContainer(channel);
        if (!response.equals(RESPONSE_OK))
        {
            this.task.out.println("Response was not OK");
            return;
        }
    }

    /**
     * Creates the container {@value #BIT_CONTAINER_NAME} and populates it with
     * data.
     * @throws CicsException
     *             If creating the container fails.
     */
    private void createBitContainer(Channel channel) throws CicsException
    {
        Container intContainer = channel.createContainer(BIT_CONTAINER_NAME);
        ProgramData data = new ProgramData(654321, 'y', 2.75f);

        intContainer.put(data.getBytes());
    }

    /**
     * Creates the container {@value #CHAR_CONTAINER_NAME} and populates it
     * with {@value #STRING_INPUT}.
     * 
     * @throws CicsException
     *             If creating the container fails.
     */
    private void createCharContainer(Channel channel) throws CicsException
    {
        Container stringContainer = channel.createContainer(CHAR_CONTAINER_NAME);
        stringContainer.putString(STRING_INPUT);
    }

    /**
     * Reads the response value from the container
     * {@value #RESPONSE_CONTAINER_NAME}.
     * 
     * @return The response from the linked program.
     * @throws CicsException
     *             If reading the container fails.
     */
    private String readResponseContainer(Channel channel) throws CicsException
    {
        Container responseContainer = channel.getContainer(RESPONSE_CONTAINER_NAME);
        return responseContainer.getString();
    }
}
