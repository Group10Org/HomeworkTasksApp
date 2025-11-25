# Project

Milestone 1 - Homework Tracker (Unit 7)
-------------------------------------
Table of Contents
1.Overview
2.Product Spec
3.Wireframes

Overview
-------------------------------------------

**Description**
The homework app allows users to log in and manage their assignments through a structured and interactive platform. Users can create homework tasks on the creation page, with each task containing a creation date, due date, priority number, title, and optional fields such as a description or an attached picture. Once a task is completed, its title is displayed on the user feed, where other users can like the completed tasks. Each user has a profile that shows the total number of completed tasks and a completion rate score, along with a list of their tasks. From their profile or home page, users can mark tasks as complete or delete them. The app also features a settings page where users can update their profile information, such as their name and whether or not to share tasks on the feed. Additionally, users can search for others by username and choose to follow or unfollow them, allowing for a more social and connected experience.

**App Evaluation** [Evaluation of your app across the following attributes]

Mobile: What makes your app more than a glorified website?
-tracks user data and shows best options in real time (using a priority scheduling algorithm) 
-users can take pictures in the app

Story: How compelling is the story around this app once completed?
-Helps students keep track of their HW assignments
-students can recommend it to their peers

Market: How large or unique is the market for this app?
-The market is large for all students looking for task organization apps
-The app is not very niche, it is for students in general

Habit: How habit-forming or addictive is this app?
-The app will be used consistently throughout their semester
-The user creates HW assignments and checks them as complete

Scope: How well-formed is the scope for this app?
-This app will not be too challenging to create,
It will cover the 6 core Screen Archetypes:
Login
Stream
Detail
Creation
Profile
Settings




Product Spec
----------------------------------------------------------------
1. User Features (Required and Optional) - Cynthia
Required Features

-user signup/login
-user can create a HW task on create page 
	-each task has a create date,
	- due date, 
	- priority number,
	- title,
	- optional description,
	- optional picture
-completed user task titles show up on the user feed
-other users can like the completed tasks
-user profile shows number of tasks completed, and complete rate score
-user profile shows list of tasks and you can click on each task to view task detail page
-user can mark tasks complete or delete tasks from their home page
-settings page allows user to change the info on their profile, like their name and whether or not to share tasks on feed
-can search for user by username and follow/unfollow them
-Navigation bar will allow user to move between (feed, create, profile, settings, search user)


[fill in your optional user features here]
-user can take a picture of an assignment from the app 
-user task completion stats

**2. Screen Archetypes**
-------------------------

-  Login Screen

    -    Allows users to sign in or create an account.

    -    Saves their homework data securely and syncs across devices.

    -    Could include “Continue as Guest” or “Sign in with Google.”

- Stream Screen

    -    The main dashboard that lists all homework assignments by people you follow.

    - user can like other users completed tasks.

- Detail Screen

    -    Shows full information about a specific homework assignment.

    -    Includes due date, description, class name, and priority level.

    -    Users can edit or delete the assignment from here.

- Creation Screen

    -    Where users add new homework tasks.

    -    Includes form fields for title, due date, class, importance, and notes.

    -    Saves new tasks to the user’s list when submitted.

- Profile Screen

    -    Displays the user’s name, profile photo, and academic info (e.g., classes).

    -    Option to view statistics, like number of completed tasks or streaks.

    -    Helps personalize the experience for each user.

    - shows list of tasks to be completed ordered by due date + priority number

- Settings Screen

    -    Allows customization of app preferences.

    -    Some Examples:

            -    Default sorting (by due date or priority)

            -    Notification reminders

            -    Theme (light/dark mode)

            -    Manages account and privacy settings.
3. Navigation
Tab Navigation (Tab to Screen)

BottomNavigationView to switch screens.
Person Icon representing the login screen (also the first page the user sees on startup)
Profile icon representing the profile screen
Settings icon representing the settings screen
Calendar icon representing the stream screen
Target icon representing the detail screen
Pencil icon representing the creation screen

Flow Navigation (Screen to Screen)

Back button on the detail screen to return to the stream screen.
Back button on the creation screen to return to your prior screen. 




Wireframes
-------------------------------------------------
[Add picture of your hand sketched wireframes in this section]


![IMG_8093](https://hackmd.io/_uploads/HJKbE0QkZx.jpg)
<img width="1035" height="679" alt="image" src="https://github.com/user-attachments/assets/60f0cb91-3e2c-43dd-baa8-43cebdcab7ce" />




![Screenshot 2025-11-01 124156](https://hackmd.io/_uploads/rk8gY2XJWx.png)
<img width="1044" height="725" alt="image" src="https://github.com/user-attachments/assets/f7eaf13e-f6cb-407f-b86a-291e35549738" />





Milestone 2 - Build Sprint 1 (Unit 8)
GitHub Project board
[Add screenshot of your Project Board with three milestones visible in this section] 
<img width="1712" height="715" alt="image" src="https://github.com/user-attachments/assets/1fa8a406-e393-4304-93c0-e6e6acf5f222" />


Issue cards
[Add screenshot of your Project Board with the issues that you've been working on for this unit's milestone] 
<img width="1079" height="508" alt="image" src="https://github.com/user-attachments/assets/4bc27850-bb50-4248-81d8-5f7ccac31a03" />

[Add screenshot of your Project Board with the issues that you're working on in the NEXT sprint. It should include issues for next unit with assigned owners.] 

<img width="539" height="504" alt="image" src="https://github.com/user-attachments/assets/5679cedb-fdee-47a7-bd5a-e9459562dc48" />


Issues worked on this sprint
- create new app file 
- set up database
- set up firebase auth login/signup
- all empty pages
- nav bar

List the issues you completed this sprint
- create new app file 
- set up database
- set up firebase auth login/signup

[Add giphy that shows current build progress for Milestone 2. Note: We will be looking for progression of work between Milestone 2 and 3. Make sure your giphys are not duplicated and clearly show the change from Sprint 1 to 2.]
![ezgif com-speed](https://github.com/user-attachments/assets/11f02a6b-388a-4e14-b24b-42a653e37996)


Milestone 3 - Build Sprint 2 (Unit 9)
GitHub Project board
[Add screenshot of your Project Board with the updated status of issues for Milestone 3. Note that these should include the updated issues you worked on for this sprint and not be a duplicate of Milestone 2 Project board.] 
<img width="1108" height="373" alt="image" src="https://github.com/user-attachments/assets/d6211444-95f3-4638-94d0-67bf79a5a003" />

Issues worked on this sprint
- profile page (statistics section remaining)
List of issues you completed this sprint
- settings page
[Add video/gif of your current application that shows build progress] 

App Demo Video
Embed the YouTube/Vimeo link of your Completed Demo Day prep video
[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/ipTkYabzb-8/0.jpg)](https://www.youtube.com/watch?v=ipTkYabzb-8)
