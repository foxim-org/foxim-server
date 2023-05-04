#!/bin/bash

ls
cd $GITHUB_WORKSPACE/chat
mvn clean install -DskipTests
cp chat-ability/chat-ability-gateway/target/chat-ability-gateway-1.0-SNAPSHOT.jar ../.
cp chat-server/target/chat-server-1.0-SNAPSHOT.jar ../.
cd ..
scp -o StrictHostKeyChecking=no -i ~/.ssh/id_rsa chat-ability-gateway-1.0-SNAPSHOT.jar chat-server-1.0-SNAPSHOT.jar ${SSH_USERNAME}@${SSH_IP}:~/server/chat-server/.

ssh -o StrictHostKeyChecking=no -i ~/.ssh/id_rsa ${SSH_USERNAME}@${SSH_IP} <<"ENDSSH"
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
cd ~/server/chat-server
pm2 delete chat-gateway-server
pm2 delete chat-server
pm2 start --name "chat-gateway-server" java -- -jar chat-ability-gateway-1.0-SNAPSHOT.jar --spring.profiles.active=prod
pm2 start --name "chat-server" java -- -jar chat-server-1.0-SNAPSHOT.jar --spring.profiles.active=prod
pm2 save
ENDSSH
