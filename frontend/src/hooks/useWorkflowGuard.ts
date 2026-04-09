import { useMemo } from 'react'

import type { BookingViewModel } from '../types/booking'
import type { SlotViewModel } from '../types/slot'

interface WorkflowGuardInput {
  slots: SlotViewModel[]
  activeBookings: BookingViewModel[]
  completedBookings: BookingViewModel[]
}

export const useWorkflowGuard = ({ slots, activeBookings, completedBookings }: WorkflowGuardInput) =>
  useMemo(
    () => ({
      hasAnySlot: slots.length > 0,
      hasAvailableSlot: slots.some((slot) => slot.status === 'AVAILABLE'),
      hasActiveBooking: activeBookings.length > 0,
      hasCompletedBooking: completedBookings.length > 0,
    }),
    [slots, activeBookings, completedBookings],
  )

