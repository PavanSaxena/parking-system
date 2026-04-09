import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { QUERY_KEYS } from '../constants/queryKeys'
import { bookingService } from '../services/bookingService'
import { slotService } from '../services/slotService'
import { getErrorMessage } from '../utils/errors'
import { toast } from '../utils/toast'

export const useActiveParking = () => {
  const queryClient = useQueryClient()

  const activeBookingsQuery = useQuery({
    queryKey: QUERY_KEYS.activeBookings,
    queryFn: async () => {
      const slots = await slotService.getSlots()
      return bookingService.getActiveBookings(slots)
    },
  })

  const endBookingMutation = useMutation({
    mutationFn: (ticketId: string) => bookingService.endBooking({ ticketId }),
    onSuccess: () => {
      toast.success('Parking session ended')
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.activeBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.completedBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.allBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.slots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.availableSlots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.payments })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.reports })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.dashboard })
    },
    onError: (error) => toast.error(getErrorMessage(error)),
  })

  return {
    activeBookingsQuery,
    endBookingMutation,
  }
}

