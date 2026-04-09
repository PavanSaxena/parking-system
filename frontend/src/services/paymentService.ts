import { API } from '../constants/api'
import type { PaymentDTO, ProcessPaymentRequest } from '../types/payment'
import { apiClient } from './apiClient'

export const paymentService = {
  processPayment: async (payload: ProcessPaymentRequest) => {
    const { data } = await apiClient.post<PaymentDTO>(API.payments, {
      ticketId: payload.ticketId,
    })
    return data
  },

  getPayments: async () => {
    const { data } = await apiClient.get<PaymentDTO[]>(API.reportsPayments)
    return data
  },
}


