#!/bin/sh
export LANG="zh_CN.UTF-8"

#编译执行结果状态码清单
#201    mvn install 失败
#203    参数错误
#0        成功

#判断命令行参数,不符合条件则退出
if (( $#<1 ))
then
    echo "Usage: $1=profile, dev | test | online | test | online_vpc"
        exit 203;
fi
OUTPUT_DIR="output";
#设置命令行参数,前期主要为编译测试版或正式版
BUILD_CONFIG=$1;

#清理及建立编译目录
rm -rf ${OUTPUT_DIR};
mkdir -p ${OUTPUT_DIR};

#编译项目
mvn clean -U -Dmaven.test.skip=true -P ${BUILD_CONFIG} -f pom.xml install -e
if [[ $? -ne 0 ]]
then
    exit 201;
fi

mv qianlima-reptile-statistics-service/output/*.tar.gz ${OUTPUT_DIR}/
rm qianlima-reptile-statistics-service pom.xml
cd ${OUTPUT_DIR}
tar -zxvf *.tar.gz && rm -rf *.tar.gz
cd  qianlima-reptile-statistics-service
mv bin ../
cd ..
rm -rf qianlima-reptile-statistics-service
cd bin
echo `pwd`
ls -al