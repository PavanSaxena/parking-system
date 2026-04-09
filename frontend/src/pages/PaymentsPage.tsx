import { useMemo, useState } from 'react'

import { ApiState } from '../components/common/ApiState'
import { EmptyState } from '../components/ui/EmptyState'
import { Badge } from '../components/ui/Badge'
import { Button } from '../components/ui/Button'
import { Card } from '../components/ui/Card'
import { PageHeader } from '../components/ui/PageHeader'
import { Select } from '../components/ui/Select'
import { Table } from '../components/ui/Table'
import { usePayments } from '../hooks/usePayments'
import { useWorkflowGuard } from '../hooks/useWorkflowGuard'
import type { PaymentMethod } from '../types/payment'
import { formatCurrency, formatDateTime } from '../utils/format'

const paymentMethods = [
  { label: 'UPI', value: 'UPI' },
  { label: 'Card', value: 'CARD' },
]

export const PaymentsPage = () => {
  const { paymentsQuery, completedBookingsQuery, processPaymentMutation } = usePayments()

  const [ticketId, setTicketId] = useState('')
  const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>('UPI')

  const processedPayments = useMemo(() => paymentsQuery.data ?? [], [paymentsQuery.data])
  const completedBookings = useMemo(() => completedBookingsQuery.data ?? [], [completedBookingsQuery.data])
  const workflowGuard = useWorkflowGuard({
    slots: [],
    activeBookings: [],
    completedBookings,
  })

  return (
    <section>
      <PageHeader
        title="Payments"
        description="Step 4: process payments only for completed bookings."
      />

      <Card className="mb-6">
        {!workflowGuard.hasCompletedBooking ? (
          <p className="mb-3 text-sm text-amber-700">Complete a parking session before making payment.</p>
        ) : null}
        <div className="grid gap-3 md:grid-cols-3">
          <Select
            label="Completed Booking"
            options={
              completedBookings.length > 0
                ? completedBookings.map((booking) => ({
                    label: `${booking.bookingId} - ${booking.vehicleNumber} (${formatCurrency(booking.totalAmount ?? 0)})`,
                    value: booking.bookingId,
                  }))
                : [{ label: 'No completed bookings available', value: '' }]
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
              onClick={() => processPaymentMutation.mutate({ ticketId, paymentMethod })}
              disabled={processPaymentMutation.isPending || !ticketId.trim()}
            >
              {processPaymentMutation.isPending ? 'Processing...' : `Pay with ${paymentMethod}`}
            </Button>
          </div>
        </div>
      </Card>

      <ApiState
        isLoading={paymentsQuery.isLoading}
        error={paymentsQuery.error ? paymentsQuery.error.message : null}
      >
        {processedPayments.length === 0 ? (
          <EmptyState title="No payments yet" description="Completed bookings paid from this page will appear here." />
        ) : (
          <Table headers={['Payment ID', 'Booking', 'Amount', 'Status', 'Payment Time']}>
            {processedPayments.map((payment) => (
              <tr key={payment.paymentId} className="bg-white">
                <td className="px-4 py-3 font-medium text-slate-900">{payment.paymentId}</td>
                <td className="px-4 py-3 text-slate-700">{payment.ticketId}</td>
                <td className="px-4 py-3 text-slate-700">{formatCurrency(payment.amount)}</td>
                <td className="px-4 py-3">
                  <Badge
                    tone={
                      payment.paymentStatus === 'COMPLETED'
                        ? 'green'
                        : payment.paymentStatus === 'FAILED'
                          ? 'red'
                          : 'yellow'
                    }
                  >
                    {payment.paymentStatus}
                  </Badge>
                </td>
                <td className="px-4 py-3 text-slate-700">{formatDateTime(payment.paymentTime)}</td>
              </tr>
            ))}
          </Table>
        )}
      </ApiState>
    </section>
  )
}


