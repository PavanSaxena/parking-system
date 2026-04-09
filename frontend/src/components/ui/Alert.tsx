import { AlertTriangle } from 'lucide-react'

interface AlertProps {
  message: string
}

export const Alert = ({ message }: AlertProps) => (
  <div className="flex items-start gap-2 rounded-lg border border-amber-200 bg-amber-50 px-3 py-2 text-sm text-amber-700">
    <AlertTriangle size={16} className="mt-0.5" />
    <span>{message}</span>
  </div>
)

