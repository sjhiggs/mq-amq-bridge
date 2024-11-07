package com.redhat.support;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;
import org.apache.camel.component.jms.JmsComponent;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class MqConfig  {

    @Value("${MQ_HOST}")
    private String mqHost;

    @Value("${MQ_PORT}")
    private int mqPort;

    @Value("${MQ_QUEUE_MANAGER}")
    private String mqQueueManager;

    @Value("${MQ_CHANNEL}")
    private String mqChannel;

    @Value("${MQ_USER}")
    private String user;

    @Value("${MQ_PASSWORD}")
    private String password;


    private MQConnectionFactory createMQConnectionFactory() {

        Logger.getLogger(getClass().getName()).warning("queue manager:  " + mqQueueManager);
        MQConnectionFactory mqQueueConnectionFactory = new MQConnectionFactory();
        try {
            mqQueueConnectionFactory.setHostName(mqHost);
            mqQueueConnectionFactory.setChannel(mqChannel);
            mqQueueConnectionFactory.setPort(mqPort);
            mqQueueConnectionFactory.setQueueManager(mqQueueManager);
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            mqQueueConnectionFactory.setStringProperty(WMQConstants.USERID, user);
            mqQueueConnectionFactory.setStringProperty(WMQConstants.PASSWORD, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mqQueueConnectionFactory;
    }

    public JmsPoolConnectionFactory jmsPoolConnectionFactory() {
        JmsPoolConnectionFactory jmsPoolConnectionFactory = new JmsPoolConnectionFactory();
        jmsPoolConnectionFactory.setConnectionFactory(createMQConnectionFactory());
        jmsPoolConnectionFactory.setMaxConnections(1);
        return jmsPoolConnectionFactory;
    }

    @Bean(name="mq")
    public JmsComponent ibmMqComponent() {
        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(jmsPoolConnectionFactory());
        jms.setCacheLevelName("CACHE_CONSUMER");
        jms.setTransacted(true);
        //jms.setAcknowledgementModeName("AUTO_ACK");
        return jms;
    }

}
