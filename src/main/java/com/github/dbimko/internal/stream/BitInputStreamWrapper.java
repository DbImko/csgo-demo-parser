package com.github.dbimko.internal.stream;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;

public class BitInputStreamWrapper extends InputStream {

    private final BitInputStream bitInput;

    public BitInputStreamWrapper(byte[] originalInputStream) {
        this(new ByteArrayInputStream(originalInputStream));
    }

    public BitInputStreamWrapper(InputStream originalInputStream) {
        this.bitInput = new BitInputStream(originalInputStream);
    }

    @Override
    public int read() throws IOException {
        BitSet set = new BitSet(7);
        for (int i = 0; i < 8; i++) {
            int read = bitInput.read();
            if (read == -1) {
                return -1;
            }
            if (read == 1) {
                set.set(i);
            }
        }
        if (set.toByteArray().length == 0) {
            return 0;
        }
        return (int) set.toLongArray()[0];
    }

    @Override
    public int read(byte[] b) throws IOException {
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) read();
        }
        return b.length;
    }

    @Override
    public int available() throws IOException {
        return bitInput.available();
    }

    public boolean readBit() {
        try {
            int read = bitInput.read();
            if (read == -1) {
                throw new EOFException();
            }
            return read == 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        bitInput.close();
    }
}
