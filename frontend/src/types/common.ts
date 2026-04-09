export interface ApiErrorResponse {
  error?: string
  message?: string
  timestamp?: string
}

export interface SelectOption {
  label: string
  value: string
}

export type SlotStatus = 'AVAILABLE' | 'OCCUPIED'
export type PaymentStatus = 'PENDING' | 'COMPLETED' | 'FAILED'

export type VehicleType = 'MINI' | 'SEDAN' | 'SUV'

export interface AuthTokenDTO {
  token: string
  userId?: string
  userName?: string
  email?: string
}

export interface LoginRequest {
  email: string
  password: string
}


