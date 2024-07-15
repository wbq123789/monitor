package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @program: monitor
 * @description: 客户端数据实体类
 * @author: 王贝强
 * @create: 2024-07-13 16:37
 */
@Data
@AllArgsConstructor
@TableName("client")
public class Client {
    @TableId
    Integer id;
    String name;
    String token;
    String location;
    String node;
    Date registerTime;
}
