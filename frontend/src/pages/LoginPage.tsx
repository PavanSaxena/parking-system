import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { Button } from '../components/ui/Button'
import { Card } from '../components/ui/Card'
import { Input } from '../components/ui/Input'
import { APP_ROUTES } from '../constants/routes'
import { useAuth } from '../hooks/useAuth'
import { getErrorMessage } from '../utils/errors'
import { toast } from '../utils/toast'

export const LoginPage = () => {
  const navigate = useNavigate()
  const { login } = useAuth()

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async () => {
    if (!email.trim() || !password.trim()) {
      setError('Email and password are required')
      return
    }

    setError(null)
    setLoading(true)

    try {
      await login({ email, password })
      toast.success('Login successful')
      navigate(APP_ROUTES.dashboard)
    } catch (requestError) {
      const message = getErrorMessage(requestError)
      setError(message)
      toast.error(message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-100 p-4">
      <Card className="w-full max-w-md">
        <h1 className="text-2xl font-semibold text-slate-900">Parking Admin Login</h1>
        <p className="mt-1 text-sm text-slate-500">Sign in to access dashboard workflows</p>

        <div className="mt-5 space-y-3">
          <Input label="Email" type="email" value={email} onChange={(event) => setEmail(event.target.value)} />
          <Input
            label="Password"
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />
          {error ? <p className="text-sm text-rose-600">{error}</p> : null}
          <Button className="w-full" onClick={handleSubmit} disabled={loading}>
            {loading ? 'Signing in...' : 'Login'}
          </Button>
        </div>
      </Card>
    </div>
  )
}

