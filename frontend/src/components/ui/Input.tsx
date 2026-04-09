import type { InputHTMLAttributes } from 'react'

import { cn } from '../../utils/cn'

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string
  error?: string
}

export const Input = ({ label, error, className, id, ...props }: InputProps) => {
  const elementId = id ?? label.toLowerCase().replace(/\s+/g, '-')

  return (
    <label htmlFor={elementId} className="flex flex-col gap-1.5 text-sm font-medium text-slate-700">
      <span>{label}</span>
      <input
        id={elementId}
        className={cn(
          'w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 outline-none transition focus:border-slate-500',
          error && 'border-rose-400 focus:border-rose-400',
          className,
        )}
        {...props}
      />
      {error ? <span className="text-xs text-rose-600">{error}</span> : null}
    </label>
  )
}

