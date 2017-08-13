package com.ifeng.fhhService.chen.boot;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * spring  启动入口
 * Created by chenwj3 on 2017/8/13.
 */
@EnableAutoConfiguration
@ComponentScan("com.ifeng.fhhService.chen.boot")
public class Application {


    public static void main(String[] args) {

        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(Application.class)
                .listeners(new BootListener())
                .run(args);

    }



}
