package com.prm.exception;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class RestErrors {
	private List errors;

	public List getErrors() {
		return errors;
	}

	public void setErrors(List errors) {
		this.errors = errors;
	}
	
	
}
