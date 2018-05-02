package com.spring.codecompiler.language.ruby;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.codecompiler.api.domain.CodeChallenge;
import com.spring.codecompiler.compiler.CompilerService;
import com.spring.codecompiler.compiler.FileMangerService;
import com.spring.codecompiler.constants.FileServiceConstants;
import com.spring.codecompiler.exception.BackEndException;

public class RubyService {
	
	@Autowired
	FileMangerService fileManager;
	
	@Autowired
	CompilerService compilerService;
	
	public Map<String, String> solveRubyChallenge(CodeChallenge codeChallenege,String sourceCode) throws IOException {
		Map<String, String> output = new HashMap<>();
		File rootDirectory = this.fileManager.createDirectory(FileServiceConstants.basic_file_Path);
		File file = this.fileManager.createFile(rootDirectory+FileServiceConstants.ruby_file_name);
		boolean writeStatus = this.fileManager.writeDataOnFile(file, sourceCode);
		
		String[] runCommand = {"ruby", file.getAbsolutePath()}; 
		if (writeStatus) {
			Map<String, String> result = this.compilerService.runFile(codeChallenege,runCommand,rootDirectory);
			if (result.get("result").equals(codeChallenege.getAnswer().getAnswers())) {
				output.put("status", "passed");
				output.put("output", result.get("Output"));
			}else {
				if(result.get("status").equals("CompilationErrors") || result.get("status").equals("RuntimeErrors")) {
					output.put("status", "failed");
					output.put("output", result.get("Output"));
				}else {
				output.put("status", "failed");
				output.put("output", result.get("Output"));
				}
			}

		}else {
			throw new BackEndException("Source Code couldnt be written in the file");
		}
		return output;
	}


}
