import { Link } from 'react-router-dom'

import { Button } from '../components/ui/Button'
import { APP_ROUTES } from '../constants/routes'

export const NotFoundPage = () => (
  <div className="flex min-h-[60vh] flex-col items-center justify-center gap-3">
    <p className="text-sm uppercase tracking-wide text-slate-500">404</p>
    <h1 className="text-2xl font-semibold text-slate-900">Page not found</h1>
    <p className="text-sm text-slate-600">The page you requested does not exist.</p>
    <Link to={APP_ROUTES.dashboard}>
      <Button>Go to Dashboard</Button>
    </Link>
  </div>
)

