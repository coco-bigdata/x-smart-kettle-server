package org.yaukie.frame.log;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaukie.auth.constant.AuthCons;
import org.yaukie.auth.entity.LoginUser;
import org.yaukie.base.annotation.EnablePage;
import org.yaukie.base.annotation.LogAround;
import org.yaukie.base.core.controller.BaseController;
import org.yaukie.base.constant.BaseResult;
import org.yaukie.base.core.entity.XOnlineUser;
import org.yaukie.base.redis.RedisOrMapCache;
import org.yaukie.base.util.StringTools;
import org.yaukie.frame.extend.api.IXOnlineUserService;

import java.util.*;

/**
* @author: yuenbin
* @create: 2020/11/09 11/28/955
 * @description 用户行为分析模块
 * 提供统一接口 专门为演示环境开发的模块,,生产环境后期也可引用
 * 1 在线用户列表
 *2 记录已登录用户信息
 * 3 记录用户操作行为
**/
@RestController
@RequestMapping(value = "/log")
@Api(value = "系统日志记录行为分析", description = "系统日志记录行为分析")
@Slf4j
public class LoggingController extends BaseController {

    @Autowired
    private RedisOrMapCache redisOrMapCache ;
    
    @Autowired
    private IXOnlineUserService ixOnlineUserService ; 

    @RequestMapping(value = "/onlineUserList")
    @ApiOperation("在线用户列表获取方法")
    @LogAround("在线用户列表获取方法")
    public BaseResult onlineUserList(
            @RequestParam(value = "ipAddr",name = "ipAddr",required = false) String ipAddr,
            @RequestParam(value = "userName",name = "userName",required = false) String userName
    ) {
        Collection<String> keys = redisOrMapCache.keys(AuthCons.LOGIN_TOKEN_KEY + "*");
        List<XOnlineUser> userOnlineList = new ArrayList<XOnlineUser>();
        for (String key : keys)
        {
            LoginUser user = redisOrMapCache.getCacheObject(key);
            if (StringTools.isNotEmpty(ipAddr) && StringTools.isNotEmpty(userName))
            {
                if (StringTools.equals(ipAddr, user.getIpaddr()) && StringTools.equals(userName, user.getUsername()))
                {
                    userOnlineList.add(ixOnlineUserService.selectOnlineByInfo(ipAddr, userName, user));
                }
            }
            else if (StringTools.isNotEmpty(ipAddr))
            {
                if (StringTools.equals(ipAddr, user.getIpaddr()))
                {
                    userOnlineList.add(ixOnlineUserService.selectOnlineByIpaddr(ipAddr, user));
                }
            }
            else if (StringTools.isNotEmpty(userName) && StringTools.isNotNull(user.getXUser()))
            {
                if (StringTools.equals(userName, user.getUsername()))
                {
                    userOnlineList.add(ixOnlineUserService.selectOnlineByUserName(userName, user));
                }
            }
            else
            {
                userOnlineList.add(ixOnlineUserService.loginUserToUserOnline(user));
            }
        }
        //对list 进行降序排序
        Collections.reverse(userOnlineList);
        //移除list中的空元素
        userOnlineList.removeAll(Collections.singleton(null)) ;
         return BaseResult.success(userOnlineList) ;
    }

    @GetMapping(value = "/loginedUserList")
    @ApiOperation("历史登录用户信息获取方法")
    @LogAround("历史登录用户信息获取方法")
    @EnablePage
    public BaseResult loginedUserList(
            @RequestParam(value = "userName",name = "userName",required = false) String userName,
            @RequestParam(value = "loginTime",name = "loginTime",required = false) String loginTime

    ) {

        Map param = new HashMap(16) ;

        return null;
    }

    @GetMapping(value = "/userBehaviorLog")
    @ApiOperation("用户操作日志分析方法")
    @LogAround("用户操作日志分析方法")
    @EnablePage
    public BaseResult userBehaviorLog(
            @RequestParam(value = "operUserName",name = "operUserName",required = false) String operUserName,
            @RequestParam(value = "operUrl",name = "operUrl",required = false) String operUrl,
            @RequestParam(value = "operTime",name = "operTime",required = false) String operTime
    ) {
        return  null ;
    }

}
