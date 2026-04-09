import type { ButtonHTMLAttributes } from 'react'

import { cn } from '../../utils/cn'

type ButtonVariant = 'primary' | 'secondary' | 'danger' | 'ghost'

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant
}

const variantClassMap: Record<ButtonVariant, string> = {
  primary:
    'bg-slate-900 text-white hover:bg-slate-800 disabled:bg-slate-400 disabled:cursor-not-allowed',
  secondary:
    'bg-white text-slate-900 border border-slate-300 hover:bg-slate-50 disabled:text-slate-400 disabled:cursor-not-allowed',
  danger:
    'bg-rose-600 text-white hover:bg-rose-500 disabled:bg-rose-300 disabled:cursor-not-allowed',
  ghost: 'bg-transparent text-slate-700 hover:bg-slate-100',
}

export const Button = ({
  className,
  variant = 'primary',
  type = 'button',
  ...props
}: ButtonProps) => (
  <button
    type={type}
    className={cn(
      'inline-flex items-center justify-center rounded-lg px-4 py-2 text-sm font-medium transition-colors',
      variantClassMap[variant],
      className,
    )}
    {...props}
  />
)

