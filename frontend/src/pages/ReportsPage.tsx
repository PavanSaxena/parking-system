import { useMemo, useState } from 'react'

import { ApiState } from '../components/common/ApiState'
import { Alert } from '../components/ui/Alert'
import { Badge } from '../components/ui/Badge'
import { Card } from '../components/ui/Card'
import { EmptyState } from '../components/ui/EmptyState'
import { Input } from '../components/ui/Input'
import { PageHeader } from '../components/ui/PageHeader'
import { Table } from '../components/ui/Table'
import { useReports } from '../hooks/useReports'
import { isWithinDateRange } from '../utils/date'
import { formatCurrency, formatDateTime, formatPercent } from '../utils/format'

export const ReportsPage = () => {
  const reportsQuery = useReports()
  const [fromDate, setFromDate] = useState('')
  const [toDate, setToDate] = useState('')
  const hasInvalidDateRange = Boolean(fromDate && toDate && fromDate > toDate)

  const filteredBookings = useMemo(
    () =>
      (reportsQuery.data?.bookings ?? []).filter((booking) =>
        isWithinDateRange(booking.startTime, fromDate || undefined, toDate || undefined),
      ),
    [reportsQuery.data?.bookings, fromDate, toDate],
  )

  const filteredPayments = useMemo(
    () =>
      (reportsQuery.data?.payments ?? []).filter((payment) =>
        isWithinDateRange(payment.paymentTime, fromDate || undefined, toDate || undefined),
      ),
    [reportsQuery.data?.payments, fromDate, toDate],
  )

  const filteredRevenue = useMemo(
    () => filteredPayments.reduce((sum, payment) => sum + payment.amount, 0),
    [filteredPayments],
  )

  const averageDuration = useMemo(() => {
    const durations = filteredBookings
      .map((booking) => booking.durationMinutes)
      .filter((value): value is number => value !== null)
    return durations.length > 0
      ? durations.reduce((sum, duration) => sum + duration, 0) / durations.length
      : 0
  }, [filteredBookings])

  return (
    <section>
      <PageHeader
        title="Reports"
        description="Step 5: analyze booking and revenue trends with date filters."
      />

      {hasInvalidDateRange ? <Alert message="Start date must be before or equal to end date." /> : null}

      <Card className="mb-6">
        <div className="grid gap-4 md:grid-cols-4">
          <Input
            label="From"
            type="date"
            value={fromDate}
            onChange={(event) => setFromDate(event.target.value)}
          />
          <Input
            label="To"
            type="date"
            value={toDate}
            onChange={(event) => setToDate(event.target.value)}
          />
          <div className="rounded-lg border border-slate-200 bg-slate-50 px-4 py-3">
            <p className="text-xs uppercase text-slate-500">Filtered Revenue</p>
            <p className="mt-1 text-lg font-semibold text-slate-900">{formatCurrency(filteredRevenue)}</p>
          </div>
          <div className="rounded-lg border border-slate-200 bg-slate-50 px-4 py-3">
            <p className="text-xs uppercase text-slate-500">Average Duration</p>
            <p className="mt-1 text-lg font-semibold text-slate-900">
              {Math.round(averageDuration)} mins
            </p>
          </div>
        </div>
      </Card>

      <div className="mb-6 grid gap-4 md:grid-cols-4">
        <Card>
          <p className="text-xs uppercase text-slate-500">Total Bookings</p>
          <p className="mt-2 text-2xl font-semibold text-slate-900">{filteredBookings.length}</p>
        </Card>
        <Card>
          <p className="text-xs uppercase text-slate-500">Total Revenue</p>
          <p className="mt-2 text-2xl font-semibold text-slate-900">{formatCurrency(filteredRevenue)}</p>
        </Card>
        <Card>
          <p className="text-xs uppercase text-slate-500">Avg Duration</p>
          <p className="mt-2 text-2xl font-semibold text-slate-900">{Math.round(averageDuration)} mins</p>
        </Card>
        <Card>
          <p className="text-xs uppercase text-slate-500">Occupancy Rate</p>
          <p className="mt-2 text-2xl font-semibold text-slate-900">
            {formatPercent(reportsQuery.data?.occupancy.occupancyPercentage ?? 0)}
          </p>
        </Card>
      </div>

      <ApiState
        isLoading={reportsQuery.isLoading}
        error={reportsQuery.error ? reportsQuery.error.message : null}
      >
        {hasInvalidDateRange ? (
          <EmptyState title="Invalid date range" description="Fix date filters to generate reports." />
        ) : (
          <div className="space-y-6">
          <div>
            <h2 className="mb-3 text-base font-semibold text-slate-900">Bookings Report</h2>
            <Table headers={['Booking', 'Vehicle', 'Slot', 'Start', 'End', 'Payment']}>
              {filteredBookings.map((booking) => (
                <tr key={booking.bookingId} className="bg-white">
                  <td className="px-4 py-3 font-medium text-slate-900">{booking.bookingId}</td>
                  <td className="px-4 py-3 text-slate-700">{booking.vehicleNumber}</td>
                  <td className="px-4 py-3 text-slate-700">{booking.slotId}</td>
                  <td className="px-4 py-3 text-slate-700">{formatDateTime(booking.startTime)}</td>
                  <td className="px-4 py-3 text-slate-700">{formatDateTime(booking.endTime)}</td>
                  <td className="px-4 py-3">
                    <Badge tone={booking.paymentStatus === 'COMPLETED' ? 'green' : 'yellow'}>
                      {booking.paymentStatus}
                    </Badge>
                  </td>
                </tr>
              ))}
            </Table>
          </div>

          <div>
            <h2 className="mb-3 text-base font-semibold text-slate-900">Payment Report</h2>
            <Table headers={['Payment', 'Ticket', 'Amount', 'Status', 'Time']}>
              {filteredPayments.map((payment) => (
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
          </div>
          </div>
        )}
      </ApiState>
    </section>
  )
}

