package com.sohu.mrd.videoDocId.utils;
import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * @author  Jin Guopan
 * @version 2016-11-22
 */
public class MD5Utils {
	public static String getMD5(String str) {
		String md5=DigestUtils.md5Hex(str);
		return md5;
	}
}
