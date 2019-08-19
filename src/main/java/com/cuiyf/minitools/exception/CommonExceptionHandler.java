package com.cuiyf.minitools.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class CommonExceptionHandler{
	private final static Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = OperateException.class)
    public Map errorHandler(OperateException ex) {
        Map map = new HashMap();
        map.put("code", ex.getCode());
        map.put("msg", ex.getMessage());
        logger.error(null,ex);
        return map;
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map myErrorHandler(Exception ex) {
        Map map = new HashMap();
        map.put("code", "200001");
        map.put("msg", "服务不可用");
        logger.error(null,ex);
        return map;
    }

}
