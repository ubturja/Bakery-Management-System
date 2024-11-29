public class Baker {
    public void addRecipe(String recipeName, String ingredients, String instructions) {
        System.out.println("Recipe Added: " + recipeName);
        System.out.println("Ingredients: " + ingredients);
        System.out.println("Instructions: " + instructions);
    }

    public void checkInventory(String ingredient) {
        System.out.println("Checking stock for: " + ingredient);
    }
}
