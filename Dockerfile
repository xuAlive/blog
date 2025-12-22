# 构建阶段
FROM maven:3.8-openjdk-8 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:8-jre-slim
WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 复制打包好的 jar 文件
COPY --from=builder /app/target/blog-1.0-SNAPSHOT.jar app.jar

# 暴露端口（默认 6101，可通过环境变量覆盖）
EXPOSE 6101

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
