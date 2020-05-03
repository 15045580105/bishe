package com.yongxv.bishe.zhanshi;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import sun.misc.Signal;

@MapperScan("com.yongxv.bishe.zhanshi.mapper")
@SpringBootApplication(scanBasePackages = "com.yongxv.bishe.zhanshi")
@EnableScheduling
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.setBannerMode(Banner.Mode.CONSOLE);
        /*此listener用于记录应用的进程id，并输出到指定文件*/
        springApplication.addListeners(new ApplicationPidFileWriter("yongxv-bishe.pid"));
        ConfigurableApplicationContext applicationContext = springApplication.run(args);
        LOGGER.info("yongxv service start success！");
        Signal.handle(new Signal("TERM"), signal -> {
            //优雅停止服务操作
            applicationContext.close();
            System.exit(0);
        });
    }
}
