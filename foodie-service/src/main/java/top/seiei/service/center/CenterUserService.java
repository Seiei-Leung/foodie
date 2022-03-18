package top.seiei.service.center;

import top.seiei.pojo.Users;
import top.seiei.pojo.bo.center.CenterUserBO;

public interface CenterUserService {

    /**
     * 根据用户Id 获取用户消息
     * @param userId 用户Id
     * @return
     */
    public Users getByUserid(String userId);

    /**
     * 修改用户消息
     * @param userId 用户Id
     * @param centerUserBO 用户消息BO
     */
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);
}
