import { useAuth } from "../context/AuthContext";

export const RoleSwitcher = () => {
  const { role, toggleRole } = useAuth();

  return (
    <button 
      onClick={toggleRole}
      className={`
        flex items-center gap-2 px-4 py-2 rounded-full font-bold text-xs uppercase tracking-wider transition-all shadow-sm border
        ${role === 'admin' 
            ? 'bg-gray-900 text-white border-gray-900' 
            : 'bg-white text-gray-500 border-gray-200 hover:border-gray-300'}
      `}
    >
      <div className={`w-2 h-2 rounded-full ${role === 'admin' ? 'bg-green-400' : 'bg-gray-300'}`}></div>
      {role === 'admin' ? 'Modo Admin' : 'Modo Cliente'}
    </button>
  );
};