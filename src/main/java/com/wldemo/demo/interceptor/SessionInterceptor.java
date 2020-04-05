package com.wldemo.demo.interceptor;

import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.User;
import com.wldemo.demo.model.UserExample;
import com.wldemo.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service//以便spring可以接管下面的注入
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();//从客户端的请求里拿cookies，拿到cookie拿token再找用户，最后放进session里
        if(cookies != null && cookies.length!=0) { //不判空的话，如果清除cookie会出现问题
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {//找一个叫token 的cookie
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);//通用sql语句拼接
                    List<User> users = userMapper.selectByExample(userExample);//返回几个都是list类型
                    //User user = userMapper.findByToken(token);
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                        Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                        request.getSession().setAttribute("unreadCount",unreadCount);
                    }
                    break;//找到了user后退出
                }
            }
        }
        return true;//表示继续执行后续的controller和interceptor，否则为false请求结束不执行
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
