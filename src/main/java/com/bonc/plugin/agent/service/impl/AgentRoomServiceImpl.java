package com.bonc.plugin.agent.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.plugin.agent.entity.*;
import com.bonc.plugin.agent.mapper.IAgentRoomMapper;
import com.bonc.plugin.agent.service.IAgentRoomService;
import com.bonc.plugin.agent.util.RedisCache;
import com.bonc.plugin.agent.util.RequestUtil;
import com.bonc.plugin.agent.util.ResultObject;
import com.bonc.plugin.utils.LoginUser;
import com.bonc.plugin.utils.SM2Utils;
import com.bonc.plugin.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/6
 */
@Service
@RefreshScope
@Slf4j
public class AgentRoomServiceImpl implements IAgentRoomService {

    @Autowired
    private IAgentRoomMapper iAgentRoomMapper;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${agent.roomlist_url}")
    private String roomlist_url;

    @Value("${agent.floorlist_url}")
    private String floorlist_url;

    @Value("${agent.roomReservation_url}")
    private String roomReservation_url;

    @Value("${agent.webhook_url}")
    private String webhook_url;

    @Value("${agent.privateKey}")
    private String privateKey;

    @Override
    public ResultObject roomlist(RoomQueryEntityVo roomQueryEntityVo) {
        ResultObject result = new ResultObject<>();

        Map map = new HashMap<>();
        List<RoomQueryEntityDto> match = new ArrayList<>();
        List<RoomQueryEntityDto> bakup = new ArrayList<>();

        JSONObject data1 = RedisCache.getData(roomQueryEntityVo.getUsername());
        if (data1 == null) {
            return ResultObject.error("用户信息获取失败");
        }

        String deptNo = data1.getString("orgCode");
//        String deptNo = "353959";

        //传入的地点
        String place = roomQueryEntityVo.getPlace();

        StringBuilder builder = new StringBuilder();
        builder.append(roomlist_url).append('?')
                .append("date=").append(roomQueryEntityVo.getDate());
        if (StringUtils.isNotEmpty(roomQueryEntityVo.getFloor())) {
            builder.append("&floor=").append(roomQueryEntityVo.getFloor());
        }

        if (StringUtils.isNotEmpty(roomQueryEntityVo.getAmpm())) {
            builder.append("&ampm=").append(roomQueryEntityVo.getAmpm());
        }

        if (StringUtils.isNotEmpty(place) && place.contains("长宁")) {
            builder.append("&floor=").append("长宁25F");
        }


        String resultJson = RequestUtil.get(builder.toString());
        if (resultJson != null) {

            if(resultJson.equals("timeout")) {
                return ResultObject.error("调用外部接口失败：会议室查询接口超时");
            }

            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            //调用接口报错;直接返回
            if (jsonObject.getInteger("code") != 200) {
                log.info("调用外部接口失败：会议室查询：{}", jsonObject);
                return ResultObject.error("调用外部接口失败：会议室查询");
            }

            JSONArray data = jsonObject.getJSONArray("data");

            if (data.isEmpty()) {
                log.info("调用外部查询会议室接口为空");
                map.put("match", match);
                map.put("bakup", bakup);

                result.setResult(map);
                return result;
            }
            //所有的会议室
            List<RoomQueryEntityDto> roomQueryEntityDto = JSONObject.parseArray(JSON.toJSONString(data), RoomQueryEntityDto.class);

            //传入是杨浦，那么排除长宁的
            if ("杨浦".equals(place)) {
                Iterator<RoomQueryEntityDto> iterator = roomQueryEntityDto.iterator();
                while (iterator.hasNext()) {
                    RoomQueryEntityDto tmp = iterator.next();
                    if (tmp.getFloor().contains("长宁")) {
                        iterator.remove();
                    }
                }
            }

            if(roomQueryEntityDto!=null && roomQueryEntityDto.size()==0){
                map.put("match", match);
                map.put("bakup", bakup);

                result.setResult(map);
                return result;
            }


            //查询部门对应的名称-码表
            DeptFloorDictEntity deptFloorDictEntity = iAgentRoomMapper.selectDict(deptNo);

            /**
             * 测试账号，没有对应的楼层，随便返回一个
             */
            if(deptFloorDictEntity==null){
                match.add(roomQueryEntityDto.get(0));
                for (int i = 1; i < roomQueryEntityDto.size(); i++) {
                    bakup.add(roomQueryEntityDto.get(i));
                }
                map.put("match", match);
                map.put("bakup", bakup);

                result.setResult(map);
                return result;
            }
            String floorDept = deptFloorDictEntity.getFloor().replace("F", "").replace("长宁", "");
            deptFloorDictEntity.setFloorNum(Integer.parseInt(floorDept));



            /**
             * 将 list 分别放到两个 map-value 中
             * match 只会有一个；其余都在 backup
             *
             */


            //使用楼层排序：就近楼层推荐
            roomQueryEntityDto.forEach(t -> {
                String floor = t.getFloor();
                String floor_num = floor.replace("F", "").replace("杨浦", "").replace("会议层", "6").replace("长宁", "");
                t.setFloorNum(Integer.parseInt(floor_num));
            });
            Collections.sort(roomQueryEntityDto, (a, b) -> Integer.compare(a.getFloorNum(), b.getFloorNum()));

//            System.out.println(JSON.toJSONString(roomQueryEntityDto));

            int resultNum = findClosestIndex(roomQueryEntityDto, deptFloorDictEntity, place);
            match.add(roomQueryEntityDto.get(resultNum));
            roomQueryEntityDto.remove(resultNum);
            roomQueryEntityDto.forEach(t -> {
                bakup.add(t);
            });

//            boolean isOne = true;
//            for (int i = 0; i < roomQueryEntityDto.size(); i++) {
//                RoomQueryEntityDto t = roomQueryEntityDto.get(i);
//
//                /**
//                 * 如果都没有匹配的，直接取最后一个放到推荐位
//                 */
//                if (i == roomQueryEntityDto.size() - 1 && isOne) {
//                    match.add(t);
//                    continue;
//                }
//
//                if (Objects.isNull(deptFloorDictEntity)) {
//                    if (isOne) {
//                        match.add(t);
//                        isOne = false;
//                    } else {
//                        bakup.add(t);
//                    }
//                }
//                else {
//                    if (deptFloorDictEntity.getFloor().equals(t.getFloor())) {
//                        if (isOne) {
//                            if (deptFloorDictEntity.getFloor().equals("6F") || "会议层".equals(t.getFloor())) {
//                                match.add(t);
//                            } else {
//                                match.add(t);
//                            }
//                            isOne = false;
//                        } else { //推荐位的只需要一个
//                            bakup.add(t);
//                        }
//                    } else {
//                        //传入是杨浦，那么排除长宁的
//                        if ("杨浦".equals(place) && t.getFloor().contains("长宁")) {
//                            continue;
//                        }
//                        bakup.add(t);
//                    }
//                }
//            }
//
            map.put("match", match);
            map.put("bakup", bakup);

            result.setResult(map);
            return result;
        }
        return result.error500("调用外部接口失败：会议室查询");

    }

