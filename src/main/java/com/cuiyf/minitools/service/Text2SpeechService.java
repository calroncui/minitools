package com.cuiyf.minitools.service;

import com.cuiyf.minitools.model.ReqConvert;
import com.cuiyf.minitools.model.ReqList;
import com.cuiyf.minitools.model.ResRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Text2SpeechService {

    String text2Speech(ReqConvert req);

    List<ResRecord> list(ReqList req);
}
