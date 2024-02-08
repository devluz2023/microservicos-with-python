# main.py
from fastapi import FastAPI
from prometheus_fastapi_instrumentator import Instrumentator
from fastapi.openapi.docs import get_swagger_ui_html
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()
app = FastAPI(root_path="/app-orchestrating")
# Instrumentator setup
Instrumentator().instrument(app).expose(app)


app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Allows all origins
    allow_credentials=True,
    allow_methods=["*"],  # Allows all methods
    allow_headers=["*"],  # Allows all headers
)

@app.get("/swagger", include_in_schema=False)
def overridden_swagger():
    return get_swagger_ui_html(openapi_url="/app-orchestrating/openapi.json", title="My API")

@app.get("/")
def read_root():
    return {"Hello": "World app orchestrating"}


@app.get("/orchestrating/hello")
def read_hello():
    return {"Hello": "World from app-orchestrating"}


