import { useMemo, useState } from 'react'

import { ApiState } from '../components/common/ApiState'
import { EmptyState } from '../components/ui/EmptyState'
import { Badge } from '../components/ui/Badge'
import { Button } from '../components/ui/Button'
import { Card } from '../components/ui/Card'
import { Input } from '../components/ui/Input'
import { PageHeader } from '../components/ui/PageHeader'
import { Select } from '../components/ui/Select'
import { Table } from '../components/ui/Table'
import { useBooking } from '../hooks/useBooking'
import { useWorkflowGuard } from '../hooks/useWorkflowGuard'
import type { VehicleType } from '../types/common'
import { formatCurrency, formatDateTime } from '../utils/format'

const vehicleTypeOptions = [
  { label: 'MINI', value: 'MINI' },
  { label: 'SEDAN', value: 'SEDAN' },
  { label: 'SUV', value: 'SUV' },
]

export const BookingPage = () => {
  const { activeBookingsQuery, availableSlotsQuery, createBookingMutation } = useBooking()

  const [createForm, setCreateForm] = useState({
    vehicleNumber: '',
    vehicleType: 'SEDAN' as VehicleType,
    slotId: '',
    operatorId: '',
  })
  const [errors, setErrors] = useState<{
    vehicleNumber?: string
    vehicleType?: string
    slotId?: string
    operatorId?: string
  }>({})

  const compatibleSlots = useMemo(
    () =>
      (availableSlotsQuery.data ?? []).filter((slot) => slot.vehicleType === createForm.vehicleType),
    [availableSlotsQuery.data, createForm.vehicleType],
  )

  const selectedSlot = compatibleSlots.find((slot) => slot.slotId === createForm.slotId)
  const workflowGuard = useWorkflowGuard({
    slots: availableSlotsQuery.data ?? [],
    activeBookings: activeBookingsQuery.data ?? [],
    completedBookings: [],
  })

  const handleCreateBooking = () => {
    const nextErrors: typeof errors = {}
    if (!createForm.vehicleNumber.trim()) {
      nextErrors.vehicleNumber = 'Vehicle number is required'
    }
    if (!createForm.slotId) {
      nextErrors.slotId = 'Select an available slot'
    }
    if (!createForm.operatorId.trim()) {
      nextErrors.operatorId = 'Operator ID is required'
    }

    setErrors(nextErrors)
    if (Object.keys(nextErrors).length > 0) {
      return
    }

    createBookingMutation.mutate(createForm, {
      onSuccess: () => {
        setCreateForm({
          vehicleNumber: '',
          vehicleType: 'SEDAN',
          slotId: '',
          operatorId: '',
        })
      },
    })
  }

  return (
    <section>
      <PageHeader
        title="Booking Creation"
        description="Step 2: create bookings by selecting available and type-compatible slots."
      />

      <div className="grid gap-4 xl:grid-cols-2">
        <Card>
          <h2 className="mb-4 text-base font-semibold text-slate-900">Create Booking</h2>
          <div className="space-y-3">
            <Input
              label="Vehicle Number"
              value={createForm.vehicleNumber}
              onChange={(event) =>
                setCreateForm((prev) => ({ ...prev, vehicleNumber: event.target.value }))
              }
              error={errors.vehicleNumber}
            />
            <Select
              label="Vehicle Type"
              options={vehicleTypeOptions}
              value={createForm.vehicleType}
              onChange={(event) =>
                setCreateForm((prev) => ({ ...prev, vehicleType: event.target.value as VehicleType }))
              }
            />
            <Select
              label="Available Slots"
              options={
                compatibleSlots.length > 0
                  ? compatibleSlots.map((slot) => ({
                      label: `${slot.slotNumber} (${formatCurrency(slot.pricePerHour)}/hr)`,
                      value: slot.slotId,
                    }))
                  : [{ label: 'No compatible slots', value: '' }]
              }
              value={createForm.slotId}
              onChange={(event) => setCreateForm((prev) => ({ ...prev, slotId: event.target.value }))}
              error={errors.slotId}
            />
            <Input
              label="Operator ID"
              value={createForm.operatorId}
              onChange={(event) =>
                setCreateForm((prev) => ({ ...prev, operatorId: event.target.value }))
              }
              error={errors.operatorId}
            />
            <div className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-700">
              Slot Price: {selectedSlot ? formatCurrency(selectedSlot.pricePerHour) : 'Select a slot'}
            </div>
            <Button
              className="w-full"
              onClick={handleCreateBooking}
              disabled={createBookingMutation.isPending}
            >
              {createBookingMutation.isPending ? 'Creating...' : 'Create Booking'}
            </Button>
          </div>
        </Card>

        <Card>
          <h2 className="mb-4 text-base font-semibold text-slate-900">Booking Preconditions</h2>
          <ul className="space-y-2 text-sm text-slate-700">
            <li>- Slot must be available ({workflowGuard.hasAvailableSlot ? 'OK' : 'Missing'})</li>
            <li>- Slot vehicle type must match booking vehicle type</li>
            <li>- Operator ID must exist in backend</li>
            <li>- Vehicle is auto-registered before ticket creation</li>
          </ul>
        </Card>
      </div>

      <div className="mt-6">
        <ApiState
          isLoading={activeBookingsQuery.isLoading}
          error={activeBookingsQuery.error ? activeBookingsQuery.error.message : null}
        >
          {(activeBookingsQuery.data ?? []).length === 0 ? (
            <EmptyState
              title="No active bookings"
              description="Once created, active bookings appear here before ending parking."
            />
          ) : (
            <Table headers={['Booking', 'Vehicle', 'Slot', 'Start Time', 'Status']}>
              {(activeBookingsQuery.data ?? []).map((booking) => (
                <tr key={booking.bookingId} className="bg-white">
                  <td className="px-4 py-3 font-medium text-slate-900">{booking.bookingId}</td>
                  <td className="px-4 py-3 text-slate-700">{booking.vehicleNumber}</td>
                  <td className="px-4 py-3 text-slate-700">{booking.slotId}</td>
                  <td className="px-4 py-3 text-slate-700">{formatDateTime(booking.startTime)}</td>
                  <td className="px-4 py-3">
                    <Badge tone="yellow">{booking.bookingStatus}</Badge>
                  </td>
                </tr>
              ))}
            </Table>
          )}
        </ApiState>
      </div>
    </section>
  )
}


