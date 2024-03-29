package com.cuiyf.minitools.tools.text2Speech;

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.SpeechSynthesizerLongTextDemo;
import com.cuiyf.minitools.constant.CommonConstant;
import com.cuiyf.minitools.exception.OperateException;
import com.cuiyf.minitools.service.impl.Text2SpeechServiceImpl;
import com.cuiyf.minitools.tools.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Component
@ConfigurationProperties(prefix = "text2speech")
public class MainMethod {
    private final static Logger logger = LoggerFactory.getLogger(MainMethod.class);
    private String akId;
    private String akSecret;
    private String appKey;
    private String url;
    private String filePrefix;

    @Autowired
    private RedisUtil redisUtil;

    public String executeMethod(String text) throws Exception{

        String file = System.currentTimeMillis() + "";

        // 先输出文件
        String fileName = filePrefix + File.separator  + "pcm" + File.separator +  file + ".pcm";
        SpeechSynthesizerLongTextDemo demo = new SpeechSynthesizerLongTextDemo(appKey, token(), url);
        File out = new File(fileName);
        demo.process(text, new FileOutputStream(out));

        // 再转换格式 wav
        String targetName = filePrefix  + File.separator  + "wav" + File.separator +  file + ".wav";
        new PCM2MP3().parse(fileName,targetName);

        // 再转换格式 mp3
        String mp3Name = filePrefix  + File.separator  + "mp3" + File.separator +  file + ".mp3";
        new PCM2MP3().parse(fileName,mp3Name);

        demo.shutdown();

        return targetName;
    }

    public String initFilePath(String fileName){
        return filePrefix  + File.separator  + "mp3" + File.separator +  fileName + ".mp3";
    }

    public String main(String text){
        String targetName = null;
        try {
            targetName = executeMethod(text);
        } catch (Exception e) {
            logger.error("语音文件生成失败,第二次尝试",e);
        }

        File target = null;
        if(StringUtils.isBlank(targetName) || new File(targetName).length() <= 1000){

                redisUtil.del(CommonConstant.ALIYUN_TOKEN);
                try {
                    targetName = executeMethod(text);
                } catch (Exception e) {
                    logger.error("语音文件生成失败",e);
                    throw new OperateException("语音文件生成失败","200003");
                }
                target = new File(targetName);
                if(target.length() <= 1000){
                    logger.error("语音文件生成失败,文件过小,"+target.length() +"B");
                    throw new OperateException("语音文件生成失败,文本格式有误","200004");
                }
                return targetName;
        }

        return targetName;
    }

    public String token() throws Exception{
        Object obj = redisUtil.get(CommonConstant.ALIYUN_TOKEN);
        if(obj != null){
            return obj.toString();
        }else{
            AccessToken token = new AccessToken(akId, akSecret);
            token.apply();
            String accessToken = token.getToken();
            long expireTime = token.getExpireTime()-60000;
            redisUtil.set(CommonConstant.ALIYUN_TOKEN,accessToken,expireTime);
            return accessToken;
        }
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    public String getAkId() {
        return akId;
    }

    public void setAkId(String akId) {
        this.akId = akId;
    }

    public String getAkSecret() {
        return akSecret;
    }

    public void setAkSecret(String akSecret) {
        this.akSecret = akSecret;
    }
}
