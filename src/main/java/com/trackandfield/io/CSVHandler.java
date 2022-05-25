package com.trackandfield.io;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVHandler {
	public static List<String[]> CSVReader(final String fileName) throws Exception {
		try (final var file = new FileReader(fileName);
				final var reader = new CSVReader(file)) {
			return reader.readAll();
		} catch (Exception e) {
			throw new Exception("File: " + fileName + "\n" + e, e);
		}
	}

	public static void CSVWriter(final String fileName, final List<String[]> data) throws Exception {
		try (final var file = new FileWriter(fileName);
				final var writer = new CSVWriter(file)) {
			writer.writeAll(data);
		} catch (Exception e) {
			throw new Exception("File: " + fileName + "\n" + e, e);
		}
	}
}