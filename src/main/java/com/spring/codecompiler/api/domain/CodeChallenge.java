package com.spring.codecompiler.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;



public class CodeChallenge implements Serializable{
	
	/**
	 * @author manshi
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String question;
	private Language language;
	private String code;
	private Answer answer;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Answer getAnswer() {
		return answer;
	}
	public void setAnswer(Answer answer) {
		this.answer = answer;
	} 
	
	public static class Answer{	
		private List<String> parameters = new ArrayList<>();
		private List<String> answers = new ArrayList<>();
		
		public List<String> getParameters() {
			return parameters;
		}
		public void setParameters(List<String> parameters) {
			this.parameters = parameters;
		}
		public List<String> getAnswers() {
			return answers;
		}
		public void setAnswers(List<String> answers) {
			this.answers = answers;
		}	
	}
	
}

