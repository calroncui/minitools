package com.cuiyf.minitools.service.impl;

import com.cuiyf.minitools.controller.Text2SpeechController;
import com.cuiyf.minitools.exception.OperateException;
import com.cuiyf.minitools.model.ReqConvert;
import com.cuiyf.minitools.service.Text2SpeechService;
import com.cuiyf.minitools.tools.text2Speech.MainMethod;
import com.cuiyf.minitools.tools.webspider.WebSpider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Text2SpeechServiceImpl implements Text2SpeechService{
    private final static Logger logger = LoggerFactory.getLogger(Text2SpeechServiceImpl.class);
    @Autowired
    private MainMethod mainMethod;

    @Override
    public String text2Speech(ReqConvert req) {
        if(StringUtils.isBlank(req.getUrl()) && StringUtils.isBlank(req.getText()) ){
            logger.error("参数未指定");
            throw new OperateException("参数未指定","100001");
        }

        String text = null;
        if(!StringUtils.isBlank(req.getUrl())){
            text = WebSpider.getContentByUrl(req.getUrl());
        }else{
            text = req.getText();
        }
        return mainMethod.main(text);
    }
}
