package com.ibm.cicsdev.java.osgi.program.control;

import static com.ibm.cicsdev.java.osgi.program.control.LinkUtils.getProgram;
import static com.ibm.cicsdev.java.osgi.program.control.LinkUtils.getTerminal;

import java.io.IOException;
import java.util.Optional;

import org.osgi.annotation.bundle.Header;

import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.TerminalPrincipalFacility;
import com.ibm.cicsdev.java.osgi.program.control.data.ProgramData;

@Header(name = "CICS-MainClass", value = "${@class}")
public class CommareaLinkProgram
{
    public static void main(String[] args)
    {
        Task task = Task.getTask();
        task.getOut().println("Entering CommareaLinkProgram.main()");

        CommareaLinkProgram program = new CommareaLinkProgram(task, getProgram("DFH$LCCA"));
        try
        {
            program.run();
        }
        catch (CicsException | IOException e)
        {
            task.getErr().println("Caught checked exception");
            e.printStackTrace(task.getErr());
        }
        finally
        {
            task.getOut().println("Leaving CommareaLinkProgram.main()");
        }
    }

    private final Task task;
    private final Program program;

    public CommareaLinkProgram(Task task, Program program)
    {
        this.task = task;
        this.program = program;
    }

    public void run() throws CicsException, IOException
    {
        byte[] inputData = generateCommareaData();

        this.task.getOut().println("About to link to C program");
        this.program.link(inputData);

        ProgramData outputData = ProgramData.fromBytes(inputData);
        verifyOutputData(outputData);

        Optional<TerminalPrincipalFacility> terminalOpt = getTerminal(this.task);
        if (!terminalOpt.isPresent())
        {
            this.task.getErr().println("Principal facility is not a terminal.");
            return;
        }

        TerminalPrincipalFacility terminal = terminalOpt.get();
        terminal.setNextCOMMAREA(inputData);
        terminal.setNextTransaction("JPC2");
    }

    private byte[] generateCommareaData() throws IOException
    {
        ProgramData data = new ProgramData(654321, 'y', 2.75f);
        return data.getBytes();
    }

    private void verifyOutputData(ProgramData data)
    {
        // Verify the integer data
        if (data.getInteger() != 123)
        {
            this.task.getErr().print("Value (" + data.getInteger() + ") ");
            this.task.getErr().println("does not match expected value (123)");
        }

        // Verify the character data
        if (data.getCharacter() != 'x')
        {
            this.task.getErr().print("Value (" + data.getCharacter() + ") ");
            this.task.getErr().println("does not match expected value ('x')");
        }

        // Note: float fields need conversion using jni until there is
        // C/390 compiler support for IEEE 754 floating point. This field
        // will not be interpreted correctly by C code using S/390 h/w
        // floating point and is therefore not checked by this sample.
    }

}
