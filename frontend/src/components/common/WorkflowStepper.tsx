import { CheckCircle2, Circle } from 'lucide-react'

interface WorkflowStep {
  label: string
  done: boolean
}

interface WorkflowStepperProps {
  steps: WorkflowStep[]
}

export const WorkflowStepper = ({ steps }: WorkflowStepperProps) => (
  <div className="mb-6 overflow-x-auto rounded-xl border border-slate-200 bg-white p-3">
    <ol className="flex min-w-max items-center gap-4">
      {steps.map((step, index) => (
        <li key={step.label} className="flex items-center gap-2 text-sm">
          {step.done ? (
            <CheckCircle2 size={16} className="text-emerald-600" />
          ) : (
            <Circle size={16} className="text-slate-400" />
          )}
          <span className={step.done ? 'text-slate-900' : 'text-slate-500'}>{step.label}</span>
          {index < steps.length - 1 ? <span className="ml-2 text-slate-300">→</span> : null}
        </li>
      ))}
    </ol>
  </div>
)

