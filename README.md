## Activiti工作流

### Maven依赖
> 除了其本身的activiti-engine 依赖之外，还依赖于mybatis、mysql、spring-bean 等。
> activiti 内置了日志框架log4j，如果项目类路径下没有log4j.properties 文件，在项目启动
> 的时候会产生警告。

+ ActivitiTest
> 该例子是 采用两种方法创建工作流的23张表。
>    1. 采用配置代码的形式
>    2. 采用xml配置文件的形式

+ HelloWorldTest
> 该例子是一个 Activiti入门流程实例，主要演示了：
> 1. 如何创建流程定义
> 2. 部署流程定义
> 3. 启动流程实例
> 4. 查询个人当前任务
> 5. 完成任务

+ ProcessDefinitionTest
> 该例子主要是操作数据库的一些方法。
> 1. 从zip中部署流程定义
> 2. 多种方式、条件查询流程定义
> 3. 删除流程定义
> 4. 查看流程图，需要依赖于common-io.jar
> 5. 查询最新版本的流程定义

很多东西还没继续学，笔记如下：
[Activiti工作流.note] [http://note.youdao.com/noteshare?id=226e8e928175353bec6e3e0dfe402a86&sub=0BE83F51FDD9472EA75D09C06CC53A04]
