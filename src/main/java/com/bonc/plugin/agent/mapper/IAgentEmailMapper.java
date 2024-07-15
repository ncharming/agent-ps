package com.bonc.plugin.agent.mapper;


import com.bonc.plugin.agent.entity.EmailSendEntity;
import com.bonc.plugin.agent.entity.UserInfoEntiry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/7
 */


@Mapper
public interface IAgentEmailMapper {

    List<UserInfoEntiry> emailAcceptList(String username);
    EmailSendEntity getEmailInfo(String username);

    void insertEmailLog(EmailSendEntity emailSendEntity);


}
