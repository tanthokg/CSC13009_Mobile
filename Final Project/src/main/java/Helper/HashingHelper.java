package Helper;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashingHelper {
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        int length = hash.length;
        for (int i = 0; i < length; ++i) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(1 == hex.length())
            {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String SHA256(String originalString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    originalString.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (Exception e) {
            Log.e("Error in hashing!", e.getMessage());
        }
        return null;
    }
}
