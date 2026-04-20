import axios from 'axios'
import axiosRetry from 'axios-retry'

import { API, REQUEST_TIMEOUT_MS } from '../constants/api'
import { APP_ROUTES } from '../constants/routes'
import { authStorage } from '../utils/storage'
import { getErrorMessage } from '../utils/errors'

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
    const requestUrl = (error.config?.url as string | undefined) ?? ''
    const isLoginRequest = requestUrl.endsWith(API.authLogin)
    const message = getErrorMessage(error)

    if (status === 401) {
      if (isLoginRequest) {
        return Promise.reject(new Error(message || 'Invalid email or password.'))
      }

      authStorage.clearToken()
      if (window.location.pathname !== APP_ROUTES.login) {
        window.location.assign(APP_ROUTES.login)
      }
      return Promise.reject(new Error('Session expired. Please login again.'))
    }

    return Promise.reject(new Error(message))
  },
)



