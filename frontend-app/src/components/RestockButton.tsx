import { usePurchase } from "../hooks/usePurchase"; // Reusamos el mismo hook de conexión

export const RestockButton = ({ productId }: { productId: string }) => {
  const { mutate, isPending } = usePurchase();

  const handleRestock = () => {
    // Enviamos una cantidad POSITIVA (50) para sumar al stock
    mutate({ productId, quantity: 50 }, {
        onSuccess: () => {
            // Opcional: Podrías poner un toast/alerta aquí
            console.log("Inventario reabastecido");
        }
    });
  };

  return (
    <button 
        onClick={handleRestock}
        disabled={isPending}
        title="Reabastecer +50 unidades"
        className="text-xs font-bold text-blue-600 hover:text-blue-800 bg-blue-50 px-2 py-1 rounded hover:bg-blue-100 transition-colors disabled:opacity-50"
    >
        {isPending ? '...' : '+ Stock'}
    </button>
  );
};