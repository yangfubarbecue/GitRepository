package com.abc.p2p;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @Author yang
 * @Date 2020/10/26 19:23
 * @Description : 定时生成收益计划
 */

@Component //交给spring容器管理
@Slf4j
public class TimerIncomeRecord {
    private int count = 0;
    @Scheduled(cron = "0/5 * * * * ?") //调度器注解，每5秒执行一次
    public void test() {
        log.info("--test() start--");
        System.out.println("这是一个线程测试游戏");
        System.out.println(++count);
        Integer a = 127;
        Integer b = 127;
        Integer c = 128;
        Integer d = 128;
        System.out.println(a == b);
        System.out.println(c == d);
        log.info("--test() end--");
    }
}
