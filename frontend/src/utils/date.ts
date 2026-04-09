export const isWithinDateRange = (
  dateValue: string,
  fromDate?: string,
  toDate?: string,
) => {
  if (!fromDate && !toDate) {
    return true
  }

  const date = new Date(dateValue)
  if (Number.isNaN(date.getTime())) {
    return false
  }

  const from = fromDate ? new Date(`${fromDate}T00:00:00`) : new Date('1970-01-01T00:00:00')
  const to = toDate ? new Date(`${toDate}T23:59:59`) : new Date('2999-12-31T23:59:59')

  return date >= from && date <= to
}


