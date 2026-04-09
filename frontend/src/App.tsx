import { QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider } from 'react-router-dom'

import { queryClient } from './app/queryClient'
import { router } from './app/router'
import { ToastViewport } from './components/common/ToastViewport'
import { AuthProvider } from './context/AuthContext'

const App = () => (
  <AuthProvider>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
      <ToastViewport />
    </QueryClientProvider>
  </AuthProvider>
)

export default App
