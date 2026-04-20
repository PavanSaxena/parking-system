import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useMemo } from 'react'

import { QUERY_KEYS } from '../constants/queryKeys'
import { bookingService } from '../services/bookingService'
import { paymentService } from '../services/paymentService'
import { slotService } from '../services/slotService'
import type { BookingViewModel } from '../types/booking'
import type { PaymentMethod, PendingPaymentBooking, ProcessPaymentRequest } from '../types/payment'
import { getErrorMessage } from '../utils/errors'
import { toast } from '../utils/toast'

interface PaymentValidationResult {
  canPay: boolean
  reason: string | null
}

const toPendingPaymentBooking = (booking: BookingViewModel): PendingPaymentBooking => ({
  bookingId: booking.bookingId,
  vehicleNumber: booking.vehicleNumber || null,
  amountDue: Math.max(booking.totalAmount ?? 0, 0),
  durationMinutes: booking.durationMinutes,
  exitTime: booking.endTime ?? booking.exitTime ?? null,
  paymentStatus: 'PENDING',
})

export const usePayments = () => {
  const queryClient = useQueryClient()

  const paymentsQuery = useQuery({
    queryKey: QUERY_KEYS.payments,
    queryFn: paymentService.getPayments,
  })

  const completedBookingsQuery = useQuery({
    queryKey: QUERY_KEYS.completedBookings,
    queryFn: async () => {
      const slots = await slotService.getSlots()
      return bookingService.getCompletedBookings(slots)
    },
  })

  const payments = paymentsQuery.data ?? []
  const completedBookings = completedBookingsQuery.data ?? []

  const completedPayments = useMemo(
    () => payments.filter((payment) => payment.paymentStatus === 'COMPLETED'),
    [payments],
  )

  const completedPaymentBookingIds = useMemo(
    () => new Set(completedPayments.map((payment) => payment.bookingId)),
    [completedPayments],
  )

  const pendingBookings = useMemo(
    () =>
      completedBookings
        .filter(
          (booking) =>
            booking.paymentStatus === 'PENDING' &&
            Boolean(booking.endTime ?? booking.exitTime) &&
            !completedPaymentBookingIds.has(booking.bookingId),
        )
        .map(toPendingPaymentBooking),
    [completedBookings, completedPaymentBookingIds],
  )

  const validateBookingPayment = (bookingId: string): PaymentValidationResult => {
    const booking = pendingBookings.find((candidate) => candidate.bookingId === bookingId)

    if (!booking) {
      return { canPay: false, reason: 'Booking is not pending payment' }
    }
    if (booking.paymentStatus !== 'PENDING') {
      return { canPay: false, reason: 'Booking is already paid' }
    }
    if (!booking.exitTime) {
      return { canPay: false, reason: 'Booking must be completed before payment' }
    }
    if (booking.amountDue <= 0) {
      return { canPay: false, reason: 'No outstanding amount for this booking' }
    }
    if (completedPaymentBookingIds.has(bookingId)) {
      return { canPay: false, reason: 'Payment already exists for this booking' }
    }

    return { canPay: true, reason: null }
  }

  const createPaymentMutation = useMutation({
    mutationFn: async (payload: ProcessPaymentRequest) => {
      const validation = validateBookingPayment(payload.ticketId)
      if (!validation.canPay) {
        throw new Error(validation.reason ?? 'Unable to process payment')
      }
      return paymentService.processPayment(payload)
    },
    onSuccess: () => {
      toast.success('Payment processed successfully')
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.payments })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.activeBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.completedBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.allBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.reports })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.dashboard })
    },
    onError: (error) => toast.error(getErrorMessage(error)),
  })

  const createPayment = (bookingId: string, paymentMethod: PaymentMethod) =>
    createPaymentMutation.mutate({ ticketId: bookingId, paymentMethod })

  const loading = paymentsQuery.isLoading || completedBookingsQuery.isLoading
  const error = paymentsQuery.error ?? completedBookingsQuery.error

  return {
    paymentsQuery,
    completedBookingsQuery,
    pendingBookings,
    completedPayments,
    createPaymentMutation,
    createPayment,
    validateBookingPayment,
    loading,
    error: error ? getErrorMessage(error) : null,
  }
}



