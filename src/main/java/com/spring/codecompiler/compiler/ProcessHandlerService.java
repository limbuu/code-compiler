package com.spring.codecompiler.compiler;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class ProcessHandlerService {
	
	public synchronized long getPidOfProcess(Process p) {
		long pid = -1;

		try {
			if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
				Field f = p.getClass().getDeclaredField("pid");
				f.setAccessible(true);
				pid = f.getLong(p);
				f.setAccessible(false);
			}
		} catch (Exception e) {
			pid = -1;
		}
		return pid;
	}

	
	public synchronized void killProcessByPID(Process process, long pid)
			throws IOException, InterruptedException {
		/*
		 * String command[] = {"kill -9 ",String.valueOf(pid)}; process = new
		 * ProcessBuilder(command).start();
		 */
		String cmd = "kill -9 " + pid;
		process = Runtime.getRuntime().exec(cmd);

		boolean exitValue = process.waitFor(5000, TimeUnit.MILLISECONDS);
		System.out.println("Process exited with the exit status :" + exitValue);
	}


}
