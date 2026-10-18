package com.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonReader {
	
	public static JsonObject read(String filePath) throws IOException {
		
		try (InputStream input = JsonReader.class.getClassLoader().getResourceAsStream(filePath)){
			if(input == null){
				throw new IllegalStateException("Json File Not found"+filePath);
			}
			
			try(InputStreamReader reader = new InputStreamReader(input,StandardCharsets.UTF_8)) {
				
				return JsonParser.parseReader(reader).getAsJsonObject();
				
			}
			
			catch (IOException e) {
				throw new IllegalStateException("unable to read JSON File"+filePath, e);
			}
		}
	}

}
