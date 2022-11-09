package com.myhr.hr.service.api.impl;

import com.myhr.auth.AuthRole;
import com.myhr.auth.AuthSystemTool;
import com.myhr.auth.UserAuth;
import com.myhr.common.BaseResponse;
import com.myhr.common.constant.RabbitMqConstants;
import com.myhr.hr.mapper.RoleMapper;
import com.myhr.hr.mapper.UserRoleMapper;
import com.myhr.hr.model.UserRoleDto;
import com.myhr.hr.service.api.ApiUserAuthService;
import com.myhr.rabbitMq.MessageContent;
import com.myhr.rabbitMq.RabbitMqService;
import com.myhr.utils.HttpUtils;
import com.myhr.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 用户角色权限对应关系同步到AUTH系统
 */

@Service
@Slf4j
public class ApiUserAuthServiceImpl implements ApiUserAuthService {

    @Value("${auth.url}")
    private String authUrl;

    @Autowired
    AuthSystemTool authSystemTool;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RabbitMqService rabbitMqService;


    private final String userAuthSyncUrl = "/api/v1/auth/userAuth/sync.json";

    //该方法只做接入auth系统后的，第一次初始化用户角色权限。
    @Override
    public BaseResponse userAuthSync() {
        try {
            String token = authSystemTool.getToken();
            if (StringUtils.isBlank(token)) {
                return BaseResponse.error("未获取到token");
            }

            //所有用户权限map
            Map<String, UserAuth> data = new HashMap<>();

            //查出所有用户的所有角色
            List<UserRoleDto> userRoleDtoList = userRoleMapper.getAllUserRole();
            if (CollectionUtils.isNotEmpty(userRoleDtoList)) {
                //将角色根据用户登录名分组
                Map<String, List<UserRoleDto>> userRoleMap = userRoleDtoList.stream().collect(Collectors.groupingBy(e -> e.getLoginName()));

                for (Map.Entry<String, List<UserRoleDto>> userRoleEntry : userRoleMap.entrySet()) {
                    String userNo = userRoleEntry.getKey();
                    //用户角色id列表
                    List<String> userRoleIds = userRoleEntry.getValue().stream().map(e -> e.getRoleId().toString()).collect(Collectors.toList());
                    if(!data.containsKey(userNo)) {
                        data.put(userNo, new UserAuth().setUserNo(userNo));
                    }
                    UserAuth auth = data.get(userNo);
                    auth.getAuths().put("ROLE", userRoleIds);
                }

            }

            if (data.isEmpty()) {
                return BaseResponse.error("无用户角色权限需要同步至AUTH");
            }

            //开始向Auth推送用户角色权限
            Map<String, String> headers = new HashMap<>();
            headers.put("Token", token);
            headers.put("Content-Type", ContentType.APPLICATION_JSON.toString());
            String url = authUrl + userAuthSyncUrl;
            String paramBody = JsonUtils.toJson(data.values());
            HttpResponse response = HttpUtils.doPost(url, headers, paramBody);
            if(response.getStatusLine().getStatusCode() != 200) {
                log.warn("同步用户角色信息报错，错误代码：{}", response.getStatusLine().getStatusCode());
            }else {
                log.warn("同步用户角色信息成功，返回数据：{}", IOUtils.toString(response.getEntity().getContent()));
            }
            return BaseResponse.success();
        } catch (IOException e) {
            e.printStackTrace();
            return BaseResponse.error("系统异常");
        }
    }

    @Override
    public BaseResponse userRoleSync() {
        String token = authSystemTool.getToken();
        if (StringUtils.isBlank(token)) {
            return BaseResponse.error("未获取到token");
        }

        //获取系统中所有的角色
        List<AuthRole> roleList = roleMapper.getAllAuthRole();

        MessageContent messageContent = new MessageContent();
        messageContent.setToken(token);
        messageContent.setOperation(RabbitMqConstants.REFRESH_ROLE_INFO);
        messageContent.setData(roleList);

        rabbitMqService.sendMessage(RabbitMqConstants.AUTH_ROLE_EXCHANGE, RabbitMqConstants.AUTH_ROLE_QUEUE_NEW, messageContent);
        return BaseResponse.success();
    }
}
