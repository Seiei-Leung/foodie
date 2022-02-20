package top.seiei.service;

import top.seiei.pojo.Users;
import top.seiei.pojo.bo.UserBO;

public interface UsersService {

    /**
     * 检测用户名是否存在
     * @param userName 用户名
     * @return
     */
    public Boolean queryUserNameIsExist(String userName);

    /**
     * 注册用户
     * @param userBO 前端传入的 BO 对象
     * @return
     */
    public Users createUser(UserBO userBO) throws Exception;

    /**
     * 检索用户名和密码是否匹配，用于登录
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public Users queryUserForLogin(String userName, String password) throws Exception;
}
