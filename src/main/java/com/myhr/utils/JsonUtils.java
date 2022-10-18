package com.myhr.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class JsonUtils {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	static {

		OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
		OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		OBJECT_MAPPER.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY,true);
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OBJECT_MAPPER.registerModule(new JavaTimeModule());
	}


	public static String toJson(Object object) {
		try {
			return OBJECT_MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void writeJson(Object object, OutputStream outputStream) {
		try {
			OBJECT_MAPPER.writeValue(outputStream, object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//反序列化复杂对象，如new TypeReference<Result<Test>>() {}
	public static <T> T fromJson(String jsonString, com.fasterxml.jackson.core.type.TypeReference<T> typeRef) {
		try {
			return (T)OBJECT_MAPPER.readValue(jsonString, typeRef);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(String json, Class<T> clazz)  {
		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> List<T> toList(String json, Class<T> clazz) {
		try {
			JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(ArrayList.class, clazz);
			return (List) OBJECT_MAPPER.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>(0);
	}

	public static <T> T toObject(InputStream inputStream, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(inputStream, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> toMap(String json) {
		try {
			return OBJECT_MAPPER.readValue(json, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HashMap<>(0);
	}

	public static Map<String, Object> InputStream(String json) {
		try {
			return OBJECT_MAPPER.readValue(json, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HashMap<>(0);
	}



}
