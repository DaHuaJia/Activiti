package com.krt.processdef;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @author 郭明德
 * @description
 * @date 2019/8/7 9:07
 */
public class ProcessDefinitionTest {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义（从类路径）
     */
    @Test
    public void deployProcessDef(){
        Deployment deployment = processEngine.getRepositoryService() // 获得与流程定义和部署相关的service
                                    .createDeployment() // 创建一个部署对象
                                    .name("流程定义CRUD") // 设置部署对象的名称
                                    .addClasspathResource("diagrams/helloworld.bpmn") // 传入流程定义的文档
                                    .addClasspathResource("diagrams/helloworld.png")
                                    .deploy(); // 部署流程定义

        System.out.println("部署ID: "+deployment.getId());
        System.out.println("部署名称: "+deployment.getName());
    }

    /**
     * 部署流程定义（从zip）
     */
    @Test
    public void deployProcessDef_zip(){
        // 从本地获得InputStream
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
        ZipInputStream zipInput = new ZipInputStream(in);

        Deployment deployment = processEngine.getRepositoryService() // 获得与流程定义和部署相关的service
                .createDeployment() // 创建一个部署对象
                .name("流程定义CRUD") // 设置部署对象的名称
                .addZipInputStream(zipInput)
                .deploy(); // 部署流程定义

        System.out.println("部署ID: "+deployment.getId());
        System.out.println("部署名称: "+deployment.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void findProcessDefinition(){
        List<ProcessDefinition> lists = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                /** 指定查询条件， where条件**/
                //.deploymentId() // 使用部署对象的ID 查询
                //.porcessDefinitionId() // 使用流程定义的ID 查询
                //.processDefinitionKey() // 使用流程定义的Key 查询
                //.processDefinitionNameLike() // 使用流程定义名称模糊查询

                /** 排序**/
                .orderByProcessDefinitionVersion().asc() // 按照版本的升序排列
                //.orderByProcessDefinitionName().desc() // 按照流程定义的名称降序排列

                /** 返回的结果集类型**/
                .list(); // 返回一个集合列表，封装流程定义
                //.singleResult(); // 返回唯一结果集
                //.count(); // 返回结果集数量
                //.listPage(); // 分页查询

        if(lists != null && lists.size() > 0){
            for(ProcessDefinition pd : lists){
                System.out.println("流程定义ID: "+pd.getId()); // 流程定义的key+版本+随机数
                System.out.println("流程定义名称: "+pd.getName()); // 对应helloworld.bpmn文件中的name属性
                System.out.println("流程定义Key: "+pd.getKey()); // 对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义版本: "+pd.getVersion()); // 当流程定义的Key值相同的情况下，版本升级，默认1
                System.out.println("png文件名称: "+pd.getDiagramResourceName());
                System.out.println("bpmn文件名称: "+pd.getResourceName());
                System.out.println("部署对象ID: "+pd.getDeploymentId());
                System.out.println("============================");
            }
        }
    }

    /**
     * 删除流程定义，根据部署id删除单个流程定义
     */
    @Test
    public void deleteProcessDefinition(){
        // 使用部署ID 完成删除
        String deployId = "1";
        /**
         * 不带级联的删除，只能删除没有启动的流程，如果流程启动则抛出异常
         */
        processEngine.getRepositoryService()
                .deleteDeployment(deployId);

        /**
         * 级联删除，不管流程是否启动，都能删除，默认值false
         */
        processEngine.getRepositoryService()
                .deleteDeployment(deployId, true);

        System.out.println("删除成功！");
    }

    /**
     * 查看流程图
     */
    @Test
    public void viewPic(){
        // 将生成的图片放到文件夹下
        String deployId = "1";
        String resName = "";
        // 获取图片资源名称
        List<String> lists = processEngine.getRepositoryService()
                                    .getDeploymentResourceNames(deployId);

        // 定义图片资源的名称
        if(lists != null && lists.size() > 0){
            for(String name : lists){
                if(name.indexOf(".png") >= 0){
                    resName = name;
                }
            }
        }

        // 获得图片的输入流
        InputStream in = processEngine.getRepositoryService()
                .getResourceAsStream(deployId, resName);

        // 将图片生成到D盘目录下
        File file = new File("D:\\"+resName);
        // 将输入流的图片写到D盘下
        try {
            FileUtils.copyInputStreamToFile(in, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询最新版本的流程定义
     */
    @Test
    public void fundNewVersionProcess(){
        List<ProcessDefinition> lists = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()
                .list();

        // 当key值相同时，后一次的值将替换前一次的值
        Map<String, ProcessDefinition> map = new LinkedHashMap<>();
        if(lists!=null && lists.size()>0){
            for (ProcessDefinition pd : lists){
                map.put(pd.getKey(), pd);
            }
        }

        // 将Map转换成list
        List<ProcessDefinition> pdList = new ArrayList(map.values());
        if(pdList!=null && pdList.size()>0){
            for(ProcessDefinition pd : pdList){
                System.out.println("流程定义ID: "+pd.getId()); // 流程定义的key+版本+随机数
                System.out.println("流程定义名称: "+pd.getName()); // 对应helloworld.bpmn文件中的name属性
                System.out.println("流程定义Key: "+pd.getKey()); // 对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义版本: "+pd.getVersion()); // 当流程定义的Key值相同的情况下，版本升级，默认1
                System.out.println("png文件名称: "+pd.getDiagramResourceName());
                System.out.println("bpmn文件名称: "+pd.getResourceName());
                System.out.println("部署对象ID: "+pd.getDeploymentId());
                System.out.println("============================");
            }
        }
    }

    /**
     * 删除流程定义
     * 删除key相同的所有不同版本的流程定义
     */
    public void delProcessDefinitionByKey(){
        // 流程定义的key
        String processKey = "helloworld";
        // 先使用流程定义的key查询流程定义，查询所有的版本
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .list();

        if(list!=null && list.size()>0){
            for (ProcessDefinition pd : list){
                // 获取部署ID
                String deployId = pd.getDeploymentId();
                processEngine.getRepositoryService()
                        .deleteDeployment(deployId, true);
            }
        }
    }

}
