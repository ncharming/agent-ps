package com.bonc.plugin.agent.util.largemodel;

import lombok.Data;

import java.util.List;

/**
 * @author suqi
 * @Title:
 * @Description:
 * @date 2024/7/118:18
 */
@Data
public class QWChatServiceParam {

    private String model;

    private List<LLMChatServiceMessageParam> messages;

    private boolean stream;

    public QWChatServiceParam() {

    }

    /**
     * 构建请求参数
     * @param messages    汇总语料或历史会话内容
     * @param stream
     */
    public QWChatServiceParam(String model, List<LLMChatServiceMessageParam> messages, boolean stream) {
        this.model = model;
        this.messages = messages;
        this.stream = stream;
    }
}
