# Payment Transfer API — Project Status

## Current state
- **Latest step completed:** Fase 1 completa — 3 microservicios funcionando con Kafka + CI/CD en GitHub
- **All services running:** yes (payment-service, account-service, notification-service)
- **CI/CD:** GitHub Actions — 3 jobs en verde
- **Repo:** https://github.com/PaulGallegos/payment-transfer-api

## Plan de 9 meses — progreso general
- **Total completado:** ~27%
- **Tiempo invertido:** Fase 1 (~120 hrs estimadas)
- **Tiempo restante:** ~420 hrs (~7 meses a 2 hrs/día)

---

## Fase 1 — Base técnica y GitHub ✅ COMPLETA

### payment-service (port 8082)
- [x] Entidad `Payment` con JPA y convenciones snake_case
- [x] Enum `PaymentStatus` (PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED)
- [x] `PaymentRepository` con queries paginadas
- [x] Excepciones: `PaymentNotFoundException`, `DuplicatePaymentException`
- [x] `GlobalExceptionHandler` con ProblemDetail RFC 9457
- [x] DTOs: `CreatePaymentRequest` con validaciones, `PaymentResponse`
- [x] `PaymentService` con idempotency check y `@Transactional`
- [x] `PaymentController` — 3 endpoints REST
  - POST `/api/v1/payments` — crear pago (201)
  - GET `/api/v1/payments/{id}` — buscar por ID con autorización
  - GET `/api/v1/payments/history` — historial paginado
- [x] Kafka producer: `PaymentEvent`, `PaymentEventProducer`
- [x] Publica a topic `payment.initiated`
- [x] Tests unitarios: `PaymentServiceTest` — 2 tests en verde
- [x] `application.properties` configurado
- [x] Docker: PostgreSQL + Kafka + Zookeeper
- [x] Flyway migration: `V1__create_payments_table.sql`
- [x] GitHub Actions CI — build + test en verde

### account-service (port 8081)
- [x] Entidad `Account` con JPA
- [x] Enum `AccountStatus` (ACTIVE, INACTIVE, SUSPENDED, CLOSED)
- [x] `AccountRepository` con queries por userId y accountNumber
- [x] Excepciones: `AccountNotFoundException`, `AccountAlreadyExistsException`
- [x] `GlobalExceptionHandler` con ProblemDetail
- [x] DTOs: `CreateAccountRequest`, `AccountResponse`
- [x] `AccountService` con generación de accountNumber (ACC-XXXXXXXX)
- [x] `AccountController` — 2 endpoints REST
  - POST `/api/v1/accounts` — crear cuenta (201)
  - GET `/api/v1/accounts/me` — obtener cuenta del usuario
- [x] Kafka consumer: `PaymentEvent`, `PaymentEventConsumer`
- [x] Consume topic `payment.initiated` y actualiza balance
- [x] `KafkaConsumerConfig` con `ErrorHandlingDeserializer`
- [x] Base de datos separada: `accounts_db` (database per service)
- [x] GitHub Actions CI — build en verde

### notification-service (port 8083)
- [x] Kafka consumer: `PaymentEvent`, `NotificationEventConsumer`
- [x] Consume `payment.initiated` y `payment.completed`
- [x] `KafkaConsumerConfig`
- [x] GitHub Actions CI — build en verde

### Infraestructura
- [x] Docker Compose: PostgreSQL, Kafka, Zookeeper, Redis
- [x] README con badge CI, tech stack, arquitectura, endpoints, curl examples
- [x] Diagrama Mermaid en README renderizado en GitHub
- [x] CI/CD — 3 jobs independientes en GitHub Actions

---

## Fase 2 — Profundidad técnica 🔄 EN CURSO (5%)

### System Design practice
- [x] "Walk me through your architecture" — practicado 2 veces
- [x] "How do you handle payment failures?" — practicado
- [x] "Why Kafka over REST?" — practicado
- [ ] Grabación en audio para práctica diaria

### auth-service (port 8084) — EN PROGRESO
- [x] Entidad `User`
- [x] JWT generation y validation
- [ ] Register endpoint
- [ ] Login endpoint
- [ ] Refresh token con Redis
- [ ] Rate limiting por IP
- [ ] Tests al 90%+

### Pendiente Fase 2
- [ ] System Design: caching, load balancing, DB indexing
- [ ] LeetCode: 3 problemas/semana nivel medium
- [ ] Mock interviews en inglés (Pramp o ChatGPT)
- [ ] Podcasts técnicos: Syntax.fm, Software Engineering Daily

---

## Fase 3 — Preparación y aplicaciones ⬜ NO INICIADA

- [ ] CV adaptado en inglés para Mercari, Rakuten, PayPay
- [ ] LinkedIn optimizado con links a GitHub
- [ ] Aplicar en TokyoDev y Japan Dev (5-8 apps/semana)
- [ ] Proyecto 3 opcional: fintech japonés o pagos

---

## Fase 4 — Entrevistas y cierre ⬜ NO INICIADA

- [ ] Tracker de aplicaciones (Notion o hoja)
- [ ] Preparar preguntas para cada empresa
- [ ] iTalki: tutor nativo 2x semana
- [ ] Simulacros de entrevista completos 45 min
- [ ] Certificate of Eligibility (COE) trámite

---

## Key context for next session

### Servicios y puertos
- `auth-service` → port 8084 (en construcción)
- `account-service` → port 8081
- `payment-service` → port 8082
- `notification-service` → port 8083

### Docker
- Siempre usar `docker compose up -d --no-recreate` para evitar conflictos
- Contenedores: `payments-postgres`, `payments-kafka`, `payments-zookeeper`, `payments-redis`
- `accounts_db` y `payments_db` son bases de datos separadas en el mismo PostgreSQL

### Decisiones de arquitectura tomadas
- Database per service — cada servicio tiene su propia base de datos
- Idempotency keys en payments — evita pagos duplicados
- X-User-Id header — el Gateway inyecta el userId, los servicios confían en él
- `saveAndFlush` en lugar de `save` — para que `createdAt` llegue populado
- `LEADER_NOT_AVAILABLE` en Kafka al inicio es normal — el topic se crea automáticamente
- `auto-offset-reset=earliest` — para no perder eventos si un servicio estuvo caído

### Problemas resueltos
- IntelliJ no compilaba Lombok → File → Settings → Delegate IDE build/run actions to Maven
- Kafka no conectaba en account-service → necesitaba `KafkaConsumerConfig` manual
- Spring Boot 4.x de Initializr → siempre bajar a 3.2.3 manualmente
- `maven-compiler-plugin` con Lombok sin versión → eliminar el plugin, Spring Boot lo maneja solo
- `./mvnw: Permission denied` en CI → `git update-index --chmod=+x service/mvnw`

### Empresas objetivo (orden de prioridad)
1. Mercari / Merpay — tokyodev.com
2. Rakuten — careers.rakuten.com
3. PayPay — tokyodev.com
4. SmartHR — japan-dev.com
5. freee — japan-dev.com

### Inglés técnico practicado
- "Walk me through your architecture" ✅
- "How do you handle payment failures?" ✅ (mencionar Transactional Outbox Pattern)
- "Why Kafka over REST?" ✅
- Vocabulario clave: ACID transactions, idempotency, decoupling, resilience, database per service

### Learning Preferences 
- They prefer to learn by doing, rather than being given complete solutions 
- Provide step-by-step instructions, one at a time 
- Wait for the user to complete each step before moving on to the next 
- If they make a mistake, offer hints, not the direct answer 
- Always ask, “Where exactly are you in the process?” before offering guidance
