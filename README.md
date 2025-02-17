# Spring Boot 中文全文搜索示例

这是一个使用 Spring Boot 和 PostgreSQL 实现的中文全文搜索示例项目。本项目结合了 Jieba 中文分词和 PostgreSQL 的全文搜索功能，实现了对中文文章的高效搜索。

## 技术栈

- Spring Boot 3.4.2
- PostgreSQL (全文搜索)
- JPA/Hibernate
- Jieba 分词器
- Java 21

## 功能特点

- 支持中文分词
- 文章标题和内容的全文搜索
- 搜索结果权重排序（标题权重高于内容）
- RESTful API 接口

## 快速开始

### 前置要求

- JDK 21
- PostgreSQL 数据库
- Maven

### 数据库配置

1. 创建 PostgreSQL 数据库：

```sql
CREATE DATABASE medusa;
```

2. 创建文章表：

```sql
CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    tokens TSVECTOR
);

CREATE EXTENSION pg_trgm;
CREATE INDEX articles_tokens_idx ON articles USING GIN (tokens);
```

### 配置文件

配置文件位于 `src/main/resources/application.yaml`，根据你的环境修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/medusa
    username: your_username
    password: your_password
```

## API 接口

### 创建文章
```
GET /articles?title={title}&content={content}
```

### 根据ID获取文章
```
GET /articles/{id}
```

### 搜索文章
```
GET /articles/search?query={searchText}
```

## 实现原理

1. 使用 Jieba 分词器对中文文本进行分词
2. 将分词结果存储为 PostgreSQL 的 tsvector 类型
3. 使用 PostgreSQL 的全文搜索功能进行查询
4. 通过权重设置（setweight）使标题搜索结果具有更高优先级

## 开发环境

- IDE: IntelliJ IDEA (推荐)
- 构建工具: Maven
- 版本控制: Git

## 许可证

[MIT License](LICENSE)

## 参考

https://www.skypyb.com/2020/12/jishu/1705/