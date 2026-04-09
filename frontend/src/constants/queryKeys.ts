export const QUERY_KEYS = {
  dashboard: ['dashboard'] as const,
  dashboardRecentBookings: ['dashboard', 'recentBookings'] as const,
  dashboardRecentPayments: ['dashboard', 'recentPayments'] as const,
  slots: ['slots'] as const,
  availableSlots: ['slots', 'available'] as const,
  activeBookings: ['bookings', 'active'] as const,
  completedBookings: ['bookings', 'completed'] as const,
  allBookings: ['bookings', 'all'] as const,
  reports: ['reports'] as const,
  payments: ['payments'] as const,
  rates: ['rates'] as const,
} as const


