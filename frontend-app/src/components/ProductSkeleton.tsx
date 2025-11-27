export const ProductSkeleton = () => {
  return (
    <div className="bg-white rounded-2xl p-6 border border-gray-100 shadow-sm animate-pulse flex flex-col h-full">
      
      {/* Simulación de Imagen */}
      <div className="h-40 bg-gray-200 rounded-xl mb-4 w-full"></div>
      
      {/* Simulación de Título y Precio */}
      <div className="flex justify-between items-center mb-4">
        <div className="h-6 bg-gray-200 rounded w-1/2"></div>
        <div className="h-6 bg-gray-200 rounded w-16"></div>
      </div>
      
      {/* Simulación de Descripción (2 líneas) */}
      <div className="space-y-2 mb-6 flex-1">
        <div className="h-3 bg-gray-200 rounded w-full"></div>
        <div className="h-3 bg-gray-200 rounded w-4/5"></div>
      </div>
      
      {/* Simulación de Footer (Stock + Botón) */}
      <div className="border-t pt-4 flex justify-between items-center mt-auto">
        <div className="h-4 bg-gray-200 rounded w-20"></div>
        <div className="h-8 bg-gray-200 rounded w-24"></div>
      </div>
    </div>
  );
};