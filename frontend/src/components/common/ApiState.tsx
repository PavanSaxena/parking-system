import { AlertCircle } from 'lucide-react'
import type { ReactNode } from 'react'

import { Spinner } from '../ui/Spinner'

interface ApiStateProps {
  isLoading: boolean
  error: string | null
  children: ReactNode
}

export const ApiState = ({ isLoading, error, children }: ApiStateProps) => {
  if (isLoading) {
    return (
      <div className="flex min-h-36 items-center justify-center rounded-xl border border-slate-200 bg-white">
        <Spinner />
      </div>
    )
  }

  if (error) {
    return (
      <div className="flex min-h-36 items-center gap-2 rounded-xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">
        <AlertCircle size={16} />
        <span>{error}</span>
      </div>
    )
  }

  return <>{children}</>
}

