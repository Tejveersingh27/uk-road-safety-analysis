# UK Road Safety Analyst Tool – Group 36

**COMP 3380 – Database Concepts (Fall 2025)**  
**Project:** UK Road Safety Data Analysis (2010–2015)

## Group Members
| Name                    | Student ID |
|-------------------------|------------|
| Chung Hoi Luong         | 7996186    |
| Tejveer Singh Ghataurey | 7993537    |
| Nishchay Kathuria       | 7989002    |

## Project Overview
Command-line analytics tool that allows road-safety analysts to explore approximately **2 million** UK traffic accident records stored in a normalized dataset on the University of Manitoba’s `uranium` Microsoft SQL Server.

**Data Source:**  
https://www.kaggle.com/datasets/tsiaras/uk-road-safety-accidents-and-vehicles  
*(sampled subset used – original dataset >700 MB)*

### Key Features
- Secure JDBC connection with credentials isolated in `auth.cfg`
- 100% Prepared Statements for queries with users' input 
    → full SQL-injection protection
- 10 complex analytical queries using joins, aggregation, and window functions
- Admin “factory reset” that drops & repopulates the entire database in ~30 seconds using JDBC batch inserts

## Submission Structure
COMP_3380_Group36_FinalSubmission
├── auth.cfg                          DB credentials (included for grading)
├── COMP_3380_Group36_database.sql    Complete schema + data script
├── DatabaseConnection.java           JDBC connection manager
├── Final_ER.drawio.pdf               Entity-Relationship diagram (PDF export)
├── Makefile                          Compile & run shortcuts
├── mssql-jdbc-11.2.0.jre11.jar       Microsoft JDBC driver
├── README.md                         This file
└── RoadSafetyTool.java               Main application & TUI


## How to Run

### Prerequisites
- Connected to UofM network (or SSH via `aviary`)
- Java JDK 11+

### Execution (using provided Makefile)
```bash
make clean   # optional – remove old .class files
make run     # compiles and launches the tool

