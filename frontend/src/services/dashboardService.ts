import type { DashboardSummary } from '../types/report'
import type { SlotViewModel } from '../types/slot'
import { bookingService } from './bookingService'
import { reportService } from './reportService'

export const dashboardService = {
  getSummary: async (slots: SlotViewModel[]): Promise<DashboardSummary> => {
    const [occupancy, totalRevenue, activeBookings] = await Promise.all([
      reportService.getOccupancy(),
      reportService.getRevenue(),
      bookingService.getActiveBookings(slots),
    ])

    return {
      totalSlots: occupancy.totalSlots,
      availableSlots: Math.max(occupancy.totalSlots - occupancy.occupiedSlots, 0),
      occupiedSlots: occupancy.occupiedSlots,
      activeBookings: activeBookings.length,
      totalRevenue,
    }
  },
}

