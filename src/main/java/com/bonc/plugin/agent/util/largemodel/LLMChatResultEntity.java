package com.bonc.plugin.agent.util.largemodel;

import lombok.Data;

//@ApiModel("LLM服务结果数据对象")
@Data
public class LLMChatResultEntity {

//    @ApiModelProperty("索引")
    private int index;

//    @ApiModelProperty("结束原因")
    private String finish_reason;

    private LLMChatResultEntityItem message;
}
