package com.dcits.clbh.cloud.zuul.server.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie cookie = null;
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie ck : cookies) {
				if (ck.getName().equals(cookieName)) {
					cookie = ck;
				}
			}
		}
		return cookie;
	}

}
