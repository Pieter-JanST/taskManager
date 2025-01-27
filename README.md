# Task Manager

A simple mock-up webapp for task management between multiple projects and users.

## Features

- **User Roles**: Assign different roles to users for access control/ project assignment.
- **Project**: Create projects consisting of multiple tasks.
- **Task**: Set dependencies between tasks, requiring the completion of previous tasks before starting new ones.
Task states act as a state machine, (dis)allowing certain actions for each state.
- **User Assignment**: Assign users to tasks and switch them around based on task status and the user roles.
- **Undo/Redo Actions**: Most actions within the system can be undone and redone.

Project requires Tomcat 8.5.87 and java 11 to run. Default user sign-in information can be found in /src/resources/seeds/User.txt