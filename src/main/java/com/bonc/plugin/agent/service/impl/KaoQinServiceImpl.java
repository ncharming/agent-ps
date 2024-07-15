package com.bonc.plugin.agent.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.service.AbstractAgentReceptionService;
import com.bonc.plugin.agent.util.RequestUtil;
import com.bonc.plugin.agent.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 业务招待费
 * @author：nihongyu
 * @date: 2024/6/21
 */
@Service("kaoQinServiceImpl")
@Slf4j
@RefreshScope
public class KaoQinServiceImpl extends AbstractAgentReceptionService {

    @Value("${agent.tianqin.cxappId}")
    private String cxappId;
    @Value("${agent.tianqin.cxtoken}")
    private String cxtoken;

    @Value("${agent.tianqin.kaoqin_url}")
    private String kaoqin_url;

    /**
     * 考勤接口处理
     *
     * @param map
     * @param request
     * @return
     */
    @Override
    public ResultObject reception(Map<String, Object> map, HttpServletRequest request) {


        Map EMPATTEN_INFO_REQ = new HashMap<>();
        Map mapInfo = new HashMap<>();

        Map<String, String> userInfo = getUserInfo(request);
        String empID = userInfo.get("empID");

        /**
         * h获取一个月的第一天和最后一天；yyytMMdd
         */
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 获取当前月份的第一天
        LocalDate startOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        // 获取当前月份的最后一天
        LocalDate endOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
        // 获取当前时间
        LocalTime currentTime = LocalTime.now();
        // 结合日期和时间，得到当前月份的起始和结束的LocalDateTime
        String startDateTime = LocalDateTime.of(startOfMonth, currentTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String endDateTime = LocalDateTime.of(endOfMonth, currentTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"));


        //入参 参数拼接
        mapInfo.put("cxcode", "code6");
        mapInfo.put("cxappId", cxappId);
        mapInfo.put("cxtoken", cxtoken);
        mapInfo.put("empID", empID);
        mapInfo.put("startTIME", startDateTime);
        mapInfo.put("endTIME", endDateTime);
        EMPATTEN_INFO_REQ.put("EMPATTEN_INFO_REQ", mapInfo);

        //处理入参，拼接为天擎格式
        JSONObject jsonObject = RequestUtil.tianQinBody(EMPATTEN_INFO_REQ);
        //调用天擎接口
        log.info("考勤接口地址：" + kaoqin_url + "，请求参数：" + jsonObject);
        String post = RequestUtil.getTianQinResult(jsonObject, kaoqin_url);

        if (post == null) {
            return ResultObject.error("调用外部接口失败：考勤接口");
        }

        JSONObject object = JSONObject.parseObject(post);

        JSONObject uni_bss_body = object.getJSONObject("UNI_BSS_BODY");
        JSONObject empatten_info_rsp = uni_bss_body.getJSONObject("EMPATTEN_INFO_RSP");
        JSONArray returnInfo = empatten_info_rsp.getJSONArray("returnInfo");
        if (returnInfo == null) {
            return ResultObject.error("调用外部接口失败：考勤接口");
        } else {
            List<JSONObject> returnMap = new ArrayList<>();
            for (int i = 0; i < returnInfo.size(); i++) {
                JSONObject obj = (JSONObject) returnInfo.get(i);
                if (obj.getString("DETAIL").contains("异常")) {
                    returnMap.add(obj);
                }
            }
            return ResultObject.ok(returnMap);
        }
    }


}
