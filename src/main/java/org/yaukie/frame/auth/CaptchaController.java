package org.yaukie.frame.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaukie.auth.constant.AjaxResult;
import org.yaukie.auth.service.api.CaptchaHandlerService;
import org.yaukie.base.annotation.LogAround;
import org.yaukie.base.util.SpringContextUtil;
import org.yaukie.base.util.StringTools;

/**
 * @Author: yuenbin
 * @Date :2021/5/12
 * @Time :16:45
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: 验证码获取逻辑
 **/
@RestController
@RequestMapping(value = "/capcha")
@Api(value = "系统验证获取接口", description = "系统验证获取接口")
@Slf4j
public class CaptchaController {

    private CaptchaHandlerService captchaHandlerService = SpringContextUtil.getBean(CaptchaHandlerService.class)==null?
            null: SpringContextUtil.getBean(CaptchaHandlerService.class) ;

    @GetMapping(value = "/verifyImg")
    @ApiOperation("验证码获取方法")
    @LogAround("验证码获取方法")
    public AjaxResult verifyImg() {

        AjaxResult ajaxResult = AjaxResult.success();

        try {
            if(StringTools.isNotNull(captchaHandlerService)){
                ajaxResult  = captchaHandlerService.getVeriCode() ;
            }
        }catch (Exception ex )
        {
            ajaxResult =  AjaxResult.error(ex.getMessage());
        }

        return ajaxResult ;
    }


}
