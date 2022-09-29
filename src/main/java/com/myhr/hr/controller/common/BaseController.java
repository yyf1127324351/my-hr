package com.myhr.hr.controller.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

	/**
	 * 获得request中的参数
	 *
	 * @return string object类型的map
	 */
	public HashMap<String, Object> getParametersMap(HttpServletRequest request) {
		HashMap<String, Object> hashMap = new HashMap<>();
		if (request == null) {
			request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
		}
		Map req = request.getParameterMap();
		if ((req != null) && (!req.isEmpty())) {
			Map<String, Object> p = new HashMap<>();
			Collection keys = req.keySet();
			for (Object key1 : keys) {
				String key = (String) key1;
				Object value = req.get(key);
				Object v;
				if (key.contains("[]")) {
					key = key.substring(0, key.length() - 2);
					p.put(key, value);
				}else {
					if ((value.getClass().isArray()) && (((Object[]) value).length > 0)) {
						if (((Object[]) value).length > 1) {
							v = value;
						} else {
							v = ((Object[]) value)[0];
						}
					} else {
						v = value;
					}

					if ((v != null) && ((v instanceof String))) {
						String s = ((String) v).trim();
						if (s.length() > 0) {
							p.put(key, v);
						}
					}
				}
			}
			hashMap.putAll(p);
			// 读取cookie
//			hashMap.putAll(ReadCookieMap(request));
		}

		/*easyui分页字段*/
		if (StringUtils.isNotBlank(request.getParameter("rows")) && StringUtils.isNotBlank(request.getParameter("page"))){
			int rows = Integer.valueOf(request.getParameter("rows"));
			int page = Integer.valueOf(request.getParameter("page"));
			int nowPage =(page - 1) * rows;
			hashMap.put("start", nowPage);
			hashMap.put("rows", rows);
		}
		/*easyui排序字段*/
		if (StringUtils.isNotBlank(request.getParameter("sort")) && StringUtils.isNotBlank(request.getParameter("order"))) {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			hashMap.put("sort", sort);
			hashMap.put("order", order);
		}
		return hashMap;
	}
//	/**
//	 * 将cookie封装到Map里面
//	 */
//	private static Map<String, String> ReadCookieMap(HttpServletRequest request) {
//		Map<String, String> cookieMap = new HashMap<>();
//		Cookie[] cookies = request.getCookies();
//		if (null != cookies) {
//			for (Cookie cookie : cookies) {
//				cookieMap.put(cookie.getName(), cookie.getValue());
//			}
//		}
//		return cookieMap;
//	}

}
