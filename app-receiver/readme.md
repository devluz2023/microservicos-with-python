docker build -t app-receiver .
docker run -d --name app-receiver -p 81:80 app-receiver

docker build -t app-receiver .
docker tag app-receiver:latest fabiojdluz/app-receiver:v2
docker push fabiojdluz/app-receiver:v2
docker pull fabiojdluz/app-receiver:v2
docker run -d --name app-receiver -p 81:80 fabiojdluz/app-receiver
 