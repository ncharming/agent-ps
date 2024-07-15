package com.bonc.plugin.agent.util.largemodel;

import lombok.Data;

/**
 * @author suqi
 * @Title:
 * @Description:
 * @date 2024/7/118:19
 */
@Data
public class LLMChatServiceMessageParam {

    private String role;

    private String content;

    public LLMChatServiceMessageParam() {

    }

    public LLMChatServiceMessageParam(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
