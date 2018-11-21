package uz.hbs.utils;

public class PasswordGeneratorUtil {
    private static final byte [] digits = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    synchronized public static String getPassword(int length) {
        byte [] buffer = new byte[length];

        for(int i = 0; i < length; ++i) {
            buffer[i] = digits[(int)(Math.random() * 10)];
        }
        
        StringBuffer sb = new StringBuffer(length);
        for(int i = 0; i < length; ++i) sb.append(buffer[i]);
        return sb.toString();
	}
}