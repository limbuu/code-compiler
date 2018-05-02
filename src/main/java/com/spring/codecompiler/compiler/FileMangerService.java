package com.spring.codecompiler.compiler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileMangerService{
	
	public File createDirectory(String filePath) {
		File dir = new File(filePath);
		if (!dir.exists()) {
			if (dir.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		} else {
			System.out.println("Directory already exists!");
		}
		return dir;
	}

	public File createFile(String filePath) throws IOException {
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				if (file.createNewFile()) {
					System.out.println("File is created!");
				} else {
					System.out.println("Failed to create file!");
				}
			} else {
				System.out.println("File already exists!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	@SuppressWarnings("finally")
	public boolean writeDataOnFile(File filePath, String data) {
		boolean writeStatus = true;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(filePath);
			bw = new BufferedWriter(fw);
			bw.write(data);
		} catch (IOException e) {
			writeStatus = false;
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return writeStatus;
		}
	}

	public boolean removeDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File aFile : files) {
					removeDirectory(aFile);
				}
			}
			return dir.delete();
		} else {
			return dir.delete();
		}
	}
	

}
