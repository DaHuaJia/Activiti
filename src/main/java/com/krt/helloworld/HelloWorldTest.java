package com.krt.helloworld;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @author 郭明德
 * @description 第一个流程实例
 * @date 2019/8/6 10:16
 */
public class HelloWorldTest {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinition(){
        Deployment deployment = processEngine.getRepositoryService() // 与流程定义和部署对象相关的service
                .createDeployment() // 创建一个部署对象
                .name("helloworld入门程序") // 添加部署名称
                .addClasspathResource("diagrams/helloworld.bpmn") // 从classpath资源中加载，一次只能加载一个文件
                .addClasspathResource("diagrams/helloworld.png") // 从classpath资源中加载，一次只能加载一个文件
                .deploy();

        System.out.println("部署ID: "+deployment.getId());
        System.out.println("部署名称: "+deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        // 流程定义的key
        String processKey = "helloworld";

        // 使用key 值启动，默认是按照最新版本的流程定义启动
        ProcessInstance instance = processEngine.getRuntimeService() // 与正在执行的流程实例和执行对象相关的service
                .startProcessInstanceByKey(processKey); // 使用流程定义的key启动流程实例，key对应 helloworld.bpmn文件中id的属性值

        System.out.println("流程实例ID: "+instance.getId());
        System.out.println("流程定义ID: "+instance.getProcessDefinitionId());
    }

    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask(){
        // 办理人姓名
        String assignee = "张三";
        List<Task> lists = processEngine.getTaskService() // 与正在执行的任务管理相关的service
                .createTaskQuery() // 创建任务查询对象
                .taskAssignee(assignee) // 指定个人任务查询，指定班里人
                .list();

        if(lists != null && lists.size() > 0){
            for(Task task : lists){
                System.out.println("任务ID: "+task.getId());
                System.out.println("任务名称: "+task.getName());
                System.out.println("任务的创建时间: "+task.getCreateTime());
                System.out.println("任务的办理人: "+task.getAssignee());
                System.out.println("流程定义ID: "+task.getProcessDefinitionId());
                System.out.println("流程实例ID: "+task.getProcessInstanceId());
                System.out.println("执行对象ID: "+task.getExecutionId());
                System.out.println("===============");
            }
        }

    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask(){
        // 任务Id
        String taskId = "5004";

        try{
            processEngine.getTaskService() // 与正在执行的任务管理相关的service
                    .complete(taskId); // 完成任务，如果找不到该任务，则抛出 ActivitiObjectNotFoundException 异常

            System.out.println("完成任务。id="+taskId);

        } catch (ActivitiObjectNotFoundException e){
            e.printStackTrace();
            System.out.println("任务失败。id="+taskId);
        }

    }

}
