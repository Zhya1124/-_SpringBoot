## 类似ElasticSearch问答社区


## 资料
[Spring官方网站](https://spring.io/guides)  
[SpringBoot官方文档](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/html/spring-boot-features.html#boot-features-sql)  
[elasticsearch社区](https://elasticsearch.cn/)  
[Springboot中文资料导航](http://springboot.fun/)  
[bootstrap入门](https://v3.bootcss.com/getting-started/)  
[Maven仓库](https://mvnrepository.com/)  
[OAuth文档](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)  
## 工具
[bootstrap下载](https://www.bootcss.com/)  
[git](https://git-scm.com/)  
[VP画图工具](https://www.visual-paradigm.com/cn/)  
[OkHttp](https://square.github.io/okhttp/)  
[h2数据库](http://h2database.com/html/main.html)  
[Mybatis持久层框架](https://mybatis.org/mybatis-3/zh/index.html)
##脚本
```sql
CREATE TABLE USER(
    "ID" INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    "ACCOUNT_ID" VARCHAR(100),
    "NAME" VARCHAR(50),
    "TOKEN" CHAR(36),
    "GMT_CREATE" BIGINT,
    "GMT_MODIFIED" BIGINT
)
```