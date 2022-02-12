package top.seiei.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.seiei.service.TestTransService;

import javax.annotation.Resource;

/**
 * 用于学习事务
 * 这里还需要注意事务的失效场景，在同一个类中，方法之间的调用会导致事务的失效
 * 比如方法A 中调用了方法B，方法B 声明了事务，而方法A 却没有声明，此时方法 B 的事务就会失效
 * 这是由于使用Spring AOP代理造成的，因为只有当事务方法被当前类以外的代码调用时，才会由Spring生成的代理对象来管理。
 * 同时事务并不会捕捉异常
 * 如果异常被捕获处理了，事务不会回滚
 */
@Service
public class TestTransServiceImpl implements TestTransService {

    @Resource
    private TestObjServiceImpl testObjService;

    /**
     * REQUIRED：事务会由父方法传递到子方法中，而子方法不会传递到父方法（如果当前存在事务，则加入到该事务中，作为一个整体）
     * SUPPORTS: 如果当前有事务则使用事务，如果当前没有事务则不使用事务
     * MANDATORY: 当前父方法必须有事务，否则抛出异常，此时父方法内的sql逻辑会执行且不回滚
     * REQUIRES_NEW: 创建一个独立的新事务，如果当前存在事务，则与当前事务互相独立，当前事务回滚并不影响新创建的事务回滚
     * NOT_SUPPORTED：当前方法不使用事务，如果当前有事务，就会挂起，自己不使用事务进行操作，不回滚
     * NEVER：不支持事务，如果当前有事务就会抛出异常，此时由于父方法声明了事务，所以此时方法会回滚
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void testForTransactional() {
        testObjService.saveParent();
        try {
            testObjService.saveTwoChildren();
        } catch (Exception error) {

        }
    }


}
