package com.bonc.plugin.agent.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.entity.reception.SessionReceptionEntity;
import com.bonc.plugin.agent.mapper.IAgentReceptionMapper;
import com.bonc.plugin.agent.service.AbstractAgentReceptionService;
import com.bonc.plugin.agent.util.*;
import com.bonc.plugin.agent.util.largemodel.ILLMChatHttpsService;
import com.bonc.plugin.agent.util.largemodel.LLMChatResult;
import com.bonc.plugin.agent.util.largemodel.LLMChatServiceMessageParam;
import com.bonc.plugin.agent.util.largemodel.QWChatServiceParam;
import com.bonc.plugin.utils.SM2Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 除开流程中心接口之外辅助方法
 * @author：nihongyu
 * @date: 2024/7/2
 */
@Service("utilServiceImpl")
@Slf4j
@RefreshScope
public class UtilServiceImpl extends AbstractAgentReceptionService {


    @Value("${largemodel.model}")
    private String model;
    @Value("${largemodel.url}")
    private String url;
    @Value("${largemodel.workflowUrl}")
    private String workflowUrl;

    @Value("${largemodel.score}")
    private int score;
    @Value("${largemodel.top}")
    private int top;

    @Value("${largemodel.top_score}")
    private double top_score;

    @Value("${agent.privateKey}")
    private String privateKey;

    @Autowired
    private ILLMChatHttpsService illmChatHttpsService;

    @Autowired
    private IAgentReceptionMapper agentReceptionMapper;

    @Override
    public ResultObject reception(Map<String, Object> map, HttpServletRequest request) {
        return null;
    }


    @Override
    public ResultObject createSeesion(SessionReceptionEntity receptionEntity, HttpServletRequest request) {
        Map<String, String> userInfo = getUserInfo(request);
        String loginId = userInfo.get("loginId");
        String sessionId = loginId + "_" + receptionEntity.getSessionId();

        List<Map<String, String>> receptionTypeList = receptionEntity.getReceptionTypeList();
        List<Map<String, String>> insertMap = new ArrayList<>();

        /**
         * 先查询是否有上次没有提交完的流程
         * 没有就将其全部改为 old
         */
        Map<String, String> mapQuery = new HashMap<>();
        mapQuery.put("sessionId", sessionId);

        //每次刷新页面当成一个新的开始
        RedisCache.delFirst(sessionId);

        List<Map<String, String>> mapsList = agentReceptionMapper.selectSession(mapQuery);
        if (!CollectionUtil.isEmpty(mapsList)) { //
            mapQuery.put("sessionNew", "old");
            agentReceptionMapper.updateSession(mapQuery);
        }

        for (Map<String, String> strMap : receptionTypeList) {
            Map<String, String> map = new HashMap<>();
            map.put("sessionId", sessionId);
            map.put("sessionFlag", "0");
            map.put("sessionNew", "new");

            //下面都只有一个key
            for (Map.Entry<String, String> entry : strMap.entrySet()) {
                map.put("sessionType", entry.getKey().trim());
                map.put("sessionName", entry.getValue().trim());
            }
            insertMap.add(map);
        }

//        try {
            agentReceptionMapper.insertSession(insertMap);
            return ResultObject.ok();
//        } catch (Exception e) {
//            log.info("数据库插入失败：{}", e);
//            return ResultObject.error("插入会话数据失败");
//        }
    }

    @Override
    public ResultObject updateSeesion(SessionReceptionEntity receptionEntity, HttpServletRequest request) {
        Map<String, String> userInfo = getUserInfo(request);
        String loginId = userInfo.get("loginId");
        String sessionId = loginId + "_" + receptionEntity.getSessionId();
        Map<String, String> map = new HashMap<>();
        map.put("sessionId", sessionId);

        Map<String, String> receptionType = receptionEntity.getReceptionType();

//        try {
        if (!CollectionUtil.isEmpty(receptionType)) {  //不为空就需要更新
            String sessionType = "";
            for (Map.Entry<String, String> entry : receptionType.entrySet()) {
                sessionType = entry.getKey().trim();
            }

            map.put("sessionType", sessionType);
            map.put("sessionFlag", "1");
            agentReceptionMapper.updateSession(map);
        }

        List<Map<String, String>> maps = agentReceptionMapper.selectSession(map);
        List<Map<String, String>> list = new ArrayList<>();
        for (Map<String, String> mapStr : maps) {
            Map<String, String> mapIn = new HashMap<>();
            mapIn.put(mapStr.get("session_type"), mapStr.get("session_name"));
            list.add(mapIn);
        }
        return ResultObject.ok(list);
//        }
//        catch (Exception e) {
//            log.info("数据库更新_查询失败：{}", e);
//            return ResultObject.error("数据库更新_查询失败");

//        }

    }

