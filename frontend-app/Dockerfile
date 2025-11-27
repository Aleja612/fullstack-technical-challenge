# ETAPA 1: Build (Construcción)
FROM node:20-alpine AS build
WORKDIR /app

# Copiamos archivos de dependencias
COPY package.json package-lock.json ./
RUN npm install

# Copiamos el código fuente
COPY . .

# Construimos la aplicación para producción (genera carpeta /dist)
RUN npm run build

# ETAPA 2: Servidor Web (Nginx)
FROM nginx:alpine

# Copiamos la configuración de Nginx que creamos
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiamos los archivos compilados de React desde la etapa 1
COPY --from=build /app/dist /usr/share/nginx/html

# Exponemos el puerto 80 (puerto web estándar)
EXPOSE 80

# Nginx arranca solo en primer plano
CMD ["nginx", "-g", "daemon off;"]