package com.trackandfield;

import java.sql.*;

public class SQLiteJDBC {
	public static void run() {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			// c = DriverManager.getConnection("jdbc:sqlite:./resources/test.db");
			c = DriverManager.getConnection("jdbc:sqlite::memory:");

			var stmt = c.createStatement();

			{
				stmt.execute("""
						create table Athletes (
							Club varchar(32),
							Name varchar(32),
							Surname varchar(32),
							Sex varchar(1),
							Age intiger(10),
							Running_Sprint_60 date,
							Running_Sprint_200 date,
							Running_Middle_800 date,
							Running_Middle_1500 date,
							Running_Long_3000 date,
							Running_Hurdles_60 date,
							Jumping_Long float,
							Jumping_Triple float,
							Jumping_High float,
							Jumping_Pole float,
							Throwing_Shot float
						);
						""");
			}

			var rs = stmt.executeQuery("select 1+1;");

			while (rs.next())
				System.out.println(rs.getInt(1));

			stmt.closeOnCompletion();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}
}