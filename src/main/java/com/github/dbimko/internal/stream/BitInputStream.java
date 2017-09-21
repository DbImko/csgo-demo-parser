package com.github.dbimko.internal.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/**
 * A stream of bits that can be read. Because they come from an underlying byte stream,
 * the total number of bits is always a multiple of 8. The bits are read in big endian.
 * Mutable and not thread-safe.
 */
public final class BitInputStream {

    private InputStream input;

    /**
     * Either in the range [0x00, 0xFF] if bits are available, or -1 if end of stream is reached.
     */
    private int currentByte;

    /**
     * Number of remaining bits in the current byte, always between 0 and 7 (inclusive).
     * private int numBitsRemaining;
     */
    private int index;


    /**
     * Constructs a bit input stream based on the specified byte input stream.
     *
     * @param in the byte input stream
     * @throws NullPointerException if the input stream is {@code null}
     */
    public BitInputStream(InputStream in) {
        Objects.requireNonNull(in);
        input = in;
        currentByte = -1;
        index = 8;
    }


    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or -1 if
     * the end of stream is reached. The end of stream always occurs on a byte boundary.
     *
     * @return the next bit of 0 or 1, or -1 for the end of stream
     * @throws IOException if an I/O exception occurred
     */
    public int read() throws IOException {
        refillBuffer();
        if (currentByte == -1) {
            return -1;
        }
        if (index >= 8) {
            throw new AssertionError();
        }
        int val = (currentByte >>> index) & 1;
        index++;
        return val;
    }

    private void refillBuffer() throws IOException {
        if (index == 8) {
            currentByte = input.read();
            index = 0;
        }
    }

    /**
     * Closes stream
     *
     * @throws IOException if an I/O exception occurred
     */
    public void close() throws IOException {
        input.close();
        currentByte = -1;
        index = 0;
    }

    public int available() throws IOException {
        return input.available();
    }

}