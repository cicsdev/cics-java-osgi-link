package com.ibm.cicsdev.java.osgi.program.control.data;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ProgramData
{
    private static final Charset LOCAL_CCSID = Charset
            .forName(System.getProperty("com.ibm.cics.jvmserver.local.ccsid"));

    public static ProgramData fromBytes(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        // Read the integer field
        int integer = buffer.getInt();

        // Read the character field with padding
        byte[] characterEncoded = new byte[1];
        characterEncoded[0] = buffer.get();
        String str = new String(characterEncoded, LOCAL_CCSID);
        char character = str.charAt(0);
        buffer.position(Integer.BYTES + (Character.BYTES * 4));

        float decimal = -1f;

        return new ProgramData(integer, character, decimal);
    }

    private static byte characterToEncodedByte(char character)
    {
        String characterStr = Character.toString(character);
        byte[] bytes = characterStr.getBytes(LOCAL_CCSID);
        return bytes[0];
    }

    private final int integer;
    private final char character;
    private final float decimal;

    public ProgramData(int integer, char character, float decimal)
    {
        this.integer = integer;
        this.character = character;
        this.decimal = decimal;
    }

    public byte[] getBytes()
    {
        int bufferSize = Integer.BYTES + (Character.BYTES * 4) + Float.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

        // Write the int data
        buffer.putInt(integer);

        // Write the character data and pad to a length of 4
        buffer.put(characterToEncodedByte(character));
        buffer.position(Integer.BYTES + (Character.BYTES * 4));

        // Write the float data
        buffer.putFloat(this.decimal);

        return buffer.array();
    }

    public int getInteger()
    {
        return this.integer;
    }

    public char getCharacter()
    {
        return character;
    }

    public float getDecimal()
    {
        return decimal;
    }
}
