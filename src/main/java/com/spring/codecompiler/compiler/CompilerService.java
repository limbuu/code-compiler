package com.spring.codecompiler.compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.ExecuteWatchdog;
import org.springframework.stereotype.Component;

import com.spring.codecompiler.exception.WatchDogException;

@Component
public class CompilerService {

	public Map<String, String> compileAndRunFile(String[] compileCommand,String[] runCommand, File rootDirectory) {
		Map<String, String> result = new HashMap<>();
		try {

			ProcessBuilder builder = new ProcessBuilder(compileCommand);
			builder.directory(rootDirectory);
			Process process = builder.start();

			if (process.getErrorStream().read() != -1) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				result.put("status", "compilationErrors");
				result.put("result", print("CompilationErrors", reader));
			}

			// wait the process to exit normally
			process.waitFor();
			if (process.exitValue() == 0) {
				builder = new ProcessBuilder(runCommand);
				builder.directory(new File("/mnt/ccDirectory/manshi/question"));
				process = builder.start();

				//get the PID of the Process
				long pid = getPidOfProcess(process);
				System.out.println("The PID is :" + pid);
				System.out.println("Process Name :" + process.getClass().getName());
				
				//execute watchdog to monitor time
				ExecuteWatchdog watchdog = executeWatchDog(process);

				OutputStream stdin = process.getOutputStream();
				InputStream stdout = process.getInputStream();
				InputStream errOut = process.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
				writer.write("Hello World\nBye Bye World\nHello Again World\nBye Bye Again World");
				writer.flush();
				writer.close();

				if (process.getErrorStream().read() != -1) {
					reader = new BufferedReader(new InputStreamReader(errOut));
					result.put("status", "RuntimeErrors");
					result.put("result", print("RuntimeErrors", reader));
				} else {
					result.put("status", "NoRuntimeErrors");
					result.put("result", print("NoRuntimeErrors", reader));
				}

				//kill watchdog if its still alives
				killWatchdogProcess(watchdog);
				//if process is still alive kill it by using processId
				if (process.isAlive()) {
					System.out.println("Process is still alive");
					
					killProcessByPID(process, pid);
				}

			} else {
				System.out.println("Process exited abnormally");
				process.destroy();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public Map<String, String> runFile(String[] runCommand, File rootDirectory) {
		Map<String, String> results = new HashMap<>();

		try {
			ProcessBuilder builder = new ProcessBuilder("java", "Solution");
			builder.directory(rootDirectory);
			Process process = builder.start();

			long pid = getPidOfProcess(process);
			System.out.println("The PID is :" + pid);
			System.out.println("Process Name :" + process.getClass().getName());
			ExecuteWatchdog watchdog = executeWatchDog(process);

			OutputStream stdin = process.getOutputStream();
			InputStream stdout = process.getInputStream();
			InputStream errOut = process.getErrorStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
			writer.write("Hello World\nBye Bye World\nHello Again World\nBye Bye Again World");
			writer.flush();
			writer.close();

			if (process.getErrorStream().read() != -1) {
				reader = new BufferedReader(new InputStreamReader(errOut));
				results.put("status", "RuntimeErrors");
				results.put("result", print("RuntimeErrors", reader));
			} else {
				results.put("status", "NoRuntimeErrors");
				results.put("result", print("NoRuntimeErrors", reader));
			}

			killWatchdogProcess(watchdog);
			try {
				if (process.isAlive()) {
					System.out.println("Process is still alive");
					killProcessByPID(process, pid);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	public static synchronized long getPidOfProcess(Process p) {
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

	public static Map<String, Object> printRuntime(Map<String, Object> runtimeResults, String status,
			BufferedReader reader) {
		String output = new String();
		System.out.println("************* " + status + "***********************");
		String line = "";
		try {

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				output += line + "\n";
			}
			runtimeResults.put("Output", output.trim());

		} catch (IOException e) {
			output = "Infinite Loop detected :" + e;
			System.out.println("Exception :" + output);
			runtimeResults.put("Output", output);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return runtimeResults;

	}

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

	public static synchronized void killProcessByPID(Process process, long pid)
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

	public synchronized static ExecuteWatchdog executeWatchDog(Process process) throws WatchDogException {
		ExecuteWatchdog watchdog = new ExecuteWatchdog(5000);
		watchdog.start(process);
		if (watchdog.isWatching()) {
			System.out.println("Watchdog has just started monitoring the process");
			try {
				watchdog.checkException();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new WatchDogException("Watchdog failed to start monitoring the process");
		}
		return watchdog;

	}

	public synchronized static void killWatchdogProcess(ExecuteWatchdog watchdog) {

		if (!watchdog.killedProcess()) {
			System.out.println("Watchdog process is not killed yet: " + watchdog.isWatching());
			watchdog.destroyProcess();
			if (watchdog.killedProcess() && !watchdog.isWatching()) {
				System.out.println("It is sucessfully killed....watchdog nolonger watching the process");
			} else {
				throw new WatchDogException("Watchdog could not be destroyed");
			}
		} else {
			System.out.println("Watchdog process is already killed");
		}
	}

}
