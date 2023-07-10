package com.ibm.cicsdev.java.osgi.program.control;

import java.io.IOException;

import org.osgi.annotation.bundle.Header;

import com.ibm.cics.server.CommAreaHolder;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.java.osgi.program.control.data.ProgramData;

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
        ProgramData data = ProgramData.fromBytes(this.commarea);

        if (data.getInteger() != 123)
        {
            this.task.getErr().println("Value (" + data.getInteger() + ") does not match expected value (123)");
        }

        if (data.getCharacter() != 'x')
        {
            this.task.getErr().println("Value (" + data.getCharacter() + ") does not match expected value (x)");
        }
    }
}
