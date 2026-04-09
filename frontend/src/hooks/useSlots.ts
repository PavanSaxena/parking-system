import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

import { QUERY_KEYS } from '../constants/queryKeys'
import { slotService } from '../services/slotService'
import type { CreateSlotRequest, UpdateSlotRequest } from '../types/slot'
import { getErrorMessage } from '../utils/errors'
import { toast } from '../utils/toast'

export const useSlots = () => {
  const queryClient = useQueryClient()

  const slotsQuery = useQuery({
    queryKey: QUERY_KEYS.slots,
    queryFn: () => slotService.getSlots(),
  })

  const availableSlotsQuery = useQuery({
    queryKey: QUERY_KEYS.availableSlots,
    queryFn: () => slotService.getSlots('AVAILABLE'),
  })

  const createSlotMutation = useMutation({
    mutationFn: (payload: CreateSlotRequest) => slotService.createSlot(payload),
    onSuccess: () => {
      toast.success('Slot created successfully')
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.slots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.availableSlots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.dashboard })
    },
    onError: (error) => toast.error(getErrorMessage(error)),
  })

  const updateSlotMutation = useMutation({
    mutationFn: (payload: UpdateSlotRequest) => slotService.updateSlot(payload),
    onSuccess: () => {
      toast.success('Slot updated')
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.slots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.availableSlots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.dashboard })
    },
    onError: (error) => toast.error(getErrorMessage(error)),
  })

  const deleteSlotMutation = useMutation({
    mutationFn: (slotId: string) => slotService.deleteSlot(slotId),
    onSuccess: () => {
      toast.success('Slot deleted')
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.slots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.availableSlots })
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.dashboard })
    },
    onError: (error) => toast.error(getErrorMessage(error)),
  })

  return {
    slotsQuery,
    availableSlotsQuery,
    createSlotMutation,
    updateSlotMutation,
    deleteSlotMutation,
  }
}



