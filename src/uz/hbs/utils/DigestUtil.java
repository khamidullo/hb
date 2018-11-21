package uz.hbs.utils;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.util.encoders.Hex;

public class DigestUtil {
    synchronized static public String getDigest(String mesg) throws Exception {

        SHA1Digest digEng = new SHA1Digest();
        byte [] mesgBytes = mesg.getBytes();
        digEng.update(mesgBytes, 0, mesgBytes.length);
        byte [] digest = new byte[digEng.getDigestSize()];
        digEng.doFinal(digest, 0);
        // Encode the digest into ASCII format
        return (new String(Hex.encode(digest)));
    }
    synchronized static public byte[] getDigest(byte [] bytes) throws Exception {

        SHA1Digest digEng = new SHA1Digest();
        digEng.update(bytes, 0, bytes.length);
        byte [] digest = new byte[digEng.getDigestSize()];
        digEng.doFinal(digest, 0);
        return digest;
    }
}
