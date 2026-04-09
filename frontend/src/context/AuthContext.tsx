import { createContext, useCallback, useMemo, useState, type ReactNode } from 'react'

import { authService } from '../services/authService'
import type { LoginRequest } from '../types/common'
import { authStorage } from '../utils/storage'

interface AuthContextValue {
  token: string | null
  isAuthenticated: boolean
  login: (payload: LoginRequest) => Promise<void>
  logout: () => void
}

export const AuthContext = createContext<AuthContextValue | null>(null)

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(authStorage.getToken())

  const login = useCallback(async (payload: LoginRequest) => {
    const response = await authService.login(payload)
    authStorage.setToken(response.token)
    setToken(response.token)
  }, [])

  const logout = useCallback(() => {
    authStorage.clearToken()
    setToken(null)
  }, [])

  const value = useMemo(
    () => ({ token, isAuthenticated: Boolean(token), login, logout }),
    [token, login, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

