import type { SelectHTMLAttributes } from 'react'

import type { SelectOption } from '../../types/common'
import { cn } from '../../utils/cn'

interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label: string
  options: SelectOption[]
  error?: string
}

export const Select = ({ label, options, className, error, id, ...props }: SelectProps) => {
  const elementId = id ?? label.toLowerCase().replace(/\s+/g, '-')

  return (
    <label htmlFor={elementId} className="flex flex-col gap-1.5 text-sm font-medium text-slate-700">
      <span>{label}</span>
      <select
        id={elementId}
        className={cn(
          'w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 outline-none transition focus:border-slate-500',
          error && 'border-rose-400 focus:border-rose-400',
          className,
        )}
        {...props}
      >
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
      {error ? <span className="text-xs text-rose-600">{error}</span> : null}
    </label>
  )
}