    @Override
    public ResultObject roomReservation(RoomReservationEntityReq roomReservationEntityReq) {

        LoginUser loginUser = TokenUtils.getLoginUser();
        log.info("系统登录用户：{}",JSON.toJSONString(loginUser));

        ResultObject result = new ResultObject<>();
        RoomReservationEntityVo roomReservationEntityVo = new RoomReservationEntityVo();
        roomReservationEntityVo.setMeetingname(roomReservationEntityReq.getMeetingname());
        roomReservationEntityVo.setName1(roomReservationEntityReq.getName1());


        JSONObject jsonObject1 = RedisCache.getData(roomReservationEntityReq.getUsername());
        if (jsonObject1 == null) {
            return ResultObject.error("用户信息获取失败");
        }

        String phone = jsonObject1.getString("phone");
        String userid = jsonObject1.getString("user");
        String orgName = jsonObject1.getString("orgName");

//        String orgName = "上海市分公司数字化部";

        //不传，代表全天
        String ampm = roomReservationEntityReq.getAmpm();
        if (StringUtils.isEmpty(ampm)) {
            log.info("预定全天会议室");
            ampm = "08:30,17:00";
        }
        String[] split = ampm.split(",");


        roomReservationEntityVo.setSqdeptname(orgName);
        roomReservationEntityVo.setTelephone(phone);
        roomReservationEntityVo.setStartTime(roomReservationEntityReq.getDate() + " " + split[0]);
        roomReservationEntityVo.setTerminalTime(roomReservationEntityReq.getDate() + " " + split[1]);
        roomReservationEntityVo.setUserid(userid);

        log.info("调用预定会议室接口入参：{}", roomReservationEntityVo);
        String resultJson = RequestUtil.formData(roomReservation_url, roomReservationEntityVo);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            if ((Integer) jsonObject.get("code") == 200) {
                result.setCode(200);
                result.setMessage((String) jsonObject.get("message"));
                /**
                 * 预定会议室成功，异步记录日志
                 */
                String date = roomReservationEntityReq.getDate();
                roomReservationEntityVo.setDate(date);
                asyncService.insertlog(roomReservationEntityVo);
            } else {
                result.setCode((Integer) jsonObject.get("code"));
                result.setMessage((String) jsonObject.get("message"));
            }

            return result;
        }

