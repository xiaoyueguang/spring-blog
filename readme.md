# 博客系统.

对应文章[具体文章](https://github.com/xiaoyueguang/blog/blob/master/7.%E8%BF%90%E7%94%A8Spring%E5%AE%9E%E7%8E%B0%E5%8D%9A%E5%AE%A2.md)

application.properties 模板

```
com.ray.blog.title=

com.ray.blog.salt=

com.ray.blog.host=

spring.datasource.url=jdbc:mysql://localhost:3306/blog
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.jpa.properties.hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.show-sql= true

spring.mail.host=smtp.163.com
spring.mail.username=
spring.mail.password=
spring.mail.default-encoding=UTF-8
# 邮件发送
mail.fromMail.addr=

# 测试用
com.ray.test.email1=
com.ray.test.email2=


spring.redis.database=0  
spring.redis.host=localhost
spring.redis.port=6379  
spring.redis.password=
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-wait=-1
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.min-idle=0
```
