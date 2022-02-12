package top.seiei.service.impl;

import org.springframework.stereotype.Service;
import top.seiei.pojo.Users;
import top.seiei.service.UsersService;
import top.seiei.mapper.UsersMapper;

import javax.annotation.Resource;

@Service
public class UsersServiceImpl implements UsersService {

    @Resource
    private UsersMapper usersMappper;

    @Override
    public Boolean queryUserNameIsExist(String userName) {
        return null;
    }
}
