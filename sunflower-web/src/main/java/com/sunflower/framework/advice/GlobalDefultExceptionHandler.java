package com.sunflower.framework.advice;

import com.sunflower.framework.api.ResultDto;
import com.sunflower.framework.constants.error.CommonEnum;
import com.sunflower.framework.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sunflower 自定义全局异常处理
 */
@ControllerAdvice
public class GlobalDefultExceptionHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(GlobalDefultExceptionHandler.class);

	public GlobalDefultExceptionHandler() {
	}

	@ExceptionHandler({ BusinessException.class })
	public ResponseEntity<ResultDto<Object>> handleMissingServletRequestParameterException(
			BusinessException e) {
		logger.error(e.getCode() + "----------" + e.getMessage(), e);
		if (e.getCode().equalsIgnoreCase(CommonEnum.PARAM_VALID_FAIL.code())
				&& e.getData() instanceof HashMap) {
			Map<Integer, Object> errorMessagesMap = (Map<Integer, Object>) e.getData();
			for (Integer integer : errorMessagesMap.keySet()) {
				Object errorMessage = errorMessagesMap.get(integer);
				if (errorMessage instanceof HashMap) {
					Map<String, String> errorMessageMap = (HashMap<String, String>) errorMessage;
					for (String s : errorMessageMap.keySet()) {
						if (StringUtils.hasText(errorMessageMap.get(s))) {
							ResultDto<Object> error = ResultDto.error(e.getCode(),
									errorMessageMap.get(s));
							error.setData(e.getData());
							return new ResponseEntity<>(error, HttpStatus.OK);
						}
					}
				}
			}
		}

		ResultDto<Object> error = ResultDto.error(e.getCode(), e.getMessage());
		error.setData(e.getData());
		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class,
			HttpMessageNotReadableException.class, MethodArgumentNotValidException.class,
			BindException.class, ValidationException.class })
	public ResponseEntity<ResultDto<Object>> handleMissingServletRequestParameterException(
			Exception e) {
		logger.error(e.getMessage(), e);
		return new ResponseEntity<>(ResultDto.error(CommonEnum.HTTP_FAIL_400),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<ResultDto<Object>> handleHttpRequestMethodNotSupportedException(
			Exception e) {
		logger.error(e.getMessage(), e);
		return new ResponseEntity<>(ResultDto.error(CommonEnum.HTTP_FAIL_405),
				HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
	public ResponseEntity<ResultDto<Object>> handleHttpMediaTypeNotSupportedException(
			Exception e) {
		logger.error(e.getMessage(), e);
		return new ResponseEntity<>(ResultDto.error(CommonEnum.HTTP_FAIL_415),
				HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<ResultDto<Object>> handleOtherExceptions(Exception e) {
		logger.error(e.getMessage(), e);
		return new ResponseEntity<>(ResultDto.error(CommonEnum.HTTP_FAIL_500),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
