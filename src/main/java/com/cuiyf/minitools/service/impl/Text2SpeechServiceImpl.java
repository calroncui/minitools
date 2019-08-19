package com.cuiyf.minitools.service.impl;

import com.alibaba.fastjson.JSON;
import com.cuiyf.minitools.exception.OperateException;
import com.cuiyf.minitools.model.Record;
import com.cuiyf.minitools.model.ReqConvert;
import com.cuiyf.minitools.model.ReqList;
import com.cuiyf.minitools.model.ResRecord;
import com.cuiyf.minitools.service.Text2SpeechService;
import com.cuiyf.minitools.tools.redis.RedisUtil;
import com.cuiyf.minitools.tools.text2Speech.MainMethod;
import com.cuiyf.minitools.tools.webspider.WebSpider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class Text2SpeechServiceImpl implements Text2SpeechService{
    private final static Logger logger = LoggerFactory.getLogger(Text2SpeechServiceImpl.class);
    @Autowired
    private MainMethod mainMethod;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String text2Speech(ReqConvert req) {
        if(StringUtils.isBlank(req.getUrl()) && StringUtils.isBlank(req.getText()) ){
            logger.error("参数未指定");
            throw new OperateException("参数未指定","100001");
        }

        if(StringUtils.isBlank(req.getOpenid())){
            logger.error("用户未指定");
            throw new OperateException("用户未指定","100002");
        }

        String redisKey = req.getOpenid()+"_audios";

        // 检查是否重复操作
        long length = redisUtil.lGetListSize(redisKey);
        if(length > 0){
            List<Object> objs = redisUtil.lGet(redisKey,0,length);
            if(!CollectionUtils.isEmpty(objs)){
                for (Object obj : objs) {
                    if(obj == null){
                        continue;
                    }
                    Record rec = (Record)JSON.parse(obj.toString());
                    if(rec != null){
                        if(StringUtils.isNotBlank(req.getUrl())){
                            if(StringUtils.equals(rec.getUrl(),req.getUrl())){
                                return rec.getAudioUrl();
                            }
                        }
                        if(StringUtils.isNotBlank(req.getText())){
                            if(StringUtils.equals(rec.getText(),req.getText())){
                                return rec.getAudioUrl();
                            }
                        }
                    }
                }
            }
        }

        Record record = new Record();

        String text = null;
        if(StringUtils.isNotBlank(req.getUrl())){
            text = WebSpider.getContentByUrl(req.getUrl());
        }else{
            text = req.getText();
        }

        // 做最后的替换
        // 1.1 换行等替换为逗号
        text = text.trim().replaceAll("[\\t\\n\\r]", ",");

        // 1.2 一个或者多个空格 替换为逗号
        text = text.replaceAll("[' ']+", ",");


        if(StringUtils.isBlank(text)){
            logger.error("获取文本为空");
            throw new OperateException("获取文本为空","200004");
        }

        if(StringUtils.isNotBlank(req.getUrl())){
            record.setTitle(req.getUrl());
        }else{
            record.setTitle(text.substring(0,text.length() > 20 ? 20 : text.length()));
        }

        String audioUrl = mainMethod.main(text);

        record.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        record.setUrl(req.getUrl());
        record.setText(req.getText());
        record.setAudioUrl(audioUrl);

        redisUtil.lSet(redisKey,record,0);

        return audioUrl;
    }

    @Override
    public List<ResRecord> list(ReqList req) {
        if(StringUtils.isBlank(req.getOpenid())){
            logger.error("用户未指定");
            throw new OperateException("用户未指定","100002");
        }

        List<ResRecord> list = new ArrayList<>();
        String redisKey = req.getOpenid()+"_audios";
        long length = redisUtil.lGetListSize(redisKey);
        if(length > 0){
            List<Object> objs = redisUtil.lGet(redisKey,0,length);
            if(!CollectionUtils.isEmpty(objs)) {
                for (Object obj : objs) {
                    if (obj == null) {
                        continue;
                    }
                    Record rec = (Record) JSON.parse(obj.toString());

                    ResRecord res = new ResRecord();
                    BeanUtils.copyProperties(rec,res);

                    list.add(res);
                }
            }
        }
        return list;
    }
}
