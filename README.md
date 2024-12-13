# Quick Start for a Highly Available React + spring boot + PostgreSQL on Kubernetes

## Summary

This project showcases a robust three-tier application architecture designed for high availability and scalability on Kubernetes. It consists of a **React front-end** built with **Vite**, a **Spring Boot API server**, and a **Cloud Native PostgreSQL database** with three instances (one primary and two replicas). The database is managed using the Cloud Native PostgreSQL operator, which provides two services: one for read/write operations and another for read-only operations. To ensure efficient database traffic handling, a **Pgpool** instance is used for load balancing.

The front-end application was built following the [Tailwind CSS Guide for Vite](https://tailwindcss.com/docs/guides/vite), with a slight modification to the `vite.config.js` file: the base path was set to `"/app/"` to align with the Ingress sub-path configuration. This ensures proper routing within the Kubernetes environment.

The Spring Boot API exposes two endpoints: 
1. `/podname` - Returns the pod name of the API server handling the request (demonstrating load balancing across three replicas).  
2. `/users` - Fetches user data from the database.

During initialization, a single user record is added to the database using the `@PostConstruct` annotation.

The React front-end features a single index page:
- Displays the pod names of its deployment (with three replicas).  
- Includes a button to call the `/podname` API and display API server pod names.  
- Includes a button to fetch user data via the `/users` API.

For more details, refer to:  
- [Pgpool Kubernetes documentation](https://www.pgpool.net/docs/pgpool-II-4.2.7/en/html/example-kubernetes.html)  
- [Cloud Native PostgreSQL Quickstart](https://cloudnative-pg.io/documentation/current/quickstart/).

---

## Installation Steps

### Step 1: Install Ingress Controller

The ingress controller is necessary to manage HTTP and HTTPS traffic in Kubernetes. It provides routing capabilities for the services exposed by the application.

Run the following command to apply the ingress controller:

```bash
kubectl apply -f 1.k8s-ingress-cloudnative-pg-operator/ingress-controller.yaml
```
### Step 2: Install Cloud Native PostgreSQL Operator

```bah
kubectl apply --server-side -f 1.k8s-ingress-cloudnative-pg-operator/cnpg-1.24.1.yaml
```

### Step 3: Deploy Cloud Native PostgreSQL Cluster
```bash
kubectl apply -f 2.cloudnative-pg/Cluster.yaml
```

### Step 4: Set up Pgpool

```bash
kubectl apply -f 3.pgpool-pg/deployment.yaml
```
### Step 5: Deploy Spring Boot API Server
```bash
kubectl apply -f SpringbootApiServer/k8s/Deployment.yaml
kubectl apply -f SpringbootApiServer/k8s/Service.yaml
kubectl apply -f SpringbootApiServer/k8s/Ingress.yaml
```
### Step 6: Deploy React Front-end
```bash
kubectl apply -f react-k8s-print-pod-name/k8s/deployment.yaml
kubectl apply -f react-k8s-print-pod-name/k8s/Service.yaml
kubectl apply -f react-k8s-print-pod-name/k8s/react-app-ingress.yaml

```
## Database Operations and Credentials

### Port-forward Pgpool to Host System
```bash
kubectl port-forward svc/pgpool 9999:9999
```
### Port-forward Cloud Native PostgreSQL Directly
```bash
kubectl port-forward svc/pg-db-rw 5432:5432
```
### Log in to PostgreSQL Pod
```bash
kubectl exec -it pg-db-1 -- /bin/bash
```
### Retrieve PostgreSQL Password
```bash
kubectl get secret pg-db-app -o jsonpath='{.data.password}' | base64 --decode
```
### Retrieve PostgreSQL Username
```bash
kubectl get secret pg-db-app -o jsonpath='{.data.username}' | base64 --decode
```
### Describe the Secret to Get Full Credentials
```bash
kubectl describe secret pg-db-app
```
example output like :

```bash
Name:         pg-db-app
Namespace:    default
Labels:       cnpg.io/cluster=pg-db
              cnpg.io/reload=true
Annotations:  cnpg.io/operatorVersion: 1.24.1

Type:  kubernetes.io/basic-auth

Data
====
dbname:    3 bytes
host:      8 bytes
jdbc-uri:  126 bytes
pgpass:    87 bytes
uri:       107 bytes
password:  64 bytes
port:      4 bytes
user:      3 bytes
username:  3 bytes
```

### Logs db server
```bash
kubectl logs -f pg-db-1
```

### successful deployment

```bash
 kubectl get pods 

NAME                                                READY   STATUS    RESTARTS      AGE
pg-db-1                                             1/1     Running   2 (82m ago)   14h
pg-db-2                                             1/1     Running   2 (82m ago)   14h
pg-db-3                                             1/1     Running   2 (82m ago)   14h
pgpool-57d8d457f6-htkvz                             1/1     Running   2 (82m ago)   14h
react-k8s-app-677dcd487c-jj7fr                      1/1     Running   2 (82m ago)   14h
react-k8s-app-677dcd487c-lp42t                      1/1     Running   2 (82m ago)   14h
react-k8s-app-677dcd487c-qqlc4                      1/1     Running   2 (82m ago)   14h
springboot-api-server-deployment-667c699bb8-7db4p   1/1     Running   5 (81m ago)   14h
springboot-api-server-deployment-667c699bb8-8ghxv   1/1     Running   5 (81m ago)   14h
springboot-api-server-deployment-667c699bb8-d9xrv   1/1     Running   6 (81m ago)   14h
```

svc:

```bash
kubectl get svc
NAME                            TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
kubernetes                      ClusterIP   10.96.0.1        <none>        443/TCP    5d16h
pg-db-r                         ClusterIP   10.103.184.135   <none>        5432/TCP   14h
pg-db-ro                        ClusterIP   10.109.24.164    <none>        5432/TCP   14h
pg-db-rw                        ClusterIP   10.96.90.213     <none>        5432/TCP   14h
pgpool                          ClusterIP   10.111.187.245   <none>        9999/TCP   14h
react-k8s-app-service           ClusterIP   10.107.190.230   <none>        80/TCP     14h
springboot-api-server-service   ClusterIP   10.110.115.230   <none>        80/TCP     14h

```

## Build and Push Docker Images

To build and push the Docker images for both the React UI and the Spring Boot API server, follow these steps:

### 1. Build and Push the Docker Image for React Front-End
First, log in to Docker:

```bash
docker login
```
Next, build the Docker image for the React front-end application(use your username insted of mine):
```bash
sudo docker build -t cvsudeep/react-k8s-print-pod-name:latest .
```
Push the built image to Docker Hub:
```bash
sudo docker push cvsudeep/react-k8s-print-pod-name:latest
```

### 2. Build and Push the Docker Image for Spring Boot API Server

Set your Docker username and password as environment variables:
```bash
export DOCKER_USERNAME=cvsudeep
export DOCKER_PASSWORD=your_docker_password
```
Build the Spring Boot application image:

```bash
mvn spring-boot:build-image -DskipTests
```
Push the built image to Docker Hub:
```bash
sudo docker push cvsudeep/spring-boot-api-server:latest
```

## Testing the Application

![alt text](<Screenshot 2024-12-13 at 12.50.51 PM.png>)

1.Access the React front-end via the Ingress URL  ([http://localhost/app/](http://localhost/app/)).

2.Verify pod names displayed from the React deployment.

3.Click the "Get API Server" button to fetch and display API server pod names.

4.Click the "Get Data from Database" button to retrieve and display user data from the database.