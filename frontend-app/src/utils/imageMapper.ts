// Mapeo de palabras clave a imágenes reales de Unsplash
export const getProductImage = (productName: string): string => {
  const name = productName.toLowerCase();

  if (name.includes('macbook')) return 'https://images.unsplash.com/photo-1517336714731-489689fd1ca4?q=80&w=1000&auto=format&fit=crop';
  if (name.includes('iphone')) return 'https://images.unsplash.com/photo-1696446701796-da61225697cc?q=80&w=1000&auto=format&fit=crop';
  if (name.includes('sony') || name.includes('xm5')) return 'https://images.unsplash.com/photo-1610986602538-431d65df4385?q=80&w=1000&auto=format&fit=crop';
  if (name.includes('monitor') || name.includes('dell')) return 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?q=80&w=1000&auto=format&fit=crop';
  if (name.includes('keychron') || name.includes('teclado')) return 'https://images.unsplash.com/photo-1595225476474-87563907a212?q=80&w=1000&auto=format&fit=crop';
  
  // Default (Tecnología genérica)
  return 'https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=1000&auto=format&fit=crop';
};