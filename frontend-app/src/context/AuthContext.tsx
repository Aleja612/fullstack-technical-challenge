import { createContext, useContext, useState, type ReactNode } from 'react';

// Tipos de roles
type Role = 'admin' | 'client';

interface AuthContextType {
  role: Role;
  toggleRole: () => void;
  isAdmin: boolean;
}

// Creamos el contexto
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Proveedor
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [role, setRole] = useState<Role>('client'); 

  const toggleRole = () => {
    setRole(prev => (prev === 'client' ? 'admin' : 'client'));
  };

  return (
    <AuthContext.Provider value={{ role, toggleRole, isAdmin: role === 'admin' }}>
      {children}
    </AuthContext.Provider>
  );
};

// 2. CORRECCIÓN LINTER: Agregamos esta línea para silenciar la advertencia de Vite
// Es un estándar aceptado en archivos de Contexto para no tener que separar en 3 archivos distintos.
// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth debe usarse dentro de un AuthProvider');
  return context;
};