package top.seiei.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie 工具类
 */
public final class CookieUtils {

    final static Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    /**
     * 根据键名获取对应的  cookie 值，默认不解码
     * @param request request 对象
     * @param cookieName 键名
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 根据键名获取对应的  cookie 值
     * @param request  request 对象
     * @param cookieName  键名
     * @param isDecoder  是否解码（url）
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        // 获取 cookie 列表
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null; //  返回数值
        try {
            // 循环 cookie 列表
            for (int i = 0; i < cookieList.length; i++) {
                // 查找对应键名的 cookie 值
                if (cookieList[i].getName().equals(cookieName)) {
                    // 是否需要解码
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * 根据键名获取对应的  cookie 值
     * @param request request 对象
     * @param cookieName 键名
     * @param encodeString URLDecoder 解码的编码类型
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
        	 e.printStackTrace();
        }
        return retValue;
    }

    /**
     * 设置 cookie，默认有效时间为 -1，则关闭浏览器后边失效，
     * 默认不设置编码（如果对象为 JSON 对象，则需要进行编码，因为引号在 cookie 上是非法字段）
     * @param request request 对象
     * @param response response 对象
     * @param cookieName 键名
     * @param cookieValue 键值
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
            String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * 设置 cookie，默认不设置编码（如果对象为 JSON 对象，则需要进行编码，因为引号在 cookie 上是非法字段）
     * @param request request 对象
     * @param response response 对象
     * @param cookieName 键名
     * @param cookieValue 键值
     * @param cookieMaxage 有效时间
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
            String cookieValue, int cookieMaxage) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
    }

    /**
     * 设置 cookie，设置有效时间为 -1
     * @param request request 对象
     * @param response response 对象
     * @param cookieName 键名
     * @param cookieValue 键值
     * @param isEncode 是否加密
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
            String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

   /**
    * 设置 cookie
    * @param request request 对象
    * @param response response 对象
    * @param cookieName 键名
    * @param cookieValue 键值
    * @param cookieMaxage 有效时间
    * @param isEncode 是否编码
    */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
            String cookieValue, int cookieMaxage, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
    }

    /**
     * 设置 Cookie
     * @Description: 设置Cookie的值 在指定时间内生效,
     * @param request request 对象
     * @param response response 对象
     * @param cookieName 键名
     * @param cookieValue 键值
     * @param cookieMaxage 有效时间
     * @param encodeString 编码参数(指定编码)
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
            String cookieValue, int cookieMaxage, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString);
    }

    /**
     * 删除 Cookie
     * @param request request 对象
     * @param response response 对象
     * @param cookieName 键名
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
            String cookieName) {
        doSetCookie(request, response, cookieName, null, -1, false);
    }

    /**
     * 设置 cookie 的真实逻辑，删除 cookie 的逻辑也是通过本方法实现，默认使用 utf-8 编码
     * @param request request 对象
     * @param response response 对象
     * @param cookieName 键名
     * @param cookieValue 键值
     * @param cookieMaxage 有效时间
     * @param isEncode 是否编码
     */
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response,
            String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        try {
            // 把键值设置为空字符串，从而达到删除 cookie 的效果
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            // 设置有效时间
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            // 设置 cookie 的域名属性
            if (null != request) {
                // 获取当前请求的一级域名
            	String domainName = getDomainName(request);
                //logger.info("========== domainName: {} ==========", domainName);
                // 不能跨域设置
                if (!"localhost".equals(domainName)) {
                	cookie.setDomain(domainName);
                }
            }
            // 设置 cookie 的路径属性
            cookie.setPath("/");
            // 绑定到 HttpServletResponse，返回到客户端
            response.addCookie(cookie);
        } catch (Exception e) {
        	 e.printStackTrace();
        }
    }

    /**
     * 设置 cookie 的真实逻辑，选择 编码类型加密
     * @param request request 对象
     * @param response response 对象
     * @param cookieName 键名
     * @param cookieValue 键值
     * @param cookieMaxage	cookie生效的最大秒数
     * @param encodeString 编码类型
     */
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response,
            String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            // 设置有效时间
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            if (null != request) {
                // 设置 cookie 的域名属性
            	String domainName = getDomainName(request);
                logger.info("========== domainName: {} ==========", domainName);
                // 不能跨域设置
                if (!"localhost".equals(domainName)) {
                	cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
        	 e.printStackTrace();
        }
    }

    /**
     * 获取当前的请求的一级域名
     * @param request request 对象
     * @return
     */
    public static final String getDomainName(HttpServletRequest request) {
        String domainName = null;
        // 请求URL
        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            // 去除 http://
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            // 获取域名夹带端口号
            serverName = serverName.substring(0, end);
            // 去除端口号
            if (serverName.indexOf(":") > 0) {
            	String[] ary = serverName.split("\\:");
            	serverName = ary[0];
            }
            // 拼装域名
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3 && !isIp(serverName)) {
            	// www.xxx.com.cn
                domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }
        return domainName;
    }

    /**
     * 去掉IP字符串前后所有的空格
     * @param IP 域名
     * @return
     */
    public static String trimSpaces(String IP){
        while(IP.startsWith(" ")){  
               IP= IP.substring(1,IP.length()).trim();  
            }  
        while(IP.endsWith(" ")){  
               IP= IP.substring(0,IP.length()-1).trim();  
            }  
        return IP;  
    }

    /**
     * 判断是否是一个IP
     * @param IP 域名
     * @return
     */
    public static boolean isIp(String IP){
        boolean b = false;  
        IP = trimSpaces(IP);  
        if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){  
            String s[] = IP.split("\\.");  
            if(Integer.parseInt(s[0])<255)  
                if(Integer.parseInt(s[1])<255)  
                    if(Integer.parseInt(s[2])<255)  
                        if(Integer.parseInt(s[3])<255)  
                            b = true;  
        }  
        return b;  
    }  

}
