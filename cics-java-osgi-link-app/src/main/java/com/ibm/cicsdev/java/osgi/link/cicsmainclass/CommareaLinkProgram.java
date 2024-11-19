package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import java.nio.charset.Charset;

import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;

/**
 * Demonstrates how an OSGi CICS-MainClass program can link to a CICS program
 * with a COMMAREA.
 * <p>
 * The COMMAREA is populated with ebcdic character data in set fields
 * <p>
 * The target program, {@value #TARGET_PROGRAM}, is linked to with this
 * COMMAREA. This should update the data in the COMMAREA.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public class CommareaLinkProgram
{
     /** The target program to link to */
     private static final String TARGET_PROGRAM = "CDEVMCTM";

     /** The local CICS CCSID as a Java charset. */
     private static final Charset LOCAL_CCSID = Charset.forName(System.getProperty("com.ibm.cics.jvmserver.local.ccsid"));
   
    /** The input character data */
    private static final String CHARACTER_DATA = "I love CICS";

    /** The current CICS task */
    private final Task task;

    /** The target CICS program */
    private final Program program;



    /**
     * Entry point to the CICS program.
     * 
     * @param args
     *            Not used.
     */
    public static void main(String[] args) throws CicsException
    {
        Task task = Task.getTask();
        
        Program target = new Program(TARGET_PROGRAM);

        CommareaLinkProgram program = new CommareaLinkProgram(task, target);

        program.run();
    }



    /**
     * Creates a new instance of the COMMAREA link program.
     * <p>
     * A new instance must be created per link request as JCICS object cannot be
     * shared across threads. For more information, see
     * {@link com.ibm.cics.server.API}.
     * 
     * @param task
     *            The current task
     * @param program
     *            The target program
     */
    public CommareaLinkProgram(Task task, Program program)
    {
        this.task = task;
        this.program = program;
    }

    /**
     * Runs the business logic of the program:
     * <ol>
     * <li>Creates the COMMAREA data and populates it with the integer,
     * character, and decimal data.</li>
     * <li>Link to the target program {@value #TARGET_PROGRAM}.</li>     
     * </ol>
     * 
     * @throws CicsException
     *             If creating the containers, linking to the program, or
     *             reading the response container fails.
     */
    public void run() throws CicsException
    {
        // Generate the COMMAREA input data as an EBCDIC encoded byte array
        byte[] inputData = CHARACTER_DATA.getBytes(LOCAL_CCSID);

        // Link to the program with the COMMAREA
        this.program.link(inputData);
    }


    
}
