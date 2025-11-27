import { useQuery } from '@tanstack/react-query';
import { productClient } from '../api/client';
import type { Product } from '../types/Product'; 

export const useProducts = (page = 0) => {
  return useQuery({
    queryKey: ['products', page],
    queryFn: async () => {
      // CORRECCIÓN 2: El interceptor ya devuelve el array limpio, no un objeto { data: ... }
      // Usamos 'as unknown as Product[]' para forzar a TypeScript a entenderlo.
      const response = await productClient.get(`/products?page=${page}&size=100`);
      return response as unknown as Product[]; 
    }
  });
};