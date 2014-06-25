package com.prm.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestErrorHandler {
	
	@ExceptionHandler(PrmInputException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public RestErrorDTO processInputException(PrmInputException ex) {
		RestErrorDTO dto = ex.getRestErrorDTO();		
		return dto;
	}
	
	@ExceptionHandler(PrmServerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public RestErrorDTO processServerException(PrmServerException ex) {
		RestErrorDTO dto = ex.getRestErrorDTO();		
		return dto;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public RestErrors processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		return processFieldErrors(fieldErrors);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public RestErrorDTO processException(Exception ex) {
		RestErrorDTO dto = new RestErrorDTO();
		dto.setMessage(ex.getMessage());
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		dto.setRootCause(errors.toString());
		return dto;
	}

	
	private RestErrors processFieldErrors(List<FieldError> fieldErrors) {
		RestErrors errorLst = new RestErrors();
		List<RestErrorDTO> dtos = new ArrayList<RestErrorDTO>();
        for (FieldError fieldError: fieldErrors) {            
    		RestErrorDTO dto = new RestErrorDTO();
    		dto.setField(fieldError.getField());
    		dto.setMessage(fieldError.toString());
    		dtos.add(dto);
        }
        errorLst.setErrors(dtos);
        return errorLst;
    }
}
