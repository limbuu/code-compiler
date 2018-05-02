package com.spring.codecompiler.compiler;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.spring.codecompiler.api.CodeChallengeManager;
import com.spring.codecompiler.api.domain.CodeChallenge;
import com.spring.codecompiler.exception.BackEndException;
import com.spring.codecompiler.language.cplusplus.CPlusPlusService;
import com.spring.codecompiler.language.cprogram.CprogramService;
import com.spring.codecompiler.language.java.JavaService;
import com.spring.codecompiler.language.javascript.JavaScriptService;
import com.spring.codecompiler.language.python.PythonService;
import com.spring.codecompiler.language.ruby.RubyService;
import com.spring.codecompiler.language.shellscript.ShellScriptService;


@Component
public class CompilerMain {
	
	@Autowired
	CodeChallengeManager codeChallengeManager;
	@Autowired
	JavaService javaService;
	@Autowired
	JavaScriptService jsService;
	@Autowired
	PythonService pythonService;
	@Autowired
	CprogramService cprogramService;
	@Autowired
	CPlusPlusService cplusplusService;
	@Autowired
	RubyService rubyService;
	@Autowired
	ShellScriptService shellscriptService;

	public Map<String,String> solveChallenge(String language, String codeChallenegeId, String sourceCode) throws IOException {
		
		CodeChallenge codeChallenege = this.codeChallengeManager.find(codeChallenegeId);
		if(codeChallenege==null ||  StringUtils.isEmpty(codeChallenege.getAnswer().getAnswers())) {
			throw new BackEndException("CodeChallenge or CodeChallenge Answers is empty!!!");
		}
		
		switch (language.toLowerCase().toString()) {
		case "java":
			return this.javaService.solveJavaChallenge(codeChallenege, sourceCode);
		case "javaScript":
			return this.jsService.solveJavaScriptChallenge(codeChallenege, sourceCode);
		case "python":
			return this.pythonService.solvePythonChallenge(codeChallenege, sourceCode);
		case "cprogram":
			return this.cprogramService.solveCprogramChallenge(codeChallenege, sourceCode);
		case "cplusplus":
			return this.cplusplusService.solveCplusplusChallenge(codeChallenege, sourceCode);
		case "ruby":
			return this.rubyService.solveRubyChallenge(codeChallenege, sourceCode);
		case "shellscript":
			return this.shellscriptService.solveShellscriptChallenge(codeChallenege, sourceCode);
		default:
				throw new IllegalArgumentException("Given lanaguage cannot be compiled by the compiler");
		}
	}

}
