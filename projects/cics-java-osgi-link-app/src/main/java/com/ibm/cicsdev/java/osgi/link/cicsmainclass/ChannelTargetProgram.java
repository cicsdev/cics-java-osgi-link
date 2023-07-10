package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import java.nio.ByteBuffer;
import java.util.List;

import org.osgi.annotation.bundle.Header;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.Task;

/**
 * Demonstrates how an OSGi CICS-MainClass program can be targeted by an EXEC
 * CICS LINK with a channel.
 * <p>
 * All container in the channel are printed.
 * <p>
 * The program checks the contianer {@value #INT_CONTAINER_NAME} expected the
 * value {@value #EXPECTED_INT}.
 * <p>
 * The program checks the contianer {@value #STRING_CONTAINER_NAME} expected the
 * value {@value #EXPECTED_STRING}.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
@Header(name = "CICS-MainClass", value = "${@class}")
public class ChannelTargetProgram
{
    private static final String INT_CONTAINER_NAME = "IntContainer";
    private static final int EXPECTED_INT = 123;

    private static final String STRING_CONTAINER_NAME = "StringContainer";
    private static final String EXPECTED_STRING = "Hello Java World";

    /**
     * Entry point to the CICS program.
     * 
     * @param args
     *            Not used.
     */
    public static void main(String[] args)
    {
        Task task = Task.getTask();

        task.getOut().println("Entering ProgramControlClassFour.main()");
        try
        {
            // Get the current channel
            Channel channel = task.getCurrentChannel();
            if (channel == null)
            {
                task.getErr().println("Transaction invoked without a Channel");
                return;
            }

            // Run the business logic
            ChannelTargetProgram program = new ChannelTargetProgram(task, channel);
            program.run();
        }
        catch (CicsException e)
        {
            task.getErr().println("Caught exception: " + e);
        }
        finally
        {
            task.getOut().println("Leaving ProgramControlClassFour.main()");
        }
    }

    /** The current CICS task */
    private final Task task;

    /** The channel from the EXEC CICS LINK */
    private final Channel channel;

    /**
     * Creates a new instance of the channel target program.
     * <p>
     * A new instance must be created per link request as JCICS object cannot be
     * shared across threads. For more information, see
     * {@link com.ibm.cics.server.API}.
     * 
     * @param task
     *            The current task
     * @param channel
     *            The current channel
     */
    ChannelTargetProgram(Task task, Channel channel)
    {
        this.task = task;
        this.channel = channel;
    }

    /**
     * Runs the business logic of the program:
     * <ol>
     * <li>Print all the containers this program was linked to with.</li>
     * <li>Retrieve the channel
     * </ol>
     * 
     * @throws CicsException
     */
    public void run() throws CicsException
    {
        // Print the container names
        List<String> containerNames = this.channel.getContainerNames();
        for (String containerName : containerNames)
        {
            this.task.getOut().println("ProgramControlClassFour invoked with Container \"" + containerName + "\"");
        }

        // Validate the integer container
        int integer = getIntContainerData();
        if (integer != EXPECTED_INT)
        {
            this.task.getErr().println("Value (" + integer + ") does not match expected value (123)");
        }

        // Validate the string container
        String stringData = getStringContainerData();
        if (!EXPECTED_STRING.equals(stringData))
        {
            this.task.getErr().println("Value (" + stringData + ") does not match expected value (Hello Java World)");
        }
    }

    private int getIntContainerData() throws CicsException
    {
        // Get the container
        Container intContainer = this.channel.getContainer(INT_CONTAINER_NAME);

        // Validate the container exists
        if (intContainer == null)
        {
            return -1;
        }

        // Get the integer data
        byte[] data = intContainer.get();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        return buffer.getInt();
    }

    private String getStringContainerData() throws CicsException
    {
        // Get the container
        Container stringContainer = this.channel.getContainer(STRING_CONTAINER_NAME);

        // Validate the container exists
        if (stringContainer == null)
        {
            return null;
        }

        // Validate the actual data equals the expected data
        return stringContainer.getString();
    }
}
