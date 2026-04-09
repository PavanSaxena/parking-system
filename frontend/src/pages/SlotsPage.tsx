import { useMemo, useState } from 'react'

import { ApiState } from '../components/common/ApiState'
import { Alert } from '../components/ui/Alert'
import { Badge } from '../components/ui/Badge'
import { Button } from '../components/ui/Button'
import { Card } from '../components/ui/Card'
import { EmptyState } from '../components/ui/EmptyState'
import { Input } from '../components/ui/Input'
import { Modal } from '../components/ui/Modal'
import { PageHeader } from '../components/ui/PageHeader'
import { Select } from '../components/ui/Select'
import { Table } from '../components/ui/Table'
import { useSlots } from '../hooks/useSlots'
import type { SlotStatus, VehicleType } from '../types/common'
import type { SlotViewModel } from '../types/slot'
import { formatCurrency } from '../utils/format'

const statusOptions = [
  { label: 'Available', value: 'AVAILABLE' },
  { label: 'Occupied', value: 'OCCUPIED' },
]

const vehicleTypeOptions = [
  { label: 'MINI', value: 'MINI' },
  { label: 'SEDAN', value: 'SEDAN' },
  { label: 'SUV', value: 'SUV' },
]

export const SlotManagementPage = () => {
  const { slotsQuery, createSlotMutation, updateSlotMutation, deleteSlotMutation } = useSlots()

  const [slotNumber, setSlotNumber] = useState('')
  const [vehicleType, setVehicleType] = useState<VehicleType>('SEDAN')
  const [pricePerHour, setPricePerHour] = useState('')
  const [statusFilter, setStatusFilter] = useState<'ALL' | SlotStatus>('ALL')
  const [editing, setEditing] = useState<SlotViewModel | null>(null)
  const [errors, setErrors] = useState<{ slotNumber?: string; pricePerHour?: string }>({})

  const filteredSlots = useMemo(() => {
    const slots = slotsQuery.data ?? []
    if (statusFilter === 'ALL') {
      return slots
    }
    return slots.filter((slot) => slot.status === statusFilter)
  }, [slotsQuery.data, statusFilter])

  const handleCreate = () => {
    const nextErrors: { slotNumber?: string; pricePerHour?: string } = {}
    if (!slotNumber.trim()) {
      nextErrors.slotNumber = 'Slot number is required'
    }

    const numericPrice = Number(pricePerHour)
    if (Number.isNaN(numericPrice) || numericPrice <= 0) {
      nextErrors.pricePerHour = 'Price per hour must be greater than zero'
    }

    setErrors(nextErrors)
    if (Object.keys(nextErrors).length > 0) {
      return
    }

    createSlotMutation.mutate(
      {
        slotNumber: slotNumber.trim(),
        vehicleType,
        pricePerHour: numericPrice,
      },
      {
        onSuccess: () => {
          setSlotNumber('')
          setVehicleType('SEDAN')
          setPricePerHour('')
        },
      },
    )
  }

  return (
    <section>
      <PageHeader
        title="Slot Management"
        description="Step 1: create and maintain parking inventory before booking workflows."
      />

      <div className="mb-6 grid gap-4 lg:grid-cols-4">
        <Card className="lg:col-span-3">
          <div className="grid gap-3 md:grid-cols-4">
            <Input
              label="Slot Number"
              placeholder="A-101"
              value={slotNumber}
              onChange={(event) => setSlotNumber(event.target.value)}
              error={errors.slotNumber}
            />
            <Select
              label="Vehicle Type"
              options={vehicleTypeOptions}
              value={vehicleType}
              onChange={(event) => setVehicleType(event.target.value as VehicleType)}
            />
            <Input
              label="Price Per Hour"
              type="number"
              min={1}
              value={pricePerHour}
              onChange={(event) => setPricePerHour(event.target.value)}
              error={errors.pricePerHour}
            />
            <div className="flex items-end">
              <Button className="w-full" disabled={createSlotMutation.isPending} onClick={handleCreate}>
                {createSlotMutation.isPending ? 'Creating...' : 'Create Slot'}
              </Button>
            </div>
          </div>
        </Card>

        <Card>
          <Select
            label="Filter"
            options={[{ label: 'All', value: 'ALL' }, ...statusOptions]}
            value={statusFilter}
            onChange={(event) => setStatusFilter(event.target.value as 'ALL' | SlotStatus)}
          />
        </Card>
      </div>

      {slotsQuery.data && slotsQuery.data.length === 0 ? (
        <Alert message="No slots found. Create at least one slot before creating bookings." />
      ) : null}

      <ApiState isLoading={slotsQuery.isLoading} error={slotsQuery.error ? slotsQuery.error.message : null}>
        {filteredSlots.length === 0 ? (
          <EmptyState title="No slots match this filter" description="Try changing the status filter." />
        ) : (
          <Table headers={['Slot', 'Vehicle Type', 'Price', 'Status', 'Actions']}>
            {filteredSlots.map((slot) => (
              <tr key={slot.slotId} className="bg-white">
                <td className="px-4 py-3 font-medium text-slate-900">{slot.slotNumber}</td>
                <td className="px-4 py-3 text-slate-700">{slot.vehicleType}</td>
                <td className="px-4 py-3 text-slate-700">{formatCurrency(slot.pricePerHour)}</td>
                <td className="px-4 py-3">
                  <Badge tone={slot.status === 'AVAILABLE' ? 'green' : 'yellow'}>{slot.status}</Badge>
                </td>
                <td className="px-4 py-3">
                  <div className="flex gap-2">
                    <Button variant="secondary" onClick={() => setEditing(slot)}>
                      Edit
                    </Button>
                    <Button
                      variant="danger"
                      onClick={() => deleteSlotMutation.mutate(slot.slotId)}
                      disabled={deleteSlotMutation.isPending}
                    >
                      Delete
                    </Button>
                  </div>
                </td>
              </tr>
            ))}
          </Table>
        )}
      </ApiState>

      <Modal
        isOpen={editing !== null}
        onClose={() => setEditing(null)}
        title="Edit Slot"
        description="Currently backend supports status updates from this screen."
      >
        {editing ? (
          <div className="space-y-4">
            <Select
              label="Status"
              options={statusOptions}
              value={editing.status}
              onChange={(event) => setEditing({ ...editing, status: event.target.value as SlotStatus })}
            />
            <Button
              className="w-full"
              onClick={() => {
                updateSlotMutation.mutate(
                  {
                    slotId: editing.slotId,
                    status: editing.status,
                  },
                  {
                    onSuccess: () => setEditing(null),
                  },
                )
              }}
              disabled={updateSlotMutation.isPending}
            >
              {updateSlotMutation.isPending ? 'Saving...' : 'Save Changes'}
            </Button>
          </div>
        ) : null}
      </Modal>
    </section>
  )
}

export const SlotsPage = SlotManagementPage


