package com.trackandfield.io;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVHandler {

	public static Consumer<? super String[]> rowPrint = row -> {
		// System.out.print("#:" + row.length + ", ");
		for (final var col : row) {
			System.out.print("[" + col + "]");
		}
		System.out.println();
	};

	static public List<String[]> CSVReader(String path) {
		try (final var file = new FileReader(path);
				final var reader = new CSVReader(file)) {

			return reader.readAll();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static public void CSVWriter(List<String[]> data, String path) {
		try (final var file = new FileWriter(path);
				final var writer = new CSVWriter(file)) {

			data.forEach(writer::writeNext);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static String CSV_INPUT_FILE_PATH = "./resources/registration-list.csv";
	private static String CSV_OUTPUT_FILE_PATH = "./resources/test.csv";

	public static final void test() {
		var data = CSVHandler.CSVReader(CSV_INPUT_FILE_PATH);

		CSVHandler.CSVWriter(data, CSV_OUTPUT_FILE_PATH);

		data.forEach(CSVHandler.rowPrint);

		// var test_data = Arrays.asList(
		// new String[] { "aa", "bb" },
		// new String[] { "cc", "dd" },
		// new String[] { "å", "ä", "ö" });

		// CSVHandler.CSVWriter(test_data, CSV_OUTPUT_FILE_PATH);
	}

}

final class CSVHandlerTest {
	public static void main(String[] args) {
		CSVHandler.test();
	}
}
