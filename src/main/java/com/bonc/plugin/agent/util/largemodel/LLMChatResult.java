package com.bonc.plugin.agent.util.largemodel;

import lombok.Data;

import java.util.List;

//@ApiModel(value = "大模型汇总结果对象")
@Data
public class LLMChatResult {

    private String id;

    private String model;

    private List<LLMChatResultEntity> choices;
}
