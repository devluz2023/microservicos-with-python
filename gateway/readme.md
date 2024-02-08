mvn clean install
docker build -t api-gateway .
docker run -d --name api-gateway -p 81:80 api-gateway

mvn clean install
docker build -t api-gateway .
docker tag api-gateway:latest fabiojdluz/api-gateway:v1
docker push fabiojdluz/api-gateway:v1
docker pull fabiojdluz/api-gateway:v1
docker run -d --name api-gateway -p 81:80 fabiojdluz/api-gateway
 