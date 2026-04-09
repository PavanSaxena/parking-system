import type { ReactNode } from 'react'

import { Card } from './Card'

interface StatCardProps {
  label: string
  value: string
  icon: ReactNode
  hint?: ReactNode
}

export const StatCard = ({ label, value, icon, hint }: StatCardProps) => (
  <Card className="flex items-start justify-between gap-4">
    <div>
      <p className="text-sm text-slate-500">{label}</p>
      <p className="mt-2 text-2xl font-semibold text-slate-900">{value}</p>
      {hint ? <p className="mt-1 text-xs text-slate-500">{hint}</p> : null}
    </div>
    <div className="rounded-lg bg-slate-100 p-2 text-slate-700">{icon}</div>
  </Card>
)


