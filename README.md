
//full name: Drăgoi Ioan-Cosmin
## Hi there,
## I'm Cosmin and here is how I solve the internship problem 👋
---

##  API testing

Below are the Postman captures made while testing the API.

| Step | Operation | Screenshot |
|------|-----------|------------|
| 1 | List all items | ![get_all_items](postman_screenshots/get_all_items.png) |
| 2 | Create an item (POST) | ![post_item](postman_screenshots/post_item.png) |
| 3 | Retrieve that item (GET by ID) | ![get_specific_item](postman_screenshots/get_specific_item.png) |
| 4 | Update an item (PUT) | ![put_modify_existing_item](postman_screenshots/put_modify_existing_item.png) |
| 5 | Attempt to update a non-existent item | ![put_unexisting_item](postman_screenshots/put_unexisting_item.png) |
| 6 | Trigger async processing of **all** items | ![process_all_items](postman_screenshots/process_all_items.png) |
| 7 | Verify statuses after processing | ![get_on_all_items_item_modified](postman_screenshots/get_on_all_items_item_modified.png) |
| 8 | Delete an item | ![delete_specified_item](postman_screenshots/delete_specified_item.png) |
| 9 | Check list after deletion | ![get_items_after_deletion](postman_screenshots/get_items_after_deletion.png) |

## Siemens Java Internship - Code Refactoring Project

This repository contains a Spring Boot application that implements a simple CRUD system with some asynchronous processing capabilities. The application was created by a development team in a hurry and while it implements all required features, the code quality needs significant improvement.

## Getting Started
- Clone this repository
- Import the project into your IDE as a Maven project (Java 17, might work with other Java versions as well)
- Study the existing code and identify issues
- Implement your refactoring changes
- Test thoroughly to ensure functionality is preserved

## Your Assignment
  The Project should have the following structure:

![image](https://github.com/user-attachments/assets/ab45f225-ff1f-4ff7-bbaa-3d5d0c21e7b1)

ⓘ
##  You will have to:
1. Fix all logical errors while maintaining the same functionality
2. Implement proper error handling and validation
3. Be well-documented with clear, concise comments
4. Write test functions with as much coverage as possible
5. Make sure that the Status Codes used in Controller are correct
6. Find a way to implement an email validation
7. Refactor the **processItemsAsync** function
    The **processItemsAsync** function is supposed to:
      1. Asynchronously process EVERY item (retrieve from database, update status, and save)
      2. Track which items were processed
      3. Return a list when all items have been processed
      4. Provide an accurate list of all successfully processed items
      HINT: You are free to modify the function and variables as much as you want :)


Copy the project and make the solution public on your personal GitHub.
Provide us the GitHub URL via email.
(Don't forget to make the repository PUBLIC 😁)
