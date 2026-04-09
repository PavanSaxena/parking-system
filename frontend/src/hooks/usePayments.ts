import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { QUERY_KEYS } from '../constants/queryKeys'
import { bookingService } from '../services/bookingService'
import { paymentService } from '../services/paymentService'
import { slotService } from '../services/slotService'
import type { ProcessPaymentRequest } from '../types/payment'
import { getErrorMessage } from '../utils/errors'
import { toast } from '../utils/toast'

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

  const processPaymentMutation = useMutation({
    mutationFn: async (payload: ProcessPaymentRequest) => {
      const existing = (paymentsQuery.data ?? []).find((payment) => payment.ticketId === payload.ticketId)
      if (existing) {
        throw new Error('Payment already exists for this booking')
      }
      return paymentService.processPayment(payload)
    },
    onSuccess: () => {
      toast.success('Payment processed successfully')
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.payments })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.completedBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.reports })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.dashboard })
    },
    onError: (error) => toast.error(getErrorMessage(error)),
  })

  return {
    paymentsQuery,
    completedBookingsQuery,
    processPaymentMutation,
  }
}



