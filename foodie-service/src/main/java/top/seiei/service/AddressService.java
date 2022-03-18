package top.seiei.service;

import top.seiei.pojo.UserAddress;
import top.seiei.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    /**
     * 根据用户id 获取地址列表
     * @param userId 用户 id
     * @return
     */
    public List<UserAddress> getAddressByUserId(String userId);

    /**
     * 新增地址
     * @param addressBO 地址BO
     * @return
     */
    public UserAddress addUserAddress(AddressBO addressBO);

    /**
     * 修改地址
     * @param addressBO 地址BO
     * @return
     */
    public UserAddress updateUserAddress(AddressBO addressBO);

    /**
     * 修改默认地址
     * @param userId 用户 id
     * @param addressId 地址 id
     */
    public void setDefalutAddress(String userId, String addressId);

    /**
     * 删除地址，如果返回 TURE，表示该地址为默认地址
     * @param addressId 地址 ID
     * @return
     */
    public boolean deleteAddress(String addressId);

    /**
     * 删除地址，如果删除的是默认地址，顺延下一个地址为默认地址
     * @param addressId 地址 ID
     * @return
     */
    public void deleteAddress(String userId, String addressId);
}
