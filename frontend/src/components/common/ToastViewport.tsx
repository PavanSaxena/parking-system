import { useEffect, useMemo, useState } from 'react'

import { toastEventName, type ToastPayload } from '../../utils/toast'

interface ToastItem extends ToastPayload {
  id: string
}

export const ToastViewport = () => {
  const [items, setItems] = useState<ToastItem[]>([])

  useEffect(() => {
    const handleToast = (event: Event) => {
      const customEvent = event as CustomEvent<ToastPayload>
      const id = crypto.randomUUID()
      setItems((prev) => [...prev, { id, ...customEvent.detail }])

      window.setTimeout(() => {
        setItems((prev) => prev.filter((item) => item.id !== id))
      }, 3500)
    }

    window.addEventListener(toastEventName, handleToast)
    return () => window.removeEventListener(toastEventName, handleToast)
  }, [])

  const rendered = useMemo(
    () =>
      items.map((item) => (
        <div
          key={item.id}
          className={`rounded-lg border px-4 py-2 text-sm shadow-sm ${
            item.variant === 'success'
              ? 'border-emerald-200 bg-emerald-50 text-emerald-700'
              : 'border-rose-200 bg-rose-50 text-rose-700'
          }`}
        >
          {item.message}
        </div>
      )),
    [items],
  )

  return <div className="fixed right-4 top-4 z-50 flex w-80 max-w-[calc(100vw-2rem)] flex-col gap-2">{rendered}</div>
}

