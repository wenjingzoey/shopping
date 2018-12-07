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

### 项目接口
#### 登录接口 /portal/user/login.do
~~~
String username,
String password；

首先应该进行参数非空校验，然后进行用户名检验，
看用户名是否存在（用到了check_valid接口），若存在则根据用户名和密码进行查询并对密码进行Md5加密；
最后进行返回的时候密码置空；
~~~
#### 注册接口 /portal/user/register.do
~~~
String username,
String password,
String email,
String phone,
String question,
String answer

首先进行参数的非空检验，然后是校验是否用户名是唯一的（该用户名是否已存在），
若不存在则继续校验邮箱是否是唯一的，校验成功之后进行注册并对密码进行Md5加密;
该接口在校验用户名和邮箱时引用了check_valid接口；
~~~
#### 检查用户名是否有效接口/portal/user/check_valid.do
~~~
String str,
String type
str可以是用户名或邮箱，对应的type是username和email

首先也是进行非空校验，然后是判断用户名和邮箱是否已存在，
若已存在则是无效的，不存在是有效的可进行注册；
~~~
#### 获取登录用户信息接口 /portal/user/get_user_info.do
~~~
是在controller控制层写的,是根据在登录界面获得的session值进而获取的用户信息；
用object接受session的值，进行非空判断以及强制转换把值为空的字段过滤掉
~~~
#### 获取用户详细信息接口
~~~
跟获取登录用户信息接口是一样的
~~~
## 知识点
~~~
mybatis：半自动化框架
在设计数据库时，某一字段属于一个表，但它又同时出现在另一个或多个表，且完全等同于它在其本来所属表的意义表示，那么这个字段就是一个冗余字段。
在db.properties中用的是jdbc.username是为了防止服务器默认加载系统的username
@RestController和@Controller的区别
@RestController往前端返回的是json数据
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) 当ServerResponse转成json字符串的时候，非空字段不会进行转化（值为空的过滤掉）
@JsonIgnore ServerResponse转成json字符串把值为success忽略掉
 