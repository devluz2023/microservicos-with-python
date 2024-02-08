cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  name: mygateway
spec:
  selector:
    istio: ingressgateway # use istio default ingress gateway
  servers:
  - port:
      number: 443
      name: https
      protocol: HTTPS
    tls:
      mode: SIMPLE
      credentialName: httpbin-credential # must be the same as secret
    hosts:
    - httpbin.example.com
EOF


cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: httpbin
spec:
  hosts:
  - "httpbin.example.com"
  gateways:
  - mygateway
  http:
  - match:
    - uri:
        prefix: /status
    - uri:
        prefix: /delay
    route:
    - destination:
        port:
          number: 8000
        host: httpbin
EOF


cat <<EOF | kubectl apply -f -
apiVersion: gateway.networking.k8s.io/v1beta1
kind: Gateway
metadata:
  name: mygateway
  namespace: istio-system
spec:
  gatewayClassName: istio
  listeners:
  - name: https-httpbin
    hostname: "httpbin.example.com"
    port: 443
    protocol: HTTPS
    tls:
      mode: Terminate
      certificateRefs:
      - name: httpbin-credential
    allowedRoutes:
      namespaces:
        from: Selector
        selector:
          matchLabels:
            kubernetes.io/metadata.name: default
  - name: https-helloworld
    hostname: "helloworld.example.com"
    port: 443
    protocol: HTTPS
    tls:
      mode: Terminate
      certificateRefs:
      - name: helloworld-credential
    allowedRoutes:
      namespaces:
        from: Selector
        selector:
          matchLabels:
            kubernetes.io/metadata.name: default
EOF


cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  name: mygateway
spec:
  selector:
    istio: ingressgateway # use istio default ingress gateway
  servers:
  - port:
      number: 443
      name: https-httpbin
      protocol: HTTPS
    tls:
      mode: SIMPLE
      credentialName: httpbin-credential
    hosts:
    - httpbin.example.com
  - port:
      number: 443
      name: https-helloworld
      protocol: HTTPS
    tls:
      mode: SIMPLE
      credentialName: helloworld-credential
    hosts:
    - helloworld.example.com
EOF


cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: helloworld
spec:
  hosts:
  - helloworld.example.com
  gateways:
  - mygateway
  http:
  - match:
    - uri:
        exact: /hello
    route:
    - destination:
        host: helloworld
        port:
          number: 5000
EOF



cat <<EOF | kubectl apply -f -
apiVersion: gateway.networking.k8s.io/v1beta1
kind: Gateway
metadata:
  name: mygateway
  namespace: istio-system
spec:
  gatewayClassName: istio
  listeners:
  - name: https
    hostname: "httpbin.example.com"
    port: 443
    protocol: HTTPS
    tls:
      mode: Terminate
      certificateRefs:
      - name: httpbin-credential
      options:
        gateway.istio.io/tls-terminate-mode: MUTUAL
    allowedRoutes:
      namespaces:
        from: Selector
        selector:
          matchLabels:
            kubernetes.io/metadata.name: default
EOF


cat <<EOF | kubectl apply -f -
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  name: mygateway
spec:
  selector:
    istio: ingressgateway # use istio default ingress gateway
  servers:
  - port:
      number: 443
      name: https
      protocol: HTTPS
    tls:
      mode: MUTUAL
      credentialName: httpbin-credential # must be the same as secret
    hosts:
    - httpbin.example.com
EOF



curl -v -HHost:httpbin.example.com --resolve "httpbin.example.com:$SECURE_INGRESS_PORT:$INGRESS_HOST" \
  --cacert example_certs2/example.com.crt "https://httpbin.example.com:$SECURE_INGRESS_PORT/status/418"


istioctl proxy-config listeners istio-ingressgateway-app-sender-v1-6ddc76cfc6-ss5s6 -n istio-system




curl localhost:80/app-receiver/hello


curl -kv https://app-receiver.com/app-receiver/app-receiver/hello --resolve "app-receiver.com:443:10.110.57.156"



kubectl exec -n product -it app-receiver-v1-86f8cfd548-hxpvv  -- curl -v http://app-receiver:80/app-receiver/hello



istioctl dashboard jaeger
istioctl dashboard prometheus
istioctl dashboard kiali
istioctl dashboard grafana
istioctl dashboard loki


istioctl dashboard jaeger &
istioctl dashboard prometheus &
istioctl dashboard kiali &
istioctl dashboard grafana &
istioctl dashboard loki &



base64 -w 0 evostack_certs1/evostack.app.com.crt > evostack_certs1/evostack.app.crt.txt
base64 -w 0 evostack_certs1/evostack.app.com.key > evostack_certs1/evostack.app.key.txt



curl -k https:/10.103.145.118.com/orchestrating/hello





