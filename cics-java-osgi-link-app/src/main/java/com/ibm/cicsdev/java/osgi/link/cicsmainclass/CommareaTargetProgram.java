package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import java.nio.charset.Charset;
import com.ibm.cics.server.CommAreaHolder;
import com.ibm.cics.server.Task;

/**
 * Demonstrates how an OSGi CICS-MainClass program can be targeted by a link with a COMMAREA.
 * <p>
 * The COMMAREA is populated with the input character data as a byte array, and
 * updated with a new EBCDIC encoded byte array.
 */
public class CommareaTargetProgram
{

    
     /** The local CICS CCSID as a Java charset. */
     private static final Charset LOCAL_CCSID = Charset.forName(System.getProperty("com.ibm.cics.jvmserver.local.ccsid"));
    
    /** The return COMMAREA data */
    private static final String RETURN_DATA = "SCIC evol I";

    /**
     * Entry point to the CICS program.
     * 
     * @param commarea
     *            The input COMMAREA.
     */
    public static void main(CommAreaHolder commarea)
    {
     
        // Instance of CICS task
        Task task = Task.getTask();
        
        // Check if the COMMAREA holder passed in is populated
        if (isCommareaEmpty(commarea))
        {
            throw new IllegalArgumentException("Commarea is empty.");
        }

        // Instantiate CommareaTargetProgram
        CommareaTargetProgram program = new CommareaTargetProgram(task, commarea.getValue());        
        
        // run the business logic to print the input, and build the return data
        byte[] returnData = program.run();

        // Update the CICS COMMAREA value for return to caller
        commarea.setValue(returnData);

    }

    /**
     * @param commarea
     *            The COMMAREA to check.
     * @return <code>true</code> if the COMMAREA is empty.
     */
    private static boolean isCommareaEmpty(CommAreaHolder commarea)
    {
        return commarea.getValue().length == 0;
    }

    /** The current CICS task */
    private final Task task;

    /** The input COMMAREA */
    private final byte[] commarea;

    /**
     * Creates a new instance of the COMMAREA target program.
     * <p>
     * A new instance must be created per link request as JCICS object cannot be
     * shared across threads. For more information, see
     * {@link com.ibm.cics.server.API}.
     * 
     * @param task
     *            The current task
     * @param commarea
     *            The commarea
     */
    CommareaTargetProgram(Task task, byte[] commarea)
    {
        this.task = task;
        this.commarea = commarea;
    }

    /**
     * Runs the business logic.
     * 
     * <ol>
     * <li>Prints the COMMAREA data</li>
     * <li>Returns data to be updated in the COMMAREA</li>
     * </ol>
     * 
     * @return The data to be updated in the COMMAREA.
     */
    public byte[] run()
    {
        // Print the input COMMAREA data
        printCommareaData(this.commarea);

        // Build the return data as byte array using the local CICS encoding
        return RETURN_DATA.getBytes(LOCAL_CCSID);
    }

    /**
     * Prints the COMMAREA input data to the terminal
     * 
     * @param data
     *            The program data.
     */
    private void printCommareaData(byte[] data)
    {
        String strCommarea = new String(data, LOCAL_CCSID);
        this.task.out.println();
        this.task.out.println(this.task.getProgramName() + ": COMMAREA input: " + strCommarea);
    }
}
