package com.example.schedulemanager.utils;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串通用类
 * 
 * @author zc
 */
@SuppressLint("SimpleDateFormat")
public class StringUtil {
	/** 邮件 */
	private static final String V_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
	/** 邮编 */
	private static final String V_ZIPCODE = "^\\d{6}$";
	/** 手机 */
	private static final String V_MOBILE = "^(1)[0-9]{10}$";
	/** ip地址 */
	private static final String V_IP4 = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";
	/** 电话号码的函数(包括验证国内区号,国际区号,分机号) */
	private static final String V_TEL = "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$";
	/** 身份证 */
	private static final String V_IDCARD = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
	/** 验证密码(数字和英文同时存在) */
	private static final String V_PASSWORD_REG = "[A-Za-z]+[0-9]";
	/** 验证密码长度(6-18位) */
	private static final String V_PASSWORD_LENGTH = "^\\d{6,18}$";
	
	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param string
	 * @return boolean
	 */
	public static boolean isEmpty(String string) {
		if (string == null || "".equals(string))
			return true;

		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * String字符串转换为Int类型
	 * 
	 * @param string
	 * @return
	 */
	public static int toInt(String string) {
		if (!StringUtil.isEmpty(string) && !string.equals("null")) {
			try {
				return Integer.parseInt(string);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * String字符串转换为Float类型
	 * 
	 * @param string
	 * @return
	 */
	public static float toFloat(String string) {
		if (!StringUtil.isEmpty(string)) {
			try {
				return Float.parseFloat(string);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * String字符串转换为Double类型
	 * 
	 * @param string
	 * @return
	 */
	public static double toDouble(String string) {
		if (!StringUtil.isEmpty(string)) {
			try {
				return Double.parseDouble(string);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return 0;
	}

	/**
	 * String字符串转换为Long类型
	 * 
	 * @param string
	 * @return
	 */
	public static long toLong(String string) {
		if (!StringUtil.isEmpty(string)) {
			try {
				return Long.parseLong(string);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * String字符串转换为Boolean类型
	 * 
	 * @param string
	 * @return
	 */
	public static boolean toBoolean(String string) {
		if (!StringUtil.isEmpty(string)) {
			try {
				return Boolean.parseBoolean(string);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date toDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			if (str != null && !str.equals("")) {
				date = format.parse(str);
			} else {
				date = new Date();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			date = new Date();
		}
		return date;
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param str
	 * @return date
	 */
	public static String DateToString(Date date, String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(date);
	}

	/**
	 * 日期字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static String DateToString(String str, String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			if (str != null && !str.equals("")) {
				if (str.length() == 13) {
					date = new Date(StringUtil.toLong(str));
				} else {
					date = format1.parse(str);
				}
			} else {
				date = new Date();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			date = new Date();
		}
		return format.format(date);
	}

	/**
	 * String日期转换为Long 
	 * @param formatDate("MM/dd/yyyy HH:mm:ss")	时间格式
	 * @param date("12/31/2013 21:08:00") 时间格式
	 * @return Long
	 * @throws ParseException
	 */
	public static Long DateToLong(String formatDate, String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
		Date dt = sdf.parse(date);
		return dt.getTime();
	}
	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendlyTime(String sdate) {
		Date date = null;
		try {
			date = new Date(Long.valueOf(sdate));
		} catch (Exception e) {
			date = new Date();
		}
		long diff = 0;
		Date dnow = new Date();
		String str = "";
		diff = (dnow.getTime() - date.getTime()) / 1000;

		if (diff == 0) {
			str = "刚刚";
		} else if (diff > 0 && diff < 60) {
			str = diff + "秒前";
		} else if (diff >= 60 && diff < 3600) {
			str = diff / 60 + "分钟前";
		} else if (diff >= 3600 && diff < 86400) { // 24 * 60 * 60 = 86400 秒
			str = diff / 3600 + "小时前";
		} else if (diff >= 86400 && diff <= 432000) { // 5 * 24 * 60 * 60 =
														// 432000 秒
			str = diff / 86400 + "天前";
		} else {
			SimpleDateFormat dateFormater = new SimpleDateFormat("MM-dd");
			str = dateFormater.format(date);
		}

		return str;
	}

	/**
	 * 文件大小文字转换（G、M、K、B）
	 * 
	 */
	public static String turnFileSize(long fileSize) {
		if (fileSize > 1024 * 1024 * 1024) {
			return String.format("%.2f", (float) fileSize / (1024 * 1024))
					+ "G";
		} else if (fileSize > 1024 * 1024) {
			return String.format("%.2f", (float) fileSize / (1024 * 1024))
					+ "M";
		} else if (fileSize > 1024) {
			return String.format("%.2f", (float) fileSize / 1024) + "K";
		}

		return fileSize + "B";
	}

	public static String GetShortString(String desc, int length) {
		if (desc != null) {
			if (desc.length() > length) {
				desc = desc.substring(0, length - 2) + "..";
				return desc;
			}
			return desc;
		} else {
			return "";
		}
	}

	/**
	 * 去除Html标签
	 * 
	 * @param string
	 * @return
	 */
	public static String clearHtmlTag(String string) {
		String newStr = string.replaceAll("\\&[a-zA-Z]{1,10};", "")
				.replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "")
				.replaceAll("\\/&[a-z]{2,4};/ig", "").replaceAll("\r", "")
				.replaceAll("\n", "");
		return newStr;
	}

	/**
	 * MD5
	 * 
	 * MD5的算法在RFC1321 中定义 在RFC 1321中，给出了Test suite用来检验你的实现是否正确： MD5 ("") =
	 * d41d8cd98f00b204e9800998ecf8427e MD5 ("a") =
	 * 0cc175b9c0f1b6a831c399e269772661 MD5 ("abc") =
	 * 900150983cd24fb0d6963f7d28e17f72 MD5 ("message digest") =
	 * f96b697d7cb7938d525a2f31aaf161d0 MD5 ("abcdefghijklmnopqrstuvwxyz") =
	 * c3fcd3d76192e4007dfb496cca67e13b
	 * 
	 * @param string
	 * @return
	 */
	public static String getMD5(String string) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(string.getBytes());
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
     return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 验证是不是邮箱地址
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean Email(String value) {
		return match(V_EMAIL, value);
	}
	
	/**
	 * 验证邮编
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean Zipcode(String value) {
		return match(V_ZIPCODE, value);
	}
	
	/**
	 * 验证是不是手机号码
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean Mobile(String value) {
		return match(V_MOBILE, value);
	}
	
	/**
	 * 验证是不是正确的IP地址
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IP4(String value) {
		return match(V_IP4, value);
	}
	
	/**
	 * 验证电话
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean Tel(String value) {
		return match(V_TEL, value);
	}
	
	/**
	 * 验证是不是正确的身份证号码
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IDcard(String value) {
		return match(V_IDCARD, value);
	}
	
	/**
	 * 验证密码(数字和英文同时存在)
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean Password_reg(String value) {
		return match(V_PASSWORD_REG, value);
	}
	
	/**
	 * 验证密码的长度(6~18位)
	 * 
	 * @param value
	 *            要验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean Number_length(String value) {
		return match(V_PASSWORD_LENGTH, value);
	}
	
	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
