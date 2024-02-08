# main.py
from fastapi import FastAPI
from prometheus_fastapi_instrumentator import Instrumentator
from fastapi.openapi.docs import get_swagger_ui_html
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(root_path="/app-sender")
# Instrumentator setup
Instrumentator().instrument(app).expose(app)



import httpx
# receiver_service_url = "http://app-receiver:80/app-receiver/sender"
receiver_service_url = "http://app-receiver:80/app-receiver/sender"


app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Allows all origins
    allow_credentials=True,
    allow_methods=["*"],  # Allows all methods
    allow_headers=["*"],  # Allows all headers
)

@app.get("/swagger", include_in_schema=False)
def overridden_swagger():
    return get_swagger_ui_html(openapi_url="/app-sender/openapi.json", title="My API")

@app.get("/")
def read_root():
    return {"Hello": "World app-sender First endpoint"}


@app.get("/app-sender/hello")
def read_hello():
    return {"Hello": "World from app-sender"}

@app.get("/send-to-receiver")
async def send_to_receiver():
    try:
        async with httpx.AsyncClient() as client:
            response = await client.get(receiver_service_url)
            return response.json()
    except Exception as e:
        return {"error": str(e)}

    
@app.get("/test")
def test_send():
  return {"Test": "World app-sender Test Endpoint!"}

@app.get("/app-sender/send-to-receiver")
async def send_to_receiver_teste_1():
    async with httpx.AsyncClient() as client:
        response = await client.get(receiver_service_url)
        return response.json()
    

@app.get("/app-sender_v2/send-to-receiver")
async def send_to_receiver_teste2():
    async with httpx.AsyncClient() as client:
        response = await client.get(receiver_service_url)
        return response.json()