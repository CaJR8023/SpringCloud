package com.cajr.springcloud.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @Author CAJR
 * @create 2019/9/3 15:53
 */
@Configuration
public class ElasticSearchConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Value("${elasticsearch.ip}")
    private String hostName;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    @Value("${elasticsearch.pool}")
    private int poolSize;

    @Bean
    public TransportClient transportClient(){
        LOGGER.info("ElasticSearch初始化开始.....");
        TransportClient transportClient = null;
        try {
        Settings settings = Settings.builder()
                .put("cluster.name",clusterName)
                //增加嗅探机制，找到ES集群
                .put("client.transport.sniff",true)
                .put("thread_pool.search.size",poolSize)
                .build();
        transportClient = new PreBuiltTransportClient(settings);
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(hostName), port);
        transportClient.addTransportAddress(transportAddress);

        } catch (Exception e) {
            LOGGER.error("elasticsearch TransportClient create error!!", e);
        }

        return transportClient;
    }
}
