package com.bonc.plugin.agent.controller;

import com.bonc.plugin.agent.entity.EmailSendEntity;
import com.bonc.plugin.agent.service.IAgentEmailService;
import com.bonc.plugin.agent.util.ResultObject;
import com.bonc.plugin.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/11
 */
@RestController
@Slf4j
@RequestMapping("")
public class AgentEmailController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{"admin"});
    }


    @Autowired
    private IAgentEmailService iAgentEmailService;

    /**
     * 查询收件人
     * @param username
     * @return
     */
    @GetMapping("/emailAcceptList")
    public ResultObject emailAcceptList(String username) {
        log.info("查询收件人接口入参：{}", username);

        return iAgentEmailService.emailAcceptList(ESAPI.encoder().canonicalize(username));
//        return iAgentEmailService.emailAcceptList(username);
    }

    /**
     * 发送邮件
     * @param
     * @return
     */
    @PostMapping("/sendEmail")
    public ResultObject sendEmail(@RequestBody EmailSendEntity emailSendEntity, HttpServletRequest request) {

        String username = TokenUtils.getUsername(request.getHeader("X-Access-Token"));
        log.info("获取name：{}", username);
        if(StringUtils.isNotEmpty(username)){
            emailSendEntity.setSendDoc(username);
        }

        log.info("发送邮件入参：{}", emailSendEntity);
        return iAgentEmailService.sendEmail(emailSendEntity);
    }

    /**
     * 查询邮件信息
     */

    @PostMapping("/getEmailInfo")
    public ResultObject getEmailInfo(String emailId) {
        log.info("查询邮件信息接口入参：{}", emailId);
        return iAgentEmailService.getEmailInfo(ESAPI.encoder().canonicalize(emailId));
//        return iAgentEmailService.getEmailInfo(emailId);
    }


    @GetMapping("/getUserDept")
    public ResultObject getUserDept(String username){
        log.info("查询用户部门接口入参：{}", username);
        return iAgentEmailService.getUserDept(username);
    }


}
