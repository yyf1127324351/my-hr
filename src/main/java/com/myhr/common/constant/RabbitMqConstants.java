package com.myhr.common.constant;

/**
 * @Description
 * @Author yyf
 */
public class RabbitMqConstants {


    /**
     * @Description 处理角色的交换机
     * */
    public static final String AUTH_ROLE_EXCHANGE = "auth_role_exchange";


   /**
    * @Description 该队列用于处理 各系统角色信息
    * */
   public static final String AUTH_ROLE_QUEUE_NEW = "auth_role_queue_new";


   public static final String AUTH_ROLE_QUEUE1 = "auth_role_queue_test";

   public static final String AUTH_ROLE_QUEUE2 = "auth_role_queue_test2";


    public static final String REFRESH_ROLE_INFO = "refresh_role_info";
}
