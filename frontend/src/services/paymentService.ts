import { API } from '../constants/api'
import type { PaymentDTO, ProcessPaymentRequest } from '../types/payment'
import { apiClient } from './apiClient'

interface PaymentApiResponse {
  paymentId: string
  bookingId?: string
  ticketId?: string
  amount: number
  paymentStatus: PaymentDTO['paymentStatus']
  paymentTime?: string | null
}

const normalizePayment = (payment: PaymentApiResponse): PaymentDTO => ({
  paymentId: payment.paymentId,
  bookingId: payment.bookingId ?? payment.ticketId ?? '',
  ticketId: payment.ticketId,
  amount: payment.amount,
  paymentStatus: payment.paymentStatus,
  paymentTime: payment.paymentTime ?? null,
})

export const paymentService = {
  processPayment: async (payload: ProcessPaymentRequest) => {
    const { data } = await apiClient.post<PaymentApiResponse>(API.payments, {
      ticketId: payload.ticketId,
    })
    return normalizePayment(data)
  },

  getPayments: async () => {
    const { data } = await apiClient.get<PaymentApiResponse[]>(API.reportsPayments)
    return data.map(normalizePayment)
  },
}