    @Override
    public ResultObject querySession(SessionReceptionEntity receptionEntity, HttpServletRequest request) {
        Map<String, String> userInfo = getUserInfo(request);
        String loginId = userInfo.get("loginId");
        String sessionId = loginId + "_" + receptionEntity.getSessionId();
        Map<String, String> map = new HashMap<>();
        map.put("sessionId", sessionId);

//        Map<String, String> receptionType = receptionEntity.getReceptionType();

//        try {
        List<Map<String, String>> maps = agentReceptionMapper.selectSession(map);
        if (CollectionUtil.isEmpty(maps)) {
            if (RedisCache.getFirst(sessionId)) {
                return ResultObject.ok(null);
            }
            RedisCache.setFirst(sessionId);
            RedisCache.delCaowei(receptionEntity.getSessionId());
        }

        List<Map<String, String>> list = new ArrayList<>();
        for (Map<String, String> mapStr : maps) {
            Map<String, String> mapIn = new HashMap<>();
            mapIn.put(mapStr.get("session_type"), mapStr.get("session_name"));
            list.add(mapIn);
        }
        return ResultObject.ok(list);
//        } catch (Exception e) {
//            log.info("数据库更新_查询失败：{}", e);
//            return ResultObject.error("数据库更新_查询失败");
//        }

    }

    @Override
    public ResultObject orderCarTime(Map<String, String> params, HttpServletRequest request) {

        String decryptData = SM2Utils.decryptByPrivateKey(params.get("data"), privateKey);
        JSONObject jsonObject = JSONObject.parseObject(decryptData);
        log.info("获取时间解密：{}", jsonObject);
        String userInput = jsonObject.getString("userInput");

        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 获取今天是周几
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        String xingqi = dayOfWeek.toString().replaceAll("(?i)monday", "周一")
                .replaceAll("(?i)tuesday", "周二")
                .replaceAll("(?i)wednesday", "周三")
                .replaceAll("(?i)thursday", "周四")
                .replaceAll("(?i)friday", "周五")
                .replaceAll("(?i)saturday", "周六")
                .replaceAll("(?i)sunday", "周日");


        log.info("系统设置的时间：{}，周几：{}", today, xingqi);

        String str = "从用户输入中提取开始时间和结束时间实体,输出{\"starttime\": \"***\",\"endtime\": \"***\"},不需要原因解释.\n" +
                "    示例：\n" +
                "\t\t已知今天的时间是:2024-06-07\n" +
                "        用户输入：今天上午9点10分到10点\n" +
                "        输出：{\"starttime\": \"2024-06-07 09:10\",\"endtime\": \"2024-06-07 10:00\"}\n" +
                "        用户输入：订今天上午的车辆\n" +
                "        输出：{\"starttime\": \"2024-06-07 00:00\",\"endtime\": \"2024-06-07 12:00\"}\n" +
                "        用户输入：订今天下午的车辆\n" +
                "        输出：{\"starttime\": \"2024-06-07 12:00\",\"endtime\": \"2024-06-07 23:59\"}\n" +
                "        用户输入：订今天的车辆\n" +
                "        输出：{\"starttime\": \"2024-06-07 00:00\",\"endtime\": \"2024-06-07 23:59\"}\n" +
                "        用户输入：我要预定车辆\n" +
                "        输出：{\"starttime\": \"\",\"endtime\": \"\"}\n" +
                "        用户输入：我要定今天10点到明天下午两点的车\n" +
                "        输出：{\"starttime\": \"2024-06-07 10:00\",\"endtime\": \"2024-06-08 14:00\"}\n" +
                "        用户输入：你能做什么\n" +
                "        输出：{\"starttime\": \"\",\"endtime\": \"\"}。\n" +
                "    示例完成，请完成以下任务:\n" +
                "\t已知今天的时间是:" + today + "\n" +
                "\t已知今天是:" + xingqi + "\n" +
                "    已知上午默认时间段是00:00,12:00，下午默认时间段是12:00,23:59。\n" +
                "    用户输入:" + userInput + "\n" +
                "    输出:";
        long start = System.currentTimeMillis();
        URI llmServiceUri = null;

        try {
            llmServiceUri = new URI(url);
        } catch (URISyntaxException e) {
            return ResultObject.error("大模型接口初始化失败");
//            throw new RuntimeException(e);
        }

        QWChatServiceParam param = new QWChatServiceParam();
        param.setModel(model);
        param.setStream(false);
        List<LLMChatServiceMessageParam> list = new ArrayList<>();
        LLMChatServiceMessageParam chatServiceMessageParam = new LLMChatServiceMessageParam();
        chatServiceMessageParam.setRole("user");
        chatServiceMessageParam.setContent(str);
        list.add(chatServiceMessageParam);
        param.setMessages(list);
        LLMChatResult result = illmChatHttpsService.tyqwChat(llmServiceUri, param);
        long end = System.currentTimeMillis();
//        long timeCost = end - start;
        log.info("大模型消耗时间：{}", Math.subtractExact(end, start));

        String content = result.getChoices().get(0).getMessage().getContent();
        log.info("大模型返回的时间内容：{}", content);
        String jsonRegex = "\\{.*\\}";
        Pattern pattern = Pattern.compile(jsonRegex);
        Matcher matcher = pattern.matcher(content);
        String jsonString = "";
        if (matcher.find()) {
            // 提取匹配的JSON字符串
            jsonString = matcher.group();

        } else {
            log.info("No JSON object found in the string.");
        }

        return ResultObject.ok(JSON.parse(jsonString));
    }


