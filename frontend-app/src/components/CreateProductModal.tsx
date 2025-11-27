import { useState } from 'react';
import { useProductMutations } from '../hooks/useProductMutations';

export const CreateProductModal = ({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) => {
  const { createProduct, isCreating } = useProductMutations();
  const [form, setForm] = useState({ name: '', description: '', price: '' });

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    createProduct({
        name: form.name,
        description: form.description,
        price: parseFloat(form.price)
    }, {
        onSuccess: () => {
            setForm({ name: '', description: '', price: '' });
            onClose();
        }
    });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50 backdrop-blur-sm">
      <div className="bg-white p-8 rounded-2xl shadow-2xl w-full max-w-md border border-gray-100">
        <h2 className="text-2xl font-bold mb-6 text-gray-800">✨ Nuevo Producto</h2>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
            <input 
              required
              className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition"
              value={form.name}
              onChange={e => setForm({...form, name: e.target.value})}
              placeholder="Ej. Laptop Gamer"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Precio</label>
            <input 
              required
              type="number"
              className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
              value={form.price}
              onChange={e => setForm({...form, price: e.target.value})}
              placeholder="0.00"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Descripción</label>
            <textarea 
              className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
              value={form.description}
              onChange={e => setForm({...form, description: e.target.value})}
              placeholder="Detalles del producto..."
            />
          </div>

          <div className="flex justify-end gap-3 mt-6">
            <button type="button" onClick={onClose} className="px-4 py-2 text-gray-500 hover:bg-gray-100 rounded-lg">Cancelar</button>
            <button 
                type="submit" 
                disabled={isCreating}
                className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium disabled:opacity-50"
            >
              {isCreating ? 'Guardando...' : 'Crear Producto'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};