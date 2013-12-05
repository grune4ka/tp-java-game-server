package helpers;

import javax.servlet.http.Cookie;

public class CookieHelper {

	public static String getCookie(Cookie[] cookies, String key) {
		String value = null;

		if (cookies == null) {
			return null;
		}
		for (int count = 0; count < cookies.length; count++) {
        	if (cookies[count].getName().equals(key)) {
       			value = cookies[count].getValue();        		
        		break;
        	}
        }
		return value;
	}

}
