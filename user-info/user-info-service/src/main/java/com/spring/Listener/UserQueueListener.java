package com.spring.Listener;


import com.spring.config.MailConfig;
import com.spring.constants.constant;
import com.spring.dataobject.UserInfo;
import com.spring.utils.JsonUtil;
import com.spring.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import java.io.IOException;

@Slf4j
@Component
public class UserQueueListener {

    @Autowired
    private MailUtil mailUtil;

    @RabbitListener(queues = constant.USER_QUEUE_NAME)
    public void handler(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try{
            String body = new String(message.getBody());
            // 发邮件
            UserInfo userInfo = JsonUtil.parseJson(body, UserInfo.class);
            try{
                mailUtil.sendMail(userInfo.getEmail(), "测试邮件", "这是一封测试邮件（注册成功）");
            }catch(Exception e){
                log.error("发送邮件失败：{}", e.getMessage());
            }
            // 收到并回复
            log.info("接收到消息：{}", body);
            channel.basicAck(deliveryTag, true);
        }catch(Exception e){
            channel.basicNack(deliveryTag, true, true);
        }

    }
}
