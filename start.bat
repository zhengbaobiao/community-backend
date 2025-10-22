@echo off
chcp 65001
cls

echo =========================================
echo   社区服务后台管理系统
echo   Community Admin System
echo =========================================
echo.

REM 检查Java版本
echo 检查Java环境...
java -version
if errorlevel 1 (
    echo 错误: 未检测到Java环境,请先安装JDK 1.8+
    pause
    exit /b 1
)
echo.

REM 检查Maven
echo 检查Maven环境...
call mvn -version
if errorlevel 1 (
    echo 错误: 未检测到Maven,请先安装Maven 3.6+
    pause
    exit /b 1
)
echo.

REM 编译项目
echo 正在编译项目...
call mvn clean package -DskipTests
if errorlevel 1 (
    echo 错误: 项目编译失败
    pause
    exit /b 1
)
echo.

REM 启动应用
echo =========================================
echo   正在启动应用...
echo =========================================
echo.
echo 访问地址:
echo   - API: http://localhost:8080/admin-api
echo   - Druid: http://localhost:8080/admin-api/druid
echo.
echo 默认账号:
echo   - 用户名: admin
echo   - 密码: admin123
echo.

java -jar target\community-admin-1.0.0.jar
