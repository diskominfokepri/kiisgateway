/**
 * class helper berisi kumpulan-kumpulan berbagai method
 * yang dimanfaatkan oleh class lain.
 * @author mrizkir
 */
package id.go.kepriprov.kiisgateway.lib;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class Helper {
	private static Pattern PRIVATE_ADDRESS_PATTERN = Pattern.compile(
			"(^127\\.)|(^192\\.168\\.)|(^10\\.)|(^172\\.1[6-9]\\.)|(^172\\.2[0-9]\\.)|(^172\\.3[0-1]\\.)|(^::1$)|(^[fF][cCdD])",
			Pattern.CANON_EQ);

	/**
	 * Extracts the "real" client IP address from the request. It analyzes request
	 * headers {@code REMOTE_ADDR}, {@code X-Forwarded-For} as well as
	 * {@code Client-IP}. Optionally private/local addresses can be filtered in
	 * which case an empty string is returned.
	 *
	 * @param request
	 *            HTTP request
	 * @param filterPrivateAddresses
	 *            true if private/local addresses (see
	 *            https://en.wikipedia.org/wiki/Private_network#Private_IPv4_address_spaces
	 *            and https://en.wikipedia.org/wiki/Unique_local_address) should be
	 *            filtered i.e. omitted
	 * @return IP address or empty string
	 */
	public static String getIPAddressFrom (HttpServletRequest request, boolean filterPrivateAddresses) {
		String ip = request.getRemoteAddr();

		String headerClientIp = request.getHeader("Client-IP");
		String headerXForwardedFor = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(ip) && StringUtils.isNotEmpty(headerClientIp)) {
			ip = headerClientIp;
		} else if (StringUtils.isNotEmpty(headerXForwardedFor)) {
			ip = headerXForwardedFor;
		}
		if (filterPrivateAddresses && isPrivateOrLocalAddress(ip)) {
			return StringUtils.EMPTY;
		} else {
			return ip;
		}
	}

	private static boolean isPrivateOrLocalAddress(String address) {
		Matcher regexMatcher = PRIVATE_ADDRESS_PATTERN.matcher(address);
		return regexMatcher.matches();
	}

	public static String getAppLocation() {
		String f = new File("").getAbsolutePath() + File.separator;
		return f;
	}

	/**
	 * digunakan untuk membuat message SHA1
	 * 
	 * @param password
	 * @return object String
	 * @throws NoSuchAlgorithmException
	 * @author reza
	 */
	public static Object createSHA1(String password) throws NoSuchAlgorithmException, Exception {

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
	 * 
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
