import type { PaymentStatus, VehicleType } from './common'

export type BookingStatus = 'ACTIVE' | 'COMPLETED'

export interface ParkingTicketDTO {
  ticketId: string
  vehicleNumber: string
  slotId: string
  entryTime: string
  exitTime: string | null
  paymentStatus: PaymentStatus
}

export interface CreateBookingRequest {
  vehicleNumber: string
  vehicleType: VehicleType
  slotId: string
  operatorId: string
}

export interface EndBookingRequest {
  ticketId: string
}

export interface BookingViewModel {
  bookingId: string
  vehicleNumber: string
  vehicleType: VehicleType
  slotId: string
  startTime: string
  endTime: string | null
  durationMinutes: number | null
  totalAmount: number | null
  bookingStatus: BookingStatus
  paymentStatus: PaymentStatus
}


