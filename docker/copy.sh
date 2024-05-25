#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/ry_20230706.sql ./mysql/db
cp ../sql/ry_config_20220929.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../xsg-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy xsg-gateway "
cp ../xsg-gateway/target/xsg-gateway.jar ./ruoyi/gateway/jar

echo "begin copy xsg-auth "
cp ../xsg-auth/target/xsg-auth.jar ./ruoyi/auth/jar

echo "begin copy xsg-visual "
cp ../xsg-visual/xsg-monitor/target/xsg-visual-monitor.jar  ./ruoyi/visual/monitor/jar

echo "begin copy xsg-modules-system "
cp ../xsg-modules/xsg-system/target/xsg-modules-system.jar ./ruoyi/modules/system/jar

echo "begin copy xsg-modules-file "
cp ../xsg-modules/xsg-file/target/xsg-modules-file.jar ./ruoyi/modules/file/jar

echo "begin copy xsg-modules-job "
cp ../xsg-modules/xsg-job/target/xsg-modules-job.jar ./ruoyi/modules/job/jar

echo "begin copy xsg-modules-gen "
cp ../xsg-modules/xsg-gen/target/xsg-modules-gen.jar ./ruoyi/modules/gen/jar

