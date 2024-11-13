package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.java.osgi.link.data.ProgramData;

/**
 * Demonstrates how an OSGi CICS-MainClass program can link to a CICS program
 * with a COMMAREA.
 * <p>
 * The COMMAREA is populated with data in set fields. The first field is an
 * integer, the second field contains 4 characters, and the third field contains
 * a float.
 * <p>
 * The target program, {@value #TARGET_PROGRAM}, is linked to with this
 * COMMAREA. This should update the data in the COMMAREA. The integer field is
 * set to a new value and the first character in the second field is modified.
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public class CommareaLinkProgram
{
    /** The target program to link to */
    private static final String TARGET_PROGRAM = "CDEVMCTM";

    /** The input integer data */
    private static final int INTEGER_DATA = 654321;
    /** The input character data */
    private static final char CHARACTER_DATA = 'y';
    /** The input decimal data */
    private static final float DECIMAL_DATA = 2.75f;

    /** The expected output integer data */
    private static final int EXPECTED_INTEGER = 123;
    /** The expected output character data */
    private static final char EXPECTED_CHARACTER = 'x';

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

        CommareaLinkProgram program = new CommareaLinkProgram(task, target);

        program.run();
    }

    /** The current CICS task */
    private final Task task;

    /** The target CICS program */
    private final Program program;

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
     * <li>Validates the data that was updated in the COMMAREA.</li>
     * <li>Gets the current CICS terminal</li>
     * <li>Set the next transaction to {@value #NEXT_TRANSACTION} that runs
     * {@link CommareaTargetProgram} with the current channel.</li>
     * </ol>
     * 
     * @throws CicsException
     *             If creating the containers, linking to the program, or
     *             reading the response container fails.
     */
    public void run() throws CicsException
    {
        // Generate the input data
        byte[] inputData = generateCommareaData();

        // Link to the program
        this.program.link(inputData);

        // Verify the output data
        ProgramData outputData = ProgramData.fromBytes(inputData);
        verifyOutputData(outputData);
    }

    /**
     * @return The generated COMMAREA data.
     */
    private byte[] generateCommareaData()
    {
        ProgramData data = new ProgramData(INTEGER_DATA, CHARACTER_DATA, DECIMAL_DATA);
        return data.getBytes();
    }

    /**
     * Verifies the data return from the target program was correct.
     * 
     * @param data
     *            The output data from the COMMAREA.
     */
    private void verifyOutputData(ProgramData data)
    {
        // Verify the integer data
        if (data.getInteger() != EXPECTED_INTEGER)
        {
            this.task.getErr().println("Value (" + data.getInteger() + ") does not match expected value (123)");
        }

        // Verify the character data
        if (data.getCharacter() != EXPECTED_CHARACTER)
        {
            this.task.getErr().println("Value (" + data.getCharacter() + ") does not match expected value ('x')");
        }
    }
}
