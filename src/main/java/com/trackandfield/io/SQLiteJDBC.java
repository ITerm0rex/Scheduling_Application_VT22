package com.trackandfield.io;

import java.sql.DriverManager;
import java.util.Properties;

public class SQLiteJDBC {
	public static String Athletes_SQL = """
			create table Athletes (
				Club varchar(64),
				Name varchar(64),
				Surname varchar(64),
				Sex varchar(1),
				Age integer(3),
				Running_Sprint_60 time,
				Running_Sprint_200 time,
				Running_Middle_800 time,
				Running_Middle_1500 time,
				Running_Long_3000 time,
				Running_Hurdles_60 time,
				Jumping_Long float,
				Jumping_Triple float,
				Jumping_High float,
				Jumping_Pole float,
				Throwing_Shot float
			);
			""";

	public static String CSV_FILE_PATH = "./resources/registration-list.csv";
	// public static String CSV_FILE_PATH = "./resources/schema-exempel.csv";

	public static String EXTENSION_PATH = "./sqlite-csv/csv.dll";

	public static void test() {
		try {
			var prop = new Properties();
			prop.setProperty("enable_load_extension", "true");
			Class.forName("org.sqlite.JDBC");
			try (var con = DriverManager.getConnection("jdbc:sqlite::memory:", prop);
					var stmt = con.createStatement()) {

				stmt.execute("select load_extension(\"" + EXTENSION_PATH + "\");");

				stmt.execute("CREATE VIRTUAL TABLE IF NOT EXISTS temp.csv USING csv("
						+ "filename = \"" + CSV_FILE_PATH + "\""
						// + ",header=yes"
						// + ",fsep=','"
						// + ",rsep='\n'"
						+ ",schema=\"" + Athletes_SQL + "\""
						+ ");");

				try (var rs = stmt.executeQuery("select * from csv;")) {
					var md = rs.getMetaData();
					var len = md.getColumnCount();
					while (rs.next()) {
						for (int i = 1; i <= len; i++)
							System.out.print("[" + rs.getObject(i) + "]");
						System.out.println("\n");
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		System.out.println("<<<Opened database/csv-file successfully>>>");
	}
}

final class SQLiteJDBCTest {
	public static void main(String args[]) {
		SQLiteJDBC.test();
	}
}