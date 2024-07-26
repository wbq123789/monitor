package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.ChangePasswordVO;
import com.example.entity.vo.request.CreateSubAccountVO;
import com.example.entity.vo.request.ModifyEmailVO;
import com.example.entity.vo.response.SubAccountVO;
import com.example.service.AccountService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: monitor
 * @description: 用户相关接口
 * @author: 王贝强
 * @create: 2024-07-24 17:05
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    AccountService service;

    @PostMapping("/change-password")
    public RestBean<Void> changePassword(@RequestBody @Valid ChangePasswordVO vo,
                                         @RequestAttribute(Const.ATTR_USER_ID) int clientId){
        return service.changePassword(clientId,vo.getPassword(), vo.getNew_password()) ?
                RestBean.success() : RestBean.failure(401,"原始密码输入错误");
    }
    @PostMapping("/modify-email")
    public RestBean<Void> modifyEmail(@RequestAttribute(Const.ATTR_USER_ID) int userId,
                                      @RequestBody @Valid ModifyEmailVO vo){
        String result = service.modifyEmail(userId, vo);
        return result==null ? RestBean.success() :RestBean.failure(401,result);
    }
    @PostMapping("/sub/create")
    public RestBean<Void> createSubAccount(@RequestBody @Valid CreateSubAccountVO vo) {
        service.createSubAccount(vo);
        return RestBean.success();
    }

    @GetMapping("/sub/delete")
    public RestBean<Void> deleteSubAccount(int uid,
                                           @RequestAttribute(Const.ATTR_USER_ID) int userId) {
        if(uid == userId)
            return RestBean.failure(401, "非法参数");
        service.deleteSubAccount(uid);
        return RestBean.success();
    }

    @GetMapping("/sub/list")
    public RestBean<List<SubAccountVO>> subAccountList() {
        return RestBean.success(service.listSubAccount());
    }
}
