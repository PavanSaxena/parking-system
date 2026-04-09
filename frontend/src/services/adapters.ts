import type { BookingViewModel, ParkingTicketDTO } from '../types/booking'
import type { VehicleType } from '../types/common'
import type { RateDTO, SlotDTO, SlotViewModel } from '../types/slot'

const backendToUiVehicleType: Record<string, VehicleType> = {
  MINI: 'MINI',
  SEDAN: 'SEDAN',
  SUV: 'SUV',
  CAR: 'SEDAN',
  BIKE: 'MINI',
  TRUCK: 'SUV',
}

const uiToBackendVehicleType: Record<VehicleType, string> = {
  MINI: 'MINI',
  SEDAN: 'SEDAN',
  SUV: 'SUV',
}

const fallbackVehicleType: VehicleType = 'SEDAN'

export const mapBackendVehicleType = (value: string): VehicleType =>
  backendToUiVehicleType[value] ?? fallbackVehicleType

export const mapUiVehicleType = (value: VehicleType): string => uiToBackendVehicleType[value]

export const toSlotViewModel = (slot: SlotDTO, rates: RateDTO[]): SlotViewModel => {
  const vehicleType = mapBackendVehicleType(slot.slotType)
  const matchedRate = rates.find((rate) => mapBackendVehicleType(rate.vehicleType) === vehicleType)

  return {
    slotId: slot.slotId,
    slotNumber: slot.slotId,
    vehicleType,
    status: slot.status,
    pricePerHour: matchedRate?.hourlyRate ?? 0,
  }
}

const calculateDurationMinutes = (entryTime: string, exitTime: string | null) => {
  if (!exitTime) {
    return null
  }
  const durationMs = new Date(exitTime).getTime() - new Date(entryTime).getTime()
  return Math.max(Math.round(durationMs / 60000), 0)
}

export const toBookingViewModel = (
  ticket: ParkingTicketDTO,
  slotMap: Map<string, SlotViewModel>,
): BookingViewModel => {
  const slot = slotMap.get(ticket.slotId)
  const durationMinutes = calculateDurationMinutes(ticket.entryTime, ticket.exitTime)
  const totalAmount = durationMinutes !== null && slot ? (durationMinutes / 60) * slot.pricePerHour : null

  return {
    bookingId: ticket.ticketId,
    vehicleNumber: ticket.vehicleNumber,
    vehicleType: slot?.vehicleType ?? fallbackVehicleType,
    slotId: ticket.slotId,
    startTime: ticket.entryTime,
    endTime: ticket.exitTime,
    durationMinutes,
    totalAmount,
    bookingStatus: ticket.exitTime ? 'COMPLETED' : 'ACTIVE',
    paymentStatus: ticket.paymentStatus,
  }
}

