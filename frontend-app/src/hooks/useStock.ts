import { useQuery } from '@tanstack/react-query';
import { inventoryClient } from '../api/client';
import type { InventoryStatus } from '../types/Product'; 

export const useStock = (productId: string) => {
  return useQuery({
    queryKey: ['stock', productId],
    queryFn: async () => {
        
        const data = await inventoryClient.get(`/inventory/${productId}`);
        return data as unknown as InventoryStatus;
    },
    retry: 1, 
  });
};