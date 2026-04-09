import { useNavigate } from 'react-router-dom'

import { APP_ROUTES } from '../../constants/routes'
import { useAuth } from '../../hooks/useAuth'
import { Button } from '../ui/Button'

export const TopBar = () => {
  const navigate = useNavigate()
  const { logout } = useAuth()

  return (
    <header className="sticky top-0 z-10 flex items-center justify-between border-b border-slate-200 bg-white/90 px-4 py-3 backdrop-blur md:px-8">
      <p className="text-sm font-medium text-slate-700">Smart Parking Management System</p>
      <Button
        variant="secondary"
        onClick={() => {
          logout()
          navigate(APP_ROUTES.login)
        }}
      >
        Logout
      </Button>
    </header>
  )
}


