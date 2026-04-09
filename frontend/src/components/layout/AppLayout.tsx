import { Outlet, useLocation } from 'react-router-dom'

import { WorkflowStepper } from '../common/WorkflowStepper'
import { Sidebar } from './Sidebar'
import { TopBar } from './TopBar'

const workflowPaths = ['/slots', '/booking', '/active-parking', '/payments', '/reports', '/dashboard']

export const AppLayout = () => {
  const location = useLocation()
  const currentIndex = workflowPaths.findIndex((path) => location.pathname.startsWith(path))

  return (
    <div className="min-h-screen bg-slate-100 text-slate-900 md:flex">
      <Sidebar />
      <div className="min-w-0 flex-1">
        <TopBar />
        <main className="mx-auto w-full max-w-7xl p-4 md:p-8">
          <WorkflowStepper
            steps={[
              { label: 'Slots', done: currentIndex >= 0 },
              { label: 'Booking', done: currentIndex >= 1 },
              { label: 'Active Parking', done: currentIndex >= 2 },
              { label: 'Payments', done: currentIndex >= 3 },
              { label: 'Reports', done: currentIndex >= 4 },
              { label: 'Dashboard', done: currentIndex >= 5 },
            ]}
          />
          <Outlet />
        </main>
      </div>
    </div>
  )
}

