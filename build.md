## 四层架构
#### 视图层
#### 控制层 controller
#### 业务逻辑层 service（负责具体的逻辑）
######         接口和实现类
####    dao层 （与数据库交互）

## Mybatis-generator插件
  ~~~
  在maven中引入插件
  在generatorConfig.xml中进行配置，引入properties，配置mysql的驱动包jar，它的位置从用户哪里找；
  实体类
  配置sql文件，生成的sql语句放在mapper包
  生成Dao接口
 配置数据表   tableName：你建的表名，domainObjectName：生成的类名
 ~~~
 ~~~ 
 可以生成dao接口、实体类和映射文件
 ~~~

## 搭建ssm框架
### 依赖的jar包
~~~
单元测试
连接mysql驱动包
mybatis依赖
mybatis-generator依赖
mybatis集成spring依赖包
数据库连接池
spring核心依赖
spring web项目的依赖
事务管理以及有事务存在，需jdbc依赖
servlet依赖
日志框架Logback的依赖    用于日志搜集和打印
json解析依赖
guava缓存 谷歌提供 项目中需要用到
mybatis-pagehelper 分页
joda-time  时间格式化 例如：将时间转字符串，将字符串转date
图片上传  商品模块会用到
ftpclient 在项目中会搭建图片服务器
 alipay 集成支付宝所需用的包
~~~
## 知识点
~~~
mybatis：半自动化框架
在db.properties中用的是jdbc.username是为了防止服务器默认加载系统的username
@RestController和@Controller的区别
@RestController往前端返回的是json数据