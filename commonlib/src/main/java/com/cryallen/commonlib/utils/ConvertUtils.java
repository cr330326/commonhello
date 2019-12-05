/**
 * ConvertUtils 2017-02-04
 * Copyright (c) 2017 TYYD Co.Ltd. All right reserved
 */
package com.cryallen.commonlib.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 基本工具类，与业务无关的公用方法，包含类型安全转换函数，SharedPreference封装
 * @author ChenRan
 * @since 2017-02-04
 * @version 1.0.0
 */
public class ConvertUtils {

	//URL字符串编码
	public static String UrlEncodeUnicode(final String s)
	{
		if (s == null)
		{
			return null;
		}
		final int length = s.length();
		final StringBuilder builder = new StringBuilder(length); // buffer
		for (int i = 0; i < length; i++)
		{
			final char ch = s.charAt(i);
			if ((ch & 0xff80) == 0)
			{
				if (ConvertUtils.IsSafe(ch))
				{
					builder.append(ch);
				}
				else if (ch == ' ')
				{
					builder.append('+');
				}
				else
				{
					builder.append('%');
					builder.append(ConvertUtils.IntToHex((ch >> 4) & 15));
					builder.append(ConvertUtils.IntToHex(ch & 15));
				}
			}
			else
			{
				builder.append("%u");
				builder.append(ConvertUtils.IntToHex((ch >> 12) & 15));
				builder.append(ConvertUtils.IntToHex((ch >> 8) & 15));
				builder.append(ConvertUtils.IntToHex((ch >> 4) & 15));
				builder.append(ConvertUtils.IntToHex(ch & 15));
			}
		}
		return builder.toString();
	}

	//整型转换为十六进制
	static char IntToHex(final int n)
	{
		if (n <= 9)
		{
			return (char) (n + 0x30);
		}
		return (char) ((n - 10) + 0x61);
	}

	//是否安全字符串 非转义字符串
	static boolean IsSafe(final char ch)
	{
		if ((((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))) || ((ch >= '0') && (ch <= '9')))
		{
			return true;
		}
		switch (ch)
		{
			case '\'':
			case '(':
			case ')':
			case '*':
			case '-':
			case '.':
			case '_':
			case '!':
				return true;
		}
		return false;
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	// MD5相关函数
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'  };

	/**
	 * MD5运算
	 *
	 * @param s
	 * @return String 返回密文
	 */
	public static String getMd5(final String s)
	{
		try
		{
			// Create MD5 Hash
			final MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.trim().getBytes());
			final byte messageDigest[] = digest.digest();
			return ConvertUtils.toHexString(messageDigest);
		}
		catch (final NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 转换为十六进制字符串
	 *
	 * @param b
	 *            byte数组
	 * @return String byte数组处理后字符串
	 */
	public static String toHexString(final byte[] b)
	{// String to byte
		final StringBuilder sb = new StringBuilder(b.length * 2);
		for (final byte element : b)
		{
			sb.append(ConvertUtils.HEX_DIGITS[(element & 0xf0) >>> 4]);
			sb.append(ConvertUtils.HEX_DIGITS[element & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * 检查是否安装了sd卡
	 *
	 * @return false 未安装
	 */
	public static boolean sdcardMounted()
	{
		final String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
		{
			return true;
		}
		return false;
	}

	/**
	 * 获取SD卡剩余空间的大小
	 *
	 * @return long SD卡剩余空间的大小（单位：byte）
	 */
	public static long getSDSize()
	{
		final String str = Environment.getExternalStorageDirectory().getPath();
		final StatFs localStatFs = new StatFs(str);
		final long blockSize = localStatFs.getBlockSize();
		return localStatFs.getAvailableBlocks() * blockSize;
	}

	public static final void saveObject(String path, Object saveObject) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		File f = new File(path);
		try {
			fos = new FileOutputStream(f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(saveObject);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static final Object restoreObject(String path) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Object object = null;
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		try {
			fis = new FileInputStream(f);
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
			return object;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * Object类型转Int
	 */
	public static final int convertToInt(Object value,int defaultValue) {
		if(value == null || "".equals(value.toString().trim())){
			return defaultValue;
		}
		try{
			return Integer.valueOf(value.toString());

		}catch (Exception e) {
			try {
				return Double.valueOf(value.toString()).intValue();
			}
			catch (Exception e1){
				return defaultValue;
			}
		}
	}

	/**
	 * Object类型转Long
	 */
	public static final Long convertToLong(Object value,Long defaultValue) {
		if(value == null || "".equals(value.toString().trim())){
			return defaultValue;
		}
		try{
			return Long.parseLong(value.toString());

		}catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Object类型转Double
	 */
	public static final Double convertToDouble(Object value,Double defaultValue) {
		if(value == null || "".equals(value.toString().trim())){
			return defaultValue;
		}
		try{
			return Double.parseDouble(value.toString());

		}catch (Exception e) {
			return defaultValue;
		}
	}

	/***
	 * 截取字符串
	 *
	 * @param start
	 *            从那里开始，0算起
	 * @param num
	 *            截取多少个
	 * @param str
	 *            截取的字符串
	 * @return
	 */
	public static String getSubString(int start, int num, String str) {
		if (str == null) {
			return "";
		}
		int leng = str.length();
		if (start < 0) {
			start = 0;
		}
		if (start > leng) {
			start = leng;
		}
		if (num < 0) {
			num = 1;
		}
		int end = start + num;
		if (end > leng) {
			end = leng;
		}
		return str.substring(start, end);
	}

}