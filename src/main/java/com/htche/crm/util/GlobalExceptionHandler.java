package com.htche.crm.util;

import com.alibaba.fastjson.JSON;
import com.htche.crm.model.ApiResult;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by jankie on 16/8/15.
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    protected static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult handleSQLException(HttpServletRequest request, SQLException ex) {
        return handleLog(request, ex);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResult handleBadRequestException(HttpServletRequest request, BadRequestException ex) {
        return handleLog(request, ex);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiResult handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        return handleLog(request, ex);
    }

//    @ExceptionHandler(Exception.class)
//    public ApiResult handleException(HttpServletRequest request, Exception ex) {
//        return handleLog(request, ex);
//    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult handleSystemException(HttpServletRequest request, SystemException ex) {
        return handleLog(request, ex);
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResult handleException(HttpServletRequest request, RuntimeException ex) {
        return handleLog(request, ex);
    }

    private ApiResult handleLog(HttpServletRequest request, Exception ex) {
        Map parameter = request.getParameterMap();
        StringBuffer logBuffer = new StringBuffer();
        if (request != null) {
            logBuffer.append("  request method=" + request.getMethod());
            logBuffer.append("  url=" + request.getRequestURL());
            logBuffer.append("  token=" + request.getHeader("token"));
        }
        if (parameter != null) {
            if (!parameter.containsKey("avatar") || !parameter.containsKey("pic")) {
                logBuffer.append("  request parameter=" + JSON.toJSONString(parameter));
            }
        }
        if (ex != null) {
            logBuffer.append("  exception:" + ex);
        }
        logger.error(logBuffer.toString());
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(0);
        apiResult.setMessage(ex.getMessage().length() > 30 ? "网络请求异常" : ex.getMessage());
        return apiResult;
    }
}
