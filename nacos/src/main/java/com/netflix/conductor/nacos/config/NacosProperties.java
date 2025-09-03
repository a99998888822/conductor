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

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("conductor.nacos")
public class NacosProperties {

    /** Format is host:port */
    private String serverAddress = null;

    /** nacos namespace */
    private String namespace = null;

    /** nacos group */
    private String group = null;

    /** nacos username */
    private String username = "";

    /** nacos password */
    private String password = "";

    /** nacos connection timeout */
    private Duration connectionTimeout = Duration.ofSeconds(3);

    /** nacos read timeout */
    private Duration readTimeout = Duration.ofSeconds(5);

    /** nacos heartbeat interval */
    private Duration heartbeatInterval = Duration.ofSeconds(5);

    /** is service registry enabled */
    private boolean serviceRegistryEnabled = true;

    /** is config center enabled */
    private boolean configCenterEnabled = true;

    //    /** 是否启用服务注册 */
    //    private boolean serviceRegistryEnabled = true;
    //
    //    /** 是否启用配置中心 */
    //    private boolean configCenterEnabled = true;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Duration getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Duration heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public boolean isServiceRegistryEnabled() {
        return serviceRegistryEnabled;
    }

    public void setServiceRegistryEnabled(boolean serviceRegistryEnabled) {
        this.serviceRegistryEnabled = serviceRegistryEnabled;
    }

    public boolean isConfigCenterEnabled() {
        return configCenterEnabled;
    }

    public void setConfigCenterEnabled(boolean configCenterEnabled) {
        this.configCenterEnabled = configCenterEnabled;
    }
}
