## 使用
### 1.在项目 pom.xml 中锁定版本
（**以下两种方式任选其一**）

- 方式一：

如您使用的是 Spring Boot Parent 的方式，则替换 Spring Boot Parent 为 Leepandar Starter。

```xml
<parent>
    <groupId>com.leepandar.starter</groupId>
    <artifactId>leepandar-starter</artifactId>
    <version>{latest-version}</version>
</parent>
```

- 方式二：

如您使用的是引入 Spring Boot Dependencies 的方式，则替换 Spring Boot Dependencies 为 Leepandar Starter Dependencies

```xml
<properties>
    <java.version>17</java.version>
    <maven.compiler.source>${java.version}</maven.compiler.source>
    <maven.compiler.target>${java.version}</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<dependencyManagement>
    <dependencies>
        <!-- Leepandar Starter Dependencies -->
        <dependency>
            <groupId>com.leepandar.starter</groupId>
            <artifactId>leepandar-starter-dependencies</artifactId>
            <version>{latest-version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 2.在项目 pom.xml 中引入所需模块依赖

```xml
<dependencies>
    <!-- Web 模块 -->
    <dependency>
        <groupId>com.leepandar.starter</groupId>
        <artifactId>leepandar-starter-web</artifactId>
    </dependency>
</dependencies>
```

3.在 application.yml 中根据引入模块，添加所需配置

示例：跨域配置

```yaml
--- ### 跨域配置
leepandar-starter.web:
  cors:
    enabled: true
    # 配置允许跨域的域名
    allowed-origins: '*'
    # 配置允许跨域的请求方式
    allowed-methods: '*'
    # 配置允许跨域的请求头
    allowed-headers: '*'
    # 配置允许跨域的响应头
    exposed-headers: '*'
```

## 模块结构

```
leepandar-starter
├─ leepandar-starter-apidoc（接口文档模块：Spring Doc + Knife4j）
├─ leepandar-starter-auth（认证模块）
│  ├─ leepandar-starter-auth-satoken（国产轻量认证鉴权）
│  └─ leepandar-starter-auth-justauth（第三方登录）
├─ leepandar-starter-cache（缓存模块）
│  ├─ leepandar-starter-cache-redisson（Redisson）
│  ├─ leepandar-starter-cache-jetcache（JetCache 多级缓存）
│  └─ leepandar-starter-cache-springcache（Spring 缓存）
├─ leepandar-starter-captcha（验证码模块）
│  ├─ leepandar-starter-captcha-graphic（静态验证码）
│  └─ leepandar-starter-captcha-behavior（动态验证码）
├─ leepandar-starter-core（核心模块：包含线程池等自动配置）
├─ leepandar-starter-json（JSON 模块）
├─ leepandar-starter-validation（校验模块：Hibernate Validator）
├─ leepandar-starter-web（Web 开发模块：包含跨域、全局异常+响应、链路追踪等自动配置）
├─ leepandar-starter-data（数据访问模块）
│  ├─ leepandar-starter-data-core（核心模块）
│  ├─ leepandar-starter-data-mp（MyBatis Plus）
│  └─ leepandar-starter-data-mf（MyBatis Flex）
├─ leepandar-starter-encrypt（加密模块）
│  ├─ leepandar-starter-encrypt-core（核心模块）
│  ├─ leepandar-starter-encrypt-field（字段加密）
│  └─ leepandar-starter-encrypt-api（API 加密）
│  └─ leepandar-starter-encrypt-password-encoder（密码编码器）
├─ leepandar-starter-xss（XSS 过滤）
├─ leepandar-starter-ratelimiter（限流模块）
├─ leepandar-starter-idempotent（幂等模块）
├─ leepandar-starter-trace（链路追踪模块）
├─ leepandar-starter-messaging（消息模块）
│  ├─ leepandar-starter-message-mail（邮件）
│  └─ leepandar-starter-message-websocket（WebSocket）
├─ leepandar-starter-log（日志模块）
│  ├─ leepandar-starter-log-core（核心模块）
│  ├─ leepandar-starter-log-aop（基于 AOP 实现）
│  └─ leepandar-starter-log-interceptor（基于拦截器实现（Spring Boot Actuator HttpTrace 增强版））
├─ leepandar-starter-excel（Excel 文件处理模块）
├─ leepandar-starter-storage（存储模块）
├─ leepandar-starter-datapermission（数据权限模块）
│  ├─ leepandar-starter-datapermission-core（核心模块）
│  └─ leepandar-starter-datapermission-mp（MyBatis Plus）
├─ leepandar-starter-tenant（租户模块）
│  ├─ leepandar-starter-tenant-core（核心模块）
│  └─ leepandar-starter-tenant-mp（MyBatis Plus）
├─ leepandar-starter-crud（CRUD 模块）
│  ├─ leepandar-starter-crud-core（核心模块）
│  ├─ leepandar-starter-crud-mp（MyBatis Plus）
└─ └─ leepandar-starter-crud-mf（MyBatis Flex）
```