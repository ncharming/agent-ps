package com.bonc.plugin.agent.util.largemodel;

//import io.swagger.annotations.ApiModel;
import lombok.Data;

//@ApiModel("LLM回答的条目对象")
@Data
public class LLMChatResultEntityItem {

    private String role;

    private String content;
}
