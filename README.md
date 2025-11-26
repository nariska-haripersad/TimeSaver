# TimeSaver 
TimeSaver is a mobile time-tracking application designed to help users record the time they spend working on projects, manage productivity and monitor progress toward daily goals. It was built as a group project in Android Studio using Kotlin. The goal of this application was to implement a functional time-tracking system while demonstrating strong understanding of backend development, database integration, UI design and general software engineering practices.
## Technologies Used
- Kotlin
- Java (very minor use)
- Android Studio
- Firebase Realtime Database
- Android XML UI layouts
## Running the Application 
- Download the project folder
- Open the project in Android Studio
- Allow Gradle to sync
- Select an emulator (recommended: 'Pixel 3a API 27' or 'Pixel 3a API 26')
- Click Run to launch the app
###### Note: An internet connection is required as all user data is securely stored using Firebase.
## Features 
### Sign Up
Users register by entering their:
- Phone number
- Email address
- Username
- Password that meets password requirements (12+ characters, uppercase, lowercase, numbers, symbols)
<p>The backend validates and stores the credentials in Firebase. </p>

### Login 
- Users log in with their registered username and password
###### My contribution: Backend development for both registration and login + full Firebase setup.

### Main Menu 
After logging in, users are presented with the home menu with the following options:
- Create Category
- Create Timesheet
- Set Daily Goal
- View
- Notifications
- Contact Us
###### My contribution: Full frontend/UI design of the main menu. 
### Create Category
- Users can create custom categories for organizing timesheets
- To create a category, enter category name, enter description (optional) and click 'Add Category' to save it
###### My contribution: Frontend and backend
### Create Timesheet 
Users can create timesheets to log work they have completed. To create a timesheet entry, the user needs to enter:
- Timesheet name
- Description
- Category (from a dropdown menu populated with categories the user created)
- Date
- Start time & end time
- Photo attachment from gallery (optional)
###### My contribution: Frontend and backend
### Set Daily Goal
Users can set productivity goals by entering: 
- Project name
- Goal description
- Date
- Minimum & maximum hours
- Priority (Low, Medium, High)
###### Built by another team member (both frontend + backend)
### View 
Opens another menu with the following menu options: 
- Timesheets <br>
— Users select a date range and view all timesheets created during that period <br>
— Tapping a timesheet opens its full details <br>
###### My contribution: Frontend and backend
- Total hours per category <br>
— Shows total hours worked per category across a selected time period <br>

Displays category name + total hours
Helps users understand where their time is going
My contribution: frontend + backend

