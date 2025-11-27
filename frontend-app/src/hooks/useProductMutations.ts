import { useMutation, useQueryClient } from '@tanstack/react-query';
import { productClient } from '../api/client';
import type { Product } from '../types/Product';

export const useProductMutations = () => {
  const queryClient = useQueryClient();

  // --- CREAR PRODUCTO ---
  const createMutation = useMutation({
    mutationFn: async (newProduct: Omit<Product, 'id'>) => {
      const { data } = await productClient.post('/products', newProduct);
      return data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['products'] }); // Refresca la lista
      alert("Producto creado con éxito");
    },
    onError: (error) => alert(`Error al crear: ${error}`)
  });

  // --- ELIMINAR PRODUCTO ---
  const deleteMutation = useMutation({
    mutationFn: async (id: string) => {
      await productClient.delete(`/products/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['products'] }); // Refresca la lista
    },
    onError: (error) => alert(`Error al eliminar: ${error}`)
  });

  return { 
    createProduct: createMutation.mutate, 
    deleteProduct: deleteMutation.mutate,
    isCreating: createMutation.isPending,
    isDeleting: deleteMutation.isPending
  };
};