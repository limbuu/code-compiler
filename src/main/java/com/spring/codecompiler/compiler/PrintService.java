package com.spring.codecompiler.compiler;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class PrintService {
	
	public String print(String status, BufferedReader reader) {
		String output = new String();

		String line = "";
		System.out.println("************* " + status + "***********************");
		try {
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				output += line + "\n";
			}
		} catch (IOException e) {
			output = output + "Infinite Loop detected :" + e;
			System.out.println("Exception :" + output);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output.trim();

	}


}
