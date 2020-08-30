package com.monkey.service;

import com.monkey.po.User;
import org.springframework.stereotype.Service;


public interface UserService {

    User checkUser(String Username,String password);
}
