
mvn clean install
docker build -t api-gateway .
docker run -d --name api-gateway -p 82:80 api-gateway

docker build -t api-gateway .
docker tag api-gateway:latest fabiojdluz/api-gateway:latest
docker push fabiojdluz/api-gateway:latest
docker pull fabiojdluz/api-gateway:latest
docker run -d --name api-gateway -p 82:80 fabiojdluz/api-gateway


