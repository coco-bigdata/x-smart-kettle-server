package org.yaukie.frame.log;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaukie.auth.constant.AuthCons;
import org.yaukie.auth.entity.LoginUser;
import org.yaukie.base.annotation.EnablePage;
import org.yaukie.base.annotation.LogAround;
import org.yaukie.base.annotation.OperLog;
import org.yaukie.base.constant.PageResult;
import org.yaukie.base.constant.SysConstant;
import org.yaukie.base.core.controller.BaseController;
import org.yaukie.base.constant.BaseResult;
import org.yaukie.base.core.entity.*;
import org.yaukie.base.core.service.api.XLoginInfoService;
import org.yaukie.base.core.service.api.XOperLogService;
import org.yaukie.base.redis.RedisOrMapCache;
import org.yaukie.base.util.DateUtils;
import org.yaukie.base.util.StringTools;
import org.yaukie.frame.autocode.model.XLogExample;
import org.yaukie.frame.extend.api.IXOnlineUserService;

import java.util.*;

/**
 * @author: yuenbin
 * @create: 2020/11/09 11/28/955
 * @description 用户行为分析模块
 * 提供统一接口 专门为演示环境开发的模块,,生产环境后期也可引用
 * 1 在线用户列表
 * 2 记录已登录用户信息
 * 3 记录用户操作行为
 **/
@RestController
@RequestMapping(value = "/log")
@Api(value = "系统日志记录行为分析", description = "系统日志记录行为分析")
@Slf4j
public class LoggingController extends BaseController {

    @Autowired
    private RedisOrMapCache redisOrMapCache;

    @Autowired
    private IXOnlineUserService ixOnlineUserService;

    @Autowired
    private XLoginInfoService xLoginInfoService;

    @Autowired
    private XOperLogService xOperLogService;

    @RequestMapping(value = "/onlineUserList")
    @ApiOperation("在线用户列表获取方法")
    @LogAround("在线用户列表获取方法")
    public BaseResult onlineUserList(
            @RequestParam(value = "offset",required = false)String offset,
            @RequestParam(value = "limit",required = false)String limit,
            @RequestParam(value = "ipAddr", name = "ipAddr", required = false) String ipAddr,
            @RequestParam(value = "userName", name = "userName", required = false) String userName
    ) {
        Collection<String> keys = redisOrMapCache.keys(AuthCons.LOGIN_TOKEN_KEY + "*");
        List<XOnlineUser> userOnlineList = new ArrayList<XOnlineUser>();
        for (String key : keys) {
            LoginUser user = redisOrMapCache.getCacheObject(key);
            if (StringTools.isNotEmpty(ipAddr) && StringTools.isNotEmpty(userName)) {
                if (StringTools.equals(ipAddr, user.getIpaddr()) && StringTools.equals(userName, user.getUsername())) {
                    userOnlineList.add(ixOnlineUserService.selectOnlineByInfo(ipAddr, userName, user));
                }
            } else if (StringTools.isNotEmpty(ipAddr)) {
                if (StringTools.equals(ipAddr, user.getIpaddr())) {
                    userOnlineList.add(ixOnlineUserService.selectOnlineByIpaddr(ipAddr, user));
                }
            } else if (StringTools.isNotEmpty(userName) && StringTools.isNotNull(user.getXUser())) {
                if (StringTools.equals(userName, user.getUsername())) {
                    userOnlineList.add(ixOnlineUserService.selectOnlineByUserName(userName, user));
                }
            } else {
                userOnlineList.add(ixOnlineUserService.loginUserToUserOnline(user));
            }
        }
        //对list 进行降序排序
        Collections.reverse(userOnlineList);
        //移除list中的空元素
        userOnlineList.removeAll(Collections.singleton(null));
        return BaseResult.success(userOnlineList);
    }

    @GetMapping(value = "/loginedUserList")
    @ApiOperation("历史登录用户信息获取方法")
    @LogAround("历史登录用户信息获取方法")
    @EnablePage
    public BaseResult loginedUserList(
            @RequestParam(value = "offset",required = false)String offset,
            @RequestParam(value = "limit",required = false)String limit,
            @RequestParam(value = "ipAddr", name = "ipAddr", required = false) String ipAddr,
            @RequestParam(value = "userName", name = "userName", required = false) String userName,
            @RequestParam(value = "status", name = "status", required = false) String status,
            @RequestParam(value = "loginStartTime", name = "loginStartTime", required = false) String loginStartTime,
            @RequestParam(value = "loginEndTime", name = "loginEndTime", required = false) String loginEndTime
    ) {

        Map result = new HashMap(16);
        XLoginInfoExample xLoginInfoExample = new XLoginInfoExample();
        XLoginInfoExample.Criteria criteria = xLoginInfoExample.createCriteria();
        if (StringTools.isNotEmpty(ipAddr)) {
            criteria.andIpaddrLike(ipAddr);
        }
        if (StringTools.isNotEmpty(userName)) {
            criteria.andUserNameLike("%"+userName+"%");
        }
        if (StringTools.isNotEmpty(status)) {
            criteria.andStatusLike("%"+status+"%");
        }

        if (StringTools.isEmpty(loginStartTime)) {
            loginStartTime = DateUtils.getTime();
        }

        if (StringTools.isEmpty(loginEndTime)) {
            loginEndTime = DateUtils.getTime();
        }

        criteria.andLoginTimeBetween(DateUtils.parseDate(loginStartTime), DateUtils.parseDate(loginEndTime));
        xLoginInfoExample.setOrderByClause(" login_time desc");
        List<XLoginInfo> xLoginInfos = xLoginInfoService.selectByExample(xLoginInfoExample);

        PageResult pageResult = new PageResult(xLoginInfos);

        result.put(RESULT_ROWS, pageResult.getRows());
        result.put(RESULT_TOTLAL, pageResult.getTotal());

        return BaseResult.success(result);

    }


