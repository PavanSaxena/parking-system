export const API = {
  authLogin: '/auth/login',
  slots: '/slots',
  availableSlots: '/slots/available',
  occupiedSlots: '/slots/occupied',
  createSlot: '/admin/slots',
  deleteSlot: (slotId: string) => `/admin/slots/${slotId}`,
  occupySlot: (slotId: string) => `/slots/${slotId}/occupy`,
  freeSlot: (slotId: string) => `/slots/${slotId}/free`,
  rates: '/rates',
  createVehicle: '/vehicles',
  createBooking: '/tickets/entry',
  startBooking: '/parking/entry',
  endBooking: '/parking/exit',
  payments: '/payments',
  reports: '/reports',
  reportsTickets: '/reports/tickets',
  reportsActive: '/reports/active',
  reportsPayments: '/reports/payments',
  reportsRevenue: '/reports/revenue',
  reportsOccupancy: '/reports/occupancy',
} as const

export const REQUEST_TIMEOUT_MS = 10_000


