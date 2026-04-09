import { Navigate, Outlet } from 'react-router-dom'

import { APP_ROUTES } from '../../constants/routes'
import { useAuth } from '../../hooks/useAuth'

export const ProtectedRoute = () => {
  const { isAuthenticated } = useAuth()
  if (!isAuthenticated) {
    return <Navigate to={APP_ROUTES.login} replace />
  }
  return <Outlet />
}

