import axios from 'axios';

// Cliente para PRODUCTOS (Puerto 8081)
export const productClient = axios.create({
    baseURL: 'http://localhost:8081/api/v1',
    headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': 'secret-key-123'
    }
});

// Cliente para INVENTARIO (Puerto 8082)
export const inventoryClient = axios.create({
    baseURL: 'http://localhost:8082/api/v1',
    headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': 'secret-key-123'
    }
});

// Limpiador de respuestas JSON:API
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const responseCleaner = (response: any) => {
    // Validación de seguridad por si viene vacío
    if (!response.data || !response.data.data) return response.data;

    const { data } = response.data;
    
    // CASO 1: La respuesta es una lista estándar JSON:API (Array directo)
    if (Array.isArray(data)) {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        return data.map((item: any) => ({
            id: item.id,
            ...item.attributes
        }));
    }
    
    // CASO 2: La lista viene dentro de 'attributes' (Paginación simplificada Java)
    if (data.attributes && Array.isArray(data.attributes)) {
        return data.attributes; // Devolvemos el array limpio directamente
    }

    // CASO 3: Es un objeto único (Detalle de producto)
    if (data) {
        return {
            id: data.id,
            ...data.attributes
        };
    }
    
    return response.data;
};

productClient.interceptors.response.use(responseCleaner);
inventoryClient.interceptors.response.use(responseCleaner);