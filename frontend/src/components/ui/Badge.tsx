import type { ReactNode } from 'react'

import { cn } from '../../utils/cn'

type BadgeTone = 'green' | 'yellow' | 'red' | 'slate'

interface BadgeProps {
  children: ReactNode
  tone?: BadgeTone
}

const toneClassMap: Record<BadgeTone, string> = {
  green: 'bg-emerald-50 text-emerald-700 border-emerald-200',
  yellow: 'bg-amber-50 text-amber-700 border-amber-200',
  red: 'bg-rose-50 text-rose-700 border-rose-200',
  slate: 'bg-slate-50 text-slate-700 border-slate-200',
}

export const Badge = ({ children, tone = 'slate' }: BadgeProps) => (
  <span
    className={cn(
      'inline-flex rounded-full border px-2.5 py-1 text-xs font-medium uppercase tracking-wide',
      toneClassMap[tone],
    )}
  >
    {children}
  </span>
)

