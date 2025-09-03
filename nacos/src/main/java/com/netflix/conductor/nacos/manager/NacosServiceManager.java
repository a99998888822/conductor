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
package com.netflix.conductor.nacos.manager;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.nacos.config.NacosClientManager;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

public class NacosServiceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(NacosServiceManager.class);

    private final NacosClientManager clientManager;
    private final Map<String, Instance> registeredServices = new HashMap<>();

    public NacosServiceManager(NacosClientManager clientManager) {
        this.clientManager = clientManager;
    }

    // register service (to be called by the service)
    public void registerService(String serviceName, String group, String ip, int port) {}

    // deregister service (to be called by the service)
    public void deregisterService(String serviceName, String group) {}

    /**
     * Get all instances of a service
     *
     * @param serviceName the service name
     * @return the list of instances
     */
    public List<Instance> getServiceInstances(String serviceName) {
        try {
            NamingService namingService = clientManager.getNamingService();
            return namingService.getAllInstances(serviceName);
        } catch (Exception e) {
            LOGGER.error("getting service instances failed: {}", serviceName, e);
            return Collections.emptyList();
        }
    }

    /**
     * Get all instances of a service
     *
     * @param serviceName the service name
     * @param group the service group
     * @return the list of instances
     */
    public List<Instance> getServiceInstances(String serviceName, String group) {
        try {
            NamingService namingService = clientManager.getNamingService();
            return namingService.getAllInstances(serviceName, group);
        } catch (Exception e) {
            LOGGER.error("getting service instances failed: {}", serviceName, e);
            return Collections.emptyList();
        }
    }

    /**
     * Check if the service is healthy
     *
     * @param serviceName the service name
     * @param group the service group
     * @return true if the service is healthy, false otherwise
     */
    public boolean isServiceHealthy(String serviceName, String group) {
        try {
            NamingService namingService = clientManager.getNamingService();
            List<Instance> instances = namingService.getAllInstances(serviceName, group);
            return instances.stream().anyMatch(Instance::isHealthy);
        } catch (Exception e) {
            LOGGER.error("checking service health failed: {}", serviceName, e);
            return false;
        }
    }

    /** select one healthy instance by service name and group */
    public Optional<Instance> selectOneHealthyInstance(String serviceName, String group) {
        try {
            NamingService namingService = clientManager.getNamingService();
            Instance instance = namingService.selectOneHealthyInstance(serviceName, group);
            return Optional.ofNullable(instance);
        } catch (Exception e) {
            LOGGER.error("select one healthyInstance failed: {}:{}", serviceName, group, e);
            return Optional.empty();
        }
    }

    /** get healthy instances by service name and group */
    public List<Instance> selectHealthyInstances(String serviceName, String group) {
        try {
            NamingService namingService = clientManager.getNamingService();
            return namingService.selectInstances(serviceName, group, true);
        } catch (Exception e) {
            LOGGER.error("get healthy instances failed: {}:{}", serviceName, group, e);
            return Collections.emptyList();
        }
    }

    /** get all instances by service name and group */
    public List<Instance> getAllInstances(String serviceName, String group) {
        try {
            NamingService namingService = clientManager.getNamingService();
            return namingService.getAllInstances(serviceName, group);
        } catch (Exception e) {
            LOGGER.error("get all instances failed: {}:{}", serviceName, group, e);
            return Collections.emptyList();
        }
    }

    /** subscribe service */
    public void subscribe(String serviceName, String group, ServiceChangeListener listener) {
        try {
            NamingService namingService = clientManager.getNamingService();
            namingService.subscribe(
                    serviceName,
                    group,
                    event -> {
                        try {
                            listener.onServiceChange(serviceName, group, event);
                            LOGGER.debug("service change notify: {}:{}", serviceName, group);
                        } catch (Exception e) {
                            LOGGER.error(
                                    "handle service change notify failed: {}:{}",
                                    serviceName,
                                    group,
                                    e);
                        }
                    });
            LOGGER.info("service subscribe success: {}:{}", serviceName, group);
        } catch (Exception e) {
            LOGGER.error("service subscribe failed: {}:{}", serviceName, group, e);
        }
    }

    /** unsubscribe service */
    public void unsubscribe(String serviceName, String group) {
        try {
            NamingService namingService = clientManager.getNamingService();
            namingService.unsubscribe(serviceName, group, null);
            LOGGER.info("unsubscribe service listener success: {}:{}", serviceName, group);
        } catch (Exception e) {
            LOGGER.error("unsubscribe service listener failed: {}:{}", serviceName, group, e);
        }
    }

    /** service change listener */
    public interface ServiceChangeListener {
        void onServiceChange(String serviceName, String group, Object event);
    }
}
