package com.spring.codecompiler.compiler;

import org.apache.commons.exec.ExecuteWatchdog;
import org.springframework.stereotype.Service;

import com.spring.codecompiler.exception.WatchDogException;

@Service
public class WatchdogService {
	
	public synchronized  ExecuteWatchdog executeWatchDog(Process process) throws WatchDogException {
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

	public synchronized  void killWatchdogProcess(ExecuteWatchdog watchdog) {

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
