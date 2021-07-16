package com.nekoimi.boot.framework.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * nekoimi  2021/7/17 上午12:45
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class SampleErrorController implements ErrorController {
    @Autowired
    private ErrorAttributes errorAttributes;

    /**
     * 处理方法
     *
     * @param request
     * @return
     */
    @RequestMapping()
    public ResponseEntity<Map<String, Object>> errorHandle(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> body = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.EXCEPTION,
                ErrorAttributeOptions.Include.STACK_TRACE,
                ErrorAttributeOptions.Include.BINDING_ERRORS
        ));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
