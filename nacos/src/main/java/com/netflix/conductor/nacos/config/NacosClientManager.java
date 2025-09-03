/*
 * Copyright 2025 Conductor Authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.nacos.config;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class NacosClientManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(NacosClientManager.class);

    private final NacosProperties properties;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    // config center client
    private ConfigService configService;
    // service registry & discovery client
    private NamingService namingService;

    public NacosClientManager(NacosProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        try {
            initializeClients();
            initialized.set(true);
            LOGGER.info("Nacos clients initialized success");
        } catch (Exception e) {
            LOGGER.error("Nacos clients initialization failed", e);
            throw new RuntimeException("Failed to initialize Nacos clients", e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (configService != null) {
                configService.shutDown();
            }
            if (namingService != null) {
                namingService.shutDown();
            }
            LOGGER.info("Nacos客户端已关闭");
        } catch (Exception e) {
            LOGGER.error("关闭Nacos客户端时发生错误", e);
        }
    }

    private void initializeClients() throws NacosException {
        Properties nacosProperties = new Properties();
        if (properties.getServerAddress() == null) {
            throw new IllegalArgumentException("serverAddress is required");
        }
        nacosProperties.setProperty("serverAddr", properties.getServerAddress());
        nacosProperties.setProperty("namingLoadCacheAtStart", "true");

        if (properties.getNamespace() != null && !properties.getNamespace().isEmpty()) {
            nacosProperties.setProperty("namespace", properties.getNamespace());
        }

        if (properties.getUsername() != null && !properties.getUsername().isEmpty()) {
            nacosProperties.setProperty("username", properties.getUsername());
        }

        if (properties.getPassword() != null && !properties.getPassword().isEmpty()) {
            nacosProperties.setProperty("password", properties.getPassword());
        }

        //        // 初始化配置服务
        //        if (properties.isConfigCenterEnabled()) {
        //            configService = NacosFactory.createConfigService(nacosProperties);
        //            LOGGER.info("Nacos config service initialized");
        //        }

        // 初始化命名服务
        if (properties.isServiceRegistryEnabled()) {
            namingService = NacosFactory.createNamingService(nacosProperties);
            LOGGER.info("Nacos naming service initialized");
        }
    }

    public ConfigService getConfigService() {
        if (!initialized.get() || configService == null) {
            throw new IllegalStateException("Nacos config service is not initialized");
        }
        return configService;
    }

    public NamingService getNamingService() {
        if (!initialized.get() || namingService == null) {
            throw new IllegalStateException("Nacos naming service is not initialized");
        }
        return namingService;
    }

    /**
     * judge whether the client is initialized
     *
     * @return
     */
    public boolean isInitialized() {
        return initialized.get();
    }

    /**
     * get the nacos config
     *
     * @return
     */
    public NacosProperties getProperties() {
        return properties;
    }
}
