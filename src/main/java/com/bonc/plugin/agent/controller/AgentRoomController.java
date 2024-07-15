package com.bonc.plugin.agent.controller;

import com.alibaba.fastjson.JSON;
import com.bonc.plugin.agent.entity.RoomQueryEntityVo;
import com.bonc.plugin.agent.entity.RoomReservationEntityReq;
import com.bonc.plugin.agent.service.IAgentRoomService;
import com.bonc.plugin.agent.util.ResultObject;
import com.bonc.plugin.utils.LoginUser;
import com.bonc.plugin.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/6
 */

@RestController
@Slf4j
@RequestMapping("")
public class AgentRoomController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{"admin"});
    }


    @Autowired
    private IAgentRoomService iAgentService;

    /**
     * 查询可预定会议室
     * @param roomQueryEntityVo
     * @return
     */
    @PostMapping("/roomList")
    public ResultObject roomList(@RequestBody RoomQueryEntityVo roomQueryEntityVo) {

        log.info("查询会议室入参：{}", roomQueryEntityVo);
        return iAgentService.roomlist(roomQueryEntityVo);
    }

    /**
     * 楼层查询
     * @return
     */
    @PostMapping("/floorList")
    public ResultObject floorList() {
        LoginUser loginUser = TokenUtils.getLoginUser();
        log.info("系统登录用户：{}", JSON.toJSONString(loginUser));
        log.info("楼层接口查询");
        return iAgentService.floorlist();
    }

    /**
     * 会议室预定
     * @param roomReservationEntityReq
     * @return
     */
    @PostMapping("/roomReservation")
    public ResultObject roomReservation(@Validated @RequestBody RoomReservationEntityReq roomReservationEntityReq, HttpServletRequest request) {

        //        log.info("获取token ：{}", request.getHeader("X-Access-Token"));
        String username = TokenUtils.getUsername(request.getHeader("X-Access-Token"));
        log.info("获取name：{}", username);
        if(StringUtils.isNotEmpty(username)){
            roomReservationEntityReq.setUsername(username);
        }

        log.info("会议室预定接口入参：{}", roomReservationEntityReq);
        return iAgentService.roomReservation(roomReservationEntityReq);
    }

    /**
     * 预定后查询
     * @param roomQueryEntityVo
     * @return
     */
    @PostMapping("/reservationList")
    public ResultObject reservationList(@RequestBody RoomQueryEntityVo roomQueryEntityVo) {
        log.info("预定过的会议室查询入参：{}",roomQueryEntityVo);
        return iAgentService.reservationList(roomQueryEntityVo);
    }

    @PostMapping("/webhook")
    public ResultObject webhook(@RequestBody Map<String,String> map){
        log.info("槽位设置入参：{}",map);
        return iAgentService.webhook(map);
    }

    @PostMapping("/decrypt")
    public ResultObject decrypt(@RequestBody Map<String,String> params){
        log.info("解密入参：{}",params);
        return iAgentService.decrypt(params);
    }

}
