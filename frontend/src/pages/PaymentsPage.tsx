import { useEffect, useMemo, useState } from 'react'

import { EmptyState } from '../components/ui/EmptyState'
import { Badge } from '../components/ui/Badge'
import { Button } from '../components/ui/Button'
import { Card } from '../components/ui/Card'
import { PageHeader } from '../components/ui/PageHeader'
import { Select } from '../components/ui/Select'
import { Skeleton } from '../components/ui/Skeleton'
import { Table } from '../components/ui/Table'
import { usePayments } from '../hooks/usePayments'
import type { PaymentMethod } from '../types/payment'
import { formatCurrency, formatDateTime } from '../utils/format'

const paymentMethods = [
  { label: 'UPI', value: 'UPI' },
  { label: 'Card', value: 'CARD' },
]

export const PaymentsPage = () => {
  const {
    pendingBookings,
    completedPayments,
    createPayment,
    createPaymentMutation,
    validateBookingPayment,
    loading,
    error,
  } = usePayments()

  const [ticketId, setTicketId] = useState('')
  const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>('UPI')

  useEffect(() => {
    if (!ticketId || pendingBookings.some((booking) => booking.bookingId === ticketId)) {
      return
    }
    setTicketId('')
  }, [pendingBookings, ticketId])

  const selectedPendingBooking = useMemo(
    () => pendingBookings.find((booking) => booking.bookingId === ticketId) ?? null,
    [pendingBookings, ticketId],
  )
  const selectedValidation = selectedPendingBooking
    ? validateBookingPayment(selectedPendingBooking.bookingId)
    : { canPay: false, reason: 'Select a pending booking' }

  const payBooking = (bookingId: string) => {
    createPayment(bookingId, paymentMethod)
  }

  return (
    <section>
      <PageHeader
        title="Payments"
        description="Step 4: process payments only for completed bookings."
      />

      {error ? (
        <div className="mb-4 rounded-lg border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">{error}</div>
      ) : null}

      <Card className="mb-6">
        <h2 className="mb-4 text-base font-semibold text-slate-900">Pending Payments</h2>

        {loading ? (
          <div className="space-y-3">
            <Skeleton className="h-12" />
            <Skeleton className="h-12" />
            <Skeleton className="h-12" />
            <Skeleton className="h-10" />
            <Skeleton className="h-10" />
            <Skeleton className="h-10" />
          </div>
        ) : (
          <>
            <div className="grid gap-3 md:grid-cols-3">
              <Select
                label="Pending Booking"
                options={
                  pendingBookings.length > 0
                    ? pendingBookings.map((booking) => ({
                        label: `${booking.bookingId}${booking.vehicleNumber ? ` - ${booking.vehicleNumber}` : ''}`,
                        value: booking.bookingId,
                      }))
                    : [{ label: 'No pending payments', value: '' }]
                }
                value={ticketId}
                onChange={(event) => setTicketId(event.target.value)}
              />
              <Select
                label="Payment Method"
                options={paymentMethods}
                value={paymentMethod}
                onChange={(event) => setPaymentMethod(event.target.value as PaymentMethod)}
              />
              <div className="flex items-end">
                <Button
                  className="w-full"
                  onClick={() => selectedPendingBooking && payBooking(selectedPendingBooking.bookingId)}
                  disabled={createPaymentMutation.isPending || !selectedPendingBooking || !selectedValidation.canPay}
                >
                  {createPaymentMutation.isPending ? 'Processing...' : 'Pay Now'}
                </Button>
              </div>
            </div>

            <div className="mt-4 grid gap-3 md:grid-cols-3">
              <div className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-slate-500">Amount Due</p>
                <p className="text-lg font-bold text-rose-600">
                  {selectedPendingBooking ? formatCurrency(selectedPendingBooking.amountDue) : '--'}
                </p>
              </div>
              <div className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-slate-500">Booking ID</p>
                <p className="text-sm font-medium text-slate-900">{selectedPendingBooking?.bookingId ?? '--'}</p>
              </div>
              <div className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-slate-500">Vehicle Number</p>
                <p className="text-sm font-medium text-slate-900">{selectedPendingBooking?.vehicleNumber ?? '--'}</p>
              </div>
            </div>

            {!selectedValidation.canPay && selectedPendingBooking ? (
              <p className="mt-2 text-xs text-amber-700">{selectedValidation.reason}</p>
            ) : null}

            <div className="mt-5">
              {pendingBookings.length === 0 ? (
                <EmptyState
                  title="No pending payments"
                  description="Completed and unpaid bookings will appear here."
                />
              ) : (
                <Table headers={['Booking ID', 'Vehicle', 'Amount Due', 'Status', 'Action']}>
                  {pendingBookings.map((booking) => {
                    const validation = validateBookingPayment(booking.bookingId)
                    return (
                      <tr key={booking.bookingId} className="bg-white">
                        <td className="px-4 py-3 font-medium text-slate-900">{booking.bookingId}</td>
                        <td className="px-4 py-3 text-slate-700">{booking.vehicleNumber ?? '-'}</td>
                        <td className="px-4 py-3 font-semibold text-rose-600">{formatCurrency(booking.amountDue)}</td>
                        <td className="px-4 py-3">
                          <Badge tone="yellow">PENDING</Badge>
                        </td>
                        <td className="px-4 py-3">
                          <Button
                            variant="secondary"
                            disabled={createPaymentMutation.isPending || !validation.canPay}
                            onClick={() => payBooking(booking.bookingId)}
                          >
                            Pay
                          </Button>
                        </td>
                      </tr>
                    )
                  })}
                </Table>
              )}
            </div>
          </>
        )}
      </Card>

      <Card>
        <h2 className="mb-4 text-base font-semibold text-slate-900">Payment History</h2>

        {loading ? (
          <div className="space-y-2">
            <Skeleton className="h-10" />
            <Skeleton className="h-10" />
            <Skeleton className="h-10" />
          </div>
        ) : (
          <Table headers={['Payment ID', 'Booking', 'Amount', 'Status', 'Payment Time']}>
            {completedPayments.map((payment) => (
              <tr key={payment.paymentId} className="bg-white">
                <td className="px-4 py-3 font-medium text-slate-900">{payment.paymentId}</td>
                <td className="px-4 py-3 text-slate-700">{payment.bookingId}</td>
                <td className="px-4 py-3 text-slate-700">{formatCurrency(payment.amount)}</td>
                <td className="px-4 py-3">
                  <Badge tone="green">COMPLETED</Badge>
                </td>
                <td className="px-4 py-3 text-slate-700">
                  {payment.paymentTime ? formatDateTime(payment.paymentTime) : '-'}
                </td>
              </tr>
            ))}
          </Table>
        )}
        {!loading && completedPayments.length === 0 ? (
          <div className="mt-4">
            <EmptyState title="No completed payments" description="Completed payments will appear in history." />
          </div>
        ) : null}
      </Card>
    </section>
  )
}


