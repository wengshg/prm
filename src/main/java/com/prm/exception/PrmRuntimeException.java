package com.prm.exception;

import org.springframework.core.NestedRuntimeException;

public abstract class PrmRuntimeException extends NestedRuntimeException {
	private String key;
	
	public PrmRuntimeException(String key, String msg) {
		super(msg);
		this.key = key;
	}

	public PrmRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public RestErrorDTO getRestErrorDTO() {
		RestErrorDTO dto = new RestErrorDTO();
		dto.setKey(key);
		dto.setMessage(getMessage());
		dto.setRootCause(getRootCause().toString());
		return dto;
	}
	
}
