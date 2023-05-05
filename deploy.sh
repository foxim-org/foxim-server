#!/bin/bash

ls
cd $GITHUB_WORKSPACE/foxim
mvn clean install -DskipTests
cp foxim-ability/foxim-ability-gateway/target/foxim-ability-gateway-1.0-SNAPSHOT.jar ../.
cp foxim-server/target/foxim-server-1.0-SNAPSHOT.jar ../.
cd ..

scp -o StrictHostKeyChecking=no -i ~/.ssh/id_rsa \
foxim-ability-gateway-1.0-SNAPSHOT.jar \
foxim-server-1.0-SNAPSHOT.jar \
${SSH_USERNAME}@${SSH_IP}:~/server/foxim-server/.

ssh -o StrictHostKeyChecking=no -i ~/.ssh/id_rsa ${SSH_USERNAME}@${SSH_IP} <<"ENDSSH"
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
cd ~/server/foxim-server
pm2 delete foxim-gateway-server
pm2 delete foxim-server
pm2 start --name "foxim-gateway-server" java -- -jar foxim-ability-gateway-1.0-SNAPSHOT.jar --spring.profiles.active=prod
pm2 start --name "foxim-server" java -- -jar foxim-server-1.0-SNAPSHOT.jar --spring.profiles.active=prod
pm2 save
ENDSSH
