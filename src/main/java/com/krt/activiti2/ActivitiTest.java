package com.krt.activiti2;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @author 郭明德
 * @description 两种方式创建工作流的23张表
 * @date 2019/8/5 10:14
 */
public class ActivitiTest {

    /**
     * 使用配置代码创建工作流需要的23张表
     */
    @Test
    public void createTableByJava(){
        ProcessEngineConfiguration proEngConf = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();

        // 连接数据库配置
        proEngConf.setJdbcDriver("com.mysql.jdbc.Driver");
        proEngConf.setJdbcUrl("jdbc:mysql://localhost:3306/activiti0805?useUnicode=true&characterEncoding=utf8");
        proEngConf.setJdbcUsername("root");
        proEngConf.setJdbcPassword("root");

        /**
         * 不能自动创建表，需要表存在
         * public static final String DB_SCHEMA_UPDATE_FALSE = "false";
         * 先删除表 再创建表
         * public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
         * 如果表不存在，自动创建表
         * public static final String DB_SCHEMA_UPDATE_TRUE = "true";
         */
        proEngConf.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        // 创建工作流的核心对象 工作流引擎，ProcessEngine
        ProcessEngine processEngine = proEngConf.buildProcessEngine();
        System.out.println("processEngine="+processEngine);

    }

    /**
     * 使用xml文件创建数据表
     */
    @Test
    public void createTableByBean(){
        // 从一个资源文件中创建流程引擎
        ProcessEngineConfiguration proEngConf = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = proEngConf .buildProcessEngine();
        System.out.println(processEngine);

        /**
         * 链式编程
         * ProcessEngine processEngine = ProcessEngineConfiguration
         *                                  .createProcessEngineConfigurationFromResource("activiti.cfg.xml")
         *                                  .buildProcessEngine();
         */

    }

}
