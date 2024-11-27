# Advanced-Inventory-Management-System-java


This Java-based **Inventory Management System** is a console application designed to manage an inventory of items. It supports features like adding, updating, and deleting items, viewing items by category, retrieving top items by quantity, and merging inventories.

## Features
- **Add or Update Items**: Add new items or update existing ones in the inventory.
- **Delete Items**: Remove items from the inventory by their ID.
- **View Inventory**: Retrieve items organized by category.
- **Top K Items**: Retrieve the top K items sorted by their quantity in descending order.
- **Restock Alerts**: Notify the user if an item's quantity falls below a threshold.
- **Merge Inventories**: Combine another inventory with the current one, ensuring no duplicate IDs.
- **Category-based Sorting**: Items are stored in categories and automatically sorted by quantity.

## Preloaded Categories
The application comes with the following preloaded categories:
- Electronics
- Furniture
- Groceries

Each category is initialized with some sample items.

## Technologies Used
- **Java**: Core programming language used.
- **Collections Framework**: Includes `HashMap`, `TreeSet`, and `List` for efficient data management.
- **Stream API**: For sorting and filtering data.
- **Scanner**: For user input in the console.
- **Comparable Interface**: To sort items by quantity in descending order.

## Prerequisites
- **Java Development Kit (JDK)**: Version 8 or later.
- **Integrated Development Environment (IDE)** (optional): IntelliJ IDEA, Eclipse, or any other IDE.
