# Quick Start for a Highly Available Application on Kubernetes

## Summary

This project showcases a robust three-tier application architecture designed for high availability and scalability on Kubernetes. It consists of a React front-end built with Vite, a Spring Boot API server, and a Cloud Native PostgreSQL database with three instances (one primary and two replicas). The database is managed using the Cloud Native PostgreSQL operator, which provides two services: one for read/write operations and another for read-only operations. To ensure efficient database traffic handling, a Pgpool instance is used for load balancing.

The front-end application was built following the [Tailwind CSS Guide for Vite](https://tailwindcss.com/docs/guides/vite), with a slight modification to the `vite.config.js` file: the base path was set to `"/app/"` to align with the Ingress sub-path configuration. This ensures proper routing within the Kubernetes environment.

The Spring Boot API exposes two endpoints: `/podname`, which returns the pod name of the API server handling the request (demonstrating load balancing across three replicas), and `/users`, which fetches user data from the database. During initialization, a single user record is added to the database using the `@PostConstruct` annotation.

The React front-end features a single index page displaying the pod names of its deployment (with three replicas), a button to call the `/podname` API and display API server pod names, and another button to fetch user data via the `/users` API. For more details, refer to the [Pgpool Kubernetes documentation](https://www.pgpool.net/docs/pgpool-II-4.2.7/en/html/example-kubernetes.html) and the [Cloud Native PostgreSQL Quickstart](https://cloudnative-pg.io/documentation/current/quickstart/).

## Architecture Overview

1. **Cloud Native PostgreSQL**:
   - Managed by the Cloud Native PostgreSQL operator.
   - Exposes two services:
     - **Read/Write Service**: Handles transactional operations.
     - **Read-Only Service**: Optimized for read-heavy workloads.
   - A **Pgpool instance** ensures load balancing between database replicas for optimal performance. Refer to the [Pgpool Kubernetes documentation](https://www.pgpool.net/docs/pgpool-II-4.2.7/en/html/example-kubernetes.html) for configuration details.

2. **Spring Boot API Server**:
   - Two APIs are exposed:
     - `/podname`: Returns the name of the pod handling the request.
     - `/users`: Fetches user data from the PostgreSQL database.
   - A single user record is initialized in the database using the `@PostConstruct` annotation.
   - Deployed with three replicas for high availability.

3. **React Front-end**:
   - Built using the [Tailwind CSS Guide for Vite](https://tailwindcss.com/docs/guides/vite), with the following modification:
     - The `base` path in `vite.config.js` is set to `"/app/"` to integrate with the Kubernetes Ingress sub-path configuration.
   - Features:
     - Displays pod names of the React front-end deployment.
     - A button to call `/podname` API and display API server pod names.
     - Another button to fetch and display user data via the `/users` API.

## Getting Started

### Prerequisites
- Docker Desktop with Kubernetes enabled (tested on macOS).
- Kubernetes tools like `kubectl` installed.
- Ingress controller and Cloud Native PostgreSQL operator installed:
  - Follow the [Cloud Native PostgreSQL Quickstart](https://cloudnative-pg.io/documentation/current/quickstart/).

### Installation Steps

1. **Set up Ingress Controller**:
   - Deploy the ingress controller using the `1.k8s-ingress-cloudnative-pg-operator/ingress-controller.yaml` file.

2. **Install Cloud Native PostgreSQL Operator**:
   - Deploy the operator using the `1.k8s-ingress-cloudnative-pg-operator/cnpg-1.24.1.yaml` file.

3. **Deploy Cloud Native PostgreSQL Cluster**:
   - Apply the configuration in `2.cloudnative-pg/Cluster.yaml`.

4. **Set up Pgpool**:
   - Deploy the Pgpool instance using the `3.pgpool-pg/deployment.yaml` file.
   - For detailed configuration, refer to the [Pgpool Kubernetes documentation](https://www.pgpool.net/docs/pgpool-II-4.2.7/en/html/example-kubernetes.html).

5. **Deploy Spring Boot API Server**:
   - Use the YAML files in `SpringbootApiServer/k8s` to deploy the backend:
     - `Deployment.yaml`
     - `Service.yaml`
     - `Ingress.yaml`

6. **Deploy React Front-end**:
   - Use the files in `react-k8s-print-pod-name/k8s` to deploy the front-end:
     - `deployment.yaml`
     - `Service.yaml`
     - `react-app-ingress.yaml`

### Testing the Application

1. Access the React front-end via the Ingress URL (`/app/`).
2. Verify pod names displayed from the React deployment.
3. Click the "Get API Server Pod Names" button to fetch and display pod names from the API server.
4. Click the "Fetch User Data" button to retrieve and display user data from the database.

## Additional References

- [Cloud Native PostgreSQL Quickstart](https://cloudnative-pg.io/documentation/current/quickstart/)
- [Pgpool Kubernetes Documentation](https://www.pgpool.net/docs/pgpool-II-4.2.7/en/html/example-kubernetes.html)
