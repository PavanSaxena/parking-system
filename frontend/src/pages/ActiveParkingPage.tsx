import { ApiState } from '../components/common/ApiState'
import { EmptyState } from '../components/ui/EmptyState'
import { Badge } from '../components/ui/Badge'
import { Button } from '../components/ui/Button'
import { PageHeader } from '../components/ui/PageHeader'
import { Table } from '../components/ui/Table'
import { useActiveParking } from '../hooks/useActiveParking'
import { formatCurrency, formatDateTime } from '../utils/format'

export const ActiveParkingPage = () => {
  const { activeBookingsQuery, endBookingMutation } = useActiveParking()

  const bookings = activeBookingsQuery.data ?? []

  return (
    <section>
      <PageHeader
        title="Active Parking"
        description="End active parking sessions and prepare bookings for payment."
      />

      <ApiState
        isLoading={activeBookingsQuery.isLoading}
        error={activeBookingsQuery.error ? activeBookingsQuery.error.message : null}
      >
        {bookings.length === 0 ? (
          <EmptyState title="No active bookings" description="Create a booking first to start a parking session." />
        ) : (
          <Table headers={['Booking', 'Vehicle', 'Slot', 'Start Time', 'Current Amount', 'Status', 'Action']}>
            {bookings.map((booking) => (
              <tr key={booking.bookingId} className="bg-white">
                <td className="px-4 py-3 font-medium text-slate-900">{booking.bookingId}</td>
                <td className="px-4 py-3 text-slate-700">{booking.vehicleNumber}</td>
                <td className="px-4 py-3 text-slate-700">{booking.slotId}</td>
                <td className="px-4 py-3 text-slate-700">{formatDateTime(booking.startTime)}</td>
                <td className="px-4 py-3 text-slate-700">{formatCurrency(booking.totalAmount ?? 0)}</td>
                <td className="px-4 py-3">
                  <Badge tone="yellow">ACTIVE</Badge>
                </td>
                <td className="px-4 py-3">
                  <Button
                    variant="secondary"
                    disabled={endBookingMutation.isPending}
                    onClick={() => endBookingMutation.mutate(booking.bookingId)}
                  >
                    End Parking
                  </Button>
                </td>
              </tr>
            ))}
          </Table>
        )}
      </ApiState>
    </section>
  )
}

