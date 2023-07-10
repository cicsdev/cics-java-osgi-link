package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import static com.ibm.cicsdev.java.osgi.link.LinkUtils.createProgram;

import java.nio.ByteBuffer;

import org.osgi.annotation.bundle.Header;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;

/**
 * Demonstrates how an OSGi CICS-MainClass program can link to a CICS program
 * with a channel.
 * <p>
 * The channel is populated with two containers:
 * <ol>
 * <li>A bit container that contains integer data -
 * {@value #INT_CONTAINER_NAME}</li>
 * <li>A char container that contains string data -
 * {@value #STRING_CONTAINER_NAME}</li>
 * </ol>
 * The target program, {@value #TARGET_PROGRAM}, is linked to with this channel.
 * This should write the string {@value #RESPONSE_OK} into the container
 * {@value #RESPONSE_CONTAINER_NAME}.
 * <p>
 * Finally, the terminal the program is run on has the next transaction set to
 * {@value #NEXT_TRANSACTION}, which is the transaction that runs the
 * {@link ChannelTargetProgram}.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
@Header(name = "CICS-MainClass", value = "${@class}")
public class ChannelLinkProgram
{
    private static final String TARGET_PROGRAM = "CDEVMCTC";

    private static final String CHANNEL_NAME = "CICSDEV";

    /** Bit container name */
    private static final String INT_CONTAINER_NAME = "IntContainer";
    /** Bit container data */
    private static final int INT_INPUT = 654321;

    /** Char container name */
    private static final String STRING_CONTAINER_NAME = "StringContainer";
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
    public static void main(String[] args)
    {
        Task task = Task.getTask();

        task.getOut().println("Entering ProgramControlClassThree.main()");
        try
        {
            Program target = createProgram(TARGET_PROGRAM);
            ChannelLinkProgram program = new ChannelLinkProgram(task, target);

            program.run();
        }
        catch (CicsException e)
        {
            task.getErr().println("Caught exception: " + e);
        }
        finally
        {
            task.getOut().println("Leaving ProgramControlClassThree.main()");
        }
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
     * <li>Create the bit container {@value #INT_CONTAINER_NAME} and populate it
     * with the data {@value #INT_INPUT}.</li>
     * <li>Create the char chatainer {@value #STRING_CONTAINER_NAME} and
     * populate it with the data {@value #STRING_INPUT}.</li>
     * <li>Link to the target program DFH$LCCC.</li>
     * <li>Get the container {@value #RESPONSE_CONTAINER_NAME} and check the
     * data is {@value #RESPONSE_OK}.</li>
     * <li>Gets the current CICS terminal</li>
     * <li>Set the next transaction to {@value #NEXT_TRANSACTION} that runs
     * {@link ChannelTargetProgram} with the current channel.</li>
     * </ol>
     * 
     * @throws CicsException
     *             If creating the containers, linking to the program, or
     *             reading the response container fails.
     */
    public void run() throws CicsException
    {
        Channel channel = this.task.createChannel(CHANNEL_NAME);

        // Create the container "IntContainer".
        createIntegerContainer(channel, INT_INPUT);

        // Create the container "StringContainer".
        createStringContainer(channel, STRING_INPUT);

        // Link to the target program with the channel.
        this.task.getOut().println("About to link to CICS program");
        program.link(channel);

        // Get the container "Response" and check the data is "OK".
        String response = readResponseContainer(channel);
        if (!response.equals(RESPONSE_OK))
        {
            this.task.getOut().println("Response was not OK");
            return;
        } 
    }

    /**
     * Creates the container {@value #INT_CONTAINER_NAME} and populates it with
     * the specified data.
     * 
     * @param data
     *            The data to put into the container.
     * @throws CicsException
     *             If creating the container fails.
     */
    private void createIntegerContainer(Channel channel, int data) throws CicsException
    {
        Container intContainer = channel.createContainer(INT_CONTAINER_NAME);

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(data);

        intContainer.put(buffer.array());
    }

    /**
     * Creates the container {@value #STRING_CONTAINER_NAME} and populates it
     * with the specified data.
     * 
     * @param data
     *            The data to put into the container.
     * @throws CicsException
     *             If creating the container fails.
     */
    private void createStringContainer(Channel channel, String data) throws CicsException
    {
        Container stringContainer = channel.createContainer(STRING_CONTAINER_NAME);
        stringContainer.putString(data);
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
