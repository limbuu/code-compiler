package com.spring.codecompiler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.codecompiler.api.CodeChallengeManager;
import com.spring.codecompiler.api.domain.CodeChallenge;
import com.spring.codecompiler.repository.CodeChallengeRepository;

@Service
public class CodeChallengerManagerImpl implements CodeChallengeManager{
	
	@Autowired
	CodeChallengeRepository codeChallengeRepository;

	@Override
	public CodeChallenge addNew(CodeChallenge codecChallenge) {
		return this.codeChallengeRepository.insert(codecChallenge);
	}

	@Override
	public List<CodeChallenge> addAll(List<CodeChallenge> codecChallenges) {
		return this.codeChallengeRepository.save(codecChallenges);
	}

	@Override
	public CodeChallenge update(CodeChallenge codecChallenge) {
		return this.codeChallengeRepository.save(codecChallenge);
	}

	@Override
	public void remove(CodeChallenge codecChallenge) {
		this.codeChallengeRepository.delete(codecChallenge);
		
	}

	@Override
	public void removeBy(String id) {
		this.codeChallengeRepository.delete(id);
	}

	@Override
	public CodeChallenge find(String id) {
		return this.codeChallengeRepository.findOne(id);
	}

	@Override
	public List<CodeChallenge> findAll() {
		return this.codeChallengeRepository.findAll();
	}

	@Override
	public Long countRecord() {
		return this.codeChallengeRepository.count();
	}
	

	
}
