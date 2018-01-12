/**
 * class helper berisi kumpulan-kumpulan berbagai method
 * yang dimanfaatkan oleh class lain.
 * @author mrizkir
 */
package id.go.kepriprov.kiisgateway.lib;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helper {
	public static String getAppLocation() {
		String f = new File("").getAbsolutePath()+File.separator;
		return f;
	}
	/**
	 * digunakan untuk membuat message SHA1
	 * @param password
	 * @return object String
	 * @throws NoSuchAlgorithmException
	 * @author reza
	 */
	public static Object createSHA1(String password) throws NoSuchAlgorithmException,Exception {
		
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(password.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	/**
	 * digunakan untuk membuat message MD5
	 * @param password
	 * @return object String
	 * @throws NoSuchAlgorithmException
	 * @author reza
	 */
	public static String createMD5(String toEncrypt) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(toEncrypt.getBytes());
			byte[] bytes = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(String.format("%02X", bytes[i]));
			}
			return sb.toString().toLowerCase();
		} catch (Exception exc) {
			return "";
		}
	}
}
