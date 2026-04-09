import type { BookingViewModel } from './booking'
import type { PaymentDTO } from './payment'

export interface OccupancyDTO {
  totalSlots: number
  occupiedSlots: number
  occupancyPercentage: number
}

export interface ReportsPayload {
  bookings: BookingViewModel[]
  payments: PaymentDTO[]
  occupancy: OccupancyDTO
  totalRevenue: number
  averageDurationMinutes: number
}

export interface DashboardSummary {
  totalSlots: number
  availableSlots: number
  occupiedSlots: number
  activeBookings: number
  totalRevenue: number
}


