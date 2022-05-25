package com.trackandfield;

public class App {
	public static void main(String[] args) {
		try {
			String configFileName = "./resources/config.json";
			if (args.length > 0)
				configFileName = args[0];

			new Schedule(configFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}