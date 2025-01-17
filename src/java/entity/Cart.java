package entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import entity.Preferential;
import repository.OrderRepository;
public class Cart {
    private String discountCode;
    private double discountPercent =0f;
    private List<Items> cart;
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    private String orderedId;
    private String orderStatus;
    private String date;
    public void removeAll(){
        cart.clear();
    }
    public String getOrderedId() {
        return orderedId;
    }

    private int paymentType = 0; //0 la COD , 1 la CK

    public void setOrderedId(String orderedId) {
        this.orderedId = orderedId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Cart() {
        cart = new ArrayList<>();
    }

    public void setCart(List<Items> cart) {
        this.cart.addAll(cart);
    }


    public String addItems(Items item) {
        if (cart.isEmpty()) {
            cart.add(item);
            return "=========>CART : add Thanh Cong<==========";
        } else {
            for (Items items : cart) {
                if (items.equal(item)) {
                    Items hang = cart.get(cart.indexOf(items));
                    hang.setAmmout(hang.getAmmout() + item.getAmmout());
                    return "=========>CART : add Thanh Cong<==========";

                }
            }
            cart.add(item);
            return "=========>CART : add Thanh Cong<==========";
        }
    }

    public double getThanhTien(double phatsinh) {
        double tong = 0;
        for (Items item : cart) {
            tong += item.getPrice();
        }
        return tong + phatsinh;
    }
    public double getThanhTienAfterPurchase(double phatsinh) {
        double tong = 0;
        for (Items item : cart) {
            tong += item.getPriceAfterPurchase(orderedId);
        }
        return tong + phatsinh;
    }
    public double getThanhTienAfterPurchaseDiscount(double phatsinh) {
        double tong = 0;
        for (Items item : cart) {
            tong += item.getPriceAfterPurchase(orderedId);
        }
         Preferential pr = new Preferential();
        double discountPercent = OrderRepository.getDiscountPercent(pr.getId());
        return (tong + phatsinh) - (tong*discountPercent);
    }
    public String getThanhTienString(double phatsinh) {
        if (cart.isEmpty()) {
            return "0";
        }
        return formatter.format(getThanhTien(phatsinh));

    }
    public String getThanhTienStringDiscount(double phatsinh) {
        if (cart.isEmpty()) {
            return "0";
        }
        return formatter.format(getThanhTienDiscount(phatsinh));

    }
    public double getThanhTienDiscount(double phatsinh) {
        Preferential pr = new Preferential();
        double tong = 0;
        double discountPercent = OrderRepository.getDiscountPercent(pr.getId());
        for (Items item : cart) {
            tong += item.getPrice();
        }
        double x = (tong - tong *discountPercent) + phatsinh;
        System.out.println(x);
        
        return x;
    }
    public String getThanhTienStringAfterPurchase(double phatsinh) {
        if (cart.isEmpty()) {
            return "0";
        }
        return formatter.format(getThanhTienAfterPurchase(phatsinh));

    }
    public String getThanhTienStringAfterPurchaseDiscount(double phatsinh) {
        if (cart.isEmpty()) {
            return "0";
        }
        return formatter.format(getThanhTienAfterPurchaseDiscount(phatsinh));

    }

    public List<Items> getCart() {
        return cart;
    }

    //    public void setCart(List<Items> cart) {
//        this.cart = cart;
//    }
    public String increaseAmmount(String id) {
        if (cart.isEmpty()) {
            return "=========>Khong ton tai san pham increaseAmmount(String id) <==========";
        } else {
            for (Items items : cart) {
                if (items.getProduct().getProductId().equals(id)) {
                    Items hang = cart.get(cart.indexOf(items));
                    if (hang.getAmmout() == hang.getProduct().getProductAmount()) {
                        return "=========>CART : Tang Thanh Cong increaseAmmount(String id)<==========";
                    }
                    hang.setAmmout(hang.getAmmout() + 1);
                    return "=========>CART : Tang Thanh Cong increaseAmmount(String id)<==========";

                }
            }
            return "=========>CART : Tang Thanh Cong<==========";
        }
    }

    public String decreaseAmmount(String id) {
        if (cart.isEmpty()) {
            return "=========>Khong ton tai san pham decreaseAmmount(String id) <==========";
        } else {
            for (Items items : cart) {
                if (items.getProduct().getProductId().equals(id)) {
                    Items hang = cart.get(cart.indexOf(items));
                    if (hang.getAmmout() ==1) {
                        return "=========>CART : giam  Thanh Cong decreaseAmmount(String id)<==========";
                    }
                    hang.setAmmout(hang.getAmmout() - 1);
                    return "=========>CART : giam  Thanh Cong decreaseAmmount(String id)<==========";

                }
            }
            return "=========>CART : giam Thanh Cong decreaseAmmount(String id)<==========";
        }
    }
    public String removeItem(String id) {
        if (cart.isEmpty()) {
            return "=========>Khong ton tai san pham decreaseAmmount(String id) <==========";
        } else {
            for (Items items : cart) {
                if (items.getProduct().getProductId().equals(id)) {
                    cart.remove(cart.indexOf(items));
                    return "=========>CART : remove Thanh Cong<==========";

                }
            }
            return "=========>CART : remove Thanh Cong removeItem(String id)<==========";
        }
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public DecimalFormat getFormatter() {
        return formatter;
    }

    public void setFormatter(DecimalFormat formatter) {
        this.formatter = formatter;
    }

}
