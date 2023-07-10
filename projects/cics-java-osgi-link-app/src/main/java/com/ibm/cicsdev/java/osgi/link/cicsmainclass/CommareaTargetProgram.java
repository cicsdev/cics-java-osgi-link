package com.ibm.cicsdev.java.osgi.link.cicsmainclass;

import java.io.IOException;

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
        task.getOut().println("Entering ProgramControlClassTwo.main()");

        try
        {
            if (isCommareaEmpty(commarea))
            {
                task.getErr().println("Transaction invoked without a CommArea");
                return;
            }

            CommareaTargetProgram program = new CommareaTargetProgram(task, commarea.getValue());

            program.run();
        }
        catch (IOException e)
        {
            task.getErr().println("Failed to read COMMAREA");
        }
        finally
        {
            task.getOut().println("Leaving ProgramControlClassTwo.main()");
        }
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

    public void run() throws IOException
    {
        // Validate the input commarea data
        ProgramData data = ProgramData.fromBytes(this.commarea);
        validateData(data);

        // Create the new data
        ProgramData newData = new ProgramData(123, 'y', 3.14f);

        // Copy the new data into the existing commarea
        System.arraycopy(newData.getBytes(), 0, commarea, 0, commarea.length);
    }

    private void validateData(ProgramData data)
    {
        // Validate the integer field
        if (data.getInteger() != 654321)
        {
            this.task.getErr().println("Value (" + data.getInteger() + ") does not match expected value (654321)");
        }

        // Validate the character field
        if (data.getCharacter() != 'x')
        {
            this.task.getErr().println("Value (" + data.getCharacter() + ") does not match expected value (y)");
        }

        // Validate the decimal field
        if (data.getDecimal() != 2.75f)
        {
            this.task.getErr().println("Value (" + data.getDecimal() + ") does not match expected value (2.75)");
        }
    }
}
