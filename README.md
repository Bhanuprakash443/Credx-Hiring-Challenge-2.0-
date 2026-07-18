# 🎓 University Placement Portal

A role-based placement portal built for the **Credx Hiring Challenge 2.0** that enables companies to post job opportunities, students to apply for approved roles, and placement cell administrators to manage the complete recruitment workflow.

---

## 📌 Overview

The University Placement Portal streamlines campus recruitment by providing a centralized platform for companies, students, and placement administrators.

The core workflow ensures that **job postings are visible to students only after approval from the placement cell**, maintaining quality control and authenticity.

---

## ✨ Features

### 🏢 Company Portal
- Register/Login
- Create new job postings
- Add:
  - Job title
  - Description
  - Eligibility criteria
  - Application deadline
- Track posting status
  - Pending
  - Approved
  - Rejected
  - Closed

---

### 👨‍🎓 Student Portal
- View approved job postings
- Search & filter available roles
- View complete job details
- Apply for jobs
- Track submitted applications

---

### 👨‍💼 Placement Admin Portal
- Review newly submitted job postings
- Approve or reject postings
- View all companies
- View student applications
- Analytics Dashboard:
  - Applications per company
  - Total applications
  - Placement rate
  - Job posting statistics

---

## 🔄 Approval Workflow

```text
Company Creates Job
        │
        ▼
Status = Pending
        │
        ▼
Placement Admin Reviews
        │
 ┌──────┴───────┐
 │              │
 ▼              ▼
Approved     Rejected
 │
 ▼
Visible to Students
 │
 ▼
Students Apply
 │
 ▼
Deadline Reached
 │
 ▼
Status = Closed
```

> **Important:** Students can only view **Approved** job postings.

---

## 🛠 Tech Stack

### Frontend
- Angular
- TypeScript
- HTML
- CSS
- Bootstrap / Angular Material

### Backend
- Spring Boot
- Spring MVC
- Spring Security
- REST APIs

### Database
- MySQL / PostgreSQL

### Build Tools
- Maven
- Node.js
- npm

---

## 📂 Project Structure

```
.
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── frontend/
│   ├── src/
│   ├── angular.json
│   └── ...
│
├── Dockerfile
├── render.yaml
├── package.json
└── README.md
```

---

## 🗄 Database Design

### Company
- id
- companyName
- email
- contactPerson

### Student
- id
- name
- email
- department
- graduationYear

### Job Posting
- id
- title
- description
- eligibility
- deadline
- status
- companyId

### Application
- id
- studentId
- jobId
- appliedAt
- status

---

## 📊 Analytics Dashboard

The Admin Dashboard displays:

- Total Companies
- Total Job Postings
- Pending Approvals
- Approved Jobs
- Rejected Jobs
- Closed Jobs
- Applications per Company
- Total Applications
- Overall Placement Rate

---

## 🔐 User Roles

| Role | Permissions |
|------|-------------|
| Company | Create and manage job postings |
| Student | View approved jobs and apply |
| Admin | Approve/reject postings and view analytics |

---

## 🚀 Getting Started

### Clone Repository

```bash
git clone https://github.com/<your-username>/Credx-Hiring-Challenge-2.0.git
```

---

### Backend Setup

```bash
cd backend

mvn clean install

mvn spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

---

### Frontend Setup

```bash
cd frontend

npm install

ng serve
```

Frontend runs on:

```
http://localhost:4200
```

---

## 📡 API Modules

### Company APIs
- Create Job
- Update Job
- Delete Job
- View Own Jobs

### Student APIs
- Get Approved Jobs
- Apply for Job
- View Applications

### Admin APIs
- View Pending Jobs
- Approve Job
- Reject Job
- Analytics

---

## 🎯 Evaluation Criteria Covered

- ✅ Company role posting
- ✅ Approval workflow
- ✅ Student application flow
- ✅ Role-based access
- ✅ Status tracking
- ✅ Analytics dashboard
- ✅ Clean REST architecture

---

## 🌟 Bonus Features

- Email/Notification simulation
- Charts for analytics
- Deadline-based automatic job closing
- Responsive UI
- Search & filtering
- Pagination

---

## 📸 Screenshots

Add application screenshots here after implementation.

```
<img width="1915" height="905" alt="Screenshot 2026-07-18 144511" src="https://github.com/user-attachments/assets/cfd89289-8372-44a2-9482-e753621ffbf2" />

```

<img width="1913" height="887" alt="Screenshot 2026-07-18 144541" src="https://github.com/user-attachments/assets/14ffb990-754b-46ca-a45c-1547ea0aefb0" />


---

<img width="1792" height="781" alt="Screenshot 2026-07-18 144547" src="https://github.com/user-attachments/assets/40a56e5e-2149-43f3-8395-26b1ff749105" />


<img width="1887" height="852" alt="Screenshot 2026-07-18 144641" src="https://github.com/user-attachments/assets/010bdb02-de04-449c-b773-c7f0e5351862" />

<img width="1916" height="840" alt="Screenshot 2026-07-18 144626" src="https://github.com/user-attachments/assets/72c2f702-5290-4429-85b7-1b6e789389b8" />

## 👨‍💻 Author

**Bhanu Prakash Reddy**

GitHub: https://github.com/Bhanuprakash443

---

## 📄 License

This project was developed as part of the **Credx Hiring Challenge 2.0**.
