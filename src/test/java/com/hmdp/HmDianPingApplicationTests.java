package com.hmdp;

import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.RedisData;
import com.hmdp.utils.RedisIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class HmDianPingApplicationTests {

    @Resource
    private ShopServiceImpl shopService;

    private ExecutorService es = Executors.newFixedThreadPool(500);

    @Resource
    private RedisIdWorker redisIdWorker;

    @Test
    void testIdWorker() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);

        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId("order");
                System.out.println("id =" + id);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i=0; i< 300; i++){
            es.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("time = "+(end-begin));

    }

    @Test
    void testSaveShop() throws InterruptedException {
        for (long i = 0; i < 14; i++) {
            shopService.saveShop2Redis(i,10L);
        }

    }

}
