
# im聊天系统部署文档

# **本系统使用必须符合国家法律法规 具体法律规定看文档**



# 必须购买腾讯或者阿里正规服务器，禁止使用私人服务器





# 自己准备好服务器和域名资料



# 用户端默认密码：admin

# 管理平台默认账号密码 123456

# 管理后台node版本要求11.0.0



# 服务端JAVA jdk11

# mysql 5.7

# mongoDB5.0

# maven要用阿里源



**本系统需要解析****二****个域名 可以用一个域名 创建****二****个二级域名（小程序的安全域名 必须吧用户端****的****代码都设置一下）**

**管理端域名   用户端域名 全部添加的 宝塔网站**

**然后用户端下 按照下方步骤 部署服务端代码 部署完成后 使用部署文档说明中的nginx配置文件都配置一遍**



**讲解一下我们为了稳定快速提问措施方案，**

**方案一使用美国服务器，直接部署，但是会比较卡，就是单独一个服务器**



**方案二、最优方案。使用分布式部署方案购买一个美国服务器，解析一个域名，部署服务端，配置一下socket这些，这个只用于请求处理socket购买一个国内服务器 解析两个域名 部署服务端 用户端 管理端 用户端和管理端代码下 ，需要把wss系列的全部替换使用国外服务器域名 其他的全局替换为国内服务器域名即可**





**注：国外服务器同样需要安装宝塔，按照下面的步骤，国外的服务需要链接国内的数据库**





用户端  管理端 分别开发主机请安装好node(版本12.0+), yarn 等工具

服务端导入IDEA



# 一、服务器的配置

最低配置为：CPU:4核 内存:8G 带宽:10M 80G系统盘 CentOs 7.7 64位

# 二、服务器里所需的服务安装

#### 1、先安装docker

```text
curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun && systemctl start docker && systemctl enable docker
```

#### 2、安装Mysql

#### 下载Mysql镜像

```
docker pull mysql:xxx  //下载指定版本的Mysql镜像 (xxx指具体版本号)
```

#### 安装运行Mysql

```
docker run \
--name mysql \
-d \
-p 3306:3306 \
--restart unless-stopped \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-v /mydata/mysql/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=123456 \
```

