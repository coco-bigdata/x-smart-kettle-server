package org.yaukie.frame.extend.impl;


import org.springframework.stereotype.Service;
import org.yaukie.auth.entity.LoginUser;
import org.yaukie.base.core.entity.XOnlineUser;
import org.yaukie.base.util.DateUtils;
import org.yaukie.base.util.StringTools;
import org.yaukie.frame.extend.api.IXOnlineUserService;

/**
 * 在线用户 服务层处理
 * 
 */
@Service
public class XOnlineUserServiceImpl implements IXOnlineUserService
{
    /**
     * 通过登录地址查询信息
     * 
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public XOnlineUser selectOnlineByIpaddr(String ipaddr, LoginUser user)
    {
        if (StringTools.equals(ipaddr, user.getIpaddr()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     * 
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public XOnlineUser selectOnlineByUserName(String userName, LoginUser user)
    {
        if (StringTools.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     * 
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public XOnlineUser selectOnlineByInfo(String ipaddr, String userName, LoginUser user)
    {
        if (StringTools.equals(ipaddr, user.getIpaddr()) && StringTools.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     * 
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public XOnlineUser loginUserToUserOnline(LoginUser user)
    {
        if (StringTools.isNull(user) || StringTools.isNull(user.getXUser()))
        {
            return null;
        }
        XOnlineUser XOnlineUser = new XOnlineUser();
        XOnlineUser.setTokenId(user.getToken());
        XOnlineUser.setUserName(user.getUsername());
        XOnlineUser.setIpaddr(user.getIpaddr());
        XOnlineUser.setLoginLocation(user.getLoginLocation());
        XOnlineUser.setBrowser(user.getBrowser());
        XOnlineUser.setOs(user.getOs());
        XOnlineUser.setLoginTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,user.getLoginTime()));
        if (StringTools.isNotNull(user.getXUser().getDept()))
        {
            XOnlineUser.setDeptName(user.getXUser().getDept().getDeptName());
        }
        return XOnlineUser;
    }
}
