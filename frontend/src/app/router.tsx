import { Navigate, createBrowserRouter } from 'react-router-dom'

import { ProtectedRoute } from '../components/common/ProtectedRoute'
import { AppLayout } from '../components/layout/AppLayout'
import { APP_ROUTES } from '../constants/routes'
import { ActiveParkingPage } from '../pages/ActiveParkingPage'
import { BookingPage } from '../pages/BookingPage'
import { DashboardPage } from '../pages/DashboardPage'
import { LoginPage } from '../pages/LoginPage'
import { NotFoundPage } from '../pages/NotFoundPage'
import { PaymentsPage } from '../pages/PaymentsPage'
import { ReportsPage } from '../pages/ReportsPage'
import { SlotsPage } from '../pages/SlotsPage'

export const router = createBrowserRouter([
  {
    path: APP_ROUTES.login,
    element: <LoginPage />,
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        path: APP_ROUTES.root,
        element: <AppLayout />,
        children: [
          {
            index: true,
            element: <Navigate to={APP_ROUTES.dashboard} replace />,
          },
          {
            path: APP_ROUTES.dashboard,
            element: <DashboardPage />,
          },
          {
            path: APP_ROUTES.slots,
            element: <SlotsPage />,
          },
          {
            path: APP_ROUTES.booking,
            element: <BookingPage />,
          },
          {
            path: APP_ROUTES.activeParking,
            element: <ActiveParkingPage />,
          },
          {
            path: APP_ROUTES.payments,
            element: <PaymentsPage />,
          },
          {
            path: APP_ROUTES.reports,
            element: <ReportsPage />,
          },
          {
            path: '*',
            element: <NotFoundPage />,
          },
        ],
      },
    ],
  },
])


