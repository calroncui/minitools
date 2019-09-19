package com.cuiyf.minitools.controller;

import com.alibaba.fastjson.JSONObject;
import com.cuiyf.minitools.exception.OperateException;
import com.cuiyf.minitools.model.ReqConvert;
import com.cuiyf.minitools.model.ReqList;
import com.cuiyf.minitools.model.ReqOauth;
import com.cuiyf.minitools.model.ResOauth;
import com.cuiyf.minitools.result.AbstractRestResponse;
import com.cuiyf.minitools.result.DefaultRestApiResponse;
import com.cuiyf.minitools.service.Text2SpeechService;
import com.cuiyf.minitools.service.weixin.WeixinService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Controller
public class Text2SpeechController {
    private final static Logger logger = LoggerFactory.getLogger(Text2SpeechController.class);
    @Autowired
    private Text2SpeechService text2SpeechService;
    @Autowired
    private WeixinService weixinService;

    private String appId = "wx6bcacbe45886f2fd";
    private String appSecrct = "**";
    public static String GET_ACCESS_TOKEN_OAUTH = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

    @RequestMapping(value = {"/convert"} , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> convert(@RequestBody ReqConvert req) throws Exception{
        logger.info("Text2SpeechController.convert.param={}", JSONObject.toJSONString(req));
        ResponseEntity<?> result = null;
        AbstractRestResponse restResponse = new DefaultRestApiResponse();

        restResponse.setRestObject(text2SpeechService.text2Speech(req));
        restResponse.setResCode("200");
        restResponse.setMessage("success");

        result = new ResponseEntity<AbstractRestResponse>(restResponse, HttpStatus.OK);
        logger.info("Text2SpeechController.convert.response={}", JSONObject.toJSONString(restResponse.getRestObject()));
        return result;
    }

    @RequestMapping(value = {"/list"} , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> list(@RequestBody ReqList req) throws Exception{
        logger.info("Text2SpeechController.list.param={}", JSONObject.toJSONString(req));
        ResponseEntity<?> result = null;
        AbstractRestResponse restResponse = new DefaultRestApiResponse();

        restResponse.setRestObject(text2SpeechService.list(req));
        restResponse.setResCode("200");
        restResponse.setMessage("success");

        result = new ResponseEntity<AbstractRestResponse>(restResponse, HttpStatus.OK);
        logger.info("Text2SpeechController.list.response={}", JSONObject.toJSONString(restResponse.getRestObject()));
        return result;
    }

    @RequestMapping(path = { "/oauth", "/oauth/" })
    @ResponseBody
    public ResponseEntity<?> oauth(@RequestBody ReqOauth req) {
        logger.info("Text2SpeechController.oauth.param={}", JSONObject.toJSONString(req));
        ResponseEntity<?> result = null;
        AbstractRestResponse restResponse = new DefaultRestApiResponse();

        if(req == null || StringUtils.isEmpty(req.getCode())){
            logger.error("参数未指定");
            throw new OperateException("参数未指定","100001");
        }

        String url = GET_ACCESS_TOKEN_OAUTH.replace("APPID", appId).replace("SECRET", appSecrct).replace("JSCODE",
                req.getCode());

        JSONObject jsonObject = weixinService.httpsRequest(url, "POST", null);


        if (jsonObject != null) {
            if (jsonObject.get("errcode") != null && StringUtils.isNotBlank(jsonObject.get("errcode").toString())
                    && (jsonObject.get("errcode") != "0")) {
                String errMsg = "获取openId失败 errcode:" + jsonObject.get("errcode") + "，errmsg:"
                        + jsonObject.getString("errmsg");
                logger.error(errMsg);
                throw new OperateException("获取openId失败","600001");
            } else {
                ResOauth res = new ResOauth();
                res.setOpenid(jsonObject.getString("openid"));
                restResponse.setRestObject(res);
                restResponse.setResCode("200");
                restResponse.setMessage("success");

                result = new ResponseEntity<AbstractRestResponse>(restResponse, HttpStatus.OK);
                logger.info("Text2SpeechController.oauth.response={}", JSONObject.toJSONString(restResponse.getRestObject()));
                return result;
            }
        } else {
            logger.error("获取openId失败");
            throw new OperateException("获取openId失败","600002");
        }

    }

    @RequestMapping(value = {"/download"} , method = RequestMethod.GET)
    public ResponseEntity<?> download(@RequestParam(value="fileName") String fileName, HttpServletResponse response) throws Exception{
        logger.info("Text2SpeechController.download.param={}", JSONObject.toJSONString(fileName));

        if(StringUtils.isEmpty(fileName)){
            logger.error("参数未指定");
            throw new OperateException("参数未指定","100001");
        }

        text2SpeechService.download(fileName,response);

        logger.info("Text2SpeechController.list.response={}", "download " + fileName + " success");
        return null;
    }
}
