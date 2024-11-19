package com.ibm.cicsdev.java.osgi.link.cicsprogram;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;

/**
 * Demonstrates how an OSGi {@link CICSProgram} defined program can be linked to with a channel.
 * 
 */
public class ChannelTargetCICSProgram
{
    static final String PROGRAM_NAME = "CDEVMCTC";
    
    /** Bit container name */
    private static final String BIT_CONTAINER_NAME = "BIT_CONT";

    /** Char container name */
    private static final String CHAR_CONTAINER_NAME = "CHAR_CONT";

    /** Response container name */
    private static final String RESPONSE_CONTAINER_NAME = "RESPONSE_CONT"; 
    
    private final Task task;

    public ChannelTargetCICSProgram()
    {
        this(Task.getTask());
    }

    ChannelTargetCICSProgram(Task task)
    {
        this.task = task;
    }

    /**
     * Runs the business logic.
     * <ol>
     * <li>Validates input channel is present and terminates task with CICS Abend if not present.</li>
     * <li>Prints out list of expected containers and contents to the terminal or stdout</li>
     * <li>Creates the response container to return to the calling program    
     * </ol>
     * 
     * @throws CicsException
     *             If interating with any of the containers fails.
     */
    @CICSProgram(PROGRAM_NAME)
    public void run() throws CicsException
    {
        // Get details of the CICS task
        task.getOut().println();        

        // Get the current channel and abend if none present
        Channel channel = this.task.getCurrentChannel();
        if (channel == null) {
            task.abend("NOCH");
        }

        // Print all the containers in the channel
        printContainers(channel);

        // Print the contents of bit and char containers
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
        //At V6.1 ContainerIterator is deprecated so getContainerNames() is used to list containers in a channel
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
        // Get byte array from the container, with existence checking set to true
        Container bitContainer = channel.getContainer(BIT_CONTAINER_NAME,true); 

        // If the container actually exists then get the data
        if (bitContainer != null) {       
            ByteBuffer bb = ByteBuffer.wrap(bitContainer.get());               

            // Print contents of byte buffer as integer to task stdout stream
            task.getOut().println(PROGRAM_NAME + ": BIT data - int: " + bb.getInt()); 
        }

        // If the container does not exist the container object will be null
        else {
            task.getOut().println(PROGRAM_NAME + ": BIT data - " + "ERROR - INVALID CONTAINER");
        }
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
        // Get String from the container, with existence checking set to true
        Container charContainer = channel.getContainer(CHAR_CONTAINER_NAME,true);
        if (charContainer != null) {
            String charData = charContainer.getString();
            task.getOut().println(PROGRAM_NAME + ": CHAR data  - " + charData);

        // If the container does not exist the container object will be null
        } else {
            task.getOut().println(PROGRAM_NAME + ": CHAR data - " + "ERROR - INVALID CONTAINER");
        }

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