        return result.error500("调用外部接口失败：会议室预定接口");
    }

    /**
     * 查询预定后的会议室
     *
     * @param roomQueryEntityVo
     * @return
     */
    @Override
    public ResultObject reservationList(RoomQueryEntityVo roomQueryEntityVo) {
        ResultObject result = new ResultObject<>();

        String loginId = ESAPI.encoder().canonicalize(roomQueryEntityVo.getUsername());
//        String loginId = roomQueryEntityVo.getUsername();
//        String date = roomQueryEntityVo.getDate();
        String date = ESAPI.encoder().canonicalize(roomQueryEntityVo.getDate());

        if (StringUtils.isEmpty(loginId) && StringUtils.isEmpty(date)) {
            return result.error500("参数不能同时为空");
        }

        List<RoomReservationEntityReq> roomReservationEntityReqList = iAgentRoomMapper.selectReservationList(loginId, date);
        result.setResult(roomReservationEntityReqList);

        return result;
    }

    @Override
    public ResultObject floorlist() {
        ResultObject result = new ResultObject<>();
        String resultJson = RequestUtil.get(floorlist_url);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            JSONArray data = jsonObject.getJSONArray("data");
            result.setResult(data);
            return result;
        }
        return result.error500("调用流程平台接口失败");
    }


    @Override
    public ResultObject webhook(Map<String, String> map) {
        ResultObject result = new ResultObject();
        String post = RequestUtil.post(webhook_url, map);

        result.setResult(post);
        return result;
    }

    @Override
    public ResultObject decrypt(Map<String, String> params) {
        ResultObject result = new ResultObject();
        String decryptData = SM2Utils.decryptByPrivateKey(params.get("data"), privateKey);
        Map map = new HashMap();
        map.put("data", decryptData);
        result.setResult(map);
        return result;
    }


    public int findClosestIndex(List<RoomQueryEntityDto> roomQueryEntityDtos, DeptFloorDictEntity deptFloorDictEntity, String place) {

        int left = 0;
        int right = roomQueryEntityDtos.size() - 1;
        int closest = 0;

        int target = deptFloorDictEntity.getFloorNum();

        while (left <= right) {
            int mid = (left + right) / 2;
            if (roomQueryEntityDtos.get(mid).getFloorNum() == target) {
                return mid;
            } else if (roomQueryEntityDtos.get(mid).getFloorNum() < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }

            // 更新最接近的位置
            if (Math.abs( roomQueryEntityDtos.get(mid).getFloorNum() - target)
                    < Math.abs( roomQueryEntityDtos.get(closest).getFloorNum() - target)) {
                closest = mid;
            }
        }

        return closest;
    }

}
