### CHAT SERVER

![main workflow](https://github.com/zhongzhikeji/chat-server/actions/workflows/maven.yml/badge.svg)

#### 开发部署


```sh
git clone https://github.com/zhongzhikeji/chat-server
cd chat-server
cd chat
mvn clean install -DskipTests
cp chat-ability/chat-ability-gateway/target/chat-ability-gateway-1.0-SNAPSHOT.jar ../.
cp chat-server/target/chat-server-1.0-SNAPSHOT.jar ../.
java -jar chat-ability-gateway-1.0-SNAPSHOT.jar
java -jar chat-server-1.0-SNAPSHOT.jar
```

#### ubuntu server 安装 java 环境（非 docker）

```sh
wget -q -O - https://download.bell-sw.com/pki/GPG-KEY-bellsoft | sudo apt-key add -
echo "deb [arch=amd64] https://apt.bell-sw.com/ stable main" | sudo tee /etc/apt/sources.list.d/bellsoft.list
sudo apt-get update
sudo apt-get install bellsoft-java8
java -version
```

#### FAQ

* 关于其他语言处理 jwt 的问题 https://github.com/auth0/node-jsonwebtoken/issues/208#issuecomment-231861138
