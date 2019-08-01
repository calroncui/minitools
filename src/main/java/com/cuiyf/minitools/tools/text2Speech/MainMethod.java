package com.cuiyf.minitools.tools.text2Speech;

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.SpeechSynthesizerLongTextDemo;
import com.cuiyf.minitools.constant.CommonConstant;
import com.cuiyf.minitools.exception.OperateException;
import com.cuiyf.minitools.service.impl.Text2SpeechServiceImpl;
import com.cuiyf.minitools.tools.redis.RedisUtil;
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
    private String pcmFilePrefix;
    private String mp3FilePrefix;

    @Autowired
    private RedisUtil redisUtil;

    public String main(String text){
        String fileName = null;
        String targetName = null;
        try {

            String file = System.currentTimeMillis() + "";

            // 先输出文件
            fileName = pcmFilePrefix + "\\" +  file + ".pcm";
            SpeechSynthesizerLongTextDemo demo = new SpeechSynthesizerLongTextDemo(appKey, token(), url);
            File out = new File(fileName);
            demo.process(text, new FileOutputStream(out));

            // 再转换格式
            targetName = mp3FilePrefix + "\\" +  file + ".mp3";
            new PCM2MP3().parse(fileName,targetName);

            demo.shutdown();
        } catch (Exception e) {
            logger.error("语音文件生成失败");
            throw new OperateException("语音文件生成失败","200003");
        }

        File target = new File(targetName);
        if(target.length() <= 1000){
            logger.error("语音文件生成失败,文件过小,"+target.length() +"B");
            throw new OperateException("语音文件生成失败,文本格式有误","200004");
        }

        return fileName;
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

    public String getPcmFilePrefix() {
        return pcmFilePrefix;
    }

    public void setPcmFilePrefix(String pcmFilePrefix) {
        this.pcmFilePrefix = pcmFilePrefix;
    }

    public String getMp3FilePrefix() {
        return mp3FilePrefix;
    }

    public void setMp3FilePrefix(String mp3FilePrefix) {
        this.mp3FilePrefix = mp3FilePrefix;
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
