package com.bonc.plugin.agent.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.config.TianQinConfig;
import com.bonc.plugin.agent.entity.reception.BusinessEntity;
import com.bonc.plugin.agent.entity.reception.OutAndCardEntity;
import com.bonc.plugin.agent.entity.reception.SessionReceptionEntity;
import com.bonc.plugin.agent.service.AbstractAgentReceptionService;
import com.bonc.plugin.agent.util.RequestUtil;
import com.bonc.plugin.agent.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 业务招待费
 * @author：nihongyu
 * @date: 2024/6/21
 */
@Service("outAndCardServiceImpl")
@Slf4j
@RefreshScope
public class OutAndCardServiceImpl extends AbstractAgentReceptionService {


    @Override
    public ResultObject reception(Map<String, Object> map, HttpServletRequest request) {

        OutAndCardEntity outAndCardEntity = JSON.parseObject(JSON.toJSONString(map), OutAndCardEntity.class);
        Map TGWORKFLOW_REQ = new HashMap<>();
        Map TGWORKFLOW_REQ_INFO = new HashMap<>();

        Map<String, String> userInfo = getUserInfo(request);
        //流程参数拼接
        Map<String, Object> start_process_req = START_PROCESS_REQ(userInfo.get("name"), userInfo.get("loginId"), "外出及刷卡异常审批单","goingOutAndSwipingCard");

        //data 参数拼接
        TGWORKFLOW_REQ_INFO.put("data",outAndCardEntity);
        TGWORKFLOW_REQ_INFO.putAll(start_process_req);
        TGWORKFLOW_REQ.put("TGWORKFLOW_REQ",TGWORKFLOW_REQ_INFO);

        //处理入参，拼接为天擎格式
        JSONObject jsonObject = RequestUtil.tianQinBody(TGWORKFLOW_REQ);
        //调用天擎接口
        log.info("发送外出打卡申请接口地址：" + TianQinConfig.getReception_url() + "，请求参数：" + jsonObject);
        String post = RequestUtil.getTianQinResult(jsonObject, TianQinConfig.getReception_url());

        if (post == null) {
            return ResultObject.error("调用外部接口失败：集团接待组织-外出打卡申请接口");
        }

        JSONObject object = JSONObject.parseObject(post);

        return result_info(object);

    }



}
