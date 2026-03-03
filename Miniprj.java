package Sesion16;

import java.util.*;

interface IRepository<T> {
    boolean add(T item);

    boolean removeById(String id);

    T findById(String id);

    List<T> findAll();
}

abstract class Product {
    protected String id;
    protected String name;
    protected double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public abstract double calculateFinalPrice();

    public void displayInfo() {
        System.out.println("Id: " + id);
        System.out.println("Name: " + name);
        System.out.println("Price: " + price);
    }
}

class ElectronicProduct extends Product {
    private int warrantyMonths;

    public ElectronicProduct(String id, String name, double price, int warrantyMonths) {
        super(id, name, price);
        this.warrantyMonths = warrantyMonths;
    }

    public double calculateFinalPrice() {
        if (warrantyMonths > 12) {
            return price + 1000000;
        }
        return price;
    }

    public void displayInfo() {
        super.displayInfo();
        System.out.println("Bao Hanh: " + warrantyMonths + " Thang");
    }
}

class FoodProduct extends Product {
    private int discountPercent;

    public FoodProduct(String id, String name, double price, int discountPercent) {
        super(id, name, price);
        this.discountPercent = discountPercent;
    }

    public double calculateFinalPrice() {
        return price - (price * discountPercent / 100.0);
    }

    public void displayInfo() {
        super.displayInfo();
        System.out.println("Discount: " + discountPercent + "%");
    }
}

class ProductRepository implements IRepository<Product> {

    private List<Product> list = new ArrayList<>();
    private Map<String, Product> map = new HashMap<>();

    public boolean add(Product item) {
        if (item == null || map.containsKey(item.getId())) {
            return false;
        }
        list.add(item);
        map.put(item.getId(), item);
        return true;
    }

    public boolean removeById(String id) {
        if (!map.containsKey(id))
            return false;

        Product p = map.get(id);
        list.remove(p);
        map.remove(id);
        return true;
    }

    public Product findById(String id) {
        return map.get(id);
    }

    public List<Product> findAll() {
        return list;
    }

    public Map<String, Integer> thongKe() {
        Map<String, Integer> result = new HashMap<>();
        for (Product p : list) {
            String loai = (p instanceof ElectronicProduct) ? "Dien Tu" : "Do An";
            result.put(loai, result.getOrDefault(loai, 0) + 1);
        }
        return result;
    }
}

public class Miniprj {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ProductRepository repo = new ProductRepository();

        while (true) {
            System.out.println("\n===== Quan Ly Cua Hang San Pham =====");
            System.out.println("1. Them Do Dien Tu");
            System.out.println("2. Them Do An");
            System.out.println("3. Hien Thi Tat Ca");
            System.out.println("4. Tim Kiem San Pham Theo ID");
            System.out.println("5. Sap Xep Theo Gia");
            System.out.println("6. Thong Ke San Pham");
            System.out.println("7. Thoat Chuong Trinh");
            System.out.print("Chon: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Id: ");
                    String idE = sc.nextLine();
                    System.out.print("Name: ");
                    String nameE = sc.nextLine();
                    System.out.print("Price: ");
                    double priceE = sc.nextDouble();
                    System.out.print("Bao Hanh (thang): ");
                    int warranty = sc.nextInt();

                    if (repo.add(new ElectronicProduct(idE, nameE, priceE, warranty)))
                        System.out.println("Them San Pham Thanh Cong");
                    else
                        System.out.println("Ma San Phan Da Ton Tai!");
                    break;

                case 2:
                    System.out.print("ID: ");
                    String idF = sc.nextLine();
                    System.out.print("Name: ");
                    String nameF = sc.nextLine();
                    System.out.print("Price: ");
                    double priceF = sc.nextDouble();
                    System.out.print("Discount (%): ");
                    int discount = sc.nextInt();

                    if (repo.add(new FoodProduct(idF, nameF, priceF, discount)))
                        System.out.println("Them San Pham Thanh Cong");
                    else
                        System.out.println("Ma San Phan Da Ton Tai!");
                    break;

                case 3:
                    for (Product p : repo.findAll()) {
                        p.displayInfo();
                        System.out.println("Tong Tien: " + p.calculateFinalPrice());
                        System.out.println("-------------------");
                    }
                    break;

                case 4:
                    System.out.print("Nhap ma san pham can tim: ");
                    String idSearch = sc.nextLine();
                    Product p = repo.findById(idSearch);
                    if (p != null) {
                        p.displayInfo();
                        System.out.println("Tong Tien: " + p.calculateFinalPrice());
                    } else {
                        System.out.println("Khong tim thay san pham");
                    }
                    break;

                case 5:
                    Collections.sort(repo.findAll(), new Comparator<Product>() {
                        public int compare(Product o1, Product o2) {
                            return Double.compare(o1.getPrice(), o2.getPrice());
                        }
                    });
                    System.out.println("Da xap xep");
                    break;

                case 6:
                    Map<String, Integer> tk = repo.thongKe();
                    for (String key : tk.keySet()) {
                        System.out.println(key + ": " + tk.get(key));
                    }
                    break;

                case 7:
                    System.out.println("Thoat");
                    return;

                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }
}