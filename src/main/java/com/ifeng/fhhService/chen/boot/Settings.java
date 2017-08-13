package com.ifeng.fhhService.chen.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

/**
 * Created by Chen Weijie on 2017/8/13.
 */
@Configuration
@ImportResource("classpath:context/applicationContext.xml")
public class Settings {

    @Autowired
    Environment environment;


}
