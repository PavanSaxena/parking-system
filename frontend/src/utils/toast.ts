export type ToastVariant = 'success' | 'error'

export interface ToastPayload {
  message: string
  variant: ToastVariant
}

const TOAST_EVENT_NAME = 'app-toast'

export const toast = {
  success: (message: string) => {
    window.dispatchEvent(
      new CustomEvent<ToastPayload>(TOAST_EVENT_NAME, {
        detail: { message, variant: 'success' },
      }),
    )
  },
  error: (message: string) => {
    window.dispatchEvent(
      new CustomEvent<ToastPayload>(TOAST_EVENT_NAME, {
        detail: { message, variant: 'error' },
      }),
    )
  },
}

export const toastEventName = TOAST_EVENT_NAME

