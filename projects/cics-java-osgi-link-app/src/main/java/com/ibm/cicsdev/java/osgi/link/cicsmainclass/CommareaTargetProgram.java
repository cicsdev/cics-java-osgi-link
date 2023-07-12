package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import org.osgi.annotation.bundle.Header;

import com.ibm.cics.server.CommAreaHolder;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.java.osgi.link.data.ProgramData;

/**
 * Demonstrates how an OSGi CICS-MainClass program can be targeted by an link
 * with a commarea.
 * <p>
 * The commarea is populated with data in set fields. The first field is an
 * integer, the second field contains 4 characters, and the third field contains
 * a float.
 */
@Header(name = "CICS-MainClass", value = "${@class}")
public class CommareaTargetProgram
{
    /**
     * Entry point to the CICS program.
     * 
     * @param commarea
     *            The input commarea.
     */
    public static void main(CommAreaHolder commarea)
    {
        Task task = Task.getTask();

        if (isCommareaEmpty(commarea))
        {
            throw new IllegalArgumentException("Commarea is empty.");
        }

        CommareaTargetProgram program = new CommareaTargetProgram(task, commarea.getValue());

        ProgramData returnData = program.run();

        // Update the commarea value
        commarea.setValue(returnData.getBytes());
    }

    /**
     * @param commarea
     *            The commarea to check.
     * @return <code>true</code> if the commarea is empty.
     */
    private static boolean isCommareaEmpty(CommAreaHolder commarea)
    {
        return commarea.getValue().length == 0;
    }

    /** The current task */
    private final Task task;

    /** The input commarea */
    private final byte[] commarea;

    /**
     * Creates a new instance of the commarea target program.
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
     * <li>Prints the commarea data</li>
     * <li>Returns data to be updated in the commarea</li>
     * </ol>
     * 
     * @return The data to be updated in the commarea.
     */
    public ProgramData run()
    {
        // Print the input commarea data
        ProgramData data = ProgramData.fromBytes(this.commarea);
        printCommareaData(data);

        // Create the new data
        return new ProgramData(123, 'x', 3.14f);
    }

    /**
     * Prints the commarea data.
     * 
     * @param data
     *            The program data.
     */
    private void printCommareaData(ProgramData data)
    {
        int intData = data.getInteger();
        char charData = data.getCharacter();
        float decimal = data.getDecimal();

        this.task.getOut().println();
        this.task.getOut().println(this.task.getProgramName() + ": Commarea - int: " + intData + ", char: " + charData
                + ", decimal: " + decimal);
    }
}
