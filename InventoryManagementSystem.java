import java.util.*;
import java.util.stream.Collectors;

public class InventoryManagementSystem {
    private final Map<String, Item> inventory; // For efficient lookup by ID
    private final Map<String, TreeSet<Item>> categoryMap; // To store category-wise sorted items
    private final int restockThreshold;
    private final List<String> availableCategories;

    public InventoryManagementSystem(int restockThreshold) {
        this.inventory = new HashMap<>();
        this.categoryMap = new HashMap<>();
        this.restockThreshold = restockThreshold;

        // Available categories
        this.availableCategories = new ArrayList<>(List.of("Electronics", "Furniture", "Groceries"));

        // Preload some items in the inventory
        addOrUpdateItem("101", "Laptop", "Electronics", 50);
        addOrUpdateItem("102", "Phone", "Electronics", 30);
        addOrUpdateItem("103", "Table", "Furniture", 20);
        addOrUpdateItem("104", "Chair", "Furniture", 5); // Below threshold
        addOrUpdateItem("105", "Rice", "Groceries", 100);
    }

    // Item Class
    static class Item implements Comparable<Item> {
        String id;
        String name;
        String category;
        int quantity;

        public Item(String id, String name, String category, int quantity) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.quantity = quantity;
        }

        @Override
        public int compareTo(Item other) {
            return Integer.compare(other.quantity, this.quantity); // Descending by quantity
        }

