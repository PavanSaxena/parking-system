import { API } from '../constants/api'
import type { CreateSlotRequest, RateDTO, SlotDTO, SlotViewModel, UpdateSlotRequest } from '../types/slot'
import { apiClient } from './apiClient'
import { mapUiVehicleType, toSlotViewModel } from './adapters'

export const slotService = {
  getSlots: async (status?: 'AVAILABLE' | 'OCCUPIED') => {
    const slotsEndpoint =
      status === 'AVAILABLE' ? API.availableSlots : status === 'OCCUPIED' ? API.occupiedSlots : API.slots

    const [{ data: slots }, { data: rates }] = await Promise.all([
      apiClient.get<SlotDTO[]>(slotsEndpoint),
      apiClient.get<RateDTO[]>(API.rates),
    ])

    return slots.map((slot) => toSlotViewModel(slot, rates))
  },

  createSlot: async (payload: CreateSlotRequest) => {
    const { data: rates } = await apiClient.get<RateDTO[]>(API.rates)
    const backendVehicleType = mapUiVehicleType(payload.vehicleType)
    const existingRate = rates.find((rate) => rate.vehicleType === backendVehicleType)

    if (!existingRate) {
      await apiClient.post(API.rates, {
        rateId: `RATE-${backendVehicleType}`,
        vehicleType: backendVehicleType,
        hourlyRate: payload.pricePerHour,
      })
    }

    await apiClient.post(API.createSlot, {
      slotId: payload.slotNumber,
      slotType: backendVehicleType,
      status: payload.status ?? 'AVAILABLE',
    })

    const allSlots = await slotService.getSlots()
    const created = allSlots.find((slot) => slot.slotNumber === payload.slotNumber) ?? allSlots[0]
    return created
  },

  updateSlot: async (payload: UpdateSlotRequest) => {
    const endpoint = payload.status === 'OCCUPIED' ? API.occupySlot(payload.slotId) : API.freeSlot(payload.slotId)
    await apiClient.put(endpoint)
  },

  deleteSlot: async (slotId: string) => {
    await apiClient.delete(API.deleteSlot(slotId))
  },
}


