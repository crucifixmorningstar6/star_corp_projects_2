# Star Corp Projects v2.0 (Android)

Enterprise mobile management and observability hub for Star Corp engineering operations, tracking deployed websites, database infrastructure clusters, engineering projects, and the developer team directory.

## Features

- **Executive Command Center & Dashboard**: High-level telemetry displaying fleet metrics, real-time infrastructure uptime (99.98%), active deployments, and quick provisioning shortcuts.
- **Projects Management**: Comprehensive tracking of enterprise mobile apps, client web portals, stream gateways, and internal observability suites with category badges, interactive deliverable checklists, milestone progress bars, tech stack tags, and assigned developer teams.
- **Websites & Endpoints Fleet**: Real-time status monitoring for production, staging, and development web services with SSL verification, latency ping tests, and one-tap browser launching.
- **Databases & Cloud Storage**: Centralized overview of PostgreSQL, Cloud SQL, Redis, Firestore, and MongoDB clusters with storage capacity gauges, active connection tracking, and instant snapshot backup triggers.
- **Developer Team Directory**: Engineering roster categorized by role (Lead Architect, Fullstack, Frontend, Backend, DevOps/SRE, Mobile) with real-time availability badges, GitHub handles, tech stack proficiencies, and direct email communication.
- **Unified Global Search & Filtering**: Fast search filtering across all 4 pillars and custom filter chips for statuses, environments, engines, and roles.
- **Adaptive Layout**: Material Design 3 layout supporting both handheld mobile phones (Bottom Navigation Bar) and tablets / foldables (Navigation Rail).

## Tech Stack

- **Platform**: Android SDK 36 (minSdk 24)
- **Language**: Kotlin 2.2+ with Coroutines & StateFlow
- **UI Framework**: Jetpack Compose & Material 3 (M3)
- **Architecture**: MVVM with Repository Pattern
- **Persistence & State**: Reactive StateFlow architecture with seamless state preservation
- **Design System**: Star Corp cosmic tech branding with dynamic Dark & Light color palettes
