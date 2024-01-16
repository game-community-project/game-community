package com.gamecommunity.domain.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

  @Value("${kakao.rest_api_key}")
  private String kakaoRestApiKey;

  @Value("${kakao.redirect_uri}")
  private String kakaoRedirectUri;

  @GetMapping("/login")
  public String loginPage(Model model) {
    model.addAttribute("kakaoRestApiKey", kakaoRestApiKey);
    model.addAttribute("kakaoRedirectUri", kakaoRedirectUri);
    return "login";
  }

  @GetMapping("/signup")
  public String signupPage() {
    return "signup";
  }

}