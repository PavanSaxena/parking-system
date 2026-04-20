import type { ApiErrorResponse } from '../types/common'

interface ErrorCandidate {
  code?: string
  message?: string
  request?: unknown
  response?: {
    status?: number
    data?: ApiErrorResponse
  }
}

const extractBackendMessage = (payload?: ApiErrorResponse) => {
  if (!payload) {
    return null
  }
  if (payload.error?.trim()) {
    return payload.error.trim()
  }
  if (payload.message?.trim()) {
    return payload.message.trim()
  }
  if (payload.details?.length) {
    return payload.details[0]
  }
  if (payload.errors?.length) {
    return payload.errors[0]
  }
  return null
}

const statusMessageMap: Record<number, string> = {
  400: 'Invalid request. Please check the entered details.',
  401: 'Unauthorized request. Please sign in and try again.',
  403: 'You do not have permission to perform this action.',
  404: 'Requested resource was not found.',
  409: 'This action conflicts with existing data.',
  422: 'Validation failed. Please correct highlighted fields.',
  429: 'Too many requests. Please wait and retry.',
  500: 'Server error. Please try again in a moment.',
  502: 'Service is temporarily unavailable. Please retry shortly.',
  503: 'Service is temporarily unavailable. Please retry shortly.',
  504: 'Request timed out. Please try again.',
}

export const getErrorMessage = (error: unknown) => {
  if (typeof error === 'string') {
    return error
  }

  if (error instanceof Error && error.message.trim()) {
    return error.message
  }

  const candidate = error as ErrorCandidate

  if (candidate.code === 'ECONNABORTED') {
    return 'Request timed out. Please check your connection and try again.'
  }

  if (!candidate.response && candidate.request) {
    return 'Unable to reach the server. Check backend status and network connection.'
  }

  const backendMessage = extractBackendMessage(candidate.response?.data)
  if (backendMessage) {
    return backendMessage
  }

  if (candidate.response?.status !== undefined) {
    return statusMessageMap[candidate.response.status] ?? 'Request failed. Please try again.'
  }

  if (
    typeof error === 'object' &&
    error !== null &&
    'message' in error &&
    typeof (error as { message?: string }).message === 'string'
  ) {
    return (error as { message: string }).message
  }


  return 'Something went wrong. Please try again.'
}

