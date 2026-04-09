import type { PaymentStatus } from './common'

export interface ProcessPaymentRequest {
  ticketId: string
  paymentMethod: PaymentMethod
}

export interface PaymentDTO {
  paymentId: string
  ticketId: string
  amount: number
  paymentStatus: PaymentStatus
  paymentTime: string
}

export type PaymentMethod = 'UPI' | 'CARD'


