import type { ApiErrorResponse } from '../types/common'

export const getErrorMessage = (error: unknown) => {
  if (typeof error === 'string') {
    return error
  }

  if (
    typeof error === 'object' &&
    error !== null &&
    'message' in error &&
    typeof (error as { message?: string }).message === 'string'
  ) {
    return (error as { message: string }).message
  }

  const candidate = error as { response?: { data?: ApiErrorResponse } }
  if (candidate.response?.data?.error) {
    return candidate.response.data.error
  }
  if (candidate.response?.data?.message) {
    return candidate.response.data.message
  }

  return 'Something went wrong. Please try again.'
}

