Project Title

Parking Management System using Spring Boot MVC Architecture

---

Brief Description of the Project

This project is a parking operations management system built with Spring Boot (backend REST API) and a React frontend.

Purpose of system:
- Manage end-to-end parking flow from vehicle onboarding to payment and reporting.
- Provide a structured workflow for slot administration, parking ticket lifecycle, and revenue visibility.

Problem solved:
- Manual parking operations are error-prone for slot assignment, vehicle tracking, and billing.
- The system centralizes slot status, active tickets, payment records, and occupancy/revenue metrics.

Actors involved:
- Vehicle Owner: vehicle is associated with owner data and can be registered before parking.
- Parking Operator: handles parking entry/exit and authentication for system access.
- Administrator: manages parking slots and parking rates.

Main functionalities:
- Vehicle entry:
  - Vehicle is checked and parking entry is created through /parking/entry and /tickets/entry.
- Slot allocation:
  - Available slots are queried and assigned via service logic using slot status and type.
- Ticket generation:
  - New parking ticket records are created with entry time, vehicle, slot, and operator.
- Parking fee calculation:
  - Fee calculated using ticket duration and vehicle-type rate in FeeService.
- Payment processing:
  - Payment is created after vehicle exit using /payments.
- Report generation:
  - Tickets, active sessions, payments, revenue, and occupancy are exposed through /reports/*.

---

Explanation of MVC Architecture

Model

In this project, the Model side is implemented through entities, DTOs, repositories, services, and enums.

Entities:
- ParkingSlot
- ParkingTicket
- Payment
- Vehicle
- ParkingRate
- ParkingOperator
- User, VehicleOwner, Administrator

DTO classes:
- Request DTOs: ParkingEntryRequest, ParkingExitRequest, PaymentRequest, TicketCreateRequest, CreateVehicleRequest, CreateRateRequest, LoginRequestDTO
- Response DTOs: ParkingTicketResponse, PaymentResponse, SlotResponse, VehicleResponse, ParkingRateResponse, OccupancyResponse, SlotStatisticsResponse, ErrorResponse, LoginResponseDTO

Repository interfaces:
- ParkingSlotRepository
- ParkingTicketRepository
- PaymentRepository
- VehicleRepository
- ParkingRateRepository
- ParkingOperatorRepository
- VehicleOwnerRepository
- UserRepository

Service classes:
- ParkingService
- TicketService
- SlotService
- PaymentService
- FeeService
- ReportService
- VehicleService
- ParkingRateService
- AdminService
- AuthServiceImpl

Enums:
- SlotStatus (AVAILABLE, OCCUPIED)
- PaymentStatus (PENDING, COMPLETED, FAILED)

Role of JPA entities:
- Map Java classes to database tables.
- Define relationships (ticket-to-vehicle, ticket-to-slot, ticket-to-operator, payment-to-ticket, owner-to-vehicle).

Role of Service layer business logic:
- Enforces operational rules:
  - no duplicate active ticket for same vehicle
  - cannot pay before exit
  - slot availability checks
  - fee computed from duration and configured rate

Role of DTOs:
- Separate API contract from entity persistence model.
- Validate request payloads (@NotBlank, @Email).
- Shape API responses for frontend consumption.

Controller

Controllers present in project:
- AuthController
- AdminController
- VehicleController
- ParkingController
- TicketController
- SlotController
- PaymentController
- ParkingRateController
- ReportController

Responsibilities:
- Handle HTTP requests.
- Validate and parse request payloads/path params.
- Delegate business work to service classes.
- Return API responses (mapped through ResponseMapper).

Real controller mapping snippet:
```java
@RestController
@RequestMapping("/parking")
public class ParkingController {
    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entry")
    public ParkingTicketResponse enterVehicle(@Valid @RequestBody ParkingEntryRequest request){
        return ResponseMapper.toParkingTicketResponse(parkingService.enterVehicle(
                request.getVehicleNumber(),
                request.getOperatorId()
        ));
    }

    @PostMapping("/exit")
    public ParkingTicketResponse exitVehicle(@Valid @RequestBody ParkingExitRequest request){
        return ResponseMapper.toParkingTicketResponse(parkingService.exitVehicle(request.getTicketId()));
    }
}
```

@PathVariable usage example:
```java
@PutMapping("/{ticketId}/exit")
public ParkingTicketResponse closeTicket(@PathVariable("ticketId") String ticketId) {
    return ResponseMapper.toParkingTicketResponse(ticketService.closeTicket(ticketId));
}
```

View

Frontend implementation is a React SPA (under frontend) that consumes backend REST APIs.

React pages interacting with backend:
- LoginPage
- DashboardPage
- SlotsPage
- BookingPage
- ActiveParkingPage
- PaymentsPage
- ReportsPage

How UI communicates with backend (Axios):
- Uses axios via centralized apiClient.
- Base URL resolved from VITE_API_BASE_URL.
- Auth token attached in request interceptor.
- Retry and error handling configured in response pipeline.

Snippet:
```ts
export const apiClient = axios.create({
  baseURL: resolveBaseUrl(),
  timeout: REQUEST_TIMEOUT_MS,
})

apiClient.interceptors.request.use((config) => {
  const token = authStorage.getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})
```

Protected routes:
- Implemented through ProtectedRoute.
- Redirects unauthenticated users to login.

```tsx
export const ProtectedRoute = () => {
  const { isAuthenticated } = useAuth()
  if (!isAuthenticated) {
    return <Navigate to={APP_ROUTES.login} replace />
  }
  return <Outlet />
}
```

Role of components and pages:
- Pages implement workflow screens and API operations.
- Shared UI components (table, card, input, badges, layout) provide reusable presentation structure.
- Router composition (createBrowserRouter) defines navigation and guarded routes.

---

Database Design Details

Database structure is defined through JPA entities and relationships.

Tables and key attributes:

ParkingSlot:
- slotId (PK)
- slotType
- status (SlotStatus)

ParkingTicket:
- ticketId (PK)
- entryTime
- exitTime
- vehicle (FK via @ManyToOne)
- slot (FK via @ManyToOne)
- operator (FK via @ManyToOne)
- payment (inverse side of one-to-one)

Payment:
- paymentId (PK)
- amount
- paymentTime
- paymentStatus
- ticket (FK ticket_id via @OneToOne @JoinColumn)

Vehicle:
- vehicleNumber (PK)
- vehicleType
- owner (FK owner_id via @ManyToOne)
- tickets (@OneToMany)

ParkingRate:
- rateId (PK)
- vehicleType
- hourlyRate
- administrator (@ManyToOne)

ParkingOperator:
- userId (PK)
- userName
- email (unique)
- password

Primary keys and foreign keys:
- PKs are defined with @Id on string identifiers.
- FK relationships are generated from @ManyToOne and @OneToOne mappings.
- Explicit FK column is used in Payment (ticket_id) and Vehicle (owner_id).

Mapping annotation examples:
- @Entity: marks persistent classes.
- @Id: primary key field.
- @OneToMany: one parent to many children (e.g., owner to vehicles).
- @ManyToOne: many records pointing to one parent (e.g., tickets to slot/operator/vehicle).

Example entity snippet:
```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingTicket {
    @Id
    private String ticketId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private ParkingSlot slot;

    @ManyToOne
    private ParkingOperator operator;

    @JsonIgnore
    @OneToOne(mappedBy = "ticket")
    private Payment payment;
}
```

<placeholder: "ER Diagram showing relationships between ParkingSlot, ParkingTicket, Payment, Vehicle, ParkingRate">

---

Relevant Diagrams

<placeholder: "Use Case Diagram">
<placeholder: "Class Diagram">
<placeholder: "Activity Diagram">
<placeholder: "State Diagram">

---

Complete Spring Boot Code

Controller snippet
```java
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseMapper.toPaymentResponse(paymentService.processPayment(request.getTicketId()));
    }
}
```

Service snippet
```java
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ParkingTicketRepository ticketRepository;
    private final FeeService feeService;

    public PaymentService(PaymentRepository paymentRepository,
                          ParkingTicketRepository ticketRepository,
                          FeeService feeService) {
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.feeService = feeService;
    }

    @Transactional
    public Payment processPayment(String ticketId){
        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));

        if (ticket.getExitTime() == null) {
            throw new InvalidParkingStateException("Cannot process payment before vehicle exit");
        }

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setAmount(feeService.calculateFee(ticket));
        payment.setTicket(ticket);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentTime(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
}
```

Repository snippet
```java
@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'COMPLETED'")
    Double getTotalRevenue();

    java.util.Optional<Payment> findByTicketTicketId(String ticketId);
}
```

Model snippet
```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private String paymentId;
    private double amount;
    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "ticket_id")
    private ParkingTicket ticket;
}
```

DTO snippet
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingEntryRequest {
    @NotBlank
    private String vehicleNumber;

    @NotBlank
    private String operatorId;
}
```

Exception handling snippet
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({InvalidParkingStateException.class, PaymentProcessingException.class})
    public ResponseEntity<ErrorResponse> handleBusinessErrors(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandled(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Unexpected server error"));
    }
}
```

---

Database Configuration Details

MySQL configuration found in application.properties:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

How Spring Boot connects to MySQL:
- Spring Boot reads datasource credentials from environment-driven placeholders (DB_URL, DB_USER, DB_PASSWORD).
- MySQL JDBC driver (com.mysql.cj.jdbc.Driver) is used for database connectivity.
- Hibernate dialect is set to MySQL (org.hibernate.dialect.MySQLDialect) for SQL generation compatibility.

Role of Hibernate ORM:
- Maps entity classes to relational tables.
- Generates and executes SQL based on JPA mappings.
- Handles schema update behavior with spring.jpa.hibernate.ddl-auto=update.
- Logs SQL statements when spring.jpa.show-sql=true is enabled.

---
