package org.yaukie.frame.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.yaukie.auth.constant.AjaxResult;
import org.yaukie.auth.constant.AuthCons;
import org.yaukie.auth.entity.LoginBody;
import org.yaukie.auth.entity.LoginUser;
import org.yaukie.auth.service.api.LoginHandlerService;
import org.yaukie.auth.service.api.TokenHandlerService;
import org.yaukie.base.annotation.LogAround;
import org.yaukie.base.core.entity.XUser;
import org.yaukie.base.util.SpringContextUtil;
import org.yaukie.base.util.StringTools;
import org.yaukie.base.uuid.IdUtils;

/**
 * @Author: yuenbin
 * @Date :2021/5/12
 * @Time :16:45
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: 用于单点登录
 **/
@RestController
 @Api(value = "系统登录单点登录接口", description = "系统登录单点登录接口")
@Slf4j
public class LoginController {


    private LoginHandlerService loginHandlerService  = SpringContextUtil.getBean(LoginHandlerService.class)==null?null:SpringContextUtil.getBean(LoginHandlerService.class) ;

    private TokenHandlerService tokenHandlerService = SpringContextUtil.getBean(TokenHandlerService.class)==null?null: SpringContextUtil.getBean(TokenHandlerService.class) ;


    @PostMapping(value = "/login")
    @ApiOperation("单点登录方法")
    @LogAround("单点登录方法")
    public AjaxResult login(@RequestBody LoginBody loginBody ) {

        if(StringTools.isNull(loginBody)){
            return AjaxResult.error("请求参数为空!");
        }

        String jwtToken = IdUtils.fastUUID();

        try {
            if(StringTools.isNotNull(loginHandlerService)){
                jwtToken = loginHandlerService.login(loginBody) ;
            }else {
                   return  AjaxResult.error(AuthCons.Login.LOGIN_FAIL.getCode(),"认证模块未初始化!");
            }
        }catch (Exception ex )
        {
                 return AjaxResult.error(AuthCons.Login.LOGIN_FAIL.getCode(),ex.getMessage());
        }

        return AjaxResult.success(AuthCons.Login.LOGIN_SUCCESS.getDes(),jwtToken) ;
    }

    @DeleteMapping(value = "/{token}")
    @ApiOperation("注销登录")
    @LogAround("注销登录")
    public AjaxResult logout(@PathVariable String token) {
        AjaxResult ajaxResult = AjaxResult.success(AuthCons.Login.LOGIN_OUT.getDes());
        try {
            if(StringTools.isNotNull(loginHandlerService)){
                ajaxResult= loginHandlerService.logout(token) ;
            }else {
                ajaxResult =AjaxResult.error(AuthCons.Login.LOGIN_OUT_FAIL.getCode(),"认证模块未初始化!");
            }
        }catch (Exception ex )
        {
            ajaxResult =  AjaxResult.error(AuthCons.Login.LOGIN_OUT_FAIL.getCode(),ex.getMessage());
        }

        return ajaxResult ;
    }

    @GetMapping(value = "/getUserInfo")
    @ApiOperation("获取登录用户信息")
    @LogAround("获取登录用户信息")
    public AjaxResult getUserInfo(){
        AjaxResult ajaxResult = AjaxResult.success( );
        LoginUser loginUser = tokenHandlerService.getLoginUser(SpringContextUtil.getRequest()) ;
        XUser xUser = new XUser() ;
        if(StringTools.isNotNull(loginUser)){
            xUser = loginUser.getXUser() ;
            ajaxResult.put("user",xUser);
        }

        return  ajaxResult ;

    }

}
