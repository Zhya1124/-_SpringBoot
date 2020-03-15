package com.wldemo.demo.controller;

import com.wldemo.demo.dto.AccessTokenDTO;
import com.wldemo.demo.dto.GithubUser;
import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.User;
import com.wldemo.demo.provider.GithubProvider;
import com.wldemo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;
    @Value("${client_id}")
    private String client_id;
    @Value("${client_secret}")
    private String client_secret;
    @Value("${redirect_uri}")
    private String redirect_uri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);//用get到的code获取accessToken，并回到redirect_uri
        GithubUser githubUser = githubProvider.getUser(accessToken);//用accessToken post方式获取用户信息
        System.out.println(githubUser.getName());
        if(githubUser != null && githubUser.getId() != null){
            //登陆成功，保存用户信息，cookie和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());
            userService.createOrUpdate(user);
//            userMapper.insert(user);//插入用户到数据库,因为这样会导致重复插入用户，若重复应更新
            response.addCookie(new Cookie("token",token));
            //request.getSession().setAttribute("user",githubUser);//Session存在于request当中
            return "redirect:/";
        }
        else{
            //登录失败，重新登陆
            return "redirect:/";
        }

    }
    @GetMapping("/logout")//退出登录，删除session属性，删除cookie
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);//新建同名cookie并设置寿命为0就是删除cookie
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
