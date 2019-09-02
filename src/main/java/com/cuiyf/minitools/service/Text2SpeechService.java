package com.cuiyf.minitools.service;

import com.cuiyf.minitools.model.ReqConvert;
import com.cuiyf.minitools.model.ReqList;
import com.cuiyf.minitools.model.ResRecord;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public interface Text2SpeechService {

    String text2Speech(ReqConvert req);

    List<ResRecord> list(ReqList req);

    String download(String fileName, HttpServletResponse response);
}
