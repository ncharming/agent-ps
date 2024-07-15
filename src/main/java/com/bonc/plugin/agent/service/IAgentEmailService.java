package com.bonc.plugin.agent.service;

import com.bonc.plugin.agent.entity.EmailSendEntity;
import com.bonc.plugin.agent.util.ResultObject;


/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/6
 */
public interface IAgentEmailService {

    ResultObject emailAcceptList(String username);
    ResultObject getUserDept(String username);
    ResultObject getEmailInfo(String emailId);
    ResultObject sendEmail(EmailSendEntity emailSendEntity);



}
