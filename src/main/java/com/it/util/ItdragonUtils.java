package com.it.util;

import com.it.config.ShiroSpringConfig;
import com.it.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 描述：〈工具类〉
 */
@Component
public class ItdragonUtils {

    private static final String ALGORITHM_NAME = "MD5";
    private static final Integer HASH_ITERATIONS = 1024;

    /**
     * 加盐加密的策略非常多,根据实际业务来
     */
    public void entryptPassword(User user) {
        String salt = UUID.randomUUID().toString();
        String temPassword = user.getPlainPassword();
        Object md5Password = new SimpleHash(ALGORITHM_NAME, temPassword, ByteSource.Util.bytes(salt), HASH_ITERATIONS);
        user.setSalt(salt);
        user.setPassword(md5Password.toString());
    }

    /**
     * 判断当前系统是否需要加密
     */
    public static boolean isEncrypted() {
        ShiroSpringConfig shiroSpringConfig = new ShiroSpringConfig();
        CustomConfiguration customConfiguration = shiroSpringConfig.setIsEncrypted();
        if ("yes".equals(customConfiguration.getIsEncrypted())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取ShiroSession
     *
     * @return
     */
    public Session getShiroSession() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return session;
    }

    /**
     * 获取Session用户信息
     *
     * @return
     */
    public User getSessionUser() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user;
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return
     */
    public String getSessionUserName() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user.getUserName();
    }
    /**
     * 获取当前登录用户的姓名
     *
     * @return
     */
    public String getSessionRealName() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user.getRealName();
    }
    /**
     * 获取当前登录用户的头像
     *
     * @return
     */
    public String getSessionUserImg() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user.getImgUrl();
    }
    /**
     * 获取当前登录用户的id
     *
     * @return
     */
    public String getSessionUserId() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user.getId();
    }

    /**
     * 生成随便编号
     *
     * @return
     */
    public String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) { //有可能是负数
            hashCodeV = -hashCodeV;
        }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    /**
     * 判断用户是否登录
     */
    public boolean isGogin() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isAuthenticated();
    }

    /**
     * 字符串不为空的判断函数
     *
     * @param string
     * @return
     */
    public static boolean stringIsNotBlack(String string) {
        if (string != null && !"".equals(string)) {
            return true;
        }
        return false;
    }

    /**
     * 获取从今天开始的7天时间包含今天
     */
    public List<String> get7DayBeginNow() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(DateUtil.getNowDate());
        arrayList.add(DateUtil.addDay(1, "yyyy-MM-dd"));
        arrayList.add(DateUtil.addDay(2, "yyyy-MM-dd"));
        arrayList.add(DateUtil.addDay(3, "yyyy-MM-dd"));
        arrayList.add(DateUtil.addDay(4, "yyyy-MM-dd"));
        arrayList.add(DateUtil.addDay(5, "yyyy-MM-dd"));
        arrayList.add(DateUtil.addDay(6, "yyyy-MM-dd"));
        return arrayList;
    }

}
