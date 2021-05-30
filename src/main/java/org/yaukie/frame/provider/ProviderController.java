package org.yaukie.frame.provider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yaukie.auth.constant.AjaxResult;
import org.yaukie.base.annotation.EnablePage;
import org.yaukie.base.annotation.LogAround;
import org.yaukie.base.core.controller.BaseController;
import org.yaukie.base.config.UniformReponseHandler;
import org.yaukie.base.constant.BaseResult;
import org.yaukie.base.redis.RedisOrMapCache;
import org.yaukie.base.util.StringTools;
import org.yaukie.frame.autocode.model.XDict;
import org.yaukie.frame.autocode.model.XDictExample;
import org.yaukie.frame.autocode.service.api.XDictService;
import org.yaukie.frame.kettle.core.XJobSubmit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author: yuenbin
* @create: 2020/11/09 11/28/955
**/
@RestController
@RequestMapping(value = "/provider")
@Api(value = "系统统一接口服务", description = "系统统一接口服务")
@Slf4j
public class ProviderController extends BaseController {

    @Autowired
    private XJobSubmit xJobSubmit;

    @Autowired
    private RedisOrMapCache redisOrMapCache ;

    @Autowired
    private XDictService xDictService ;

    @GetMapping(value = "/keys")
    @ApiOperation("系统内存缓存key方法")
    @EnablePage
    @LogAround("系统内存缓存key方法")
    public AjaxResult keys(
            @RequestParam(value = "patterns",name = "patterns",required = false) String patterns
    ) {
        if(StringTools.isNull(patterns)){
            patterns="" ;
        }
        Set<String> set = (Set<String>) redisOrMapCache.keys(patterns);
          return AjaxResult.success(set);
    }


    @GetMapping(value = "/qryQueueTasks")
    @ApiOperation("查询队列任务数")
    public BaseResult qryQueueTasks() {
        int currentTasks = xJobSubmit.getCurrentTaskCounts();
        log.debug("当前任务数有{}个", currentTasks);
        Map data = new HashMap();
        data.put("TASKS", currentTasks);
        return new UniformReponseHandler<>().sendSuccessResponse(data);
    }

    @GetMapping(value = "/getSysDicts")
    @ApiOperation("查询系统枚举")
    public BaseResult getSysDicts(
            @RequestParam(value = "dictType",name = "dictType") String dictType ) {
        XDictExample xDictExample = new XDictExample() ;
        xDictExample.createCriteria().andDictIdEqualTo(dictType);
        List<XDict> xDictxList = xDictService.selectByExample(xDictExample);
        return new UniformReponseHandler<>().sendSuccessResponse(xDictxList);
    }

}
