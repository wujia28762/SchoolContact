package com.schoolcontact.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	/** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }
	public static String enCorder(String pwd)
	{
//		 try  
//	        {  
//	          byte[] _ssoToken = pwd.getBytes("GBK");  
//	          String name = new String();  
//	          for (int i = 0; i < _ssoToken.length; i++) {  
//	              int asc = _ssoToken[i];  
//	              _ssoToken[i] = (byte) (asc + 27);  
//	              name = name + (asc + 27) + "%";  
//	          }  
//	          return name;  
//	        }catch(Exception e)  
//	        {  
//	          e.printStackTrace() ;  
//	          return null;  
//	        }  
		return pwd;
	}
	public static String deCorder(String token)
	{
//		try  
//        {  
//          String name = new String();  
//          java.util.StringTokenizer st=new java.util.StringTokenizer(token,"%");  
//          while (st.hasMoreElements()) {  
//            int asc =  Integer.parseInt((String)st.nextElement()) - 27;  
//            name = name + (char)asc;  
//          }  
//  
//          return name;  
//        }catch(Exception e)  
//        {  
//          e.printStackTrace() ;  
//          return null;  
//        }  
		return token;
	}
	public static String getEncoderByMd5(String sessionid)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		StringBuffer hexString = null;
		byte[] defaultBytes = sessionid.getBytes();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				if (Integer.toHexString(0xFF & messageDigest[i]).length() == 1) {
					hexString.append(0);
				}
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			messageDigest.toString();
			// System.out.println("sessionid "+sessionid+" md5 version is "+hexString.toString());
			sessionid = hexString + "";
		} catch (NoSuchAlgorithmException nsae) {

		}
	//	System.out.println(hexString.toString().toUpperCase());
		return hexString.toString().toUpperCase(Locale.getDefault());

	}

}
