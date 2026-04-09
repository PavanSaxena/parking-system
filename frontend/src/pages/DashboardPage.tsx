import { Car, CircleDollarSign, LayoutGrid, ParkingSquare, SquareStack } from 'lucide-react'

import { ApiState } from '../components/common/ApiState'
import { Badge } from '../components/ui/Badge'
import { EmptyState } from '../components/ui/EmptyState'
import { PageHeader } from '../components/ui/PageHeader'
import { StatCard } from '../components/ui/StatCard'
import { Table } from '../components/ui/Table'
import { useDashboard } from '../hooks/useDashboard'
import { formatCurrency, formatDateTime } from '../utils/format'

export const DashboardPage = () => {
  const { data, isLoading, error } = useDashboard()

  return (
    <section>
      <PageHeader
        title="Dashboard"
        description="Step 6: monitor slots, bookings, payments, and revenue in one place."
      />

      <ApiState isLoading={isLoading} error={error ? error.message : null}>
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-5">
          <StatCard
            label="Total Slots"
            value={String(data?.summary.totalSlots ?? 0)}
            icon={<LayoutGrid size={18} />}
          />
          <StatCard
            label="Available Slots"
            value={String(data?.summary.availableSlots ?? 0)}
            icon={<ParkingSquare size={18} />}
          />
          <StatCard
            label="Occupied Slots"
            value={String(data?.summary.occupiedSlots ?? 0)}
            icon={<SquareStack size={18} />}
          />
          <StatCard
            label="Active Bookings"
            value={String(data?.summary.activeBookings ?? 0)}
            icon={<Car size={18} />}
          />
          <StatCard
            label="Total Revenue"
            value={formatCurrency(data?.summary.totalRevenue ?? 0)}
            icon={<CircleDollarSign size={18} />}
            hint="Updated from completed payments"
          />
        </div>

        <div className="mt-6 grid gap-6 lg:grid-cols-2">
          <div>
            <h2 className="mb-3 text-base font-semibold text-slate-900">Recent Bookings</h2>
            {(data?.recentBookings ?? []).length === 0 ? (
              <EmptyState title="No recent bookings" description="Bookings will appear once users create them." />
            ) : (
              <Table headers={['Booking', 'Vehicle', 'Slot', 'Start', 'Status']}>
                {(data?.recentBookings ?? []).map((booking) => (
                  <tr key={booking.bookingId} className="bg-white">
                    <td className="px-4 py-3 font-medium text-slate-900">{booking.bookingId}</td>
                    <td className="px-4 py-3 text-slate-700">{booking.vehicleNumber}</td>
                    <td className="px-4 py-3 text-slate-700">{booking.slotId}</td>
                    <td className="px-4 py-3 text-slate-700">{formatDateTime(booking.startTime)}</td>
                    <td className="px-4 py-3">
                      <Badge tone={booking.bookingStatus === 'ACTIVE' ? 'yellow' : 'green'}>
                        {booking.bookingStatus}
                      </Badge>
                    </td>
                  </tr>
                ))}
              </Table>
            )}
          </div>

          <div>
            <h2 className="mb-3 text-base font-semibold text-slate-900">Recent Payments</h2>
            {(data?.recentPayments ?? []).length === 0 ? (
              <EmptyState title="No recent payments" description="Completed payments show up here." />
            ) : (
              <Table headers={['Payment', 'Booking', 'Amount', 'Status', 'Time']}>
                {(data?.recentPayments ?? []).map((payment) => (
                  <tr key={payment.paymentId} className="bg-white">
                    <td className="px-4 py-3 font-medium text-slate-900">{payment.paymentId}</td>
                    <td className="px-4 py-3 text-slate-700">{payment.ticketId}</td>
                    <td className="px-4 py-3 text-slate-700">{formatCurrency(payment.amount)}</td>
                    <td className="px-4 py-3">
                      <Badge tone={payment.paymentStatus === 'COMPLETED' ? 'green' : 'red'}>
                        {payment.paymentStatus}
                      </Badge>
                    </td>
                    <td className="px-4 py-3 text-slate-700">{formatDateTime(payment.paymentTime)}</td>
                  </tr>
                ))}
              </Table>
            )}
          </div>
        </div>
      </ApiState>
    </section>
  )
}


