import { useStock } from "../hooks/useStock";

export const StockBadge = ({ productId }: { productId: string }) => {
  const { data: stock, isLoading, isError } = useStock(productId);

  if (isLoading) return <span className="text-gray-400 text-xs font-mono">Cargando...</span>;
  
  if (isError) return <span className="text-gray-300 text-xs">Sin info</span>;

  const qty = stock?.quantity || 0;
  const noStock = qty === 0;

  return (
    <span className={`px-2 py-1 rounded text-xs font-bold ${
        noStock ? 'bg-red-100 text-red-700' : 'bg-green-100 text-green-700'
    }`}>
        {noStock ? 'AGOTADO' : `${qty} Unidades`}
    </span>
  );
};