    @Override
    public ResultObject workflow(Map<String, String> params, HttpServletRequest request) {
        JSONArray arrayMark = new JSONArray();
        JSONArray arrayBack = new JSONArray();
//        String decryptData = SM2Utils.decryptByPrivateKey(params.get("data"), privateKey);
//        JSONObject jsonObject = JSONObject.parseObject(decryptData);
//        log.info("获取时间解密：{}", jsonObject);
//        String userInput = jsonObject.getString("userInput");

        String userInput = params.get("data");

        URI llmServiceUri = null;

        try {
            llmServiceUri = new URI(workflowUrl);
        } catch (URISyntaxException e) {
            return ResultObject.error("流程推荐接口初始化失败");
        }
        JSONObject object = new JSONObject();
        object.put("expr", "");
        object.put("score", score);
        object.put("top", top);
        object.put("question", userInput);
        object.put("collection_name", "sh_agent");

        long start = System.currentTimeMillis();
        JSONObject result = illmChatHttpsService.workflow(llmServiceUri, object);
        long end = System.currentTimeMillis();
//        long timeCost = end - start;
        log.info("大模型消耗时间：{}", Math.subtractExact(end, start));

        log.info("大模型返回的时间内容：{}", result);
        if (result.getInteger("code") == 200) {
            JSONArray jsonObjectList = result.getJSONArray("data");
            List<JSONObject> uniqueList = new ArrayList<>();  //去重后，且保证正序
            //根据 processName 字段去重
            Set<String> uniqueIds = new HashSet<>();
            for (int i = 0; i < jsonObjectList.size(); i++) {
                JSONObject jsonObject1 = jsonObjectList.getJSONObject(i);
                String processName = jsonObject1.getString("processName");
                if (!uniqueIds.contains(processName)) {
                    uniqueIds.add(processName);
                    uniqueList.add(jsonObject1);
                }
            }

            //根据score倒排；返回全部的，不需要排序
            Comparator<JSONObject> score1 = Comparator.comparingDouble(o -> o.getDouble("score"));
            uniqueList.sort(score1.reversed());

            for (int i = 0; i < uniqueList.size(); i++) {
                JSONObject map = new JSONObject();
                JSONObject obj =  uniqueList.get(i);
                map.put("processId", obj.getString("processId"));
                map.put("processName", obj.getString("processName"));
                map.put("processCode", obj.getString("processCode"));
//                map.put("text", obj.getString("text"));
                map.put("score", obj.getDouble("score"));
//                if (obj.getDouble("score") > top_score) {
//                    arrayMark.add(map);
//                    continue;
//                }
                arrayBack.add(map);
            }

//            if (arrayMark.size() == 1) {  //唯一匹配的
//                Map map = new HashMap<>();
//                map.put("isUnique", true);
//                map.put("data", arrayMark);
//                return ResultObject.ok(map);
//            } else  {
//                arrayMark.addAll(arrayBack);
                Map map = new HashMap<>();
                map.put("isUnique", false);
                map.put("data", arrayBack);
                return ResultObject.ok(map);
//            }
        } else {
            return ResultObject.error("接口返回失败");
        }
    }

}
