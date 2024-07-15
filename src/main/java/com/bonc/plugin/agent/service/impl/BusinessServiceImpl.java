package com.bonc.plugin.agent.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.config.TianQinConfig;
import com.bonc.plugin.agent.entity.reception.BusinessEntity;
import com.bonc.plugin.agent.entity.reception.SessionReceptionEntity;
import com.bonc.plugin.agent.service.AbstractAgentReceptionService;
import com.bonc.plugin.agent.util.RequestUtil;
import com.bonc.plugin.agent.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 业务招待费
 * @author：nihongyu
 * @date: 2024/6/21
 */
@Service("businessServiceImpl")
@Slf4j
public class BusinessServiceImpl extends AbstractAgentReceptionService {

    @Override
    public ResultObject reception(Map<String, Object> map, HttpServletRequest request) {

        BusinessEntity businessEntity = JSON.parseObject(JSON.toJSONString(map), BusinessEntity.class);
        Map TGWORKFLOW_REQ = new HashMap<>();
        Map TGWORKFLOW_REQ_INFO = new HashMap<>();

        Map<String, String> userInfo = getUserInfo(request);
        //流程参数拼接
        Map<String, Object> start_process_req = START_PROCESS_REQ(userInfo.get("name"), userInfo.get("loginId"), "业务招待费事前申请","fete");
        //data 参数拼接

        if("对内".equals(businessEntity.getBehoofType().trim())){
            businessEntity.setInternalType(businessEntity.getForeignType());
        }

        TGWORKFLOW_REQ_INFO.put("data",businessEntity);
        TGWORKFLOW_REQ_INFO.putAll(start_process_req);
        TGWORKFLOW_REQ.put("TGWORKFLOW_REQ",TGWORKFLOW_REQ_INFO);

        //处理入参，拼接为天擎格式
        JSONObject jsonObject = RequestUtil.tianQinBody(TGWORKFLOW_REQ);
        //调用天擎接口
        log.info("发送业务招待费事前申请接口地址：" + TianQinConfig.getReception_url() + "，请求参数：" + jsonObject);
        String post = RequestUtil.getTianQinResult(jsonObject, TianQinConfig.getReception_url());

        if (post == null) {
            return ResultObject.error("调用外部接口失败：集团接待组织-业务招待费接口");
        }

        JSONObject object = JSONObject.parseObject(post);

        return result_info(object);

    }




}
