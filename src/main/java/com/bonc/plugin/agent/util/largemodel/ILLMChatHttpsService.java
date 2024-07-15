package com.bonc.plugin.agent.util.largemodel;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;

/**
 * @author suqi
 * @Title:
 * @Description:
 * @date 2024/7/117:01
 */
@FeignClient(url="EMPTY",value = "iLLMChatHttpsService")
public interface ILLMChatHttpsService {

    @PostMapping(value = "",consumes = {"application/json"})
    LLMChatResult tyqwChat(URI uri, QWChatServiceParam qwChatServiceParam);

    @PostMapping(value = "",consumes = {"application/json"})
    JSONObject workflow(URI uri, JSONObject object);
}
