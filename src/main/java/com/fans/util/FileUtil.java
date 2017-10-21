package com.fans.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {

	public static String getFileMD5(Path fileName) {
		File file = fileName.toFile();
		if (!file.exists() || !file.isFile()) {
			return "";
		}

		byte[] buffer = new byte[2048];
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			FileInputStream in = new FileInputStream(file);
			while (true) {
				int len = in.read(buffer, 0, 2048);
				if (len != -1) {
					digest.update(buffer, 0, len);
				} else {
					break;
				}
			}
			in.close();

			byte[] md5Bytes = digest.digest();
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();

			// String hash = new BigInteger(1,digest.digest()).toString(16);
			// return hash;

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
