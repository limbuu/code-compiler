package com.spring.codecompiler.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.codecompiler.compiler.CompilerMain;

@RestController
@RequestMapping("/compiler")
public class compilerController {

	@Autowired
	CompilerMain compilerMain;
	
	@PostMapping(value="/solve")
	public Map<String,String> solveChallenge(@RequestParam String language, @RequestParam String codeChallenegeId, @RequestBody String sourceCode) throws IOException{
		return this.compilerMain.solveChallenge(language, codeChallenegeId, sourceCode);
	}
}
