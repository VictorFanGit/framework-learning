package com.victor.commonapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 获取配置文件中的配置项
 */
@Component
public class ConfigReader {
    @Value("${file.store.dir: /root/data}")   //冒号后面指定默认值
    private int dataDir;

    public int getDataDir() {
        return dataDir;
    }

    public void setDataDir(int dataDir) {
        this.dataDir = dataDir;
    }
}
