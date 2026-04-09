import { Car, CreditCard, LayoutDashboard, ParkingSquare, FileSpreadsheet } from 'lucide-react'
import { NavLink } from 'react-router-dom'

import { APP_ROUTES } from '../../constants/routes'
import { cn } from '../../utils/cn'

const navItems = [
  { label: 'Slots', to: APP_ROUTES.slots, icon: ParkingSquare },
  { label: 'Booking', to: APP_ROUTES.booking, icon: Car },
  { label: 'Active Parking', to: APP_ROUTES.activeParking, icon: Car },
  { label: 'Payments', to: APP_ROUTES.payments, icon: CreditCard },
  { label: 'Reports', to: APP_ROUTES.reports, icon: FileSpreadsheet },
  { label: 'Dashboard', to: APP_ROUTES.dashboard, icon: LayoutDashboard },
]

export const Sidebar = () => (
  <aside className="w-full border-b border-slate-200 bg-white md:h-screen md:w-64 md:border-b-0 md:border-r">
    <div className="px-4 py-4 md:px-6 md:py-6">
      <p className="text-lg font-semibold text-slate-900">Parking Admin</p>
      <p className="text-xs text-slate-500">Operations Dashboard</p>
    </div>
    <nav className="flex gap-2 overflow-x-auto px-3 pb-4 md:block md:space-y-1 md:px-3">
      {navItems.map((item) => (
        <NavLink
          key={item.to}
          to={item.to}
          className={({ isActive }) =>
            cn(
              'inline-flex min-w-max items-center gap-2 rounded-lg px-3 py-2 text-sm font-medium transition-colors md:flex',
              isActive
                ? 'bg-slate-900 text-white'
                : 'text-slate-600 hover:bg-slate-100 hover:text-slate-900',
            )
          }
        >
          <item.icon size={16} />
          <span>{item.label}</span>
        </NavLink>
      ))}
    </nav>
  </aside>
)


