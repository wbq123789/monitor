package com.example.entity.dto;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 数据库中的用户信息
 */
@Data
@TableName("account")
@AllArgsConstructor
public class Account implements BaseData {
    @TableId(type = IdType.AUTO)
    Integer id;
    String username;
    String password;
    String email;
    String role;
    String clients;
    Date registerTime;

    public List<Integer> getClientList(){
        if (this.clients == null) return Collections.emptyList();
        return JSONArray.parse(this.clients).toList(Integer.class);
    }
}