        @Override
        public String toString() {
            return String.format("ID: %s, Name: %s, Category: %s, Quantity: %d", id, name, category, quantity);
        }
    }

    // Add or update an item
    private void createOrUpdateItemInCategory(Item item) {
        categoryMap.computeIfAbsent(item.category, k -> new TreeSet<>()).add(item);
    }

    public void addOrUpdateItem(String id, String name, String category, int quantity) {
        Item item = new Item(id, name, category, quantity);
        
        // Remove old item if it exists to ensure the latest quantity update
        if (inventory.containsKey(id)) {
            removeItemFromCategory(id); // Remove from category map before updating
        }
        inventory.put(id, item);
        
        // Re-add the item to the category map
        createOrUpdateItemInCategory(item);

        // Check for restock notification
        if (quantity < restockThreshold) {
            System.out.println("Restock Notification: Item " + id + " (" + name + ") is below the threshold!");
        }
    }

    // Remove an item
    public void removeItem(String id) {
        if (inventory.containsKey(id)) {
            removeItemFromCategory(id);
            inventory.remove(id);
            System.out.println("Item with ID: " + id + " removed successfully.");
        } else {
            System.out.println("Item with ID: " + id + " not found.");
        }
    }

    private void removeItemFromCategory(String id) {
        Item item = inventory.get(id);
        if (item != null && categoryMap.containsKey(item.category)) {
            categoryMap.get(item.category).remove(item);
        }
    }

    // Retrieve items by category
    public List<Item> getItemsByCategory(String category) {
        return categoryMap.getOrDefault(category, new TreeSet<>()).stream().collect(Collectors.toList());
    }

    // Merge another inventory with the current one, ensuring no duplicate item IDs
    public void mergeInventory(InventoryManagementSystem other) {
        for (Item item : other.inventory.values()) {
            if (inventory.containsKey(item.id)) {
                // If the item exists, compare quantities and retain the item with the higher quantity
                Item existing = inventory.get(item.id);
                if (item.quantity > existing.quantity) {
                    // If the item in the other inventory has a higher quantity, update it
                    addOrUpdateItem(item.id, item.name, item.category, item.quantity);
                }
            } else {
                // If the item does not exist in the current inventory, add it
                addOrUpdateItem(item.id, item.name, item.category, item.quantity);
            }
        }
    }

    // Retrieve top k items by quantity
    public List<Item> getTopKItems(int k) {
        return inventory.values().stream()
                .sorted((a, b) -> Integer.compare(b.quantity, a.quantity)) // Descending order
                .limit(k)
                .collect(Collectors.toList());
    }

    // Display all items
    public void displayAllItems() {
        inventory.values().forEach(System.out::println);
    }

    // Main method with interactive menu
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InventoryManagementSystem ims = new InventoryManagementSystem(10);

        while (true) {
            // Display menu options
            System.out.println("\nWelcome to the Inventory Management System");
            System.out.println("Please choose an option:");
            System.out.println("1. Add Item");
            System.out.println("2. Update Stock");
            System.out.println("3. Delete Item");
            System.out.println("4. View Inventory");
            System.out.println("5. View Top K Items");
            System.out.println("6. Merge Inventories");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Add Item
                    System.out.println("Choose a category for the item:");
                    for (int i = 0; i < ims.availableCategories.size(); i++) {
                        System.out.println((i + 1) + ". " + ims.availableCategories.get(i));
                    }
                    System.out.print("Enter the number corresponding to the category: ");
                    int categoryChoice = scanner.nextInt();
                    scanner.nextLine();  // Consume newline

                    if (categoryChoice < 1 || categoryChoice > ims.availableCategories.size()) {
                        System.out.println("Invalid category choice.");
                        break;
                    }

                    String selectedCategory = ims.availableCategories.get(categoryChoice - 1);

                    System.out.print("Enter item ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter item name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter item quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();  // Consume newline

                    ims.addOrUpdateItem(id, name, selectedCategory, quantity);
                    System.out.println("Item added/updated successfully.");
                    break;

                case 2:
                    // Update Stock
                    System.out.print("Enter item ID to update: ");
                    String updateId = scanner.nextLine();
                    if (!ims.inventory.containsKey(updateId)) {
                        System.out.println("Item not found.");
                        break;
                    }
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    scanner.nextLine();  // Consume newline

                    ims.addOrUpdateItem(updateId, ims.inventory.get(updateId).name, ims.inventory.get(updateId).category, newQuantity);
                    System.out.println("Stock updated successfully.");
                    break;

                case 3:
                    // Delete Item
                    System.out.print("Enter item ID to delete: ");
                    String deleteId = scanner.nextLine();

                    ims.removeItem(deleteId);
                    break;

                case 4:
                    // View Inventory by Category
                    System.out.println("Choose a category to view items:");
                    for (int i = 0; i < ims.availableCategories.size(); i++) {
                        System.out.println((i + 1) + ". " + ims.availableCategories.get(i));
                    }
                    System.out.print("Enter the number corresponding to the category: ");
                    int viewCategoryChoice = scanner.nextInt();
                    scanner.nextLine();  // Consume newline

                    if (viewCategoryChoice < 1 || viewCategoryChoice > ims.availableCategories.size()) {
                        System.out.println("Invalid category choice.");
                        break;
                    }

                    String selectedViewCategory = ims.availableCategories.get(viewCategoryChoice - 1);
                    List<Item> items = ims.getItemsByCategory(selectedViewCategory);
                    if (items.isEmpty()) {
                        System.out.println("No items found in category: " + selectedViewCategory);
                    } else {
                        System.out.println("Items in " + selectedViewCategory + " category:");
                        items.forEach(System.out::println);
                    }
                    break;

                case 5:
                    // View Top K Items
                    System.out.print("Enter value of K (Top K items by quantity): ");
                    int k = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    List<Item> topItems = ims.getTopKItems(k);
                    System.out.println("Top " + k + " items by quantity:");
                    topItems.forEach(System.out::println);
                    break;

                case 6:
                    // Merge Inventories
                    System.out.print("Enter the ID of the inventory to merge: ");
                    String mergeInventoryId = scanner.nextLine();

                   
                    InventoryManagementSystem otherInventory = new InventoryManagementSystem(5);
                    ims.mergeInventory(otherInventory);
                    System.out.println("Inventory merged successfully.");
                    break;

                case 7:
                    // Exit
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
