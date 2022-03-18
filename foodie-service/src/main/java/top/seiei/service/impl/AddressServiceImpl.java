package top.seiei.service.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.seiei.enums.YesOrNo;
import top.seiei.mapper.UserAddressMapper;
import top.seiei.pojo.UserAddress;
import top.seiei.pojo.bo.AddressBO;
import top.seiei.service.AddressService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private UserAddressMapper userAddressMapper;

    @Resource
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> getAddressByUserId(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public UserAddress addUserAddress(AddressBO addressBO) {
        UserAddress userAddress = new UserAddress();
        /**
         * 复制属性插入
         */
        BeanUtils.copyProperties(addressBO, userAddress);

        // 赋予其他属性值
        String addressId = sid.nextShort(); // 获取主键
        userAddress.setId(addressId);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());

        // 查询该用户有没有记录过地址信息，如果没有，则默认当前的地址信息为默认地址
        List<UserAddress> userAddressList = this.getAddressByUserId(addressBO.getUserId());
        userAddress.setIsDefault(userAddressList.size() == 0 ? 1 : 0);
        userAddressMapper.insert(userAddress);

        return userAddress;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public UserAddress updateUserAddress(AddressBO addressBO) {
        String addressId = addressBO.getAddressId();
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
        return userAddress;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void setDefalutAddress(String userId, String addressId) {
        // 目前默认地址的默认属性取消
        UserAddress oldDefaultUserAddress = new UserAddress();
        oldDefaultUserAddress.setIsDefault(YesOrNo.Yes.type);
        oldDefaultUserAddress.setUserId(userId);
        oldDefaultUserAddress = userAddressMapper.selectOne(oldDefaultUserAddress);
        if (oldDefaultUserAddress != null) {
            oldDefaultUserAddress.setIsDefault(YesOrNo.No.type);
            oldDefaultUserAddress.setUpdatedTime(new Date());
            userAddressMapper.updateByPrimaryKeySelective(oldDefaultUserAddress);
        }
        // 更新默认地址
        UserAddress nowDefaultUserAddress = new UserAddress();
        nowDefaultUserAddress.setId(addressId);
        nowDefaultUserAddress.setIsDefault(YesOrNo.Yes.type);
        userAddressMapper.updateByPrimaryKeySelective(nowDefaultUserAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteAddress(String addressId) {
        UserAddress userAddress = userAddressMapper.selectByPrimaryKey(addressId);
        Boolean isDefaultAddress = userAddress.getIsDefault() == YesOrNo.Yes.type;
        int result = userAddressMapper.deleteByPrimaryKey(addressId);
        return (isDefaultAddress && result > 0);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAddress(String userId, String addressId) {
        boolean result = this.deleteAddress(addressId);
        if (result) {
            List<UserAddress> userAddressList = this.getAddressByUserId(userId);
            if (userAddressList.size() > 0) {
                UserAddress userAddress = userAddressList.get(0);
                userAddress.setIsDefault(YesOrNo.Yes.type);
                userAddressMapper.updateByPrimaryKeySelective(userAddress);
            }
        }
    }
}
