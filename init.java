import java.util.*;
import java.io.ByteArrayInputStream;

abstract class Person {
  String name;
  String email;
  long phoneNo;
  String address;

  public Person(String n, String e, long p, String a) {
    name = n;
    email = e;
    phoneNo = p;
    address = a;
  }

  public void displayDeets() {
    System.out.println("Name:" + name);
    System.out.println("Email:" + email);
    System.out.println("Phone Number:" + phoneNo);
    System.out.println("Address:" + address);
  }
}

class Product {
  int ASIN;
  String name;
  int ratings;
  int price;
  int qty;
  String description;
  ArrayList<Review> reviews = new ArrayList<>();

  public Product(int a, String n, int r, int p, int q, String d) {
    ASIN = a;
    name = n;
    ratings = r;
    price = p;
    qty = q;
    description = d;
  }

  public void addReview() {
    int r;
    String c;
    String rv;
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter Customer name:");
    c = sc.nextLine();
    System.out.println("Enter Ratings:");
    r = sc.nextInt();
    sc.nextLine();
    System.out.println("Enter Review:");
    rv = sc.nextLine();
    reviews.add(new Review(c, r, rv));
  }

  public void displayDeets() {
    System.out.println("Name:" + name);
    System.out.println("Ratings:" + ratings);
    System.out.println("Price:" + price);
    System.out.println("Quantity:" + qty);
    System.out.println("Description:" + description);
  }

  public int asinNum() {
    return ASIN;
  }

  public void updateDeets() {
    String a;
    String n;
    String r;
    String q;
    String p;
    String d;

    Scanner sc = new Scanner(System.in);

    System.out.println("Enter ASIN Number:");
    a = sc.nextLine();
    if (!a.isEmpty()) {
      try {
        ASIN = Integer.parseInt(a);
      } catch (NumberFormatException e) {
      }
    }
    System.out.println("Enter Product Name:");
    n = sc.nextLine();
    if (!n.isEmpty()) {
      name = n;
    }
    System.out.println("Enter ratings:");
    r = sc.nextLine();
    if (!r.isEmpty()) {
      try {
        ratings = Integer.parseInt(r);
      } catch (NumberFormatException e) {
      }
    }
    System.out.println("Enter price:");
    p = sc.nextLine();
    if (!p.isEmpty()) {
      try {
        price = Integer.parseInt(p);
      } catch (NumberFormatException e) {
      }
    }
    System.out.println("Enter quantity:");
    q = sc.nextLine();
    if (!q.isEmpty()) {
      try {
        qty = Integer.parseInt(q);
      } catch (NumberFormatException e) {
      }
    }
    System.out.println("Enter description:");
    d = sc.nextLine();
    if (!d.isEmpty()) {
      description = d;
    }
  }
}

class Review {
  String cust_name;
  int ratings;
  String review;

  public Review(String c, int rt, String r) {
    cust_name = c;
    ratings = rt;
    review = r;
  }

  public void DisplayReview() {
    System.out.println("Customer name:" + cust_name);
    System.out.println("Ratings:" + ratings);
    System.out.println("Review:" + review);
  }
}

class Customer extends Person {
  ArrayList<Product> product_cart = new ArrayList<>();

  public Customer(String n, String e, long p, String a) {
    super(n, e, p, a);
  }

  public void addToCart(Product p) {
    product_cart.add(p);
  }

  public void showCart() {
    if (product_cart == null || product_cart.isEmpty()) {
      System.out.println("Cart is empty.");
      return;
    }
    for (Product p : product_cart)
      p.displayDeets();
  }

  public void deleteFromCart(int a) {
    try {
      for (int i = 0; i < product_cart.size(); i++) {
        if (a == product_cart.get(i).asinNum()) {
          product_cart.remove(i);
          break;
        }
      }
    } catch (NullPointerException e) {
    }
  }

}

class Seller extends Person {
  ArrayList<Product> product_inventory = new ArrayList<>();

  public Seller(String n, String e, long p, String a) {
    super(n, e, p, a);
  }

  public void addProduct() {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter ASIN Number:");
    int a = sc.nextInt();
    sc.nextLine();

    System.out.println("Enter Product Name:");
    String n = sc.nextLine();

    System.out.println("Enter ratings:");
    int r = sc.nextInt();
    sc.nextLine();

    System.out.println("Enter price:");
    int p = sc.nextInt();
    sc.nextLine();

    System.out.println("Enter quantity:");
    int q = sc.nextInt();
    sc.nextLine();

    System.out.println("Enter description:");
    String d = sc.nextLine();

    product_inventory.add(new Product(a, n, r, p, q, d));
  }

