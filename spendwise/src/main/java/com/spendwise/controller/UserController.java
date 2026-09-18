package com.spendwise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spendwise.dto.UpdateCurPreferenceRequest;
import com.spendwise.dto.UserCurPreferenceResponse;
import com.spendwise.service.CurrencyService;
import com.spendwise.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    final CurrencyService currService;
    final UserService userService;
    public UserController(CurrencyService currService,UserService userService){
        this.currService=currService;
        this.userService=userService;
    }
    @PatchMapping("/me/preferences")
    public ResponseEntity<UserCurPreferenceResponse> setCurrencyPreference(@RequestBody UpdateCurPreferenceRequest request) {
        UserCurPreferenceResponse response = userService.setCurrForUser(request);
        return ResponseEntity.ok(response);
    }
}
