package com.bonc.plugin.agent.controller;

import com.bonc.plugin.agent.entity.reception.SessionReceptionEntity;
import com.bonc.plugin.agent.service.AgentReceptionService;
import com.bonc.plugin.agent.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:   集团接待接口
 * 调用天擎接口：订车 + 业务招待费
 * @author：nihongyu
 * @date: 2024/6/21
 */
@RestController
@Slf4j
@RequestMapping("")
public class AgentReceptionController {



    @Resource
    private Map<String, AgentReceptionService> idGenerator;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{"admin"});
    }

    /**
     *
     * @param objectMap  分别的入参
     * @param type  取值：orderCar / business
     * @return
     */
    @PostMapping("/reception")
    public ResultObject reception(@RequestBody Map<String,Object> objectMap, @RequestParam("type") String type, HttpServletRequest request){
        log.info("招待类型：{}",type);
        log.info("招待入参：{}",objectMap);
        return idGenerator.get(type).reception(objectMap,request);
    }

    /**
     * 组织接待下的三种类型
     * orderCar
     * business
     * orderRoom
     * @param objectMap
     * @param request
     * @return
     */
    @PostMapping("/createSession")
    public ResultObject createSeesion(@RequestBody SessionReceptionEntity objectMap, HttpServletRequest request){
        log.info("组织接待创建入参：{}",objectMap);
        return idGenerator.get("util").createSeesion(objectMap,request);
    }

    @PostMapping("/updateSession")
    public ResultObject updateSeesion(@RequestBody SessionReceptionEntity objectMap, HttpServletRequest request){
        log.info("组织接待更新入参：{}",objectMap);
        return idGenerator.get("util").updateSeesion(objectMap,request);
    }


    @PostMapping("/querySession")
    public ResultObject querySession(@RequestBody SessionReceptionEntity objectMap, HttpServletRequest request){
        log.info("组织接待查询入参：{}",objectMap);
        return idGenerator.get("util").querySession(objectMap,request);
    }


    @PostMapping("/orderCarTime")
    public ResultObject orderCarTime(@RequestBody Map<String,String> params,HttpServletRequest request){
        log.info("解密入参：{}",params);
        return  idGenerator.get("util").orderCarTime(params, request);
    }


    @PostMapping("/workflow")
    public ResultObject workflow(@RequestBody Map<String,String> params,HttpServletRequest request){
        log.info("解密入参：{}",params);
        return  idGenerator.get("util").workflow(params, request);
    }

    @GetMapping("/empattenInfo")
    public ResultObject empattenInfo(HttpServletRequest request){
        return  idGenerator.get("kaoQin").reception(new HashMap<>(),request);
    }



}