fabio@fabio-VJF155F11X-B0311B:~/Documents/GitHub/big-data-with-kubernetes/kubernetes$ kubectl get virtualservice -n product
NAME                                              GATEWAYS                       HOSTS                                  AGE
app-internal-application-gateway-virtualservice   ["istio-system/app-gateway"]   ["internal-application-gateway.com"]   3h35m
app-orchestrating-virtualservice                  ["istio-system/app-gateway"]   ["app-orchestrating.com"]              3h35m
app-receiver-virtualservice                       ["istio-system/app-gateway"]   ["app-receiver.com"]                   3h35m
app-sender-virtualservice                         ["istio-system/app-gateway"]   ["app-sender.com"]                     3h35m
fabio@fabio-VJF155F11X-B0311B:~/Documents/GitHub/big-data-with-kubernetes/kubernetes$ kubectl get gateway -n istio-system
NAME                               CLASS   ADDRESS          PROGRAMMED   AGE
app-gateway                        istio   10.103.172.112   True         3h35m
app-internal-application-gateway   istio   10.109.244.246   True         3h35m
app-orchestrating-gateway          istio   10.107.80.174    True         3h35m
app-receiver-gateway               istio   10.97.247.255    True         3h35m
app-sender-gateway                 istio   10.110.167.127   True         3h35m
fabio@fabio-VJF155F11X-B0311B:~/Documents/GitHub/big-data-with-kubernetes/kubernetes$ kubectl get pod -n product
NAME                                               READY   STATUS             RESTARTS      AGE
app-orchestrating-v1-5ccd654df8-94bbk              1/1     Running            1 (46m ago)   4h2m
app-receiver-v1-86f8cfd548-sf4l9                   1/1     Running            1 (46m ago)   4h2m
app-sender-v1-6ddc76cfc6-d8w9h                     1/1     Running            1 (46m ago)   4h2m
internal-application-gateway-v1-56b4845ff4-hsc8c   0/1     Running             1 (46m ago)   4h2m
fabio@fabio-VJF155F11X-B0311B:~/Documents/GitHub/big-data-with-kubernetes/kubernetes$ kubectl get svc -n product
NAME                           TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)   AGE
app-orchestrating              ClusterIP   10.96.252.168    <none>        80/TCP    4h3m
app-receiver                   ClusterIP   10.102.97.219    <none>        80/TCP    4h3m
app-sender                     ClusterIP   10.110.189.129   <none>        80/TCP    4h3m
internal-application-gateway   ClusterIP   10.104.190.203   <none>        80/TCP    4h3m
fabio@fabio-VJF155F11X-B0311B:~/Documents/GitHub/big-data-with-kubernetes/kubernetes$ kubectl get svc -n istio-system
NAME                                     TYPE           CLUSTER-IP       EXTERNAL-IP      PORT(S)                                                                      AGE
app-gateway-istio                        LoadBalancer   10.103.172.112   10.103.172.112   15021:30151/TCP,443:32220/TCP                                                3h42m
app-internal-application-gateway-istio   LoadBalancer   10.109.244.246   10.109.244.246   15021:31959/TCP,443:30623/TCP                                                3h42m
app-orchestrating                        ClusterIP      10.109.122.214   <none>           80/TCP                                                                       4h3m
app-orchestrating-gateway-istio          LoadBalancer   10.107.80.174    10.107.80.174    15021:31719/TCP,443:31618/TCP                                                3h42m
app-receiver                             ClusterIP      10.111.250.112   <none>           80/TCP                                                                       4h3m
app-receiver-gateway-istio               LoadBalancer   10.97.247.255    10.97.247.255    15021:32506/TCP,443:32116/TCP                                                3h42m
app-sender                               ClusterIP      10.103.93.22     <none>           80/TCP                                                                       4h3m
app-sender-gateway-istio                 LoadBalancer   10.110.167.127   10.110.167.127   15021:31899/TCP,443:31163/TCP                                                3h42m
grafana                                  ClusterIP      10.97.95.69      <none>           3000/TCP                                                                     43h
internal-application-gateway             ClusterIP      10.103.156.207   <none>           80/TCP                                                                       4h3m
istio-egressgateway                      ClusterIP      10.97.10.227     <none>           80/TCP,443/TCP                                                               44h
istio-ingressgateway                     LoadBalancer   10.103.145.118   10.103.145.118   15021:30735/TCP,80:31534/TCP,443:31985/TCP,31400:32636/TCP,15443:31014/TCP   44h
istiod                                   ClusterIP      10.106.36.56     <none>           15010/TCP,15012/TCP,443/TCP,15014/TCP                                        44h
jaeger-collector                         ClusterIP      10.105.67.148    <none>           14268/TCP,14250/TCP,9411/TCP,4317/TCP,4318/TCP                               43h
loki-headless                            ClusterIP      None             <none>           3100/TCP                                                                     43h
prometheus                               ClusterIP      10.111.155.121   <none>           9090/TCP                                                                     43h
tracing                                  ClusterIP      10.101.81.253    <none>           80/TCP,16685/TCP                                                             43h
zipkin                                   ClusterIP      10.110.255.237   <none>           9411/TCP                                                                     43h
fabio@fabio-VJF155F11X-B0311B:~/Documents/GitHub/big-data-with-kubernetes/kubernetes$ 



10.103.145.118