    @GetMapping(value = "/deleteLoginInfo")
    @ApiOperation("清理登录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids", required = true, dataType = "string" ),
    })
    @OperLog(moduleName = "日志监控-清理登录信息",operationType = SysConstant.OperationType.DELETE)
    public BaseResult deleteLoginInfo(@RequestParam String ids) {
        String[] logIds = ids.split(",");
        XLoginInfoExample xLoginInfoExample = new  XLoginInfoExample();
        XLoginInfoExample.Criteria criteria = xLoginInfoExample.createCriteria();
        List<Long> list = new ArrayList<>() ;
        for (String logId : logIds) {
            list.add(Long.parseLong(logId)) ;
        }

        criteria.andIdIn(list);
        this.xLoginInfoService.deleteByExample(xLoginInfoExample);
        return BaseResult.success();
    }




    @GetMapping(value = "/deleteOperLog")
    @ApiOperation("清理操作日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids", required = true, dataType = "string" ),
    })
    @OperLog(moduleName = "日志监控-清理操作日志",operationType = SysConstant.OperationType.DELETE)
    public BaseResult deleteOperLog(@RequestParam String ids) {
        String[] logIds = ids.split(",");
        XOperLogExample xOperLogExample = new  XOperLogExample();
        XOperLogExample.Criteria criteria = xOperLogExample.createCriteria();
        List<Long> list = new ArrayList<>() ;
        for (String logId : logIds) {
            list.add(Long.parseLong(logId)) ;
        }

        criteria.andIdIn(list);
        this.xOperLogService.deleteByExample(xOperLogExample);
        return BaseResult.success();
    }


    @GetMapping(value = "/userBehaviorLog")
    @ApiOperation("用户操作日志分析方法")
    @LogAround("用户操作日志分析方法")
    @EnablePage
    public BaseResult userBehaviorLog(
            @RequestParam(value = "offset",required = false)String offset,
            @RequestParam(value = "limit",required = false)String limit,
            @RequestParam(value = "moduleName", name = "moduleName", required = false) String moduleName,
            @RequestParam(value = "operUserName", name = "operUserName", required = false) String operUserName,
            @RequestParam(value = "operatorName", name = "operatorName", required = false) String operatorName,
            @RequestParam(value = "status", name = "status", required = false) String status,
            @RequestParam(value = "operStartTime", name = "operStartTime", required = false) String operStartTime,
            @RequestParam(value = "operEndTime", name = "operEndTime", required = false) String operEndTime
    ) {

        Map result = new HashMap(16);
        XOperLogExample xOperLogExample = new XOperLogExample();
        XOperLogExample.Criteria criteria = xOperLogExample.createCriteria();
        if (StringTools.isNotEmpty(moduleName)) {
            criteria.andModuleNameLike("%"+moduleName+"%");
        }
        if (StringTools.isNotEmpty(operUserName)) {
            criteria.andOperatorNameLike("%"+operUserName+"%");
        }

        if (StringTools.isNotEmpty(operatorName)) {
            criteria.andOperatorNameLike("%"+operatorName+"%");
        }

        if (StringTools.isNotEmpty(status)) {
            criteria.andStatusLike(status);
        }

        if (StringTools.isEmpty(operStartTime)) {
            operStartTime = DateUtils.getTime();
        }

        if (StringTools.isEmpty(operEndTime)) {
            operEndTime = DateUtils.getTime();
        }

        criteria.andOperTimeBetween(DateUtils.parseDate(operStartTime), DateUtils.parseDate(operEndTime));
        xOperLogExample.setOrderByClause(" oper_time desc");
        List<XOperLog> xOperLogs = xOperLogService.selectByExample(xOperLogExample);

        PageResult pageResult = new PageResult(xOperLogs);

        result.put(RESULT_ROWS, pageResult.getRows());
        result.put(RESULT_TOTLAL, pageResult.getTotal());

        return BaseResult.success(result);

    }

}
