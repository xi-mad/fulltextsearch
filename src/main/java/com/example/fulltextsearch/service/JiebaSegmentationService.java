package com.example.fulltextsearch.service;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class JiebaSegmentationService {

    private JiebaSegmenter segmenter;

    public JiebaSegmentationService() {
        this.segmenter = new JiebaSegmenter();
    }

    // 分词并返回空格分隔的字符串
    public String segmentText(String text) {
        return String.join(" ", segmenter.sentenceProcess(text).stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet()));
    }
}