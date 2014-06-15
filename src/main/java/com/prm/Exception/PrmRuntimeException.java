package com.prm.Exception;

import org.springframework.core.NestedRuntimeException;

public class PrmRuntimeException extends NestedRuntimeException {

	public PrmRuntimeException(String msg) {
		super(msg);
	}

	public PrmRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
