package com.ibm.cicsdev.java.osgi.link.data;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Encapsulates data being passed between two programs on a commarea.
 */
public class ProgramData
{
    /** The local CCSID as a Java charset. */
    private static final Charset LOCAL_CCSID = Charset
            .forName(System.getProperty("com.ibm.cics.jvmserver.local.ccsid"));

    /**
     * Generates a {@link ProgramData} object from raw bytes.
     * 
     * @param bytes
     *            The raw bytes.
     * @return A {@link ProgramData} object
     */
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

    /**
     * Helper method that converts a character into a {@link #LOCAL_CCSID}
     * encoded byte.
     * 
     * @param character
     *            The character to encode.
     * @return The byte value of character, encoded in {@link #LOCAL_CCSID}.
     */
    private static byte characterToEncodedByte(char character)
    {
        String characterStr = Character.toString(character);
        byte[] bytes = characterStr.getBytes(LOCAL_CCSID);
        return bytes[0];
    }

    /** An integer value */
    private final int integer;

    /** A character value */
    private final char character;

    /** A decimal value */
    private final float decimal;

    /**
     * @param integer
     *            An integer value
     * @param character
     *            A character value
     * @param decimal
     *            A decimal value
     */
    public ProgramData(int integer, char character, float decimal)
    {
        this.integer = integer;
        this.character = character;
        this.decimal = decimal;
    }

    /**
     * @return The object as an array of bytes, understood by target programs.
     */
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

    /**
     * @return The integer value in the data
     */
    public int getInteger()
    {
        return this.integer;
    }

    /**
     * @return The character value in the data
     */
    public char getCharacter()
    {
        return character;
    }

    /**
     * @return The decimal value in the data
     */
    public float getDecimal()
    {
        return decimal;
    }
}
