# Parking Management System — Deployment Plan

**Goal:** Take the existing Spring Boot + MySQL project from "runs on my laptop" to "tested, containerized, live on the internet, and auto-deploying" — with a resume bullet and a demo link to show for it.

This plan is broken into 5 phases. At each step, options are ranked roughly from **fastest/easiest** to **most impressive/production-grade**. Pick one per phase based on how much time you have.

---

## Phase 1: Testing

Goal: prove the backend logic is correct and get a coverage number you can cite.

| Option | What it covers | Effort | Notes |
|---|---|---|---|
| **JUnit 5 + Mockito (unit tests)** | Service layer logic — slot allocation, fee calculation, ticket validation | Low (1 evening) | Mock the repository layer so tests don't need a real DB. Start here regardless of what else you pick. |
| **@WebMvcTest (controller tests)** | REST endpoint behavior, request/response shapes, validation errors | Low-Medium | Uses MockMvc; no server actually starts. Good for testing your DTOs and `@RestControllerAdvice` exception handling. |
| **@SpringBootTest + H2 in-memory DB** | Full integration test — controller → service → repository → DB | Medium | Fast to run since H2 is in-memory, but doesn't behave 100% like MySQL (different SQL dialect quirks). |
| **@SpringBootTest + Testcontainers (real MySQL in Docker)** | Same as above but against an actual MySQL container | Medium-High | Most realistic option. Doubles as your first hands-on Docker experience, which you need for Phase 2 anyway. |
| **JaCoCo coverage report** | Generates a % coverage number and HTML report | Low (just a Maven plugin) | Add regardless of which testing option above you pick — this is what gives you the "80%+ coverage" line for your resume. |

**Recommended combo:** JUnit + Mockito for services, @WebMvcTest for controllers, JaCoCo for the coverage number. Add Testcontainers only if you have extra time — it's the most "senior" looking choice.

---

## Phase 2: Containerization

Goal: package the app so it runs identically anywhere.

| Option | Description | Effort |
|---|---|---|
| **Single Dockerfile (multi-stage build)** | Maven build stage → slim JRE runtime stage. Produces one image for the Spring Boot app. | Low |
| **Docker Compose (app + MySQL)** | Adds a `docker-compose.yml` that spins up your app *and* a MySQL container together with one command | Low-Medium | Strongly recommended — lets anyone (including an interviewer) run the whole thing with `docker compose up`. |
| **Docker Compose + Nginx reverse proxy** | Adds Nginx in front for TLS/routing | Medium | Only worth it if you want to self-host on a raw VM (see Phase 4 options further down). |

**Recommended:** Dockerfile + docker-compose (app + DB). This is the single highest-leverage step — it's required for almost every cloud option below anyway.

---

## Phase 3: CI (automated testing on every push)

Goal: tests run automatically so you can say "CI/CD" truthfully, not just "I deployed it once."

| Option | Cost | Effort | Notes |
|---|---|---|---|
| **GitHub Actions** | Free for public repos | Low | The default choice — most recruiters/interviewers recognize it immediately, and it's zero-infrastructure (config lives in your repo as `.github/workflows/ci.yml`). |
| **GitLab CI** | Free tier available | Low-Medium | Only makes sense if you migrate the repo to GitLab; not worth it just for this. |
| **Jenkins (self-hosted)** | Free but needs a server to run on | High | More "enterprise," but overkill for a portfolio project and costs setup time you don't need to spend. |
| **CircleCI** | Free tier available | Low-Medium | Fine alternative to GitHub Actions but no real advantage since you're already on GitHub. |

**Recommended:** GitHub Actions. Have it run `mvn test` (and the JaCoCo report) on every push and PR.

---

## Phase 4: Cloud Deployment

Goal: a live URL you can put on your resume and LinkedIn.

### App hosting options

| Option | Cost | Effort | Impressiveness | Notes |
|---|---|---|---|---|
| **Render / Railway** | Free tier | Low (30-60 min) | Medium | Connect GitHub repo, it detects the Dockerfile, builds and deploys automatically. Best option if your priority is *getting something live fast*. |
| **Fly.io** | Free tier (small apps) | Low-Medium | Medium-High | Similar ease to Render/Railway but slightly more "real infra" feel (you interact with a CLI, regions, etc.) |
| **AWS Elastic Beanstalk** | Free tier (12 months) | Medium | High | Managed but still "AWS" on your resume, which carries more weight with recruiters than Render/Railway. Handles load balancing, scaling, health checks for you. |
| **AWS ECS/Fargate** | Free tier limited | High | Highest | Real container orchestration — closest to how production systems actually run. Worth it only if you have a free weekend and want the deepest learning/resume signal. |
| **Azure App Service** | Free tier available | Medium | High | Equivalent to Beanstalk; pick this instead of AWS only if you're targeting companies that are visibly Azure-shops. |
| **Google Cloud Run** | Generous free tier | Low-Medium | High | Serverless containers — genuinely one of the easiest "real cloud" options once your app is Dockerized. Scales to zero, so effectively free for a demo project. |

