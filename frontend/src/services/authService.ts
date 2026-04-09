import { API } from '../constants/api'
import type { AuthTokenDTO, LoginRequest } from '../types/common'
import { apiClient } from './apiClient'

export const authService = {
  login: async (payload: LoginRequest) => {
    const { data } = await apiClient.post<AuthTokenDTO>(API.authLogin, payload)
    return data
  },
}

