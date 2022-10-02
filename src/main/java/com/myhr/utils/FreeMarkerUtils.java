package com.myhr.utils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * free模板工具
 */
public class FreeMarkerUtils {

	private static Logger logger = LoggerFactory.getLogger(FreeMarkerUtils.class);

	private static final Configuration TEXT_CONFIGURATION = new Configuration();

	static {
		TEXT_CONFIGURATION.setTemplateLoader(new StringTemplateLoader());
		TEXT_CONFIGURATION.setCacheStorage(new NullCacheStorage());
		TEXT_CONFIGURATION.clearTemplateCache();
	}
	
	public static String process(Map<String, Object> map, String templatePath, String templateName) {
		Configuration cfg = new Configuration();
		FileTemplateLoader loader = null;
		Writer out = null;
		try {
			// 根据模板路径得到模板加载器
			loader = new FileTemplateLoader(new File(templatePath));
			cfg.setTemplateLoader(loader);
			cfg.setEncoding(Locale.CHINA, "UTF-8");
			// 根据模板名字得到模板
			Template template = cfg.getTemplate(templateName);
			template.setEncoding("UTF-8");
			template.setNumberFormat("0.##########");
			// 将数据模型和模板合并（返回的out-String为填充内容后的模板文件）
			out = new StringWriter();
			template.process(map, out);
			return out.toString();
		} catch (Exception e) {
			logger.error("异常信息----：", e);
			throw new RuntimeException("手动ftl渲染出错", e);
		} finally {
			out = null;
		}
	}

	public static String process(String templateContent, Map<String, Object> map) {
		try {
			String templateKey = Integer.toString(templateContent.hashCode());
			Template template = null;

			synchronized (TEXT_CONFIGURATION) {
				try {
					template = TEXT_CONFIGURATION.getTemplate(templateKey);
				}catch (FileNotFoundException e) {
					logger.info("未找到模板, 将进行初始化模板");
				}
				if(template == null) {
					StringTemplateLoader templateLoader = (StringTemplateLoader) TEXT_CONFIGURATION.getTemplateLoader();
					templateLoader.putTemplate(templateKey, templateContent);
					template = TEXT_CONFIGURATION.getTemplate(templateKey);
				}
			}

			Writer out = new StringWriter();
			template.process(map, out);
			return out.toString();
		}catch (IOException e) {
			logger.error(e.getMessage(), e);
			return "error";
		}catch (TemplateException e) {
			logger.error("模板异常: " + e.getMessage(), e);
		}
		return StringUtils.EMPTY;
	}
}

