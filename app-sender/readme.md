docker build -t app-sender .
docker run -d --name app-sender -p 81:80 app-sender

docker build -t app-sender .
docker tag app-sender:latest fabiojdluz/app-sender:v4
docker push fabiojdluz/app-sender:v4
docker pull fabiojdluz/app-sender:v4
docker run -d --name app-sender -p 81:80 fabiojdluz/app-sender
 

 docs from localhost:8000
 swagger-ui-bundle.js from cdn.jsdelivr.net
 opeanapi.json from localhost:8000
 favicon.png from fasapi.tianlogo.com
