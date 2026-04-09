import { API } from '../constants/api'
import type { BookingViewModel, ParkingTicketDTO } from '../types/booking'
import type { PaymentDTO } from '../types/payment'
import type { OccupancyDTO, ReportsPayload } from '../types/report'
import type { SlotViewModel } from '../types/slot'
import { apiClient } from './apiClient'
import { toBookingViewModel } from './adapters'

export const reportService = {
  getOccupancy: async () => {
    const { data } = await apiClient.get<OccupancyDTO>(API.reportsOccupancy)
    return data
  },

  getRevenue: async () => {
    const { data } = await apiClient.get<number>(API.reportsRevenue)
    return data
  },

  getPayments: async () => {
    const { data } = await apiClient.get<PaymentDTO[]>(API.reportsPayments)
    return data
  },

  getReports: async (slots: SlotViewModel[]): Promise<ReportsPayload> => {
    const [ticketsResponse, payments, occupancy, totalRevenue] = await Promise.all([
      apiClient.get<ParkingTicketDTO[]>(API.reportsTickets),
      reportService.getPayments(),
      reportService.getOccupancy(),
      reportService.getRevenue(),
    ])

    const slotMap = new Map(slots.map((slot) => [slot.slotId, slot]))
    const bookings = ticketsResponse.data.map((ticket) => toBookingViewModel(ticket, slotMap))
    const finishedDurations = bookings
      .map((booking) => booking.durationMinutes)
      .filter((value): value is number => value !== null)
    const averageDurationMinutes =
      finishedDurations.length > 0
        ? finishedDurations.reduce((sum, duration) => sum + duration, 0) / finishedDurations.length
        : 0

    return {
      bookings,
      payments,
      occupancy,
      totalRevenue,
      averageDurationMinutes,
    }
  },
}


