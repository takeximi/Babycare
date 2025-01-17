package repository;

import config.DBConnect;
import entity.*;
import service.Isvalid;
import service.RandomGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class OrderRepository {
    public static String getOrderId() {
        try {
            String OrderId = RandomGenerator.generateRandomString();
            String query = "select BillID from tblBill";
            Connection con = DBConnect.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet results = stmt.executeQuery();
            ArrayList<String> listOrderID = new ArrayList<>();
            while (results.next()) {
                String OrderIdDB = results.getString(1);
                listOrderID.add(OrderIdDB);
            }
            for (String oDB : listOrderID) {
                if (oDB.equals(OrderId)) {
                    OrderId = RandomGenerator.generateRandomString();
                }
            }
            return OrderId;
        } catch (Exception e) {
            System.out.println("Loi method checkExistOrder(Cart cart ) trong OrderRepository.java ");
        }
        return null;
    }

    public static String createOrder(Cart cart, User user) {
        try {
            Connection con = DBConnect.getConnection();
            String query = "insert into tblBill (BillID,CustomerID,AddressDelivery,DateCreate,PreferentialID,StatusBill) values (?,?,?,?,?,?)";
            String orderID = getOrderId();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, orderID);
            stmt.setString(2, user.getUserId());
            stmt.setString(3, user.getAddress());
            stmt.setString(4, Isvalid.getCurrentDate());
            stmt.setString(5, cart.getDiscountCode());
            if (cart.getPaymentType() == 0) {
                stmt.setString(6, "Đang xử lý-COD");

            } else {
                stmt.setString(6, "Đang xử lý-CK");
            }
            stmt.executeUpdate();
            con.close();
            createOrderDetail(cart, orderID);
            return orderID;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Loi method createOrder(Cart cart, User user) trong OrderRepository.java ");
        }
        return null;
    }

    public static boolean createOrderDetail(Cart cart, String orderId) {
        System.out.println("=>>>>....>>>>>>>>>>>>>>>>>>>>>>>" + orderId);
        System.out.println(cart.getCart());
        System.out.println(cart.getCart().get(0).getProduct().getProductId());
        System.out.println(cart.getCart().get(0).getProduct().getProductAmount());
        System.out.println(cart.getCart().get(0).getProduct().getProductPrice());
        for (Items i : cart.getCart()) {
            try {
                Connection con = DBConnect.getConnection();
                String query = "insert into tblOrderDetails values (?,?,?,?)";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, orderId);
                stmt.setString(2, i.getProduct().getProductId());
                stmt.setInt(3, i.getAmmout());
                stmt.setDouble(4, i.getPrice());
                stmt.executeUpdate();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Loi method createOrderDetail(Cart cart,String orderId) trong OrderRepository.java ");

            }
        }
        return true;
    }

    ///bugggggggggggggggggggggggggggggggggggggggggggggg
    public static ArrayList<Items> getOrder(String OrderId) {
        try {
            ArrayList<Items> orderedList = new ArrayList<>();
            Connection con = DBConnect.getConnection();
            String query = "select * from tblOrderDetails where BillID = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, OrderId);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                Items item = new Items();
                item.setAmmout(results.getInt(3));
                item.setProduct(getProductById(results.getString(2)));
                //lay id product
                String productID = results.getString(2);
                if (productID.startsWith("P")) {
                    item.getProduct().setListImg(ProductRepository.getListPetImage(productID));
                } else if (productID.startsWith("F")) {
                    item.getProduct().setListImg(ProductRepository.getListFoodImage(productID));
                }
                orderedList.add(item);
            }
            con.close();
            return orderedList;
        } catch (Exception e) {
            System.out.println("=============>ERROR :  ArrayList<Items> getOrder(String OrderId) <==============");
        }
        return null;
    }

    public static Product getProductById(String orderdetailId) {
        try {
            Connection con = DBConnect.getConnection();
            String query = (orderdetailId.contains("F")) ? "select * from tblFood where FoodID=?" : " select * from tblPet where PetID=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, orderdetailId);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                if (orderdetailId.contains("F")) {
                    String id = results.getString(1);
                    String name = results.getString(2);
                    double price = results.getDouble(5);
                    con.close();
                    return new Product(id, name, price);
                } else {
                    String id = results.getString(1);
                    String name = results.getString(2);
                    double price = results.getDouble(4);
                    con.close();
                    return new Product(id, name, price);
                }
            }
            con.close();
            return null;

        } catch (Exception e) {
            System.out.println("=========>ERROR :getProductById(String orderdetailId) <===========");
        }
        return null;
    }

    public static String getOrderStatus(String orderId) {
        String id = null;
        try {
            Connection con = DBConnect.getConnection();
            String query = "select StatusBill from tblBill where BillID = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, orderId);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                id = results.getString(1);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getOrderStatus()<=============");
        }
        return id;
    }

    public static ArrayList<String> getOrderIdList(String userId) {
        ArrayList<String> listOrderId = new ArrayList<>();
        try {
            Connection con = DBConnect.getConnection();
            String query = "select BillID from tblBill where CustomerID = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, userId);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                listOrderId.add(results.getString(1));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getOrderIdList(String userId)<=============");
        }
        return listOrderId;
    }

    public static String getOrderDate(String orderId) {
        String date = null;
        try {
            Connection con = DBConnect.getConnection();
            String query = "select DateCreate from tblBill where BillID= ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, orderId);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                date = results.getString(1);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getOrderStatus()<=============");
        }
        return date;
    }

    public static boolean acceptOrder(String orderId, String employeeID) {

        try {
            Connection con = DBConnect.getConnection();
            String query = "update tblBill set StatusBill=N'Đã xác nhận',EmployeeID=? where BillID=?";
            PreparedStatement stmt = con.prepareStatement(query);
            String accept = "Đã xác nhận";
            stmt.setString(1, employeeID);

            stmt.setString(2, orderId);
            stmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : acceptOrder()<=============");
            return false;
        }
        return true;
    }

    public static boolean cancelOrder(String orderId, String employeeID) {

        try {
            Connection con = DBConnect.getConnection();
            String query = "update tblBill set StatusBill=N'Đã hủy',employeeID=?  where BillID=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, employeeID);
            stmt.setString(2, orderId);
            stmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : cancelOrder()<=============");
            return false;
        }
        return true;
    }

    public static ArrayList<OrderAccept> getAllOrder() {
        ArrayList<OrderAccept> listOrder;
        try {
            listOrder = new ArrayList<>();
            Connection con = DBConnect.getConnection();
            String query = "select * from tblBill where StatusBill like N'Đang%'\n" +
                    "order by DateCreate    ";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String orderID = rs.getString(1);
                String employeeID = rs.getString(2);
                String username = rs.getString(3);
                String address = rs.getString(4);
                String date = rs.getString(5);
                String preferentialId = rs.getString(6);
                String status = rs.getString(7);
                OrderAccept orderAccept = new OrderAccept();
                orderAccept.setIdOrder(orderID);
                orderAccept.setUsername(username);
                orderAccept.setAddress(address);
                orderAccept.setDate(date);
                orderAccept.setEmployeeID(employeeID);
                orderAccept.setOrderStatus(status);
                orderAccept.setDiscountId(preferentialId);
                listOrder.add(orderAccept);
            }


            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getAllOrder()<=============");
            return null;
        }
        return listOrder;
    }

    public static ArrayList<OrderAccept> getAllOrderPaid() {
        ArrayList<OrderAccept> listOrder;
        try {
            listOrder = new ArrayList<>();
            Connection con = DBConnect.getConnection();
            String query = "select * from tblBill where StatusBill=N'Đã thanh toán'\n" +
                    "order by DateCreate desc";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String orderID = rs.getString(1);
                String employeeID = rs.getString(2);
                String username = rs.getString(3);
                String address = rs.getString(4);
                String date = rs.getString(5);
                String preferentialId = rs.getString(6);
                String status = rs.getString(7);
                OrderAccept orderAccept = new OrderAccept();
                orderAccept.setIdOrder(orderID);
                orderAccept.setUsername(username);
                orderAccept.setAddress(address);
                orderAccept.setDate(date);
                orderAccept.setEmployeeID(employeeID);
                orderAccept.setOrderStatus(status);
                orderAccept.setDiscountId(preferentialId);
                listOrder.add(orderAccept);
            }


            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getAllOrderPaid()<=============");
            return null;
        }
        return listOrder;
    }

    public static ArrayList<OrderAccept> getAllOrderAccepted() {
        ArrayList<OrderAccept> listOrder;
        try {
            listOrder = new ArrayList<>();
            Connection con = DBConnect.getConnection();
            String query = "select * from tblBill where StatusBill=N'Đã xác nhận'\n" +
                    "order by DateCreate";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String orderID = rs.getString(1);
                String employeeID = rs.getString(2);
                String username = rs.getString(3);
                String address = rs.getString(4);
                String date = rs.getString(5);
                String preferentialId = rs.getString(6);
                String status = rs.getString(7);
                OrderAccept orderAccept = new OrderAccept();
                orderAccept.setIdOrder(orderID);
                orderAccept.setUsername(username);
                orderAccept.setAddress(address);
                orderAccept.setDate(date);
                orderAccept.setEmployeeID(employeeID);
                orderAccept.setOrderStatus(status);
                orderAccept.setDiscountId(preferentialId);
                listOrder.add(orderAccept);
            }


            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : cancelOrder()<=============");
            return null;
        }
        return listOrder;
    }

    public static ArrayList<OrderAccept> getAllOrderCancel() {
        ArrayList<OrderAccept> listOrder;
        try {
            listOrder = new ArrayList<>();
            Connection con = DBConnect.getConnection();
            String query = "select * from tblBill where StatusBill=N'Đã hủy'\n" +
                    "order by DateCreate desc";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String orderID = rs.getString(1);
                String employeeID = rs.getString(2);
                String username = rs.getString(3);
                String address = rs.getString(4);
                String date = rs.getString(5);
                String preferentialId = rs.getString(6);
                String status = rs.getString(7);
                OrderAccept orderAccept = new OrderAccept();
                orderAccept.setIdOrder(orderID);
                orderAccept.setUsername(username);
                orderAccept.setAddress(address);
                orderAccept.setDate(date);
                orderAccept.setEmployeeID(employeeID);
                orderAccept.setOrderStatus(status);
                orderAccept.setDiscountId(preferentialId);
                listOrder.add(orderAccept);
            }


            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : cancelOrder()<=============");
            return null;
        }
        return listOrder;
    }

    public static double getPriceOrdered(String orderId, String productId) {
       
        double price = 0;
        try {
            Connection con = DBConnect.getConnection();
            String query = "select PriceAtPuchase from tblOrderDetails where ProductID=? and BillID =?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, productId);
            stmt.setString(2, orderId);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                price = results.getDouble(1);
                System.out.println("=>>>>>>>>>>>>>>>>>>.." + price);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getPriceOrdered()<=============");
        }
        return price;
    }

    public static double getDiscountPercent(String discountID) {
        double quantity = 0f;
        try {
            Connection con = DBConnect.getConnection();
            String query = "select Quantity from tblPreferential where Preferential =?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, discountID);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                quantity = results.getDouble(1);
                System.out.println("=>>>>>>>>>>>>>>>>>>.." + quantity);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getDiscountPercent()<=============");
        }
        return quantity;
    }

    public static String getDiscountCodeByOrderID(String orderid) {
        String discountCode = null;
        try {
            Connection con = DBConnect.getConnection();
            String query = "select PreferentialID from tblBill where BillID =?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, orderid);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                discountCode = results.getString(1);
                System.out.println("=>>>>>>>>>>>>>>>>>>.." + discountCode);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getDiscountCodeByOrderID()<=============");
        }
        return discountCode;
    }


    public static boolean paidOrder(String orderId, String employeeID) {

        try {
            Connection con = DBConnect.getConnection();
            String query = "update tblBill set StatusBill=N'Đã thanh toán',employeeID=?  where BillID=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, employeeID);
            stmt.setString(2, orderId);
            stmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : cancelOrder()<=============");
            return false;
        }
        return true;
    }

    public static boolean checkValidStatusOfPet(String orderId) {

        try {
            Connection con = DBConnect.getConnection();
            String query = "select b.BillID,p.PetID,p.StatusPet from tblBill b\n" +
                    "join tblOrderDetails o on o.BillID=b.BillID\n" +
                    "join tblPet p on o.ProductID=p.PetID\n" +
                    "where b.BillID=?";
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, orderId);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                if (results.getInt(3) == 0) return false;
            }
            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : checkValidStatusOfPet()<=============");
            return false;
        }
        return true;
    }

    public static boolean acceptedPet(String billID) {
        try {
            Connection con = DBConnect.getConnection();
            String query = "update tblPet \n" +
                    "set StatusPet=0\n" +
                    "where PetID in\n" +
                    "(\n" +
                    "select ProductID\n" +
                    "from tblOrderDetails\n" +
                    "where BillID=? and ProductID like 'P%'\n" +
                    ")";
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, billID);
            stmt.executeUpdate();

            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : acceptedPet()<=============");
            return false;
        }
        return true;
    }

    public static int getRemainingAmount(String foodID) {
        int amount = 0;
        try {
            Connection con = DBConnect.getConnection();
            String query = "select y.FoodID, y.SumAmount,z.SellAmount from \n" +
                    "(\n" +
                    "select imp.FoodID ,Sum(imp.Amount) as SumAmount  from tblImported imp\n" +
                    "group by imp.FoodID\n" +
                    ") y\n" +
                    "left join (\n" +
                    "select odr.ProductID,sum(odr.AmountProduct) as SellAmount from tblOrderDetails odr\n" +
                    "join tblBill on tblBill.BillID=odr.BillID\n" +
                    "where (StatusBill=N'Đã xác nhận' or StatusBill=N'Đã thanh toán')\n" +
                    "group by odr.ProductID\n" +
                    "\n" +
                    ")z on y.FoodID=z.ProductID \n" +
                    "where FoodID=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, foodID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int sumAmount = rs.getInt(2);
                int sellAmount = rs.getInt(3);
                amount = sumAmount - sellAmount;
            }

            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getRemainingAmount()<=============");
            return amount;
        }
        return amount;
    }

    public static boolean checkValidAmountOfPet(String billID) {

        try {
            Connection con = DBConnect.getConnection();
            String query = "select b.BillID,f.FoodID,od.AmountProduct from tblBill b \n" +
                    "join tblOrderDetails od on od.BillID=b.BillID \n" +
                    "join tblFood f on f.FoodID= od.ProductID\n" +
                    "where b.BillID=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, billID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int remainingAmount = getRemainingAmount(rs.getString(2));
                int amount = rs.getInt(3);
                if (remainingAmount - amount < 0) return false;
            }

            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : checkValidAmountOfPet()<=============");
            return false;
        }
        return true;


    }


    public static ArrayList<Preferential> getListDiscount() {

        ArrayList<Preferential> listP;
        try {
            Connection con = DBConnect.getConnection();
            String query = "select * from tblPreferential";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            listP = new ArrayList<Preferential>();
            while (rs.next()) {
                String id = rs.getString(1);
                String preferentialName = rs.getString(2);
                String startDay = rs.getString(3);
                String endDay = rs.getString(4);
                double rate = rs.getDouble(5);
                Preferential newP = new Preferential(id, preferentialName, startDay, endDay, rate);
                listP.add(newP);
            }

            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : getListDiscount()<=============");
            return null;
        }
        return listP;


    }

    public static boolean createDiscount(Preferential p) {
        try {
            Connection con = DBConnect.getConnection();
            String query = "insert into tblPreferential\n" +
                    "(Preferential,PreferentialName,StartDay,EndDay,Quantity)\n" +
                    "values (?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, p.getId());
            stmt.setString(2, p.getPreferentialName());
            stmt.setString(3, p.getStartDay());
            stmt.setString(4, p.getEndDay());
            stmt.setDouble(5, p.getRate() / 100);
            stmt.executeUpdate();


            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : checkValidAmountOfPet()<=============");
            return false;
        }
        return true;


    }
    public static User getCustomerByBillID(String billID) {
        User user=null;

        try {
            Connection con = DBConnect.getConnection();
            String query = "select tblCustomer.* from tblBill\n" +
                    "join tblCustomer on tblBill.CustomerID=tblCustomer.CustomerID\n" +
                    "where BillID=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, billID);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                user=new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
            }


            con.close();
        } catch (Exception e) {
            System.out.println("==========>ERROR : checkValidAmountOfPet()<=============");
            return null;
        }
        return user;


    }

    public static void main(String[] args) {
        for (Preferential p : getListDiscount()
        ) {
            System.out.println(p);
        }

    }
}


