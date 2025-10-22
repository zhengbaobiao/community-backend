#!/bin/bash

# 社区服务后台管理系统 - 快速启动脚本
# 使用方法: ./start.sh

echo "========================================="
echo "  社区服务后台管理系统"
echo "  Community Admin System"
echo "========================================="
echo ""

# 检查Java版本
echo "检查Java环境..."
java -version
if [ $? -ne 0 ]; then
    echo "错误: 未检测到Java环境,请先安装JDK 1.8+"
    exit 1
fi
echo ""

# 检查Maven
echo "检查Maven环境..."
mvn -version
if [ $? -ne 0 ]; then
    echo "错误: 未检测到Maven,请先安装Maven 3.6+"
    exit 1
fi
echo ""

# 检查MySQL
echo "检查MySQL连接..."
mysql --version
if [ $? -ne 0 ]; then
    echo "警告: 未检测到MySQL客户端,请确保MySQL已安装并运行"
fi
echo ""

# 检查Redis
echo "检查Redis连接..."
redis-cli ping
if [ $? -ne 0 ]; then
    echo "警告: 未检测到Redis,请确保Redis已安装并运行"
fi
echo ""

# 编译项目
echo "正在编译项目..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "错误: 项目编译失败"
    exit 1
fi
echo ""

# 启动应用
echo "========================================="
echo "  正在启动应用..."
echo "========================================="
echo ""
echo "访问地址:"
echo "  - API: http://localhost:8080/admin-api"
echo "  - Druid: http://localhost:8080/admin-api/druid"
echo ""
echo "默认账号:"
echo "  - 用户名: admin"
echo "  - 密码: admin123"
echo ""

java -jar target/community-admin-1.0.0.jar
