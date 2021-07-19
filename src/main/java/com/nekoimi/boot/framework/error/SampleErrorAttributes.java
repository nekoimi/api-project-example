package com.nekoimi.boot.framework.error;

import com.nekoimi.boot.framework.contract.IError;
import com.nekoimi.boot.framework.error.enums.Errors;
import com.nekoimi.boot.framework.error.exception.BaseRuntimeException;
import com.nekoimi.boot.framework.error.exception.RequestValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * nekoimi  2021/7/17 上午12:58
 */
@Slf4j
public class SampleErrorAttributes extends DefaultErrorAttributes {
    private ErrorAttributes errorAttributes;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        errorAttributes = ErrorAttributes.of();
        log.debug("webRequest: " + webRequest.getClass());
        ServletWebRequest request = (ServletWebRequest) webRequest;
        addMethodAndPath(request);
        addErrorDetails(request);
        return errorAttributes.build();
    }

    private void addMethodAndPath(ServletWebRequest request) {
        errorAttributes.withMethod(Objects.requireNonNull(request.getHttpMethod()).name());
        String path = (String) request.getAttribute("javax.servlet.error.request_uri", RequestAttributes.SCOPE_REQUEST);
        if (path != null) {
            errorAttributes.withPath(path);
        }
    }

    private void addErrorDetails(ServletWebRequest request) {
        Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code", RequestAttributes.SCOPE_REQUEST);
        if (code != null) {
            try {
                HttpStatus httpStatus = HttpStatus.valueOf(code);
                IError iError = null;
                if (httpStatus.is5xxServerError()) {
                    iError = buildHttpStatusServerError(httpStatus);
                } else if (httpStatus.is4xxClientError()) {
                    iError = buildHttpStatusClientError(httpStatus);
                }
                if (iError != null) {
                    errorAttributes.withCode(iError.getCode());
                    errorAttributes.withMessage(iError.getMessage());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        Throwable error = getError(request);
        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = error.getCause();
            }
            log.debug(error.getClass().toString());
            if (error instanceof HttpMessageNotReadableException) {
                error = new RequestValidationException("Request body is missing!");
            }

            if (error instanceof BaseRuntimeException) {
                errorAttributes.withCode(((BaseRuntimeException) error).getCode());
                errorAttributes.withMessage(error.getMessage());
            } else {
                addErrorMessage(error);
            }
        } else {
            String message = (String) request.getAttribute("javax.servlet.error.message", RequestAttributes.SCOPE_REQUEST);
            if (message != null) {
                errorAttributes.withMessage(message);
            }
        }
    }

    private void addErrorMessage(Throwable error) {
        BindingResult result = extractBindingResult(error);
        if (result == null) {
            errorAttributes.withMessage(error.getMessage());
        } else {
            if (result.hasErrors()) {
                List<FieldError> fieldErrors = result.getFieldErrors();
                StringBuilder errorBuilder = new StringBuilder(fieldErrors.size() * 16);
                for (int i = 0; i < fieldErrors.size(); i++) {
                    if (i > 0) {
                        errorBuilder.append(", ");
                    }
                    errorBuilder.append(fieldErrors.get(i).getField());
                    errorBuilder.append(":");
                    errorBuilder.append(fieldErrors.get(i).getDefaultMessage());
                }
                errorAttributes.withMessage(errorBuilder.toString());
            }
        }
    }

    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof BindingResult) {
            return (BindingResult) error;
        }
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }
        return null;
    }

    private IError buildHttpStatusClientError(HttpStatus status) {
        switch (status) {
            case BAD_REQUEST:
                return Errors.HTTP_STATUS_BAD_REQUEST;
            case UNAUTHORIZED:
                return Errors.HTTP_STATUS_UNAUTHORIZED;
            case FORBIDDEN:
                return Errors.HTTP_STATUS_FORBIDDEN;
            case NOT_FOUND:
                return Errors.HTTP_STATUS_NOT_FOUND;
            case METHOD_NOT_ALLOWED:
                return Errors.HTTP_STATUS_METHOD_NOT_ALLOWED;
        }
        return Errors.DEFAULT_CLIENT_ERROR;
    }

    private IError buildHttpStatusServerError(HttpStatus status) {
        log.error("Server Error! " + status);
        return Errors.DEFAULT_SERVER_ERROR;
    }


    private static final class ErrorAttributes {
        private int code = Errors.DEFAULT_SERVER_ERROR.getCode();
        private String message = Errors.DEFAULT_SERVER_ERROR.getMessage();
        private String method = HttpMethod.GET.name();
        private String path = "/";
        private LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));

        private ErrorAttributes() {
        }

        public static ErrorAttributes of() {
            return new ErrorAttributes();
        }

        public void withCode(int code) {
            this.code = code;
        }

        public void withMessage(String message) {
            if (StringUtils.isNotBlank(message)) {
                this.message = message;
            }
        }

        public void withMethod(String method) {
            this.method = method;
        }

        public void withPath(String path) {
            this.path = path;
        }

        public void withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public Map<String, Object> build() {
            return new LinkedHashMap<>() {{
                put("code", code);
                put("message", message);
                put("method", method);
                put("path", path);
                put("timestamp", timestamp.toInstant(ZoneOffset.UTC).toEpochMilli());
            }};
        }
    }
}
