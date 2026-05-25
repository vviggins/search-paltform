# 聚合搜索后端

一个用于学习聚合搜索实现方式的 Spring Boot 示例项目，包含文章、图片、用户等多数据源查询能力，以及 Elasticsearch 同步、文件上传、权限校验等常见后端模块。

## 项目目标

- 学习多类型内容的统一搜索接口设计
- 学习数据抓取、数据同步与 ES 检索的基本流程
- 学习 Spring Boot 项目中的分层组织、异常处理和权限控制

## 技术栈

- Java 8
- Spring Boot 2.7
- MyBatis-Plus
- MySQL
- Elasticsearch
- Redis
- Knife4j / Swagger
- 腾讯云 COS SDK

## 主要模块

- `controller`: 对外提供文章、图片、用户、搜索等接口
- `datasource`: 聚合不同来源的数据查询逻辑
- `service`: 业务层实现
- `mapper`: 数据访问层
- `job`: 数据初始化与同步任务
- `wxmp`: 微信公众号相关能力

## 运行说明

1. 修改 `src/main/resources/application.yml` 中的数据库、Redis、Elasticsearch 配置。
2. 按需修改 COS、微信等第三方配置。
3. 执行 `sql/create_table.sql` 初始化数据库。
4. 运行启动类 `com.search.platform.MainApplication`。

## 说明

- 仓库中的示例数据和文档已清理为中性内容，仅用于代码学习。
- 若不需要第三方能力，可先忽略 COS、微信菜单、定时同步等模块。
