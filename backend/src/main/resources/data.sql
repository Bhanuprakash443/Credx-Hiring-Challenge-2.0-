-- Clean up existing data to prevent primary key collisions on reload
DELETE FROM applications;
DELETE FROM job_postings;
DELETE FROM notifications;
DELETE FROM users;
DELETE FROM students;
DELETE FROM companies;

-- Seed Companies
INSERT INTO companies (id, name, industry, website, description, contact_email) VALUES
(1, 'TechCorp Solutions', 'Technology & Software', 'https://techcorp.example.com', 'A global leader in cloud and enterprise solutions.', 'careers@techcorp.com'),
(2, 'InnovateSoft Labs', 'Artificial Intelligence', 'https://innovate.example.com', 'A cutting-edge startup developing AI agents and LLM frameworks.', 'jobs@innovatesoft.com'),
(3, 'Global Finance Group', 'Investment Banking', 'https://globalfin.example.com', 'Top-tier multinational investment banking and wealth management firm.', 'hr@globalfinance.com');

-- Seed Students
INSERT INTO students (id, name, email, roll_number, department, cgpa, resume_text) VALUES
(1, 'Rahul Sharma', 'rahul.sharma@uni.edu', 'CS2023001', 'Computer Science & Engineering', 8.5, 'Proficient in Java, Angular, and Spring Boot. Built multiple full-stack projects.'),
(2, 'Priya Patel', 'priya.patel@uni.edu', 'CS2023002', 'Information Technology', 6.2, 'Enthusiastic web designer. Familiar with HTML, CSS, JavaScript, and UI design principles.'),
(3, 'Amit Kumar', 'amit.kumar@uni.edu', 'EC2023045', 'Electronics & Communication', 7.8, 'Strong base in digital logic and embedded systems. Good command over Python and C++.');

-- Seed Users (Passwords are plaintext for local mock authentication convenience)
INSERT INTO users (id, username, password, email, role, associated_id) VALUES
(1, 'admin', 'admin123', 'admin@placementcell.uni.edu', 'ADMIN', NULL),
(2, 'rahul', 'password', 'rahul.sharma@uni.edu', 'STUDENT', 1),
(3, 'priya', 'password', 'priya.patel@uni.edu', 'STUDENT', 2),
(4, 'amit', 'password', 'amit.kumar@uni.edu', 'STUDENT', 3),
(5, 'techcorp', 'password', 'careers@techcorp.com', 'COMPANY', 1),
(6, 'innovatesoft', 'password', 'jobs@innovatesoft.com', 'COMPANY', 2);

-- Seed Job Postings (Deadlines in future or past)
-- TechCorp Software Intern: ID=1 (Approved, Active)
INSERT INTO job_postings (id, title, description, requirements, min_cgpa, salary, location, deadline, status, company_id, created_at) VALUES
(1, 'Software Engineer Intern', 'We are looking for a backend engineering intern to help scale our REST APIs.', 'Experience with Java or Python. Good understanding of relational databases.', 7.5, '$30/hr', 'Remote / New York', '2027-12-31', 'APPROVED', 1, CURRENT_TIMESTAMP - INTERVAL '5' DAY);

-- InnovateSoft AI Engineer: ID=2 (Pending Approval)
INSERT INTO job_postings (id, title, description, requirements, min_cgpa, salary, location, deadline, status, company_id, created_at) VALUES
(2, 'AI & NLP Specialist', 'Research and implement conversational AI agents using open-source LLMs.', 'Strong Python backend skills. Experience with PyTorch or HuggingFace is a plus.', 8.0, '$120,000/yr', 'San Francisco, CA', '2027-09-30', 'PENDING', 2, CURRENT_TIMESTAMP - INTERVAL '1' DAY);

-- TechCorp Data Analyst: ID=3 (Approved, Active)
INSERT INTO job_postings (id, title, description, requirements, min_cgpa, salary, location, deadline, status, company_id, created_at) VALUES
(3, 'Junior Data Analyst', 'Perform dashboard creation, SQL queries, and generate recruiting intelligence reports.', 'Expertise in SQL, Excel, and Tableau/PowerBI.', 6.0, '$75,000/yr', 'Chicago, IL', '2027-11-15', 'APPROVED', 1, CURRENT_TIMESTAMP - INTERVAL '3' DAY);

-- Global Finance Analyst: ID=4 (Approved, Closed - Deadline Passed)
INSERT INTO job_postings (id, title, description, requirements, min_cgpa, salary, location, deadline, status, company_id, created_at) VALUES
(4, 'Graduate Research Analyst', 'Support financial analysts in compiling market research reports and economic modeling.', 'High academic achievement. Outstanding analytical skills.', 8.0, '$110,000/yr', 'London, UK', '2026-06-01', 'CLOSED', 3, CURRENT_TIMESTAMP - INTERVAL '60' DAY);

-- Seed Applications
-- Rahul (CS2023001, CGPA 8.5) applied to TechCorp Software Intern (Min CGPA 7.5) -> Shortlisted
INSERT INTO applications (id, job_posting_id, student_id, applied_at, status, notes) VALUES
(1, 1, 1, CURRENT_TIMESTAMP - INTERVAL '4' DAY, 'SHORTLISTED', 'Strong performance in initial screening code test.');

-- Amit (EC2023045, CGPA 7.8) applied to TechCorp Software Intern (Min CGPA 7.5) -> Applied
INSERT INTO applications (id, job_posting_id, student_id, applied_at, status, notes) VALUES
(2, 1, 3, CURRENT_TIMESTAMP - INTERVAL '2' DAY, 'APPLIED', NULL);

-- Rahul (CS2023001, CGPA 8.5) applied to TechCorp Data Analyst (Min CGPA 6.0) -> Selected (Placed!)
INSERT INTO applications (id, job_posting_id, student_id, applied_at, status, notes) VALUES
(3, 3, 1, CURRENT_TIMESTAMP - INTERVAL '2' DAY, 'SELECTED', 'Offered extended to candidate. Candidate accepted.');

-- Seed Notifications
INSERT INTO notifications (id, user_id, message, created_at, is_read) VALUES
(1, 1, 'New job posting AI & NLP Specialist from InnovateSoft Labs is pending approval.', CURRENT_TIMESTAMP - INTERVAL '1' DAY, false),
(2, 5, 'Your job posting Software Engineer Intern has been APPROVED by admin.', CURRENT_TIMESTAMP - INTERVAL '5' DAY, true),
(3, 2, 'Your application status for Software Engineer Intern has been updated to SHORTLISTED.', CURRENT_TIMESTAMP - INTERVAL '4' DAY, false);
