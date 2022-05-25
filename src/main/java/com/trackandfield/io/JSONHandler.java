package com.trackandfield.io;

import java.io.FileReader;

import com.google.gson.Gson;

public class JSONHandler {
	public static <T> T ConfigReader(final String fileName, final Class<T> format) throws Exception {
		try (final var reader = new FileReader(fileName)) {
			final var gson = new Gson();
			return gson.fromJson(reader, format);
		} catch (Exception e) {
			throw new Exception("File: " + fileName + "\n" + e, e);
		}
	}
}