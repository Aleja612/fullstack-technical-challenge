export const PurchaseSummary = ({ total }: { total: number }) => {
  if (total === 0) return null;

  return (
    <div className="fixed bottom-6 right-6 bg-gray-900 text-white p-4 rounded-xl shadow-2xl flex items-center gap-4 z-50 animate-bounce-in border border-gray-700">
      <div className="bg-green-500 p-2 rounded-full shadow-lg">
        💰
      </div>
      <div>
        <p className="text-xs text-gray-400 font-bold uppercase tracking-wide">Total Gastado</p>
        <p className="text-xl font-bold font-mono">${total.toLocaleString()}</p>
      </div>
    </div>
  );
};