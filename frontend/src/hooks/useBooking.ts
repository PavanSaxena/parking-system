import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { QUERY_KEYS } from '../constants/queryKeys'
import { bookingService } from '../services/bookingService'
import { slotService } from '../services/slotService'
import type { CreateBookingRequest } from '../types/booking'
import { getErrorMessage } from '../utils/errors'
import { toast } from '../utils/toast'

export const useBooking = () => {
  const queryClient = useQueryClient()

  const availableSlotsQuery = useQuery({
    queryKey: QUERY_KEYS.availableSlots,
    queryFn: () => slotService.getSlots('AVAILABLE'),
  })

  const activeBookingsQuery = useQuery({
    queryKey: QUERY_KEYS.activeBookings,
    queryFn: async () => {
      const slots = await slotService.getSlots()
      return bookingService.getActiveBookings(slots)
    },
  })

  const createBookingMutation = useMutation({
    mutationFn: (payload: CreateBookingRequest) => bookingService.createBooking(payload),
    onSuccess: () => {
      toast.success('Booking created and slot assigned')
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.activeBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.allBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.completedBookings })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.slots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.availableSlots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.dashboard })
    },
    onError: (error) => toast.error(getErrorMessage(error)),
  })

  return {
    availableSlotsQuery,
    activeBookingsQuery,
    createBookingMutation,
  }
}



