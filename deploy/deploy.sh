#!/bin/bash

# 博客系统部署脚本
# 用法: ./deploy.sh [start|stop|restart|logs|status|build|clean]

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 项目目录
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$PROJECT_DIR"

# 日志函数
log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# 检查 Docker 环境
check_docker() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker 未安装，请先安装 Docker"
        exit 1
    fi
    if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
        log_error "Docker Compose 未安装"
        exit 1
    fi
}

# Docker Compose 命令兼容
docker_compose() {
    if docker compose version &> /dev/null; then
        docker compose "$@"
    else
        docker-compose "$@"
    fi
}

# 启动服务
start() {
    log_info "启动服务..."
    docker_compose up -d
    log_info "服务启动完成"
    status
}

# 停止服务
stop() {
    log_info "停止服务..."
    docker_compose down
    log_info "服务已停止"
}

# 重启服务
restart() {
    log_info "重启服务..."
    docker_compose restart
    log_info "服务重启完成"
}

# 查看日志
logs() {
    local service=${1:-}
    if [ -n "$service" ]; then
        docker_compose logs -f "$service"
    else
        docker_compose logs -f
    fi
}

# 查看状态
status() {
    log_info "服务状态:"
    docker_compose ps
}

# 构建镜像
build() {
    log_info "构建后端镜像..."
    docker_compose build --no-cache blog-backend
    log_info "构建完成"
}

# 清理
clean() {
    log_info "清理容器和镜像..."
    docker_compose down -v --rmi local
    log_info "清理完成"
}

# 初始化部署目录
init() {
    log_info "初始化部署目录..."
    mkdir -p deploy/nginx/conf.d
    mkdir -p deploy/nginx/ssl
    mkdir -p deploy/frontend/dist
    mkdir -p deploy/mysql/init

    # 检查前端文件
    if [ ! -f "deploy/frontend/dist/index.html" ]; then
        log_warn "前端文件不存在，请将前端打包文件放到 deploy/frontend/dist 目录"
        echo "<h1>Blog Frontend</h1><p>请将前端打包文件放到此目录</p>" > deploy/frontend/dist/index.html
    fi

    # 检查环境变量文件
    if [ ! -f ".env" ]; then
        if [ -f ".env.example" ]; then
            cp .env.example .env
            log_info "已从 .env.example 创建 .env 文件，请修改配置"
        fi
    fi

    log_info "初始化完成"
}

# 更新部署
update() {
    log_info "更新部署..."

    # 拉取最新代码（如果是git仓库）
    if [ -d ".git" ]; then
        log_info "拉取最新代码..."
        git pull
    fi

    # 重新构建后端
    build

    # 重启服务
    docker_compose up -d

    log_info "更新完成"
}

# 帮助信息
usage() {
    echo "用法: $0 [命令]"
    echo ""
    echo "命令:"
    echo "  init      初始化部署目录"
    echo "  start     启动所有服务"
    echo "  stop      停止所有服务"
    echo "  restart   重启所有服务"
    echo "  status    查看服务状态"
    echo "  logs      查看日志 (可选: logs [服务名])"
    echo "  build     重新构建后端镜像"
    echo "  update    更新并重启服务"
    echo "  clean     清理容器和镜像"
    echo ""
    echo "示例:"
    echo "  $0 start          # 启动所有服务"
    echo "  $0 logs nginx     # 查看 nginx 日志"
    echo "  $0 logs mysql     # 查看 mysql 日志"
}

# 主函数
main() {
    check_docker

    case "${1:-}" in
        init)    init ;;
        start)   start ;;
        stop)    stop ;;
        restart) restart ;;
        status)  status ;;
        logs)    logs "$2" ;;
        build)   build ;;
        update)  update ;;
        clean)   clean ;;
        *)       usage ;;
    esac
}

main "$@"
