import type { PaymentStatus } from './common'

export type PaymentMethod = 'UPI' | 'CARD'

export interface ProcessPaymentRequest {
  ticketId: string
  paymentMethod: PaymentMethod
}

export interface PaymentDTO {
  paymentId: string
  bookingId: string
  amount: number
  paymentStatus: PaymentStatus
  paymentTime: string | null
  ticketId?: string
}

export interface PendingPaymentBooking {
  bookingId: string
  vehicleNumber: string | null
  amountDue: number
  durationMinutes: number | null
  exitTime: string | null
  paymentStatus: 'PENDING'
}



