package com.cuiyf.minitools.tools.webspider;

import com.cuiyf.minitools.exception.OperateException;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebSpider {
    private final static Logger logger = LoggerFactory.getLogger(WebSpider.class);
    public static String getURLInfo(String urlInfo,String charset) throws Exception {
        if(StringUtils.isEmpty(charset)){
            charset = "utf-8";
        }
        //读取目的网页URL地址，获取网页源码
        URL url = new URL(urlInfo);
        HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
        httpUrl.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");

        InputStream is = httpUrl.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is,charset));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        br.close();
        //获得网页源码
        return sb.toString();
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public static String getContentByUrl(String url)  {
        String content = null;
        String text = null;
        try {
            content = getURLInfo(url,null);
            logger.info("url:"+url+",content size:"+content.length());

            text = html2text(content);
            logger.info("url:"+url+",text size:"+content.length());
        } catch (Exception e) {
            logger.error("抓取页面数据失败");
            throw new OperateException("抓取页面数据失败","200002");
        }

        return text;
    }
}
