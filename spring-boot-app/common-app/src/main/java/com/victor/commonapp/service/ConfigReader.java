package com.victor.commonapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 获取配置文件中的配置项
 */
@Component
public class ConfigReader {
    @Value("${file.store.dir}")   //冒号后面指定默认值
    private String dataDir;

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }
}
