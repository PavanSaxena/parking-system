import { useQuery } from '@tanstack/react-query'

import { QUERY_KEYS } from '../constants/queryKeys'
import { bookingService } from '../services/bookingService'
import { dashboardService } from '../services/dashboardService'
import { paymentService } from '../services/paymentService'
import { slotService } from '../services/slotService'

export const useDashboard = () =>
  useQuery({
    queryKey: QUERY_KEYS.dashboard,
    queryFn: async () => {
      const slots = await slotService.getSlots()
      const [summary, bookings, payments] = await Promise.all([
        dashboardService.getSummary(slots),
        bookingService.getAllBookings(slots),
        paymentService.getPayments(),
      ])

      return {
        summary,
        recentBookings: bookings.slice(0, 5),
        recentPayments: payments.slice(0, 5),
      }
    },
  })


