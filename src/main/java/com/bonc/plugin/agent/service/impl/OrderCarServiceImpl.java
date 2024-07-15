package com.bonc.plugin.agent.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.config.TianQinConfig;
import com.bonc.plugin.agent.entity.reception.OrderCarEntity;
import com.bonc.plugin.agent.entity.reception.OutAndCardEntity;
import com.bonc.plugin.agent.entity.reception.SessionReceptionEntity;
import com.bonc.plugin.agent.service.AbstractAgentReceptionService;
import com.bonc.plugin.agent.util.RequestUtil;
import com.bonc.plugin.agent.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 订车业务
 * @author：nihongyu
 * @date: 2024/6/21
 */
@Service("orderCarServiceImpl")
@Slf4j
@RefreshScope
public class OrderCarServiceImpl extends AbstractAgentReceptionService {

    @Value("${agent.tianqin.tgcreateprocess_url}")
    private String tgcreateprocess_url;

    List<OrderCarEntity.Star> starList = new ArrayList<>();

    /**
     * 此入参写死的，直接启动初始化一次就行
     */
    @PostConstruct
    public void getStarList(){
        for (int i = 0; i < 5; i++) {
            OrderCarEntity.Star star = new OrderCarEntity().new Star();
            star.setFlag(true);
            star.setBackgroundposition("top");
            if (i == 4) {
                star.setFlag(false);
                star.setBackgroundposition("bottom");
            }
            starList.add(star);
        }
    }

//    @Override
    public ResultObject reception1(Map<String, Object> map, HttpServletRequest request) {

        OrderCarEntity orderCarEntity = JSON.parseObject(JSON.toJSONString(map), OrderCarEntity.class);

        //此入参写死
//        List<OrderCarEntity.Star> starList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            OrderCarEntity.Star star = orderCarEntity.new Star();
//            star.setFlag(true);
//            star.setBackgroundposition("top");
//            if (i == 4) {
//                star.setFlag(false);
//                star.setBackgroundposition("bottom");
//            }
//            starList.add(star);
//        }

        orderCarEntity.setStar(starList);
        String localDateTime= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        orderCarEntity.setFilingDate(localDateTime);

        Map TGWORKFLOW_REQ = new HashMap<>();
        Map TGWORKFLOW_REQ_INFO = new HashMap<>();

        Map<String, String> userInfo = getUserInfo(request);
        //流程参数拼接
        Map<String, Object> start_process_req = START_PROCESS_REQ(userInfo.get("name"), userInfo.get("loginId"), "车辆服务申请单", "applicationForVehicleService");
        //data 参数拼接
        TGWORKFLOW_REQ_INFO.put("data", orderCarEntity);
        TGWORKFLOW_REQ_INFO.putAll(start_process_req);
        TGWORKFLOW_REQ.put("TGWORKFLOW_REQ",TGWORKFLOW_REQ_INFO);

        //处理入参，拼接为天擎格式
        JSONObject jsonObject = RequestUtil.tianQinBody(TGWORKFLOW_REQ);
        //调用天擎接口
        log.info("发送车辆服务申请单接口地址：" + TianQinConfig.getReception_url() + "，请求参数：" + jsonObject);
        String post = RequestUtil.getTianQinResult(jsonObject, TianQinConfig.getReception_url());

        if (post == null) {
            return ResultObject.error("调用外部接口失败：集团接待组织-车辆服务申请单接口");
        }

        JSONObject object = JSONObject.parseObject(post);

        return result_info(object);

    }


    @Override
    public ResultObject reception(Map<String, Object> map, HttpServletRequest request) {

        OrderCarEntity orderCarEntity = JSON.parseObject(JSON.toJSONString(map), OrderCarEntity.class);
        Map TGWORKFLOW_REQ = new HashMap<>();
        Map TGWORKFLOW_REQ_INFO = new HashMap<>();

        Map<String, String> userInfo = getUserInfo(request);
        //流程参数拼接
        Map<String, Object> start_process_req = START_PROCESS_REQ(userInfo.get("name"), userInfo.get("loginId"), "车辆服务申请单", "applicationForVehicleService");


        orderCarEntity.setStar(starList);
        String localDateTime= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        orderCarEntity.setFilingDate(localDateTime);
        //data 参数拼接
        TGWORKFLOW_REQ_INFO.put("data",orderCarEntity);
        TGWORKFLOW_REQ_INFO.putAll(start_process_req);
        TGWORKFLOW_REQ.put("TGCREATEPROCESS_REQ",TGWORKFLOW_REQ_INFO);

        //处理入参，拼接为天擎格式
        JSONObject jsonObject = RequestUtil.tianQinBody(TGWORKFLOW_REQ);
        //调用天擎接口
        log.info("发送车辆服务申请单接口地址：" + tgcreateprocess_url + "，请求参数：" + jsonObject);
        String post = RequestUtil.getTianQinResult(jsonObject, tgcreateprocess_url);

        if (post == null) {
            return ResultObject.error("调用外部接口失败：集团接待组织-发送车辆服务申请单接口");
        }

        JSONObject object = JSONObject.parseObject(post);

        return result_info(object);

    }





}
