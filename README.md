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
- To create a category: enter category name, enter description (optional) and click 'Add Category' to save it
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
— Tapping a timesheet opens its full details
###### My contribution: Frontend and backend
- Total hours per category <br>
— Shows total hours worked per category across a selected time period (displays category name + total hours)
###### My contribution: Frontend and backend
- Total hours worked per day <br>
— Displays a bar graph for total daily hours in a selected timeframe
###### Built by another team member
- Daily Goals <br>
— Users can select a month and year and see their daily goal statistics through a visual chart 
###### Built by another team member
- Archived Projects <br>
— Users can view any archived projects 
###### Built by another team member
- Achievements <br>
— A gamified feature listing milestones users can unlock <br>
— Achievements automatically update depending on the timeframe (weekly, monthly, yearly, overall)
###### My contribution: Frontend and backend
### Notifications 
- Users can view alerts related to timesheet reminders, daily goal updates and productivity prompts
###### My contribution: Frontend and backend
### Contact Us
- Allows users to send queries or feedback
- The user needs to enter their email address, write a message and click 'Submit'
###### Built by another team member
## Team Contributions Summary
My primary contributions included:
- Firebase setup and integration
- Sign Up and Login (backend)
- Main menu (frontend)
- Create Category (frontend + backend)
- Create Timesheet (frontend + backend)
- Timesheets page (frontend + backend)
- Total hours per category page (frontend + backend)
- Achievements (frontend + backend)
- Notifications (frontend + backend)
<p>Other team members handled: <p>
  
- App name and logo
- Welcome page 
- Sign Up and Login frontend
- Set Daily Goal (frontend + backend)
- View Menu
- Total hours worked per day page (frontend + backend)
- Daily Goals page (frontend + backend)
- Archived Projects (frontend + backend)
- Contact Us