### Database hosting options

| Option | Cost | Effort | Notes |
|---|---|---|---|
| **AWS RDS (MySQL/Postgres)** | Free tier (12 months) | Low | Pairs naturally if you deploy the app on AWS. |
| **Railway/Render managed Postgres** | Free tier | Very low | Easiest if you're already using Railway/Render for the app — same dashboard. |
| **Supabase / Neon (managed Postgres)** | Free tier | Low | Popular with early-career devs right now; also gives you a nice dashboard to screenshot. |
| **Self-hosted DB in the same Docker Compose** | Free | Low | Simplest technically, but the database dies whenever the free-tier container sleeps/restarts — fine for a demo, not for "production-grade" credibility. |

**Recommended fast-track:** Fly.io or Render for the app + their built-in managed Postgres. Total setup: under an hour, free, and gives you a real public URL.

**Recommended "impress a recruiter" track:** AWS Elastic Beanstalk (app) + AWS RDS (DB). Takes a weekend, but "deployed on AWS with RDS" is a stronger resume line than "deployed on Render."

---

## Phase 5: CD (auto-deploy on push)

Goal: pushing to `main` automatically updates the live app — this is what makes it "CI/**CD**" rather than just CI.

| Option | Effort | Notes |
|---|---|---|
| **Provider's native Git integration** (Render, Railway, Fly.io, Azure App Service via GitHub Actions deploy) | Low | These platforms watch your GitHub repo and redeploy automatically on push — often just a checkbox, no pipeline code needed. |
| **GitHub Actions deploy step** (build → push image to a registry → deploy to AWS/Azure/GCP) | Medium | Add a job to the same `ci.yml` (or a separate `cd.yml`) that runs *after* tests pass — e.g., `docker build` → push to Docker Hub or Amazon ECR → trigger an Elastic Beanstalk/ECS deployment. |
| **Manual deploy trigger** | Low | Acceptable fallback if you're short on time — you still get "CI" (automated tests) truthfully, just say "manual deployment" rather than full CD. |

**Recommended:** If you went with Render/Railway/Fly.io in Phase 4, their native Git integration gives you CD almost for free. If you went the AWS route, add a deploy job to your GitHub Actions workflow that only runs after tests pass.

---

## Suggested Paths (pick one)

### 🟢 Weekend Fast-Track (~4-6 hours total)
1. JUnit + Mockito + @WebMvcTest + JaCoCo
2. Dockerfile + docker-compose
3. GitHub Actions (runs tests on push)
4. Deploy app + DB on Railway or Render (free tier, auto-deploys from GitHub)

**Resume line:** *"Containerized and deployed the application to Railway with automated CI via GitHub Actions and 80%+ test coverage (JUnit, Mockito)."*

### 🔵 Resume-Maximizing Track (~1-2 weekends)
1. JUnit + Mockito + @WebMvcTest + Testcontainers + JaCoCo
2. Dockerfile + docker-compose
3. GitHub Actions (test → build image → push to Amazon ECR)
4. Deploy on AWS Elastic Beanstalk + RDS MySQL, deploy step wired into the same GitHub Actions pipeline

**Resume line:** *"Implemented a full CI/CD pipeline (GitHub Actions) building and deploying a containerized Spring Boot service to AWS Elastic Beanstalk with RDS, backed by a JUnit/Testcontainers suite achieving 85%+ coverage."*

---

## Order of Operations

1. Write tests first (Phase 1) — you want a green test suite *before* you containerize, so you know Docker isn't the thing breaking things.
2. Containerize (Phase 2) and confirm `docker compose up` works locally.
3. Set up CI (Phase 3) so tests run in GitHub Actions on your next push.
4. Deploy manually once (Phase 4) to confirm the cloud target actually works.
5. Wire up CD (Phase 5) last, once you trust the manual deploy.

This order avoids the common trap of debugging Docker, cloud config, and CI all at once.

Once any track is done, I can help you write the actual `Dockerfile`, `docker-compose.yml`, the JUnit test files, or the GitHub Actions YAML — just tell me which piece you want to start with.
