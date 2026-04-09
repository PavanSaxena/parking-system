import axios from 'axios'
import axiosRetry from 'axios-retry'

import { REQUEST_TIMEOUT_MS } from '../constants/api'
import { APP_ROUTES } from '../constants/routes'
import { authStorage } from '../utils/storage'
import { getErrorMessage } from '../utils/errors'
import { toast } from '../utils/toast'

const resolveBaseUrl = () => {
  const rawValue = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim()
  if (!rawValue) {
    return '/api'
  }
  if (rawValue.startsWith('http://') || rawValue.startsWith('https://')) {
    return rawValue
  }
  return rawValue.startsWith('/') ? rawValue : `/${rawValue}`
}

export const apiClient = axios.create({
  baseURL: resolveBaseUrl(),
  timeout: REQUEST_TIMEOUT_MS,
})

apiClient.interceptors.request.use((config) => {
  const token = authStorage.getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

axiosRetry(apiClient, {
  retries: 2,
  retryDelay: axiosRetry.exponentialDelay,
  retryCondition: (error) =>
    axiosRetry.isNetworkOrIdempotentRequestError(error) ||
    (error.response?.status !== undefined && error.response.status >= 500),
})

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status as number | undefined

    if (status === 401) {
      authStorage.clearToken()
      if (window.location.pathname !== APP_ROUTES.login) {
        window.location.assign(APP_ROUTES.login)
      }
      return Promise.reject(new Error('Session expired. Please login again.'))
    }

    if (status !== undefined && status >= 500) {
      toast.error('Server error. Please try again in a moment.')
    }

    return Promise.reject(new Error(getErrorMessage(error)))
  },
)



