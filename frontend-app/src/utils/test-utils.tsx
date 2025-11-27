import { render } from '@testing-library/react';
import { AuthProvider } from '../context/AuthContext';
import {type ReactElement } from 'react';

// ELIMINAMOS QueryClientProvider porque estamos mockeando los hooks.
// Esto evita el conflicto de versiones en los tests.

export const renderWithProviders = (ui: ReactElement) => {
  return render(
    <AuthProvider>
      {ui}
    </AuthProvider>
  );
};