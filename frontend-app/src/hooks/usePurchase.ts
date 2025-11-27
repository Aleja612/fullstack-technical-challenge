import { useMutation, useQueryClient } from '@tanstack/react-query';
import { inventoryClient } from '../api/client';

export const usePurchase = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ productId, quantity }: { productId: string; quantity: number }) => {
      // Enviamos cantidad negativa para RESTAR stock
      const response = await inventoryClient.post(`/inventory/${productId}`, {
        quantity: quantity 
      });
      return response.data;
    },
    onSuccess: (_, variables) => {
      // Invalida la query 'stock' para recargar el dato
      queryClient.invalidateQueries({ queryKey: ['stock', variables.productId] });
    },
    onError: (error) => {
      console.error("Error en la compra:", error);
      alert("No se pudo realizar la compra.");
    }
  });
};