# Parking System Frontend

Production-quality React + TypeScript dashboard for the Spring Boot Parking System backend.

## Tech Stack

- Vite + React + TypeScript
- Tailwind CSS
- React Router
- React Query
- Axios with timeout + retries

## Environment Configuration

Copy the sample environment file:

```bash
cp .env.example .env
```

Default variable:

```dotenv
VITE_API_BASE_URL=http://localhost:8080/api
```

During local development, Vite proxy rewrites `/api/*` to `http://localhost:8080/*`.

## Install and Run

```bash
npm install
npm run dev
```

## Application Routes

- `/`
- `/dashboard`
- `/slots`
- `/booking`
- `/payments`
- `/reports`

## Project Structure

```text
src/
  app/
  components/
  constants/
  hooks/
  pages/
  services/
  styles/
  types/
  utils/
```

## Backend Connection Notes

- Ensure backend is running on `http://localhost:8080`.
- API endpoints are centralized in `src/constants/api.ts`.
- Components do not call HTTP directly; they use hooks + service abstractions.

