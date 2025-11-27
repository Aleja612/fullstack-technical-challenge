# 🛒 FullStack Microservices Market

![Java](https://img.shields.io/badge/Java-17-orange?style=flat&logo=java) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-green?style=flat&logo=springboot) ![React](https://img.shields.io/badge/React-18-blue?style=flat&logo=react) ![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat&logo=docker) ![Coverage](https://img.shields.io/badge/Coverage-91%25-brightgreen)

Solución técnica de alto nivel implementada con **Arquitectura de Microservicios**, enfocada en la **robustez transaccional**, la resiliencia y el cumplimiento estricto de estándares (JSON:API).

---

## 📋 Tabla de Contenidos
1. [Arquitectura y Diseño](#-arquitectura-y-diseño)
2. [Stack Tecnológico](#-stack-tecnológico)
3. [Decisiones de Arquitectura y Patrones](#-decisiones-de-arquitectura-y-patrones)
4. [Instalación y Ejecución](#-instalación-y-ejecución)
5. [Estrategia de Testing](#-estrategia-de-testing)
6. [Mejoras Futuras](#-mejoras-futuras-scalability)

---

## 🏗 Arquitectura y Diseño

El proyecto sigue el patrón **Database per Service** para garantizar el desacoplamiento total. La comunicación entre servicios es síncrona (REST) y está protegida por patrones de resiliencia.

### Diagrama de Flujo de Datos

```md
```mermaid
graph TD
    User((Cliente)) -->|HTTP/80| NGINX[Frontend Container]
    NGINX --> FE[React App]
    
    subgraph "Docker Network"
        FE -->|JSON:API| PROD[MS Productos :8081]
        FE -->|JSON:API| INV[MS Inventario :8082]
        
        INV -- Feign Client (Sync) --> PROD  <-- Flecha Simple con texto
        INV -- Fallback --> PROD
        
        PROD --> DB1[(PostgreSQL: Products)]
        INV --> DB2[(PostgreSQL: Inventory)]
    end

## 🛠 Stack Tecnológico

### Backend (Java Ecosystem)
* **Framework:** Spring Boot 3.3.5 (Java 17).
* **Comunicación Inter-servicios:** Spring Cloud OpenFeign.
* **Resiliencia:** Resilience4j (Circuit Breaker & Retry).
* **Persistencia:** Spring Data JPA + PostgreSQL 15.
* **Estándar API:** Implementación estricta de JSON:API.

### Frontend (Modern Web)
* **Core:** React 18 + TypeScript + Vite.
* **Estado del Servidor:** TanStack Query (React Query) para caché e invalidación.
* **Estilos:** TailwindCSS 3.4.
* **UX/Patrones:** Context API (simulación de Roles), Adapter Pattern (Axios Interceptor).
* **Hosting:** Servidor Nginx ultra-ligero.

---

## 🧠 Decisiones de Arquitectura y Patrones

### 1. Modelo de Datos: Elección de SQL
* **Decisión:** Se eligió PostgreSQL para ambos microservicios.
* **Justificación:** Los datos de Producto e Inventario son inherentemente **estructurados y transaccionales**. La integridad referencial y la consistencia son críticas para el manejo de stock, haciendo de SQL la opción más robusta y adecuada para el dominio transaccional.

### 2. Patrón de Arquitectura: Dependencia Controlada
* **Dirección de la Dependencia:** El servicio de Inventario consume al de Productos.
* **Justificación:** Se sigue el principio de **Dominio Core Autónomo**. El Catálogo (Productos) no debe depender de servicios secundarios (Inventario) para evitar acoplamiento y garantizar que el catálogo siga funcionando incluso en fallos de stock.

### 3. Patrones de Control y Resiliencia
* **Circuit Breaker:** Implementado con Resilience4j en InventoryService para proteger contra fallos en cascada, permitiendo la degradación de la respuesta.
* **Integridad Distribuida:** En la escritura (POST/PUT), el InventoryService valida que el producto exista en el catálogo remoto antes de modificar el inventario, previniendo datos huérfanos.

### 4. Patrones de Código Relevantes
* **DTO Pattern:** Separación estricta de Entidades JPA de los objetos de transferencia (CreateRequest, ResponseDTO) para desacoplar la API del esquema de la base de datos.
* **Adapter Pattern (Frontend):** Uso de un Interceptor en Axios para normalizar las respuestas anidadas de JSON:API a objetos planos de JavaScript, manteniendo los componentes de React limpios.

---

## 🚀 Instalación y Ejecución

La aplicación está diseñada para levantarse con un solo comando en cualquier entorno con Docker.

### Prerrequisitos
* Docker y Docker Compose instalados.

### Pasos
1.  **Clonar el repositorio:**
    ```bash
    git clone <TU_URL_DEL_REPO>
    cd fullstack-challenge
    ```
2.  **Desplegar la solución:**
    ```bash
    docker-compose up -d --build
    ```
    *Nota: El Data Seeder cargará productos iniciales automáticamente al iniciar.*

### Acceder
* **Frontend (Tienda):** http://localhost:5173
* **Swagger Productos:** http://localhost:8081/swagger-ui.html

---

## 🧪 Estrategia de Testing

El enfoque fue pragmático debido a la limitación de tiempo, priorizando el blindaje de la lógica de negocio.

### Backend (JUnit 5 + Mockito)
* **Enfoque:** Unit Testing exhaustivo en la capa de Servicios (ProductService, InventoryService) y Filtros de Seguridad.
* **Logro:** Cobertura >90% en la lógica de negocio, validando creación, eliminación, y flujos de resiliencia (Circuit Breaker).
* **Sacrificio:** Se omitió la implementación de tests de controlador y la integración de tests de Frontend, priorizando la entrega de la funcionalidad End-to-End.

### Mejoras Futuras (Scalability)
* **API Gateway / BFF:** Implementar una capa de agregación (Gateway) para unificar la URL y manejar la autenticación (JWT) centralizadamente.
* **Event-Driven Architecture (EDA):** Migrar la notificación de cambios de inventario a un broker de mensajes (Kafka/RabbitMQ) para desacoplar el proceso de compra de los sistemas de envío y analítica.
* **Seguridad Centralizada:** Reemplazar la validación de API Key por un sistema de autenticación moderno (JWT/OAuth2).