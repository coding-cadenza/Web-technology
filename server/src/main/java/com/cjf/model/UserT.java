package com.cjf.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component("UserT") // 注册为POJO对象
@Data
public class UserT {
    private int id;
    private String username;
    private  String password;
    private String email;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    private float balance;
    private int articleNum;

    @Override
    public String toString(){
        return "UserT{"+"id="+id+",name='"+username+'\''+",password='"+password+'\''+",email='"+email+'\''+",birthday='"+birthday+'\''+",balance='"+balance+'\''+'}';
    }
}
