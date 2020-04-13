## 类似ElasticSearch问答社区

##部署
- Git
- JDK
- MySQL
- Maven
##步骤
- yum update
- yum install git
- mkdir App
- cd App
- git clone 仓库地址
- mvn -v
- mvn compile package
- cp src/main/resources/application.properties src/main/resources/application-production.properties
- vim application-production.properties
- mvn package
- java -jar -Dspring.profiles.active=production target/demo-0.0.1-SNAPSHOT.jar
- ps -aux|grep java
- git pull
- mvn clean compile flyway:migrate
## 资料
[Spring官方网站](https://spring.io/guides)  
[SpringBoot官方文档](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html#boot-features-sql)  
[elasticsearch社区](https://elasticsearch.cn/)  
[Springboot中文资料导航](http://springboot.fun/)  
[bootstrap入门](https://v3.bootcss.com/getting-started/)  
[Maven仓库](https://mvnrepository.com/)  
[OAuth文档](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)  
[thymeleaf官方文档](https://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html#setting-attribute-values)  
[MyBatisGenerator代码生成](http://mybatis.org/generator/)  
[Jqeury](https://jquery.com/)   
[Ufile SDK](https://github.com/ucloud/ufile-sdk-java)
## 工具
[bootstrap下载](https://www.bootcss.com/)  
[git](https://git-scm.com/)  
[VP画图工具](https://www.visual-paradigm.com/cn/)  
[OkHttp](https://square.github.io/okhttp/)  
[h2数据库插件](http://h2database.com/html/main.html)  
[Mybatis持久层框架](https://mybatis.org/mybatis-3/zh/index.html)  
[Flyway数据库版本管理](https://flywaydb.org/)  
[自动生成POJO代码工具lombok](https://projectlombok.org/)  
[tabbed postman插件](https://chrome.google.com/webstore/detail/tabbed-postman-rest-clien/)   
[EditorMD编辑器](http://editor.md.ipandao.com/)
##脚本
```sql
CREATE TABLE USER(
    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ACCOUNT_ID VARCHAR(100),
    NAME VARCHAR(50),
    TOKEN CHAR(36),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT
)
```
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```