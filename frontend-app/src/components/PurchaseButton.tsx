import { useState } from "react";
import { useStock } from "../hooks/useStock";
import { usePurchase } from "../hooks/usePurchase";

interface Props {
  productId: string;
  price: number; // Nuevo: Para calcular el total gastado
  onPurchaseSuccess: (amount: number) => void; // Nuevo: Callback para avisar al padre
}

export const PurchaseButton = ({ productId, price, onPurchaseSuccess }: Props) => {
  const [quantity, setQuantity] = useState(1);
  
  const { data: stock, isLoading: isStockLoading } = useStock(productId);
  const { mutate, isPending } = usePurchase();

  const currentStock = stock?.quantity || 0;
  const isOutOfStock = currentStock === 0;

  // --- LÓGICA DE CANTIDAD ---
  const increase = () => {
    if (quantity < currentStock) setQuantity(q => q + 1);
  };

  const decrease = () => {
    if (quantity > 1) setQuantity(q => q - 1);
  };

  // --- LÓGICA DE COMPRA ---
  const handleBuy = () => {
    if (quantity > currentStock) return;
    
    // Enviamos negativo para restar
    mutate({ productId, quantity: -quantity }, {
        onSuccess: () => {
            // 1. Avisamos al componente padre cuánto dinero se gastó
            const totalCost = price * quantity;
            onPurchaseSuccess(totalCost);

            // 2. Reiniciamos el selector
            setQuantity(1);
        }
    });
  };

  // --- RENDERIZADO ---
  if (isStockLoading) return <div className="h-10 w-full bg-gray-100 rounded animate-pulse"></div>;

  if (isOutOfStock) {
    return (
      <div className="w-full py-2 bg-gray-100 text-gray-400 text-center text-sm font-medium rounded cursor-not-allowed border border-gray-200">
        Sin Stock
      </div>
    );
  }

  return (
    <div className="flex flex-col gap-2">
      {/* Selector - + */}
      <div className="flex items-center justify-between border border-gray-300 rounded-lg overflow-hidden">
        <button 
            onClick={decrease}
            disabled={quantity <= 1 || isPending}
            className="px-3 py-1 bg-gray-50 hover:bg-gray-200 disabled:opacity-50 text-gray-600 font-bold transition-colors"
        >
            -
        </button>
        
        <span className="text-sm font-semibold text-gray-800 w-8 text-center">
            {quantity}
        </span>
        
        <button 
            onClick={increase}
            disabled={quantity >= currentStock || isPending}
            className="px-3 py-1 bg-gray-50 hover:bg-gray-200 disabled:opacity-50 text-gray-600 font-bold transition-colors"
        >
            +
        </button>
      </div>

      {/* Botón Comprar */}
      <button 
        onClick={handleBuy}
        disabled={isPending}
        className={`w-full py-2 px-4 rounded-lg text-sm font-medium text-white shadow-sm transition-all active:scale-95
          ${isPending 
            ? 'bg-blue-400 cursor-wait' 
            : 'bg-blue-600 hover:bg-blue-700 hover:shadow-md'}
        `}
      >
        {isPending ? 'Procesando...' : `Comprar (${quantity})`}
      </button>
    </div>
  );
};