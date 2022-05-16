package com.trackandfield.io;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVHandler {

	static Consumer<? super String[]> rowPrint = row -> {
		System.out.print("#:" + row.length + ", ");
		for (final var col : row) {
			System.out.print("[" + col + "]");
		}
		System.out.println();
	};

	public static String CSV_INPUT_FILE_PATH = "./resources/registration-list.csv";

	static public void CSVReader() {
		try (final var file = new FileReader(CSV_INPUT_FILE_PATH);
				final var reader = new CSVReader(file)) {

			reader.forEach(rowPrint);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static String CSV_OUTPUT_FILE_PATH = "./resources/test.csv";

	static public synchronized void CSVWriter(List<String[]> data) {
		try (final var file = new FileWriter(CSV_OUTPUT_FILE_PATH);
				final var writer = new CSVWriter(file)) {

			data.forEach(rowPrint);

			data.forEach(writer::writeNext);

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
}

final class CSVHandlerTest {
	public static void main(String[] args) {
		// CSVHandler.CSVReader();

		var data = Arrays.asList(
				new String[] { "aa", "bb" },
				new String[] { "cc", "dd" },
				new String[] { "å", "ä", "ö" });

		CSVHandler.CSVWriter(data);

	}
}
