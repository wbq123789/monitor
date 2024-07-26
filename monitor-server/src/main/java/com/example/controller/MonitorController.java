package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;
import com.example.entity.vo.response.ClientSimpleVO;
import com.example.entity.vo.response.RuntimeHistoryVO;
import com.example.service.AccountService;
import com.example.service.ClientService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: monitor
 * @description: 监控信息接口
 * @author: 王贝强
 * @create: 2024-07-18 15:28
 */

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Resource
    ClientService clientService;
    @Resource
    AccountService accountService;

    @GetMapping("/list")
    public RestBean<List<ClientPreviewVO>> listAllClient(@RequestAttribute(Const.ATTR_USER_ID) int userId,
                                                         @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        List<ClientPreviewVO> clients = clientService.listClients();
        if (this.isAdminAccount(userRole))
            return RestBean.success(clients);
        else {
            List<Integer> ids = this.accountAccessClients(userId);
            return RestBean.success(clients
                    .stream()
                    .filter(vo -> ids.contains(vo.getId()))
                    .toList());
        }
    }

    @GetMapping("/simple-list")
    public RestBean<List<ClientSimpleVO>> simpleClientList(@RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if (this.isAdminAccount(userRole))
            return RestBean.success(clientService.listSimpleClients());
        else
            return RestBean.noPermission();
    }
    @PostMapping("/rename")
    public RestBean<Void> renameClient(@RequestBody @Valid RenameClientVO client,
                                       @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                       @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if (this.permissionCheck(userId,userRole,client.getId())) {
            clientService.renameClient(client);
            return RestBean.success();
        }else
            return RestBean.noPermission();

    }
    @PostMapping("/node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO vo,
                                     @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                     @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if (this.permissionCheck(userId,userRole,vo.getId())) {
            clientService.renameNode(vo);
            return RestBean.success();
        }else
            return RestBean.noPermission();
    }
    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(@RequestParam int clientId,
                                             @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                             @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if (this.permissionCheck(userId,userRole,clientId)) {
            return RestBean.success(clientService.clientDetails(clientId));
        }else
            return RestBean.noPermission();
    }
    @GetMapping("/runtime_history")
    public RestBean<RuntimeHistoryVO> runtimeDetailsHistory(@RequestParam int clientId,
                                                            @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                                            @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if (this.permissionCheck(userId,userRole,clientId)) {
            return RestBean.success(clientService.clientRuntimeDetailsHistory(clientId));
        }else
            return RestBean.noPermission();
    }
    @GetMapping("/runtime_now")
    public RestBean<RuntimeDetailVO> runtimeDetailsNow(@RequestParam int clientId,
                                                       @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                                       @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if (this.permissionCheck(userId,userRole,clientId)) {
            return RestBean.success(clientService.clientRuntimeDetailsNow(clientId));
        }else
            return RestBean.noPermission();

    }
    @GetMapping("/register")
    public RestBean<String> registerToken(@RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if (this.isAdminAccount(userRole))
            return RestBean.success(clientService.getToken());
        else
            return RestBean.noPermission();
    }
    @GetMapping("/delete")
    public RestBean<Void> deleteClient(@RequestParam int clientId,
                                       @RequestAttribute(Const.ATTR_USER_ROLE) String userRole){
        if (this.isAdminAccount(userRole)) {
            clientService.deleteClient(clientId);
            return RestBean.success();
        }else
            return RestBean.noPermission();
    }
    private List<Integer> accountAccessClients(int uid){
        Account account=accountService.getById(uid);
        return account.getClientList();
    }
    private boolean isAdminAccount(String role){
        role=role.substring(5);
        return Const.ROLE_ADMIN.equals(role);
    }
    private boolean permissionCheck(int uid,String role,int clientId){
        if (this.isAdminAccount(role)) return true;
        else return this.accountAccessClients(uid).contains(clientId);
    }
}
