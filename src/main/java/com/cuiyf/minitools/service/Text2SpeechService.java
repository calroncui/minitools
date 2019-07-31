package com.cuiyf.minitools.service;

import com.cuiyf.minitools.model.ReqConvert;
import org.springframework.stereotype.Component;

@Component
public interface Text2SpeechService {

    String text2Speech(ReqConvert req);
}
