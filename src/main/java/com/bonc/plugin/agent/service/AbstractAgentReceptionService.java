package com.bonc.plugin.agent.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.entity.reception.SessionReceptionEntity;
import com.bonc.plugin.agent.mapper.IAgentReceptionMapper;
import com.bonc.plugin.agent.util.RedisCache;
import com.bonc.plugin.agent.util.ResultObject;
import com.bonc.plugin.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/24
 */
@Slf4j
public abstract class AbstractAgentReceptionService implements AgentReceptionService {

    @Autowired
    private IAgentReceptionMapper agentReceptionMapper;

    /**
     * 获取后台用户信息
     *
     * @param request
     * @return
     */
    public Map<String, String> getUserInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        String username = TokenUtils.getUsername(request.getHeader("X-Access-Token"));
        log.info("token 查出来的用户名是：{}", username);
        if (username == null) {
            map.put("name", "谈佳俊");
            map.put("loginId", "tanjj11");
            return map;
        }
        JSONObject data = RedisCache.getData(username);
        String name = data.getString("name");
        String empCode = data.getString("empCode");

        map.put("name", name);
        map.put("loginId", username);
        map.put("empID", empCode);
        return map;

    }


    /**
     * 拼接参数
     *
     * @param name
     * @param loginId
     * @param desc
     * @return
     */
    public Map<String, Object> START_PROCESS_REQ(String name, String loginId, String desc, String workflow) {
        Map<String, Object> mapResult = new HashMap<>();
        Map<String, Object> mapInfo = new HashMap<>();
        mapInfo.put("workflowParameter", new HashMap<String, Object>() {{
            put("tenantId", "shanghaiworkflow");
            put("workflowDescription", desc);
            put("workflowId", workflow);
        }});
        mapInfo.put("wBusinessDataParameter", new HashMap<String, Object>() {{
            put("wfSectionName", desc);
            put("datePrimarykeyId", workflow);
        }});
        mapInfo.put("cWfProMessageParameter", new HashMap<String, Object>() {{
            put("startUserName", name);
            put("startUserCuMail", loginId);
        }});
        if("applicationForVehicleService".equals(workflow)){
            mapInfo.put("nextDetail", new HashMap<String, Object>() {{
                put("auditAgree", "【完成】,完成");
                put("yesOrNo", "Y");
                put("cuMail", loginId);
                put("nextStepName", "");
                put("selectSteps", "");
                Map map=new HashMap();
                map.put("deptOu","");
                map.put("cuMail","");
                List list=new ArrayList<>();
                list.add(map);
                put("standardNextParticipants",list);
            }});
            mapResult.put("START_PROCESS_AND_SUBMIT_REQ", mapInfo);
            return mapResult;
        }

        mapResult.put("START_PROCESS_REQ", mapInfo);
        return mapResult;
    }

    public ResultObject result_info(JSONObject jsonObject) {

        JSONObject uni_bss_body = jsonObject.getJSONObject("UNI_BSS_BODY");

        JSONObject tgworkflow_rsp = uni_bss_body.getJSONObject("TGWORKFLOW_RSP");

        if (tgworkflow_rsp != null) {
            if ("200".equals(tgworkflow_rsp.getString("code"))) {
                return ResultObject.ok(tgworkflow_rsp.getString("message"));
            } else {
                return ResultObject.error(tgworkflow_rsp.getString("message"));
            }
        } else {
            if(uni_bss_body.getJSONObject("TGCREATEPROCESS_RSP")!=null && uni_bss_body.getJSONObject("TGCREATEPROCESS_RSP").getInteger("code")==200 ){
                return ResultObject.ok(uni_bss_body.getJSONObject("TGCREATEPROCESS_RSP").getString("message"));
            }
            return ResultObject.error(jsonObject.getJSONObject("UNI_BSS_HEAD").getString("RESP_DESC"));
        }

    }


    @Override
    public ResultObject createSeesion(SessionReceptionEntity map, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResultObject updateSeesion(SessionReceptionEntity map, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResultObject querySession(SessionReceptionEntity map, HttpServletRequest request) {
        return null;
    }
    @Override
    public ResultObject orderCarTime(Map<String, String> params, HttpServletRequest request) {
        return null;
    }
    @Override
    public ResultObject workflow(Map<String, String> params, HttpServletRequest request) {
        return null;
    }



}
