package top.seiei.service.impl;
import java.util.Date;

import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.seiei.enums.Sex;
import top.seiei.pojo.Users;
import top.seiei.pojo.bo.UserBO;
import top.seiei.service.UsersService;
import top.seiei.mapper.UsersMapper;
import top.seiei.utils.DateUtil;
import top.seiei.utils.MD5Utils;

import javax.annotation.Resource;

@Service
public class UsersServiceImpl implements UsersService {

    @Resource
    private UsersMapper usersMappper;

    @Resource
    private Sid sid;

    // 默认头像路径
    private static final String USER_FACE = "";

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Boolean queryUserNameIsExist(String userName) {
        // 创建 Example 对象，传入要查询的映射对象 Class
        Example userExample = new Example(Users.class);
        // 创建 Criteria
        Example.Criteria userCriteria = userExample.createCriteria();
        // 键名需要和映射对象的键名一致，而不是与数据库的字段名一致
        userCriteria.andEqualTo("username", userName);
        Users result = usersMappper.selectOneByExample(userExample);
        return result != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) throws Exception {
        Users user = new Users();
        String userId = sid.nextShort();
        user.setId(userId);
        user.setUsername(userBO.getUserName());
        user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        user.setNickname(userBO.getUserName());
        user.setFace(USER_FACE);
        user.setSex(Sex.secret.type);
        user.setBirthday(DateUtil.stringToDate("1900-01-01", DateUtil.ISO_EXPANDED_DATE_FORMAT));
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        usersMappper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String userName, String password) throws Exception {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", userName);
        criteria.andEqualTo("password", MD5Utils.getMD5Str(password));
        Users result = usersMappper.selectOneByExample(example);
        return result;
    }
}
