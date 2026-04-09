import type { SlotStatus, VehicleType } from './common'

export interface SlotDTO {
  slotId: string
  slotType: string
  status: SlotStatus
}

export interface RateDTO {
  rateId: string
  vehicleType: string
  hourlyRate: number
}

export interface SlotViewModel {
  slotId: string
  slotNumber: string
  vehicleType: VehicleType
  status: SlotStatus
  pricePerHour: number
}

export interface CreateSlotRequest {
  slotNumber: string
  vehicleType: VehicleType
  pricePerHour: number
  status?: SlotStatus
}

export interface UpdateSlotRequest {
  slotId: string
  status: SlotStatus
}


