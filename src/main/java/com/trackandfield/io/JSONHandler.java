package com.trackandfield.io;

import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JSONHandler {
	static private class json_stucture {
		List<JsonObject> stations;
	}

	static String JSON_PATH = "./resources/format.json";

	public static void test() {
		var gson = new Gson();
		try (var reader = new FileReader(JSON_PATH)) {

			var obj = gson.fromJson(reader, json_stucture.class);

			var ress = obj.stations.get(0).get("running").getAsJsonArray();

			for (var res : ress)
				System.out.println(res.getAsInt());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

final class JSONHandlerTest {
	public static void main(String args[]) {
		JSONHandler.test();
	}
}