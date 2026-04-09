import { useQuery } from '@tanstack/react-query'

import { QUERY_KEYS } from '../constants/queryKeys'
import { reportService } from '../services/reportService'
import { slotService } from '../services/slotService'

export const useReports = () =>
  useQuery({
    queryKey: QUERY_KEYS.reports,
    queryFn: async () => {
      const slots = await slotService.getSlots()
      return reportService.getReports(slots)
    },
  })


