package top.seiei.service;

import top.seiei.pojo.Users;

public interface UsersService {

    /**
     *
     * @param userName
     * @return
     */
    public Boolean queryUserNameIsExist(String userName);
}
