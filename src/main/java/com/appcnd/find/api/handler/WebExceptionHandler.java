package com.appcnd.find.api.handler;

import com.appcnd.find.api.pojo.result.JsonResult;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author nihao 2018/11/23
 */
@RestControllerAdvice
public class WebExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    public JsonResult errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        String errorMsg;
        int code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        if (e instanceof HttpMessageNotReadableException) {
            errorMsg = "【参数解析失败】" + e.getMessage();
            code = HttpStatus.SC_BAD_REQUEST;
        }
        else if (e instanceof HttpRequestMethodNotSupportedException) {
            errorMsg = "【不支持当前请求方法】" + e.getMessage();
            code = HttpStatus.SC_METHOD_NOT_ALLOWED;
        }
        else if (e instanceof HttpMediaTypeNotSupportedException) {
            errorMsg = "【不支持当前媒体类型】" + e.getMessage();
            code = HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE;
        }
        else if (e instanceof MissingServletRequestParameterException) {
            errorMsg = "【请求参数不全】" + e.getMessage();
            code = HttpStatus.SC_BAD_REQUEST;
        }
        else {
            errorMsg = "抱歉，系统异常，异常信息：" + e.getMessage();
            LOGGER.error("RequestURL:{}", request.getRequestURL(), e);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        return JsonResult.fail(errorMsg).pull("code", code);
    }

}
