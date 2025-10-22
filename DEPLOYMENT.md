# 部署指南

## 开发环境部署

### 1. 准备工作

#### 安装MySQL 8.0
```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install mysql-server

# CentOS/RHEL
sudo yum install mysql-server

# 启动MySQL
sudo systemctl start mysql
sudo systemctl enable mysql
```

#### 安装Redis
```bash
# Ubuntu/Debian
sudo apt-get install redis-server

# CentOS/RHEL
sudo yum install redis

# 启动Redis
sudo systemctl start redis
sudo systemctl enable redis
```

### 2. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source /path/to/community-backend/sql/init.sql

# 验证表是否创建成功
USE community_admin;
SHOW TABLES;
```

### 3. 配置文件修改

修改 `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/community_admin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: your_mysql_username
    password: your_mysql_password
  
  redis:
    host: localhost
    port: 6379
    password: your_redis_password  # 如果没有密码可以留空
```

### 4. 启动应用

```bash
# 方式1: Maven运行
mvn spring-boot:run

# 方式2: 打包后运行
mvn clean package
java -jar target/community-admin-1.0.0.jar

# 方式3: IDE运行
# 直接在IDE中运行CommunityAdminApplication.java
```

### 5. 验证部署

访问以下地址验证部署是否成功:

- 应用地址: http://localhost:8080/admin-api
- Druid监控: http://localhost:8080/admin-api/druid

使用Postman或curl测试登录接口:

```bash
curl -X POST http://localhost:8080/admin-api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

## 生产环境部署

### 1. Docker部署

#### 创建Dockerfile

```dockerfile
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/community-admin-1.0.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8080
```

#### 创建docker-compose.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: community-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: community_admin
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./sql/init.sql:/docker-entrypoint-initdb.d/init.sql
    command: --default-authentication-plugin=mysql_native_password

  redis:
    image: redis:6-alpine
    container_name: community-redis
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data

  app:
    build: .
    container_name: community-admin
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/community_admin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379

volumes:
  mysql-data:
  redis-data:
```

#### 启动服务

```bash
# 构建并启动
docker-compose up -d

# 查看日志
docker-compose logs -f app

# 停止服务
docker-compose down
```

### 2. Nginx配置

创建Nginx配置文件 `/etc/nginx/sites-available/community-admin`:

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态资源
    location / {
        root /var/www/community-admin-frontend;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端API代理
    location /admin-api/ {
        proxy_pass http://localhost:8080/admin-api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # WebSocket支持
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}
```

启用配置:

```bash
# 创建软链接
sudo ln -s /etc/nginx/sites-available/community-admin /etc/nginx/sites-enabled/

# 测试配置
sudo nginx -t

# 重启Nginx
sudo systemctl restart nginx
```

### 3. 生产环境配置优化

创建 `application-prod.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    druid:
      initial-size: 10
      min-idle: 20
      max-active: 50
      max-wait: 60000

  redis:
    lettuce:
      pool:
        max-active: 500
        max-idle: 20
        min-idle: 5

logging:
  level:
    com.community.admin: info
    org.springframework.security: warn
  file:
    name: /var/log/community-admin/application.log
    max-size: 200MB
    max-history: 30

jwt:
  secret: your-production-secret-key-please-change-this-to-a-strong-random-string
```

启动时指定配置文件:

```bash
java -jar community-admin-1.0.0.jar --spring.profiles.active=prod
```

### 4. 系统服务配置

创建systemd服务文件 `/etc/systemd/system/community-admin.service`:

```ini
[Unit]
Description=Community Admin Service
After=syslog.target network.target

[Service]
Type=simple
User=appuser
WorkingDirectory=/opt/community-admin
ExecStart=/usr/bin/java -jar /opt/community-admin/community-admin-1.0.0.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

管理服务:

```bash
# 重新加载systemd配置
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start community-admin

# 设置开机自启
sudo systemctl enable community-admin

# 查看状态
sudo systemctl status community-admin

# 查看日志
sudo journalctl -u community-admin -f
```

## 监控与维护

### 1. 健康检查

创建健康检查脚本:

```bash
#!/bin/bash
HEALTH_URL="http://localhost:8080/admin-api/actuator/health"
STATUS=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_URL)

if [ $STATUS -eq 200 ]; then
    echo "Application is healthy"
    exit 0
else
    echo "Application is unhealthy (HTTP $STATUS)"
    exit 1
fi
```

### 2. 日志管理

使用logrotate管理日志:

```bash
# /etc/logrotate.d/community-admin
/var/log/community-admin/*.log {
    daily
    rotate 30
    compress
    delaycompress
    notifempty
    create 644 appuser appuser
    sharedscripts
    postrotate
        systemctl reload community-admin > /dev/null 2>&1 || true
    endscript
}
```

### 3. 备份策略

MySQL备份脚本:

```bash
#!/bin/bash
BACKUP_DIR="/backup/mysql"
DATE=$(date +%Y%m%d_%H%M%S)
MYSQL_USER="root"
MYSQL_PASS="your_password"

# 创建备份目录
mkdir -p $BACKUP_DIR

# 备份数据库
mysqldump -u$MYSQL_USER -p$MYSQL_PASS community_admin > $BACKUP_DIR/community_admin_$DATE.sql

# 压缩备份
gzip $BACKUP_DIR/community_admin_$DATE.sql

# 删除30天前的备份
find $BACKUP_DIR -name "*.sql.gz" -mtime +30 -delete

echo "Backup completed: community_admin_$DATE.sql.gz"
```

添加到crontab (每天凌晨2点备份):

```bash
0 2 * * * /path/to/backup-script.sh
```

## 故障排查

### 常见问题

1. **应用无法启动 - 端口被占用**
```bash
# 查看端口占用
lsof -i:8080
netstat -tulpn | grep 8080

# 杀死占用进程
kill -9 <PID>
```

2. **数据库连接失败**
```bash
# 检查MySQL状态
sudo systemctl status mysql

# 测试数据库连接
mysql -h localhost -u root -p

# 检查防火墙
sudo ufw status
sudo firewall-cmd --list-all
```

3. **Redis连接失败**
```bash
# 检查Redis状态
sudo systemctl status redis

# 测试Redis连接
redis-cli ping

# 检查Redis配置
cat /etc/redis/redis.conf | grep bind
```

4. **查看应用日志**
```bash
# 实时查看日志
tail -f /var/log/community-admin/application.log

# 查看错误日志
grep ERROR /var/log/community-admin/application.log

# 查看最近100行
tail -n 100 /var/log/community-admin/application.log
```

## 性能优化建议

1. **JVM参数优化**
```bash
java -Xms2g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 \
     -jar community-admin-1.0.0.jar --spring.profiles.active=prod
```

2. **MySQL优化**
- 启用慢查询日志
- 优化索引
- 调整缓冲池大小

3. **Redis优化**
- 启用持久化(AOF或RDB)
- 设置合理的过期策略
- 监控内存使用

4. **Nginx优化**
- 启用gzip压缩
- 配置缓存
- 限流配置

---

**部署成功后请及时修改默认密码和JWT密钥!**
