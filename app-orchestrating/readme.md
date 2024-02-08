uvicorn main:app --host "0.0.0.0" --port 80


docker build -t app-orchestrating .
docker run -d --name app-orchestrating -p 8084:80 app-orchestrating

docker build -t app-orchestrating .
docker tag app-orchestrating:latest fabiojdluz/app-orchestrating:v2
docker push fabiojdluz/app-orchestrating:v2
docker pull fabiojdluz/app-orchestrating:v2
docker run -d --name app-orchestrating -p 83:80 fabiojdluz/app-orchestrating
 