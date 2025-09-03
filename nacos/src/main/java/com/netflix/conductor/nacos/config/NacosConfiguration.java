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

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.conductor.nacos.manager.NacosConfigManager;
import com.netflix.conductor.nacos.manager.NacosServiceManager;

@Configuration
@EnableConfigurationProperties(NacosProperties.class)
@ConditionalOnProperty(
        name = "conductor.nacos.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class NacosConfiguration {

    @Bean
    public NacosClientManager nacosClientManager(NacosProperties properties) {
        return new NacosClientManager(properties);
    }

    @Bean
    public NacosServiceManager nacosServiceManager(NacosClientManager nacosClientManager) {
        return new NacosServiceManager(nacosClientManager);
    }

    @Bean
    public NacosConfigManager nacosConfigCenter(NacosClientManager nacosClientManager) {
        return new NacosConfigManager(nacosClientManager);
    }
}
