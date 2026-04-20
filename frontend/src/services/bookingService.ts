import { API } from '../constants/api'
import type { CreateBookingRequest, EndBookingRequest, ParkingTicketDTO } from '../types/booking'
import type { SlotViewModel } from '../types/slot'
import { apiClient } from './apiClient'
import { mapUiVehicleType, toBookingViewModel } from './adapters'

export const bookingService = {
  getActiveBookings: async (slots: SlotViewModel[]) => {
    const { data } = await apiClient.get<ParkingTicketDTO[]>(API.reportsActive)
    const slotMap = new Map(slots.map((slot) => [slot.slotId, slot]))
    return data.map((ticket) => toBookingViewModel(ticket, slotMap))
  },

  getCompletedBookings: async (slots: SlotViewModel[]) => {
    const { data } = await apiClient.get<ParkingTicketDTO[]>(API.reportsTickets)
    const slotMap = new Map(slots.map((slot) => [slot.slotId, slot]))
    return data.filter((ticket) => ticket.exitTime !== null).map((ticket) => toBookingViewModel(ticket, slotMap))
  },

  getAllBookings: async (slots: SlotViewModel[]) => {
    const { data } = await apiClient.get<ParkingTicketDTO[]>(API.reportsTickets)
    const slotMap = new Map(slots.map((slot) => [slot.slotId, slot]))
    return data.map((ticket) => toBookingViewModel(ticket, slotMap))
  },

  createBooking: async (payload: CreateBookingRequest) => {
    await apiClient.post(API.createVehicle, {
      vehicleNumber: payload.vehicleNumber,
      vehicleType: mapUiVehicleType(payload.vehicleType),
    })

    const { data } = await apiClient.post<ParkingTicketDTO>(API.createBooking, {
      ticketId: crypto.randomUUID(),
      vehicleNumber: payload.vehicleNumber,
      slotId: payload.slotId,
      operatorId: payload.operatorId,
    })
    return data
  },

  endBooking: async (payload: EndBookingRequest) => {
    const { data } = await apiClient.post<ParkingTicketDTO>(API.endBooking, payload)
    return data
  },
}


