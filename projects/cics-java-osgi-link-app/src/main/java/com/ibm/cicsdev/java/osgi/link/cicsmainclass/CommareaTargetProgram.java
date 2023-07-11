package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import org.osgi.annotation.bundle.Header;

import com.ibm.cics.server.CommAreaHolder;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.java.osgi.link.data.ProgramData;

@Header(name = "CICS-MainClass", value = "${@class}")
public class CommareaTargetProgram
{
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

    private static boolean isCommareaEmpty(CommAreaHolder commarea)
    {
        return commarea.getValue().length == 0;
    }

    private final Task task;
    private final byte[] commarea;

    CommareaTargetProgram(Task task, byte[] commarea)
    {
        this.task = task;
        this.commarea = commarea;
    }

    public ProgramData run()
    {
        // Validate the input commarea data
        ProgramData data = ProgramData.fromBytes(this.commarea);
        validateData(data);

        // Create the new data
        return new ProgramData(123, 'x', 3.14f);
    }

    private void validateData(ProgramData data)
    {
        int intData = data.getInteger();
        char charData = data.getCharacter();
        float decimal = data.getDecimal();

        this.task.getOut().println();
        this.task.getOut().println(this.task.getProgramName() + ": Commarea - int: " + intData + ", char: " + charData + ", decimal: " + decimal);

        // Validate the integer field
        if (intData != 654321)
        {
            this.task.getErr().println("Value (" + intData + ") does not match expected value (654321)");
        }

        // Validate the character field
        if (charData != 'y')
        {
            this.task.getErr().println("Value (" + charData + ") does not match expected value ('y')");
        }

        // Validate the decimal field
        if (decimal != 2.75f)
        {
            this.task.getErr().println("Value (" + decimal + ") does not match expected value (2.75)");
        }

    }
}
