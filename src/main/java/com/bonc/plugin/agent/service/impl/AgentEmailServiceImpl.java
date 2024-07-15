package com.bonc.plugin.agent.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.config.TianQinConfig;
import com.bonc.plugin.agent.entity.EmailSendEntity;
import com.bonc.plugin.agent.entity.UserInfoEntiry;
import com.bonc.plugin.agent.mapper.IAgentEmailMapper;
import com.bonc.plugin.agent.service.IAgentEmailService;
import com.bonc.plugin.agent.util.RedisCache;
import com.bonc.plugin.agent.util.RequestUtil;
import com.bonc.plugin.agent.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/11
 */
@Service
@Slf4j
public class AgentEmailServiceImpl implements IAgentEmailService {



    @Autowired
    private IAgentEmailMapper iAgentEmailMapper;

    @Autowired
    private AsyncService asyncService;

    @Override
    public ResultObject emailAcceptList(String username) {
        ResultObject result = new ResultObject<>();

        List<UserInfoEntiry> mapList = iAgentEmailMapper.emailAcceptList(username);
        Iterator<UserInfoEntiry> iterator = mapList.iterator();
        while(iterator.hasNext()){
            UserInfoEntiry next = iterator.next();
            if(StringUtils.isEmpty(next.getEmail())){
                iterator.remove();
            }
        }
        result.setResult(mapList);

        return result;
    }

    @Override
    public ResultObject getUserDept(String username) {

        ResultObject result = new ResultObject<>();
        JSONObject data = RedisCache.getData(username);

        if (data == null) {
            return ResultObject.error("用户信息获取失败");
        }

        data.put("orgName", data.getString("orgName").replaceAll("上海市分公司", "").replaceAll("上海市", ""));
        data.put("token", data.getString("token"));
        result.setResult(data);
        return result;
    }

    @Override
    public ResultObject getEmailInfo(String emailId) {
        ResultObject result = new ResultObject<>();

        EmailSendEntity emailInfo = iAgentEmailMapper.getEmailInfo(emailId);
        result.setResult(emailInfo);

        return result;
    }

    @Override
    public ResultObject sendEmail(EmailSendEntity emailSendEntity) {
        ResultObject result = new ResultObject<>();


        /**
         * 没有传 remark ,就按照下面的模板传入
         */
        if (StringUtils.isEmpty(emailSendEntity.getRemark())) {
            JSONObject data = RedisCache.getData(emailSendEntity.getSendDoc());
            if (data == null) {
                return ResultObject.error("用户信息获取失败");
            }
            String remark = "联系人：" + data.getString("name") + " " + data.getString("phone") + " " + emailSendEntity.getSendDoc() + "@chinaunicom.cn";
            emailSendEntity.setRemark(remark);
        }

        String sendEmail = emailSendEntity.getSendDoc() + "@chinaunicom.cn";
        //发件人前端传 username,这里做拼接
        emailSendEntity.setSendDoc(sendEmail);


        Map<String,Object> mapInfo=new HashMap<>();
        Map<String,Object> map=new HashMap<>();
        mapInfo.put("GET_EMAIL_REQ",map);
        map.put("sendDoc",emailSendEntity.getSendDoc());
        map.put("receivingDoc",emailSendEntity.getReceivingDoc());
        map.put("meetTime",emailSendEntity.getMeetTime());
        map.put("meetDesc",emailSendEntity.getMeetDesc());
        map.put("meetPlace",emailSendEntity.getMeetPlace());
        map.put("meetName",emailSendEntity.getMeetName());
        map.put("meetTopic",emailSendEntity.getMeetTopic());
        map.put("meetObject",emailSendEntity.getMeetObject());
        map.put("remark",emailSendEntity.getRemark());

        /**
         * 天擎调用
         */
        JSONObject jsonObject = RequestUtil.tianQinBody(mapInfo);

        log.info("发送邮件接口地址：" + TianQinConfig.getEmail_url() + "，请求参数：" + jsonObject);
        String post = RequestUtil.getTianQinResult(jsonObject, TianQinConfig.getEmail_url());

        //调用发送邮件接口
        /**
         * 直接调用
         */
//        String post = RequestUtil.post(email_url, emailSendEntity);


        if (post == null) {
            return ResultObject.error("调用外部接口失败：邮件发送接口");
        }

        //成功之后记录日志 暂时不需要返回此id用作查询
//        String uuid = UUID.fastUUID().toString().replaceAll("-", "");
        emailSendEntity.setEmailId("");
        asyncService.insertEmaillog(emailSendEntity);

        JSONObject object = JSONObject.parseObject(post);

        JSONObject uni_bss_body = object.getJSONObject("UNI_BSS_BODY");
        JSONObject get_email_rsp = uni_bss_body.getJSONObject("GET_EMAIL_RSP");
        if (get_email_rsp != null) {
            if ("200".equals(get_email_rsp.getString("code"))) {
                return ResultObject.ok(get_email_rsp.getString("message"));
            } else {
                return ResultObject.error(get_email_rsp.getString("message"));
            }
        } else {
            return ResultObject.error(object.getJSONObject("UNI_BSS_HEAD").getString("RESP_DESC"));
        }


    }


}
