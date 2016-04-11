package com.tuibian.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.tuibian.util.JSONUtil;

import android.content.Context;

public class CommUtils {
	public static final int REQUEST_TIME_OUT_DATA = 30000;
	
	public static CookieStore gCookieStore = null;
	

	public static String encode(String code) {
		if (!isNull(code)) {
			try {
				return URLEncoder.encode(code, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return code;
	}

	public static String decode(String code) {
		if (!isNull(code)) {
			try {
				return URLDecoder.decode(code, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return code;
	}

	public static boolean isNull(String str) {
		if (str == null || str.length() == 0 || "null".equalsIgnoreCase(str)) {
			return true;
		}
		return false;
	}

	public static boolean isNotNull(String str) {
		return !isNull(str);
	}

	/**
	 * 去掉所有html标签
	 * 
	 * @param html
	 * @return
	 */
	public static String cutHtml(String html) {
		if (isNull(html)) {
			return "";
		}
		String result = "";
		result = html.replaceAll("(<[^/\\s][\\w]*)[\\s]*([^>]*)(>)", "$1$3")
				.replaceAll("<[^>]*>", "");
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isNullMap(Map<?, ?> map) {
		if (map == null || map.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotNullMap(Map<?, ?> map) {
		return !isNullMap(map);
	}

	public static boolean isNullList(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotNullList(List<?> list) {
		return !isNullList(list);
	}

	/**
	 * 计算精度(四舍五入)
	 */
	public static double round(double number, String partten) {
		if (partten == null || partten == "") {
			partten = "#.00";
		}
		return Double.parseDouble(new DecimalFormat(partten).format(number));
	}

	/**
	 * 把一个double类型的数据转换为字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String formatNum(double value) {
		String retValue = null;
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(0);
		df.setMaximumFractionDigits(2);
		retValue = df.format(value);
		retValue = retValue.replaceAll(",", "");
		return retValue;
	}

	/**
	 * 转换字符数字
	 * 
	 * @param doubleValue
	 * @return
	 */
	public static String formatNumStr(String doubleValue) {
		String doubleValueY = "0.00";
		if (isNull(doubleValue)) {
			doubleValueY = "0.00";
		} else if (doubleValue.indexOf("￥") != -1) {
			doubleValueY = doubleValue.replace("￥", "");
		} else {
			doubleValueY = doubleValue;
		}
		double b = Double.parseDouble(doubleValueY);
		if (doubleValue.indexOf("￥") != -1) {
			return "￥" + b;
		}
		return formatNum(b);
	}

	/**
	 * 通过httpPost的方式进行网络请求获取到返回结果
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String requestPath,
			Map<String, Object> requestValues) throws Exception {
		// 发出HTTP request
		HttpPost httpRequest = null;// 请求对象
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();// 请求值
			HttpResponse httpResponse;// 请求响应对象
			/* 建立HttpPost连接 */
			httpRequest = new HttpPost(requestPath);
			/* Post运作传送变数必须用NameValuePair[]阵列储存 */
			params = new ArrayList<NameValuePair>();
			Iterator<String> keys = requestValues.keySet().iterator();
			String key;
			while (keys.hasNext()) {
				key = keys.next();
				if (key != null && key.length() > 0) {
					Object value = requestValues.get(key);
					if (value != null) {
						params.add(new BasicNameValuePair(key, value.toString()));
					}
				}
			}
			httpRequest.getParams().setBooleanParameter(
					CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			HttpConnectionParams.setSoTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			httpRequest.setParams(httpParameters);
			// shenxy 设置压缩算法
			// httpRequest.setHeader("Accept-Encoding", "gzip");
			httpResponse = client.execute(httpRequest);
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == 200) {
				Header head = httpResponse.getEntity().getContentEncoding(); // 检查压缩算法
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				
		        // 下一次的Cookie的值，将使用上一次请求  
		        gCookieStore = client.getCookieStore();  
				return strResult;
			} else {
				throw new Exception("连接服务器失败:返回码" + code);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new Exception("连接超时", e1);
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
	}

	public static String doGet(String url, HashMap<String, String> paramMap)
			throws Exception {
		StringBuffer sb = new StringBuffer("?");
		String paramUrl = "";
		if (paramMap != null) {
			for (Map.Entry<String, String> map : paramMap.entrySet()) {
				sb.append(map.getKey() + "=" + map.getValue());
				sb.append("&");
			}
			paramUrl = sb.substring(0, sb.lastIndexOf("&"));
		}
		HttpGet get = new HttpGet(url + paramUrl);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);// 执行Post方法
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			HttpConnectionParams.setSoTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			String result = EntityUtils.toString(response.getEntity());
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				return result;
			} else {
				throw new Exception("连接服务器失败:返回码" + code);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("连接超时", e);
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
	}
	//add by jason 2015.8.29
	/**
	 * 通过httpPost的方式进行网络请求获取到返回结果，使用全局变量保存Session
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String doPostWithSession(String requestPath,
			Map<String, Object> requestValues) throws Exception {
		// 发出HTTP request
		HttpPost httpRequest = null;// 请求对象
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();// 请求值
			HttpResponse httpResponse;// 请求响应对象
			/* 建立HttpPost连接 */
			httpRequest = new HttpPost(requestPath);
			/* Post运作传送变数必须用NameValuePair[]阵列储存 */
			params = new ArrayList<NameValuePair>();
			Iterator<String> keys = requestValues.keySet().iterator();
			String key;
			while (keys.hasNext()) {
				key = keys.next();
				if (key != null && key.length() > 0) {
					Object value = requestValues.get(key);
					if (value != null) {
						params.add(new BasicNameValuePair(key, value.toString()));
					}
				}
			}
			httpRequest.getParams().setBooleanParameter(
					CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			HttpConnectionParams.setSoTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			httpRequest.setParams(httpParameters);
			
			//使用上一次的gCookieStore作为本次会话的CookieStore
			if(gCookieStore != null)
			{
				client.setCookieStore(gCookieStore);
				/*
				CookieStore cookieStore = client.getCookieStore();
				List<Cookie> list = gCookieStore.getCookies();  
		        for(Cookie o : list){  
		        	cookieStore.addCookie(o);
		        */  
		    }  
		        
			// shenxy 设置压缩算法
			// httpRequest.setHeader("Accept-Encoding", "gzip");
			httpResponse = client.execute(httpRequest);
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == 200) {
				Header head = httpResponse.getEntity().getContentEncoding(); // 检查压缩算法
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				gCookieStore = client.getCookieStore();
				
				return strResult;
			} else {
				gCookieStore = null;
				throw new Exception("连接服务器失败:返回码" + code);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new Exception("连接超时", e1);
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
	}
	//end by jason 2015.8.29
	//add by jason 2015.9.1
	public static File doDownload(Context context,String requestPath,String filename,
			Map<String, Object> requestValues, String fileExt) throws Exception {
		// 发出HTTP request
		HttpGet httpRequest = null;// 请求对象
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();// 请求值
			HttpResponse httpResponse;// 请求响应对象
			/* 建立HttpPost连接 */
			httpRequest  = new HttpGet (requestPath); 
			/* Post运作传送变数必须用NameValuePair[]阵列储存 */
			params = new ArrayList<NameValuePair>();
			Iterator<String> keys = requestValues.keySet().iterator();
			String key;
			while (keys.hasNext()) {
				key = keys.next();
				if (key != null && key.length() > 0) {
					Object value = requestValues.get(key);
					if (value != null) {
						params.add(new BasicNameValuePair(key, value.toString()));
					}
				}
			}
			httpRequest.getParams().setBooleanParameter(
					CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			HttpConnectionParams.setSoTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			httpRequest.setParams(httpParameters);
			// shenxy 设置压缩算法
			// httpRequest.setHeader("Accept-Encoding", "gzip");
			httpResponse = client.execute(httpRequest);
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == 200) {
				Header head = httpResponse.getEntity().getContentEncoding(); // 检查压缩算法
				
				HttpEntity entity = httpResponse.getEntity();  
	            InputStream is = entity.getContent();  
	            
	            File destDir = getCacheFile(context);
	            
	            if(filename == null)
	            	filename = getFileName(httpResponse) + "." + fileExt;  
	            
	            File file = null;
	            if(filename.indexOf("/") < 0 )//仅文件名，无路径名称
	            	file = new File(destDir,filename);
	            else
	            	file = new File(filename);
	            
	            //String filepath = file.getAbsolutePath();
	            
	            FileOutputStream fileout = new FileOutputStream(file);  
	            /** 
	             * 根据实际运行效果 设置缓冲区大小 
	             */
	            byte[] buffer=new byte[1024];  
	            int ch = 0;  
	            while ((ch = is.read(buffer)) != -1) {  
	                fileout.write(buffer,0,ch);  
	            }  
	            is.close();  
	            fileout.flush();  
	            fileout.close();
	            boolean bb = file.exists();
				return file;
			} else {
				throw new Exception("连接服务器失败:返回码" + code);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new Exception("连接超时", e1);
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
	}
	public static String uploadFile(String requestPath,String filename,
			Map<String, Object> requestValues) throws Exception {
		// 发出HTTP request
		String strResult = null;
		HttpPost httpRequest = null;// 请求对象
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();// 请求值
			HttpResponse httpResponse;// 请求响应对象
			/* 建立HttpPost连接 */
			httpRequest  = new HttpPost(requestPath);
			//httpRequest.addHeader("charset", HTTP.UTF_8); 
			File file = new File(filename);
			//MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("utf-8"));
			MultipartEntity entity = new MultipartEntity();
			ContentBody cbFile = new FileBody(file);
			entity.addPart("file", cbFile); // <input type="file" name="userfile" />  对应的
		    
			/* Post运作传送变数必须用NameValuePair[]阵列储存 */
			//params = new ArrayList<NameValuePair>();
			Iterator<String> keys = requestValues.keySet().iterator();
			String key;
			
			while (keys.hasNext()) {
				key = keys.next();
				if (key != null && key.length() > 0) {
					Object value = requestValues.get(key);
					if (value != null) {
						//params.add(new BasicNameValuePair(key, value.toString()));
						entity.addPart(key, new StringBody(value.toString(), Charset.forName("utf-8")));
						//entity.addPart(key, new StringBody(value.toString()));
					}
				}
			}
			httpRequest.setEntity(entity);
			
			httpRequest.getParams().setBooleanParameter(
					CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			HttpConnectionParams.setSoTimeout(httpParameters,
					REQUEST_TIME_OUT_DATA);
			httpRequest.setParams(httpParameters);
			// shenxy 设置压缩算法
			// httpRequest.setHeader("Accept-Encoding", "gzip");
			httpResponse = client.execute(httpRequest);
			
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == 200) {
				Header head = httpResponse.getEntity().getContentEncoding(); // 检查压缩算法
				strResult = EntityUtils.toString(httpResponse.getEntity());
				
			} else {
				throw new Exception("连接服务器失败:返回码" + code);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new Exception("连接超时", e1);
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
		return strResult;
	}
	/**
	 * 获取文件名
	 * @param response
	 * @return
	 */
	public static String getFileName(HttpResponse response) {
		
		String filename = getFileNameByResponse(response);
		if (filename == null) {
			filename = getRandomFileName();
		}
		return filename;
	}
	
	public static File getCacheFile(Context context) {
		File file = context.getExternalCacheDir();
		File myfile = new File(file, "jlsdoc");
		if (!myfile.exists()) {
			myfile.mkdirs();
		}
		return myfile;
	}
	
	/**
	 * 获取随机文件名
	 * @return
	 */
	public static String getRandomFileName() {
		return String.valueOf(System.currentTimeMillis());
	}
	/**
	 * 获取response header中Content-Disposition中的filename值
	 * @param response
	 * @return
	 */
	public static String getFileNameByResponse(HttpResponse response) {
		Header contentHeader = response.getFirstHeader("Content-Disposition");
		String filename = null;
		if (contentHeader != null) {
			HeaderElement[] values = contentHeader.getElements();
			if (values.length == 1) {
				NameValuePair param = values[0].getParameterByName("filename");
				if (param != null) {
					try {
						//filename = new String(param.getValue().toString().getBytes(), "utf-8");
						//filename=URLDecoder.decode(param.getValue(),"utf-8");
						filename = param.getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return filename;
	}
	public static void SaveFile(File f, String toSaveString)
			throws IOException {

		if (!f.exists()) {

			File file = new File(f.getParent());

			file.mkdirs();
			f.createNewFile();
		}
		FileOutputStream outStream = new FileOutputStream(f);
		outStream.write(toSaveString.getBytes());
		outStream.close();

	}
	public static String ReadFile(File f)
			throws IOException {

		String result = null;
		if (!f.exists()) {
			return null;
		}
		InputStream in = new FileInputStream(f);  
        
		byte b[]=new byte[(int)f.length()];     //创建合适文件大小的数组  
        in.read(b);    //读取文件中的内容到b[]数组  
        in.close();  
       
        result = new String(b);
        return result;
	}
	//end by jason 2015.9.1
	/**
	 * 转全角的函数(SBC case) 全角空格为12288，半角空格为32
	 * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
	 * 
	 * @param input
	 *            任意字符串
	 * @return 全角字符串
	 */
	public static String toSBC(String input) {
		if (isNull(input)) {
			return "";
		}
		// 半角转全角：
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	/**
	 * 转半角的函数(DBC case) 全角空格为12288，半角空格为32
	 * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
	 * 
	 * @param input
	 *            任意字符串
	 * @return 半角字符串
	 */
	public static String toDBC(String input) {
		if (isNull(input)) {
			return "";
		}
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 正则验证电子邮件
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (isNull(email)) {
			return false;
		}
		// String
		// str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		String str = "^(\\w)+(\\-\\_\\w)?(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 验证是否是手机号
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isMobilePhone(String phone) {
		if (isNull(phone)) {
			return false;
		}
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 正则验证固定电话
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if (isNull(phone)) {
			return false;
		}
		Pattern p = Pattern.compile("^((\\d{3,4})|\\d{3,4}-)?\\d{7,8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 验证是不是电话号码
	 * 
	 * @return
	 */
	public static boolean isMobileOrPhone(String phone) {
		if (isMobilePhone(phone) || isPhone(phone)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断有没有乱码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isGoodString(String str) {
		Pattern p = Pattern
				.compile("^[0-9]{0,}[a-zA-Z]{0,}[\u4e00-\u9fa5]{0,}$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static String getRandomNum() {
		String result = "";
		for (int i = 0; i < 6; i++) {
			int k = (int) (Math.random() * 9);
			result += k;
		}
		return result;
	}

	/**
	 * 子分类
	 * 
	 * @param dayTypeId
	 * @return
	 * @throws Exception
	 */
	public static String post(String requestUrl) {
		// 得到打开的链接对象
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		String result = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			url = new URL(requestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(15 * 1000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charset", "UTF-8");
			is = conn.getInputStream();
			int len = 0;
			byte[] bytes = new byte[1024];
			while ((len = is.read(bytes)) != -1) {
				bos.write(bytes, 0, len);
			}
			result = bos.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	// 中国移动
	public static String[] chinaMobiles = new String[] { "134", "135", "136",
			"137", "138", "139", "150", "151", "152", "157", "158", "159",
			"187", "188", "182" };
	// 中国联通
	public static String[] chinaUnion = new String[] { "130", "131", "132",
			"155", "156", "185", "186" };
	// 中国电信
	public static String[] chinaTelecom = new String[] { "133", "153", "180",
			"189" }; // 1349
	// 虚拟运营商_中国移动
	public static String[] vm_chinaMobiles = new String[] { "1705" };
	// 虚拟运营商_中国电信1700
	public static String[] vm_chinaUnion = new String[] { "1700" };
	// 虚拟运营商_中国联通1700
	public static String[] vm_chinaTelecom = new String[] { "1709" };

	public static final int is_chinaMobile = 0;
	public static final int is_chinaUnion = 1;
	public static final int is_chinaTelecom = 2;

	/**
	 * 判断手机号运营商
	 * 
	 * @param mobile
	 * @return
	 */
	public static int checkMobileType(String mobile) {
		if (CommUtils.isNull(mobile)) {
			return 100000;
		}
		if (mobile.length() >= 3) {
			String sbmobile = mobile.substring(0, 3);
			for (String sub : chinaMobiles) { // 移动
				if (sub.equals(sbmobile)) {
					return is_chinaMobile;
				}
			}
			for (String sub : chinaUnion) {// 联通
				if (sub.equals(sbmobile)) {
					return is_chinaUnion;
				}
			}
			for (String sub : chinaTelecom) {// 电信
				if (sub.equals(sbmobile)) {
					return is_chinaTelecom;
				}
			}
		}
		if (mobile.length() >= 4) {
			String sbmobile = mobile.substring(0, 4);
			for (String sub : vm_chinaMobiles) { // 移动
				if (sub.equals(sbmobile)) {
					return is_chinaMobile;
				}
			}
			for (String sub : vm_chinaUnion) { // 联通
				if (sub.equals(sbmobile)) {
					return is_chinaUnion;
				}
			}
			for (String sub : vm_chinaTelecom) { // 电信
				if (sub.equals(sbmobile)) {
					return is_chinaTelecom;
				}
			}
		}
		return 111;
	}

}
