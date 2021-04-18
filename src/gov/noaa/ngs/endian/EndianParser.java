package gov.noaa.ngs.endian;

/**
 *
 * Utils for encoding/decoding binary data in big/little endian format
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 07/15/2015
 *
 */
public class EndianParser {

    private boolean littleEndian;

    /**
     * default constructor for big endian
     */
    public EndianParser() {
        this("be");

    }

    /**
     * constructor for big/little endian
     *
     * @param endian; be=big endian; le=little endian
     */
    public EndianParser(String endian) {
        this.littleEndian = endian.equalsIgnoreCase("le") ? true : false;
    }

    /**
     * Parses a 64-bit integer from a byte array at a given offset
     *
     * @param b byte array to parse from
     * @param offset offset
     * @return long value
     */
    public long getLong(byte[] b, int offset) {
        long l = ((b[offset + 0] & 0xffL) << 56)
                | ((b[offset + 1] & 0xffL) << 48)
                | ((b[offset + 2] & 0xffL) << 40)
                | ((b[offset + 3] & 0xffL) << 32)
                | ((b[offset + 4] & 0xffL) << 24)
                | ((b[offset + 5] & 0xffL) << 16)
                | ((b[offset + 6] & 0xffL) << 8)
                | ((b[offset + 7] & 0xffL));
        return littleEndian ? Long.reverseBytes(l) : l;
    }

    /**
     * Parses a 32-bit integer from a byte array at a given offset
     *
     * @param b byte array to parse from
     * @param offset offset
     * @return int value
     */
    public int getInt(byte[] b, int offset) {
        int i = ((b[offset + 0] & 0xff) << 24)
                | ((b[offset + 1] & 0xff) << 16)
                | ((b[offset + 2] & 0xff) << 8)
                | ((b[offset + 3] & 0xff));
        return littleEndian ? Integer.reverseBytes(i) : i;
    }

    /**
     * Parses a 16-bit integer from a byte array at a given offset
     *
     * @param b byte array to parse from
     * @param offset offset
     * @return short int value
     */
    public short getShort(byte[] b, int offset) {
        short s;
        s = (short) (((b[offset + 0] & 0xff) << 8)
                | ((b[offset + 1] & 0xff)));
        return littleEndian ? Short.reverseBytes(s) : s;

    }

    /**
     * Parses a double-precision float from a byte array at a given offset
     *
     * @param b byte array to parse from
     * @param offset offset
     * @return double value
     */
    public double getDouble(byte[] b, int offset) {
        return Double.longBitsToDouble(getLong(b, offset));
    }

    /**
     * Parses a single-precision float from a byte array at a given offset
     *
     * @param b byte array to parse from
     * @param offset offset
     * @return float value
     */
    public float getFloat(byte[] b, int offset) {
        return Float.intBitsToFloat(getInt(b, offset));
    }

    /**
     * sets an integer in a byte array at a given offset
     *
     * @param b byte array
     * @param offset offset
     * @param val an int to be set
     *
     */
    public void setInt(byte[] b, int offset, int val) {
        val = littleEndian ? Integer.reverseBytes(val) : val;
        b[offset + 0] = (byte) ((val >> 24) & 0xff);
        b[offset + 1] = (byte) ((val >> 16) & 0xff);
        b[offset + 2] = (byte) ((val >> 8) & 0xff);
        b[offset + 3] = (byte) ((val) & 0xff);
    }

    /**
     * sets a short integer in a byte array at a given offset
     *
     * @param b byte array
     * @param offset offset
     * @param val a short int to be set
     *
     */
    public void setShort(byte[] b, int offset, short val) {
        val = littleEndian ? Short.reverseBytes(val) : val;
        b[offset + 0] = (byte) ((val >> 8) & 0xff);
        b[offset + 1] = (byte) ((val) & 0xff);
    }

    /**
     * sets a long integer in a byte array at a given offset
     *
     * @param b byte array
     * @param offset offset
     * @param val a long value to be set
     */
    public void setLong(byte[] b, int offset, long val) {
        val = littleEndian ? Long.reverseBytes(val) : val;
        b[offset + 0] = (byte) ((val >> 56) & 0xff);
        b[offset + 1] = (byte) ((val >> 48) & 0xff);
        b[offset + 2] = (byte) ((val >> 40) & 0xff);
        b[offset + 3] = (byte) ((val >> 32) & 0xff);
        b[offset + 4] = (byte) ((val >> 24) & 0xff);
        b[offset + 5] = (byte) ((val >> 16) & 0xff);
        b[offset + 6] = (byte) ((val >> 8) & 0xff);
        b[offset + 7] = (byte) ((val) & 0xff);
    }

    /**
     * sets a double-precision float in a byte array at a given offset
     *
     * @param b byte array
     * @param offset offset
     * @param val a double value to be set
     */
    public void setDouble(byte[] b, int offset, double val) {
        setLong(b, offset, Double.doubleToLongBits(val));
    }

    /**
     * sets a float in the byte array at a given offset
     *
     * @param b byte array
     * @param offset offset
     * @param val a float value to be set
     */
    public void setFloat(byte[] b, int offset, float val) {
        setInt(b, offset, Float.floatToIntBits(val));
    }

    /**
     * Parses a string from a byte array at a given offset
     *
     * @param b byte array to parse from
     * @param offset offset
     * @param len length of the string
     * @return String
     */
    public String getString(byte[] b, int offset, int len) {
        byte byteAr[] = new byte[len];
        System.arraycopy(b, offset, byteAr, 0, len);
        String str = new String(byteAr);
        return str;
    }

    /**
     * sets a string in a byte array at a given offset
     *
     * @param b byte array
     * @param offset offset
     * @param str a string to be set
     */
    public void setString(byte[] b, int offset, String str) {
        int len = str.length();
        byte[] byteAr = str.getBytes();
        System.arraycopy(byteAr, 0, b, offset, len);
    }
    /**
     * returns endianess of a grid file being used 
     * @return true=LE
     */
    public boolean isLittleEndian() {
        return littleEndian;
    }

}
