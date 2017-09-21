package com.github.dbimko;

import com.github.dbimko.internal.stream.BitInputStreamWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

@Slf4j
public class DemoFileReader implements AutoCloseable {

    private final InputStream inputStream;

    public DemoFileReader(byte[] chunk) {
        this(chunk, false);
    }

    public DemoFileReader(byte[] chunk, boolean supportBitRead) {
        this(supportBitRead ? new BitInputStreamWrapper(new ByteArrayInputStream(chunk)) : new ByteArrayInputStream(chunk));
    }

    public DemoFileReader(InputStream originalInputStream) {
        inputStream = originalInputStream;
    }


    public int readSignedInt() {
        final int length = 4;
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(length);
            byte[] buffer = byteBuffer.array();
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != length) {
                throw new IllegalStateException();
            }
            return byteBuffer.order(LITTLE_ENDIAN).getInt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long readUnsignedInt() {
        final int length = 4;
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(length);
            byte[] buffer = byteBuffer.array();
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != length) {
                throw new IllegalStateException();
            }
            return byteBuffer.order(LITTLE_ENDIAN).getInt() & 0xffffffffL;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String readCString(int length) {
        try {
            byte[] buffer = new byte[length];
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != length) {
                throw new IllegalStateException("bytes requested " + length + ", bytes actually read " + bytesRead);
            }
            return new String(buffer).split("\0", 2)[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float readFloat() {
        try {
            int len = 4;
            ByteBuffer byteBuffer = ByteBuffer.allocate(len);
            byte[] buffer = byteBuffer.array();
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != len) {
                throw new IllegalStateException();
            }
            return byteBuffer.order(LITTLE_ENDIAN).getFloat();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int readByte() {
        try {
            return inputStream.read();
        } catch (EOFException e) {
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] readBytes(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length less than 0 " + length);
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        byte[] buffer = byteBuffer.array();
        try {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != length) {
                throw new IllegalStateException("Length read " + bytesRead + ", asked " + length);
            }
            return buffer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public int readProtobufVarInt() {
        try {
            int b = 0x80; // 128
            int result = 0;
            for (int count = 0; (b & 0x80) != 0; count++) {
                b = inputStream.read();

                if ((count < 4) || ((count == 4) && (((b & 0xF8) == 0) || ((b & 0xF8) == 0xF8)))) {
                    result |= (b & ~0x80) << (7 * count);
                } else {
                    if (count >= 10) {
                        throw new RuntimeException("Nope nope nope nope! 10 bytes max!");
                    }
                    if ((count == 9) ? (b != 1) : ((b & 0x7F) != 0x7F)) {
                        throw new RuntimeException("more than 32 bits are not supported");
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int length() {
        try {
            return inputStream.available();
        } catch (IOException e) {
            return 0;
        }
    }


    public DemoFileReader readBytesAsWrapper(int length) {
        return readBytesAsWrapper(length, inputStream instanceof BitInputStreamWrapper);
    }

    public DemoFileReader readBytesAsWrapper(int length, boolean supportBitRead) {
        if (supportBitRead) {
            return new DemoFileReader(new BitInputStreamWrapper(new ByteArrayInputStream(readBytes(length))));
        } else {
            return new DemoFileReader(readBytes(length));
        }
    }

    public byte[] toByteArray() {
        int length = length();
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        byte[] buffer = byteBuffer.array();
        try {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != length) {
                throw new IllegalStateException("Length read " + bytesRead + ", asked " + length);
            }
            return buffer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
