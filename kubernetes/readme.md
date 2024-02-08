
```bash
## dowload istio 
1. Dowload To Istio
`curl -L https://istio.io/downloadIstio | sh -`
2. change directory
`cd istio-1.20.1`
3. set path of istio
`export PATH=$PWD/bin:$PATH`
4. Install Istio
`istioctl install --set profile=demo -y`
5. get crd
`kubectl get crd gateways.gateway.networking.k8s.io &> /dev/null || { kubectl kustomize "github.com/kubernetes-sigs/gateway-api/config/crd?ref=v1.0.0" | kubectl apply -f -; }`
6. create name space product
`kubectl create namespace product`
7. set product label on istio
`kubectl label namespace product istio-injection=enabled`
set product as context
8. `kubectl config set-context --current --namespace=product`

# Create  key secret to ssl on local machine
1. create directory to record certicates
`mkdir evostack_certs1`

2. Create a root certificate and private key to sign the certificates for your services:
`openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -subj '/O=evostack Inc./CN=evostack.com' -keyout evostack_certs1/evostack.com.key -out evostack_certs1/evostack.com.crt`

3. Generate a certificate and a private key for httpbin.example.com:
`openssl req -out evostack_certs1/evostack.app.com.csr -newkey rsa:2048 -nodes -keyout evostack_certs1/evostack.app.com.key -subj "/CN=evostack.app.com/O=httpbin organization"`

`openssl x509 -req -sha256 -days 365 -CA evostack_certs1/evostack.com.crt -CAkey evostack_certs1/evostack.com.key -set_serial 0 -in evostack_certs1/evostack.app.com.csr -out evostack_certs1/evostack.app.com.crt`

4. generate secret:
`kubectl create -n istio-system secret tls httpbin-credential --key=evostack_certs1/evostack.app.com.key --cert=evostack_certs1/evostack.app.com.crt`


5. export path to source to use any terminal
`export PATH="~/Documents/GitHub/big-data-with-kubernetes/kubernetes/istio-1.20.1/bin"`
6. get ingres_host
`export INGRESS_HOST=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')`
7. get ingress port
`export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')`
8. get secure port 
`export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].port}')`
9. get the gateway url
`export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT`
10. test gateway url
`echo "$GATEWAY_URL"`
11. teste secure ingress port
`echo "$SECURE_INGRESS_PORT"`
12. teste ingress port
`echo "$INGRESS_PORT"`
13. test ingress host
`echo "$ INGRESS_HOST"`

## to get istio ip
1. `kubectl get svc -n istio-system istio-ingressgateway`

15. install prometheus
`kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.20/samples/addons/prometheus.yaml`

16. install kiali 
`kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.20/samples/addons/kiali.yaml`

167. run kiali 
`istioctl dashboard kiali`
# Istio dashboard

istioctl dashboard jaeger
istioctl dashboard prometheus
istioctl dashboard kiali
istioctl dashboard grafana
```

criar tres servicos
e acessos externamente
o servico 1 se comunica com o servico 2 e o servico 2 se comunica com o servico 3
both services has prometheus setting
servico 3 return "hello world servico 3"
servico 1 retain log save "i am servivo 1"
servico 2 return "hello world servico 2"
servico 2 retain log save "i am servivo 2"
servico 1 return "hello world servico 1"
servico 3 retain log save "i am servivo 3"
log de pod should be clear logs from last 24 hs



1. kubectl get crd gateways.gateway.networking.k8s.io &> /dev/null || \
  { kubectl kustomize "github.com/kubernetes-sigs/gateway-api/config/crd?ref=v1.0.0" | kubectl apply -f -; }







curl -v -HHost:httpbin.example.com --resolve "httpbin.example.com:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
  --cacert example_certs1/example.com.crt "https://httpbin.example.com:$SECURE_INGRESS_PORT/status/418"



curl -kv https://app-receiver.com/app-receiver --resolve "app-receiver.com:443:10.110.57.156"
curl -kv https://app-orchestrating.com/app-orchestrating --resolve "app-orchestrating.com:443:10.110.57.156"
curl -kv https://app-sender.com/app-sender --resolve "app-sender.com:443:10.110.57.156"



base64 -w0 evostack_certs1/evostack.app.com.crt
base64 -w0 evostack_certs1/evostack.app.com.key


# Istio dashboard

istioctl dashboard jaeger
istioctl dashboard prometheus
istioctl dashboard kiali
istioctl dashboard grafana