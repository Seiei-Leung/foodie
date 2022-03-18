package top.seiei.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.seiei.service.OrderService;

import javax.annotation.Resource;

/**
 * 定时关闭未付款订单
 */
@Component
public class OrderJob {

    @Resource
    private OrderService orderService;

    /**
     * @Scheduled 是定时任务注解，同时需要在 Application 中添加 @EnableScheduling 注释开启任务
     * cron 表达式可以在 https://cron.qqe2.com/ 中自定义(注意前后空格符)
     *
     * 使用 @Scheduled 的弊端：
     * 1、有时间差，即每条订单不一定会刚刚达到一天的时长后就马上关闭，程序不严谨
     * 2、假如数据量大，全表查找全部未支付的订单对数据库造成性能负担
     * 3、不支持集群，假设部署了多台服务器，定时任务就会多台执行，导致浪费，解决方案只使用一台服务器单独使用定时任务
     *
     * 所以 @Scheduled 只适用一些小型轻量级项目，传统项目
     * 最好使用消息队列：MQ -> RabbitMQ，Kafka 等的延时任务
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    public void autoCloseOrder() {
        orderService.closeOverTimeOrders();
    }

}
