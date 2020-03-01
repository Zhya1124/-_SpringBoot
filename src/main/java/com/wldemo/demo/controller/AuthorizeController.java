package com.wldemo.demo.controller;

import com.wldemo.demo.dto.AccessTokenDTO;
import com.wldemo.demo.dto.GithubUser;
import com.wldemo.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${client_id}")
    private String client_id;
    @Value("${client_secret}")
    private String client_secret;
    @Value("${redirect_uri}")
    private String redirect_uri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                            @RequestParam(name="state")String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        String token = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(token);
        System.out.println(user.getName());
        return "index";
    }
}
