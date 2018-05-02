package com.spring.codecompiler.compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.exec.ExecuteWatchdog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.codecompiler.api.domain.CodeChallenge;


@Service
public class CompilerService {

	@Autowired
	FileMangerService fileManagerService;
	@Autowired
	PrintService printService;
	@Autowired
	ProcessHandlerService processHandlerService;
	@Autowired
	WatchdogService watchdogService;
	
	public Map<String, String> compileAndRunFile(CodeChallenge codeChallenege,String[] compileCommand,String[] runCommand, File rootDirectory) {
		Map<String, String> result = new HashMap<>();
		try {

			ProcessBuilder builder = new ProcessBuilder(compileCommand);
			builder.directory(rootDirectory);
			Process process = builder.start();

			if (process.getErrorStream().read() != -1) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				result.put("status", "compilationErrors");
				result.put("result", this.printService.print("CompilationErrors", reader));
			}

			// wait the process to exit normally
			process.waitFor();
			if (process.exitValue() == 0) {
				builder = new ProcessBuilder(runCommand);
				builder.directory(rootDirectory);
				process = builder.start();

				//get the PID of the Process
				long pid = this.processHandlerService.getPidOfProcess(process);
				System.out.println("The PID is :" + pid);
				System.out.println("Process Name :" + process.getClass().getName());
				
				//execute watchdog to monitor time
				ExecuteWatchdog watchdog = this.watchdogService.executeWatchDog(process);

				OutputStream stdin = process.getOutputStream();
				InputStream stdout = process.getInputStream();
				InputStream errOut = process.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
				if(!codeChallenege.getAnswer().getParameters().isEmpty() && !codeChallenege.getAnswer().getAnswers().isEmpty()){
					writer.write(codeChallenege.getAnswer().getParameters().toString());
					writer.flush();
					writer.close();
				}
				

				if (process.getErrorStream().read() != -1) {
					reader = new BufferedReader(new InputStreamReader(errOut));
					result.put("status", "RuntimeErrors");
					result.put("result", this.printService.print("RuntimeErrors", reader));
				} else {
					result.put("status", "NoRuntimeErrors");
					result.put("result", this.printService.print("NoRuntimeErrors", reader));
				}

				//kill watchdog if its still alives
				this.watchdogService.killWatchdogProcess(watchdog);
				//if process is still alive kill it by using processId
				if (process.isAlive()) {
					System.out.println("Process is still alive");
					
					this.processHandlerService.killProcessByPID(process, pid);
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

	public Map<String, String> runFile(CodeChallenge codeChallenege,String[] runCommand, File rootDirectory) {
		Map<String, String> results = new HashMap<>();

		try {
			ProcessBuilder builder = new ProcessBuilder(runCommand);
			builder.directory(rootDirectory);
			Process process = builder.start();

			long pid = this.processHandlerService.getPidOfProcess(process);
			ExecuteWatchdog watchdog = this.watchdogService.executeWatchDog(process);

			OutputStream stdin = process.getOutputStream();
			InputStream stdout = process.getInputStream();
			InputStream errOut = process.getErrorStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
			if(!codeChallenege.getAnswer().getParameters().isEmpty() && !codeChallenege.getAnswer().getAnswers().isEmpty()){
				writer.write(codeChallenege.getAnswer().getParameters().toString());
				writer.flush();
				writer.close();
			}
			

			if (process.getErrorStream().read() != -1) {
				reader = new BufferedReader(new InputStreamReader(errOut));
				results.put("status", "RuntimeErrors");
				results.put("result", this.printService.print("RuntimeErrors", reader));
			} else {
				results.put("status", "NoRuntimeErrors");
				results.put("result", this.printService.print("NoRuntimeErrors", reader));
			}

			this.watchdogService.killWatchdogProcess(watchdog);

			if (process.isAlive()) {
				System.out.println("Process is still alive");
				this.processHandlerService.killProcessByPID(process, pid);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	
	
	
	
}
