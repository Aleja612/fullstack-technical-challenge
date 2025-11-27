import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
// Importamos nuestro proveedor de autenticación
import { AuthProvider } from './context/AuthContext';

// Cliente de React Query para las peticiones al servidor
const queryClient = new QueryClient()

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    {/* 1. Proveedor de Datos (React Query) */}
    <QueryClientProvider client={queryClient}>
      
      {/* 2. Proveedor de Autenticación (Roles) - AQUÍ ESTÁ LA CLAVE */}
      <AuthProvider>
        {/* Ahora App y todos sus hijos tienen acceso al rol */}
        <App />
      </AuthProvider>

    </QueryClientProvider>
  </React.StrictMode>,
)