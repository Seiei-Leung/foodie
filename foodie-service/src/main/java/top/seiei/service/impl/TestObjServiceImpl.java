package top.seiei.service.impl;

import org.springframework.stereotype.Service;
import top.seiei.mapper.TestMapper;
import top.seiei.pojo.Test;
import top.seiei.service.TestObjService;

import javax.annotation.Resource;

@Service
public class TestObjServiceImpl implements TestObjService {

    @Resource
    private TestMapper testMapper;

    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveParent() {
        Test test = new Test();
        test.setType("parent");
        testMapper.insert(test);
    }

    public void saveChildren(String number)
    {
        Test test = new Test();
        test.setType("children" + number);
        testMapper.insert(test);
    }

    //@Transactional(propagation = Propagation.REQUIRED)
    //@Transactional(propagation = Propagation.SUPPORTS)
    //@Transactional(propagation = Propagation.MANDATORY)
    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    //@Transactional(propagation = Propagation.NOT_SUPPORTED)
    //@Transactional(propagation = Propagation.NEVER)
    public void saveTwoChildren() {
        saveChildren("1");
        int i= 1/0;
        saveChildren("2");
    }
}
