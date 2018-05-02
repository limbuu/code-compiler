package com.spring.codecompiler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.codecompiler.api.CodeChallengeManager;
import com.spring.codecompiler.api.domain.CodeChallenge;

@RestController
@RequestMapping("/codeChallenge")
public class codeChallengeController {
	
	@Autowired
	CodeChallengeManager codeChallengeManager;
	
	@PostMapping(value="/add")
	public CodeChallenge addChallenge(@RequestBody CodeChallenge codeChallenge) {
		return this.codeChallengeManager.addNew(codeChallenge);
	}
	
	@GetMapping
	public List<CodeChallenge> findAllChallenge(){
		return this.codeChallengeManager.findAll();
	}

}