详细可看教程[(48条消息) Docker 安装 Mysql 容器 (完整详细版)_docker mysql_Touch&的博客-CSDN博客](https://blog.csdn.net/BThinker/article/details/123471514)

#### 2、安装redis

```
docker pull redis
```

#### docker启动redis

```
docker run --restart=always --log-opt max-size=100m --log-opt max-file=2 -p 6379:6379 --name myredis -v /home/redis/myredis/myredis.conf:/etc/redis/redis.conf -v /home/redis/myredis/data:/data -d redis redis-server /etc/redis/redis.conf  --appendonly yes  --requirepass 000415
```

详细可看教程[(48条消息) 史上最详细Docker安装Redis （含每一步的图解）实战_docker redis_宁在春的博客-CSDN博客](https://blog.csdn.net/weixin_45821811/article/details/116211724)

#### 3、安装mongodb

#### 拉取mongo镜像

```
docker pull mongo:5.0.10-focal
```

#### 运行容器

#### 首先创建宿主机挂载目录

```
#mongo数据目录
mkdir /opt/dockerstore/mongo/data
#mongo日志目录
mkdir /opt/dockerstore/mongo/logs
```

#### 然后新建docker-compose.yml文件，具体配置如下：

```
version: '3.1'
services:
  mongodb:
    container_name: mongodb
    image: mongo:5.0.10-focal
    ports:
      - "27017:27017"
    restart: always
    command:
      - "--auth"
    environment:
#      MONGO_INITDB_ROOT_USERNAME: root
#      MONGO_INITDB_ROOT_PASSWORD: "x+s9zI&VA!s"
      wiredTigerCacheSizeGB: 2
    volumes:
      - "/opt/dockerstore/mongo/data:/data/db"
      - "/opt/dockerstore/mongo/logs:/var/log/mongodb"
      - "/usr/share/zoneinfo/Asia/Shanghai:/etc/localtime"

networks:
  docker:
    external: true
```

#### docker-compose启动mongoDB：

```
docker-compse up -d --build
```

#### 执行命令进入mongo容器：

```
docker exec -it mongodb /bin/bash
```

#### 进入容器后连接mongo客户端

```
mongo admin
```

#### 进入客户端后创建用户

```
#使用rad_app库，如果不存在会创建
use rad_app;
#创建用户并赋予角色权限
db.createUser({user:'rad_app_user',pwd:'123',roles:[{role:'userAdmin',db:'rad_app'},"readWrite"]});
```

详细内容请看[(48条消息) docker-compose安装mongoDB详细步骤_docker-compose mongodb_07feng的博客-CSDN博客](https://blog.csdn.net/weixin_43358050/article/details/127430557)

#### 4、安装nginx

#### 下载Nginx镜像

```
docker pull nginx:xxx //xxx:指定版本
```

####  创建Nginx配置文件 

```
# 创建挂载目录
mkdir -p /home/nginx/conf
mkdir -p /home/nginx/log
mkdir -p /home/nginx/html
```

#### 创建Nginx容器并运行

```
# 直接执行docker rm nginx或者以容器id方式关闭容器
# 找到nginx对应的容器id
docker ps -a
# 关闭该容器
docker stop nginx
# 删除该容器
docker rm nginx
 
# 删除正在运行的nginx容器
docker rm -f nginx
```

```
docker run \
-p 9002:80 \
--name nginx \
-v /home/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
-v /home/nginx/conf/conf.d:/etc/nginx/conf.d \
-v /home/nginx/log:/var/log/nginx \
-v /home/nginx/html:/usr/share/nginx/html \
-d nginx:latest
```

详细内容请看[(48条消息) Docker 安装 Nginx 容器 (完整详细版)_docker nginx_Touch&的博客-CSDN博客](https://blog.csdn.net/BThinker/article/details/123507820?ops_request_misc=%7B%22request%5Fid%22%3A%22168412877816782427451693%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=168412877816782427451693&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-123507820-null-null.142^v87^control_2,239^v2^insert_chatgpt&utm_term=docker安装nginx&spm=1018.2226.3001.4187)

#### 4、安装nacos

#### 拉取镜像

```
docker pull nacos/nacos-server
```

#### 挂载目录

```
mkdir -p /mydata/nacos/logs/                      #新建logs目录
mkdir -p /mydata/nacos/conf/						#新建conf目录
```

#### 启动容器

```
docker run -p 8848:8848 --name nacos -d nacos/nacos-server
```

详细可看教程[(48条消息) Docker启动安装nacos（详情讲解，全网最细）_docker启动nacos命令_Color L的博客-CSDN博客](https://blog.csdn.net/ilvjiale/article/details/129417768?ops_request_misc=%7B%22request%5Fid%22%3A%22168412790016800182711170%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=168412790016800182711170&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-129417768-null-null.142^v87^control_2,239^v2^insert_chatgpt&utm_term=docker安装nacos&spm=1018.2226.3001.4187)

#### 4、安装seaweedfs

##### 服务器创建文件夹

```
mkdir -p /myapp/seaweedfs
cd /myapp/seaweedfs
```

##### 访问[Releases · seaweedfs/seaweedfs · GitHub](https://github.com/seaweedfs/seaweedfs/releases)下载linux_amd64.tar.gz文件,使用XShell工具下载到/myapp/seaweedfs路径。

```
tar -xzvf linux_amd64.tar.gz 
```

##### 生成一个weed可执行文件，用于操作master和volume等。 创建数据卷根目录以及各个数据卷目录、master数据目录和filter目录

```
mkdir -p /myapp/seaweedfs/data/master
mkdir -p /myapp/seaweedfs/data/vola
mkdir -p /myapp/seaweedfs/data/volb
mkdir -p /myapp/seaweedfs/data/volc
mkdir -p /myapp/seaweedfs/data/vold
mkdir -p /data/weedfs_data/filer
```

## SeaWeedfs启动

##### 启动SeaWeedfs可使用nohup命令进行后台运行并且将日志输出到文件中。

##### 启动mster，-defaultReplication="001"表示相同机架存储一份副本，而且如果一个卷服务器没在相同的rack开启一个vol就会报错:

```
nohup ./weed -v=3 master -mdir=/myapp/seaweedfs/data/master -defaultReplication="001" >> /myapp/seaweedfs/data/master/wdfsmaster.log &
```

##### 启动volume，两个卷服务器放在同一个机架:

```
nohup ./weed -v=3 volume -port=8081 -dir=/myapp/seaweedfs/data/vola -mserver=localhost:9333 -rack=rack1 >> /myapp/seaweedfs/data/vola/wefsvola.log &
​
nohup ./weed -v=3 volume -port=8082 -dir=/myapp/seaweedfs/data/volb -mserver=localhost:9333 -rack=rack2 >> /myapp/seaweedfs/data/volb/wefsvolb.log &
​
nohup ./weed -v=3 volume -port=8083 -dir=/myapp/seaweedfs/data/volc -mserver=localhost:9333 -rack=rack3 >> /myapp/seaweedfs/data/volc/wefsvolc.log &
​
nohup ./weed -v=3 volume -port=8084 -dir=/myapp/seaweedfs/data/vold -mserver=localhost:9333 -rack=rack1 >> /myapp/seaweedfs/data/vold/wefsvold.log &
```

##### 开启filer服务器:

```
nohup ./weed -v=3 filer -port=8888 -master=localhost:9333 >> /myapp/seaweedfs/data/filer/filer.log &
```

##### 开启挂载服务器：

```
nohup ./weed mount -filer=localhost:8888 -dir=/myapp/seaweedfs/data/filermount/data -filer.path=/  >> /myapp/seaweedfs/data/filermount/wefsmount.log &
```

##### 详细教程：[(48条消息) SeaWeedFS安装以及部署，以及master模式和filer模式的介绍_seaweedfs 安装_-37度阳光的博客-CSDN博客](https://blog.csdn.net/qq_42412606/article/details/126944470)

## 三、开发所需安装（开发人员观看）

### 1.前端安装

**node环境要求11.0.0** 

#### node环境安装说明

https://www.runoob.com/nodejs/nodejs-install-setup.html

##### 下载VSCode

下载地址[Visual Studio Code - Code Editing. Redefined](https://code.visualstudio.com/)

##### vscode教程

[https://www.runoob.com/w3cnote/vscode-tutorial.html](https://www.runoob.com/w3cnote/vscode-tutorial.html)

#### 

### 2.前端打包

#### 获取前端源码并打包最后向服务器导入源码

#### 执行yarn bulid命令打包

![](https://foxim.lvyanhui.com/uploads/1,018266f241fa)

#### 将项目dist文件夹压缩打包

![](https://foxim.lvyanhui.com/uploads/4,01849a0444ed)



#### 将打包好的文件上传到服务器

### 3.部署java后台服务

#### 1、安装java环境

教程：[(48条消息) JAVA环境安装_橘柒啊的博客-CSDN博客](https://blog.csdn.net/qq_41737353/article/details/126967592?ops_request_misc=%7B%22request%5Fid%22%3A%22168413208216800197012759%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=168413208216800197012759&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_click~default-2-126967592-null-null.142^v87^control_2,239^v2^insert_chatgpt&utm_term=安装java环境&spm=1018.2226.3001.4187)

#### 2、maven开发环境配置

maven需要配置阿里源

[https://jingyan.baidu.com/article/ce09321ba734f02bff858fcf.html](https://jingyan.baidu.com/article/ce09321ba734f02bff858fcf.html?accessToken=eyJhbGciOiJIUzI1NiIsImtpZCI6ImRlZmF1bHQiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE2ODM1Mjk5MjEsImZpbGVHVUlEIjoiOTAzME1abTRnWVNwcHF3cCIsImlhdCI6MTY4MzUyOTYyMSwiaXNzIjoidXBsb2FkZXJfYWNjZXNzX3Jlc291cmNlIiwidXNlcklkIjo4OTEyMzk5Nn0.lQ9p_HghduwYQ5LCyqdC4xUqxoz3D30slAyqNivO6Js)

#### 三、IDEA开发工具下载

[https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/?accessToken=eyJhbGciOiJIUzI1NiIsImtpZCI6ImRlZmF1bHQiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE2ODM1Mjk5MjEsImZpbGVHVUlEIjoiOTAzME1abTRnWVNwcHF3cCIsImlhdCI6MTY4MzUyOTYyMSwiaXNzIjoidXBsb2FkZXJfYWNjZXNzX3Jlc291cmNlIiwidXNlcklkIjo4OTEyMzk5Nn0.lQ9p_HghduwYQ5LCyqdC4xUqxoz3D30slAyqNivO6Js)

####  

#### 4、本地maven安装

教程：[(48条消息) 史上最详细的Maven安装教程_mvn安装_chenxiky的博客-CSDN博客](https://blog.csdn.net/weixin_44080187/article/details/122933194?ops_request_misc=%7B%22request%5Fid%22%3A%22168413249916782425148459%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=168413249916782425148459&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-1-122933194-null-null.142^v87^control_2,239^v2^insert_chatgpt&utm_term=本地maven安装&spm=1018.2226.3001.4187)

#### 5、后端java代码打包

```
git clone https://github.com/zhongzhikeji/chat-server
cd chat-server
cd chat
mvn clean install
cp chat-ability/chat-ability-gateway/target/chat-ability-gateway-1.0-SNAPSHOT.jar
cp chat-server/target/chat-server-1.0-SNAPSHOT.jar
java -jar chat-ability-gateway-1.0-SNAPSHOT.jar
java -jar chat-server-1.0-SNAPSHOT.jar
```

下载源码，使用maven工具中的mvn clean install 打包

工具中执行mvn clean install 显示打包成功 就表示成功了

#### foxim后端服务打包

![](https://foxim.lvyanhui.com/uploads/1,0185bf32b6fd)

#### foxim-admin后台后端服务打包

##### 同上一样操作

### 6.foxim-ui后台页面打包

##### 下载源码后

##### 配置镜像加速
https://www.ydyno.com/archives/1219.html

```
安装依赖
npm install
启动服务 localhost:8013
npm run dev
构建生产环境
npm run build:prod
```



## 四、上传服务器并启动

![](https://foxim.lvyanhui.com/uploads/3,0188ce43d203)

1.打开redis.conf文件配置如下映射

	foxim后台权限管理 Nginx配置 修改成对应的目录和端口 /app映射对应foxim-server网关地址


        server {
            listen       80;
            server_name  IP/域名;
        #charset koi8-r;
    		listen 443 ssl;
    		#ssl    on;
    		ssl_certificate    /etc/letsencrypt/live/admin.ctcim.top/fullchain.pem;
    		ssl_certificate_key    /etc/letsencrypt/live/admin.ctcim.top/privkey.pem;
        	#access_log  logs/host.access.log  main;
    
    	location / {
            root   html/foxim-admin;
            index  index.html index.htm;
            error_log logs/f_error.log;
        }
    
    	location /api {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://localhost:8002;
        }
    	
    	location /app {
            proxy_set_header Host $http_host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header REMOTE-HOST $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://****:8001/api/v1;
        }
    	
    	# 授权接口
        location /auth {
          proxy_pass http://localhost:8002;
          proxy_set_header X-Forwarded-Proto $scheme;
          proxy_set_header X-Forwarded-Port $server_port;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header Upgrade $http_upgrade;
          proxy_set_header Connection "upgrade";
        }
    
        # WebSocket 服务
        location /webSocket {
          proxy_redirect off;
          proxy_pass http://localhost:8002/webSocket;
          proxy_http_version 1.1;
          proxy_set_header Upgrade $http_upgrade;
          proxy_set_header Connection "upgrade";
          proxy_set_header Host $http_host;
          proxy_set_header X-Real-IP $remote_addr;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_connect_timeout 60s;
          proxy_read_timeout 86400s;
          proxy_send_timeout 60s;
        }
    
        # 头像
        location /avatar {
          proxy_pass http://localhost:8002;
        }
    
        # 文件
        location /file {
          proxy_pass http://localhost:8002;
        }
    	
        error_page  404              /index.html;
    }

 



foxim后端Nginx配置 

		server {
	        listen       80;
	        server_name  IP/域名;
		listen 443 ssl;
		#ssl    on;
		ssl_certificate    /etc/letsencrypt/live/m.ctcim.top/fullchain.pem;
		ssl_certificate_key    /etc/letsencrypt/live/m.ctcim.top/privkey.pem;
	
	    #charset koi8-r;
	
	    #access_log  logs/host.access.log  main;
	
	    location / {
	        root   html/foxim-ui;
	        index  index.html index.htm;
	        error_log logs/f_error.log;
	    }
	
	    location /api/ {
	      proxy_pass http://localhost:8001/api/;
	      proxy_connect_timeout 300; #单位秒
	      proxy_send_timeout 300; #单位秒
	      proxy_read_timeout 300; #单位秒
	
	      proxy_set_header   Host    $host;
	      proxy_set_header   X-Real-IP   $remote_addr;
	      proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
	    }
	
	    location /mqtt {
	    proxy_pass http://localhost:10889;
	    proxy_http_version 1.1;
	    proxy_set_header Upgrade $http_upgrade;
	    proxy_set_header Connection "upgrade";
	    proxy_set_header Host $host;
	    }
	        error_page  404              /index.html;
	    }

2.上传dist前端打包文件到nginx目录html下foxim-项目对应文件夹

3.上传jar包到www/jar目录下

rz命令

4.启动命令如下：

nohup java -Xms256m -Xmx256m -Xmn128m -jar /www/jar/foxim-ability-gateway.jar >  /www/jar/foxim-ability-gateway.jar_log.txt 2>&1

nohup java -Xms256m -Xmx256m -Xmn128m -jar /www/jar/foxim-server.jar >  /www/jar/foxim-server.jar_log.txt 2>&1 &

nohup java -Xms256m -Xmx256m -Xmn128m -jar /www/jar/foxim-admin.jar >  /www/jar/foxim-admin.jar_log.txt 2>&1 &



至此部署完毕