  public void showInventory() {
    if (product_inventory == null || product_inventory.isEmpty()) {
      System.out.println("Cart is empty.");
      return;
    }
    for (Product p : product_inventory)
      p.displayDeets();
  }

  public void deleteFromInventory(int a) {
    try {
      for (int i = 0; i < product_inventory.size(); i++) {
        if (a == product_inventory.get(i).asinNum()) {
          product_inventory.remove(i);
          break;
        }
      }
    } catch (NullPointerException e) {
    }
  }

  public void showSingleProduct(int a) {
    try {
      for (int i = 0; i < product_inventory.size(); i++) {
        if (a == product_inventory.get(i).asinNum()) {
          product_inventory.get(i).displayDeets();
          break;
        }
      }
    } catch (NullPointerException e) {
    }
  }

  public void updateProduct(int a) {
    try {
      for (int i = 0; i < product_inventory.size(); i++) {
        if (a == product_inventory.get(i).asinNum()) {
          product_inventory.get(i).updateDeets();
          break;
        }
      }
    } catch (NullPointerException e) {
    }
  }

}

public class init {
  public static void main(String[] args) {
    getimg imgGetter = new getimg();
    try {
      imgGetter.getImages("cats");
    } catch (Exception e) {
      System.out.println(e);
    }
    // // --- People (also exercises Person.displayDeets) ---
    // Seller s = new Seller("Alice Seller", "alice@shop.com", 9876543210L, "123
    // Market St");
    // Customer c = new Customer("Carl Customer", "carl@mail.com", 9123456780L, "45
    // River Rd");
    // s.displayDeets();
    // c.displayDeets();
    //
    // // --- Products + Reviews (displayDeets, asinNum, Review.DisplayReview) ---
    // Product p1 = new Product(111, "Keyboard", 4, 1999, 10, "Compact mechanical
    // keyboard");
    // Product p2 = new Product(222, "Mouse", 5, 999, 25, "Wireless mouse");
    // Review r1 = new Review("Bob", 5, "Great build quality!");
    // p1.displayDeets();
    // p2.displayDeets();
    //
    // // --- Seller inventory (showInventory, showSingleProduct) ---
    // s.product_inventory.add(p1);
    // s.product_inventory.add(p2);
    // s.showInventory();
    // s.showSingleProduct(222);
    //
    // // --- Customer cart (addToCart, showCart, deleteFromCart) ---
    // c.addToCart(p1);
    // c.addToCart(p2);
    // c.showCart();
    // c.deleteFromCart(111);
    // c.showCart();
    //
    // // --- Product.updateDeets via Seller.updateProduct (hardcoded System.in) ---
    // // This updates product with ASIN 222
    // String updateInputs = String.join("\n",
    // "333", // new ASIN
    // "Mouse Pro", // new name
    // "4", // ratings
    // "1299", // price
    // "30", // qty
    // "Upgraded sensor"); // description
    // System.setIn(new ByteArrayInputStream(updateInputs.getBytes()));
    // s.updateProduct(222); // calls Product.updateDeets() internally
    // s.showSingleProduct(333); // ASIN changed from 222 -> 333 above
    //
    // // --- Seller.addProduct (hardcoded System.in) ---
    // // NOTE: addProduct mixes nextInt() and nextLine(), so name/description will
    // // read as empty
    // // because of trailing newlines after nextInt(). We're just smoke-testing the
    // // method here.
    // // --- Seller.addProduct (hardcoded System.in) ---
    // String addInputs = String.join("\n",
    // "444", // ASIN (int)
    // "Mouse X", // name
    // "5", // ratings (int)
    // "1499", // price (int)
    // "12", // qty (int)
    // "" // description (empty line)
    // ) + "\n"; // <-- ensure there's an actual final line
    //
    // System.setIn(new ByteArrayInputStream(addInputs.getBytes()));
    // s.addProduct();
    //
    // // --- Seller.deleteFromInventory (also uses Product.asinNum) ---
    // s.deleteFromInventory(111); // remove p1 by ASIN
    // s.showInventory();
    //
    // // Done: every method is exercised at least once:
    // // Person.displayDeets, Product.displayDeets, Product.asinNum,
    // // Product.updateDeets,
    // // Review.DisplayReview, Customer.addToCart/showCart/deleteFromCart,
    // //
    // Seller.addProduct/showInventory/deleteFromInventory/showSingleProduct/updateProduct.
  }
}
