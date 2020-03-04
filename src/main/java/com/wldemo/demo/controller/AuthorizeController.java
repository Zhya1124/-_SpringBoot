package com.wldemo.demo.controller;

import com.wldemo.demo.dto.AccessTokenDTO;
import com.wldemo.demo.dto.GithubUser;
import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.User;
import com.wldemo.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;
    @Value("${client_id}")
    private String client_id;
    @Value("${client_secret}")
    private String client_secret;
    @Value("${redirect_uri}")
    private String redirect_uri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        String token = githubProvider.getAccessToken(accessTokenDTO);//用get到的code获取accessToken，并回到redirect_uri
        GithubUser githubUser = githubProvider.getUser(token);//用accessToken post方式获取用户信息
        System.out.println(githubUser.getName());
        if(githubUser != null){
            //登陆成功，保存用户信息，cookie和session
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            request.getSession().setAttribute("user",githubUser);//Session存在于request当中
            return "redirect:/";
        }
        else{
            //登录失败，重新登陆
            return "redirect:/";
        }

    }
}
