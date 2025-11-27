import { useState } from 'react';
// Hooks
import { useProducts } from '../hooks/useProducts';
import { useProductMutations } from '../hooks/useProductMutations';
import { useAuth } from '../context/AuthContext';
// Utils
import { getProductImage } from '../utils/imageMapper';

// Componentes
import { StockBadge } from '../components/StockBadge';
import { PurchaseButton } from '../components/PurchaseButton';
import { RestockButton } from '../components/RestockButton';
import { CreateProductModal } from '../components/CreateProductModal';
import { ProductSkeleton } from '../components/ProductSkeleton';
import { RoleSwitcher } from '../components/RoleSwitcher';
import { PurchaseSummary } from '../components/PurchaseSummary'; // <--- Nuevo Import

export const ProductList = () => {
  const { data: products, isLoading, isError } = useProducts(0);
  const { deleteProduct } = useProductMutations();
  const { isAdmin } = useAuth();
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [totalSpent, setTotalSpent] = useState(0); // Estado para el ticket flotante

  // --- HANDLERS ---
  const handleDelete = (id: string) => {
    if (confirm('¿Estás seguro de eliminar este producto permanentemente?')) {
        deleteProduct(id);
    }
  };

  const handlePurchaseSuccess = (amount: number) => {
    setTotalSpent(prev => prev + amount);
  };

  // --- RENDERIZADO ---
  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 p-8">
        <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
            {[...Array(6)].map((_, i) => <ProductSkeleton key={i} />)}
        </div>
      </div>
    );
  }

  if (isError) return <div className="p-10 text-center text-red-500 font-bold text-xl">Error de conexión con el Backend</div>;

  return (
    <div className="min-h-screen bg-gray-50 px-6 py-12 pb-24"> {/* pb-24 para dar espacio al widget flotante */}
      <div className="max-w-7xl mx-auto">
        
        {/* HEADER */}
        <header className="flex flex-col md:flex-row justify-between items-start md:items-center mb-12 gap-4">
            <div>
                <h1 className="text-4xl font-extrabold text-gray-900 tracking-tight flex items-center gap-3">
                    Catálogo <span className="text-blue-600">Productos</span>
                </h1>
                <p className="text-gray-500 mt-2">Gestión profesional de inventario distribuido.</p>
                <div className="mt-4">
                    <RoleSwitcher />
                </div>
            </div>

            {isAdmin && (
                <button 
                    onClick={() => setIsModalOpen(true)}
                    className="bg-gray-900 hover:bg-black text-white px-6 py-3 rounded-xl font-bold shadow-lg transform hover:scale-105 transition-all flex items-center gap-2"
                >
                    <span>+</span> Nuevo Producto
                </button>
            )}
        </header>
        
        {/* GRID PRODUCTOS */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
          {products?.map((product) => (
            <article 
                key={product.id} 
                className="bg-white rounded-2xl shadow-sm hover:shadow-xl transition-all duration-300 border border-gray-100 flex flex-col overflow-hidden group relative"
            >
              {/* Eliminar (Admin Only) */}
              {isAdmin && (
                  <button 
                    onClick={() => handleDelete(product.id)}
                    className="absolute top-3 right-3 z-10 bg-white/90 p-2 rounded-full shadow-md text-gray-400 hover:text-red-500 hover:bg-red-50 transition-all opacity-0 group-hover:opacity-100 backdrop-blur-sm"
                    title="Eliminar producto"
                  >
                    🗑️
                  </button>
              )}

              {/* Imagen Inteligente */}
              <div className="h-48 overflow-hidden bg-gray-100 relative">
                <img 
                    src={getProductImage(product.name)} 
                    alt={product.name}
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                />
              </div>

              {/* Info */}
              <div className="p-6 flex-1 flex flex-col">
                <div className="flex justify-between items-start mb-2">
                    <h3 className="text-lg font-bold text-gray-900 leading-snug">
                        {product.name}
                    </h3>
                </div>
                
                <p className="text-gray-500 text-sm mb-4 line-clamp-2 flex-1">
                    {product.description}
                </p>
                
                <div className="mb-4">
                    <span className="text-2xl font-bold text-gray-900">
                        ${product.price.toLocaleString()}
                    </span>
                </div>

                {/* Acciones */}
                <div className="pt-4 border-t border-gray-50 space-y-4">
                    <div className="flex justify-between items-center bg-gray-50 p-2 rounded-lg">
                        <span className="text-xs font-semibold text-gray-400 uppercase">Inventario</span>
                        <div className="flex items-center gap-2">
                            {isAdmin && <RestockButton productId={product.id} />}
                            <StockBadge productId={product.id} />
                        </div>
                    </div>
                    
                    {isAdmin ? (
                        <div className="w-full py-2 text-center text-xs text-gray-400 font-medium italic border border-dashed border-gray-200 rounded-lg">
                            Vista de Administrador
                        </div>
                    ) : (
                        <PurchaseButton 
                            productId={product.id} 
                            price={product.price}
                            onPurchaseSuccess={handlePurchaseSuccess}
                        />
                    )}
                </div>
              </div>
            </article>
          ))}
        </div>

        {/* Componentes Flotantes / Modales */}
        <CreateProductModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} />
        <PurchaseSummary total={totalSpent} />
      
      </div>
    </div>
  );
};