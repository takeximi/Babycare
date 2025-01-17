package repository;

import config.DBConnect;
import entity.Food;
import entity.Revenue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class StatisticsRepository {


    public static int getUserAmount() {

        try {
            Connection con = DBConnect.getConnection();
            String query = "select Count(1) from tblCustomer";

            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getInt(1);


        } catch (Exception e) {
            System.out.println("loi acceptBill() servicerespository");
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static int getNumberOfOrdersToConfirm() {

        try {
            Connection con = DBConnect.getConnection();
            String query = "select count(1) from tblBill t where  t.StatusBill like N'Đang%'";

            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getInt(1);


        } catch (Exception e) {
            System.out.println("loi acceptBill() servicerespository");
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static int getNumberOfPetsLeft() {

        try {
            Connection con = DBConnect.getConnection();
            String query = "select Count(1) from tblPet where StatusPet=1";

            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getInt(1);


        } catch (Exception e) {
            System.out.println("loi acceptBill() servicerespository");
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public static double getOrderRevenue() {

        try {
            Connection con = DBConnect.getConnection();
            String query = "SELECT SUM(PriceAtPuchase) AS total_price\n" +
                    "FROM [PetShopDB].[dbo].[tblOrderDetails] od\n" +
                    "JOIN [PetShopDB].[dbo].[tblBill] b ON b.BillID = od.BillID\n" +
                    "LEFT JOIN [PetShopDB].[dbo].[tblPreferential] p ON b.PreferentialID = p.Preferential\n" +
                    "WHERE b.StatusBill = N'Đã thanh toán'\n" +
                    "    AND YEAR(b.DateCreate) = 2024";

            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getDouble(1);


        } catch (Exception e) {
            System.out.println("loi acceptBill() servicerespository");
            e.printStackTrace();
            return 0;
        }
        return 0;
    }
    public static double getOrder1() {
        return getMonthlyOrderSum(1);

    }
    public static double getOrder2() {
        return getMonthlyOrderSum(2);
    }

    public static double getOrder3() {
        return getMonthlyOrderSum(3);
    }

    public static double getOrder4() {
        return getMonthlyOrderSum(4);
    }

    public static double getOrder5() {
        return getMonthlyOrderSum(5);
    }

    public static double getOrder6() {
        return getMonthlyOrderSum(6);
    }

    public static double getOrder7() {
        return getMonthlyOrderSum(7);
    }

    public static double getOrder8() {
        return getMonthlyOrderSum(8);
    }

    public static double getOrder9() {
        return getMonthlyOrderSum(9);
    }

    public static double getOrder10() {
        return getMonthlyOrderSum(10);
    }

    public static double getOrder11() {
        return getMonthlyOrderSum(11);
    }

    public static double getOrder12() {
        return getMonthlyOrderSum(12);
    }

    private static double getMonthlyOrderSum(int month) {
        try {
            Connection con = DBConnect.getConnection();
            String query = "SELECT SUM(PriceAtPuchase) AS total_price\n" +
                    "FROM [PetShopDB].[dbo].[tblOrderDetails] od\n" +
                    "JOIN [PetShopDB].[dbo].[tblBill] b ON b.BillID = od.BillID\n" +
                    "LEFT JOIN [PetShopDB].[dbo].[tblPreferential] p ON b.PreferentialID = p.Preferential\n" +
                    "WHERE b.StatusBill = N'Đã thanh toán'\n" +
                    "    AND YEAR(b.DateCreate) = 2024 and MONTH(b.DateCreate)= ?";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, month);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("total_price");
            }

        } catch (Exception e) {
            System.out.println("Error in getMonthlyOrderSum() method in StatisticsRepository");
            e.printStackTrace();
            return 0;
        }
        return 0;
    }


    public static ArrayList<Food> getListRankOfFood() {
        ArrayList<Food> listFood = null;
        try {
            Connection con = DBConnect.getConnection();
            String query = " select x.ProductID,f.FoodName,x.AmountProduct \n" +
                    "\t from(\n" +
                    "\tselect o.ProductID,Sum(o.AmountProduct) as AmountProduct  from tblBill b\n" +
                    "\tjoin tblOrderDetails o on o.BillID=b.BillID\n" +
                    "\twhere b.StatusBill=N'Đã thanh toán' and  YEAR(b.DateCreate)=2023\n" +
                    "\tgroup by o.ProductID\n" +
                    "\t  ) as x\n" +
                    "\t  join  tblFood f on x.ProductID=f.FoodID\n" +
                    "\t  order by x.AmountProduct desc";

            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            listFood = new ArrayList<Food>();
            while (resultSet.next()) {
                String foodID = resultSet.getString(1);
                String foodName = resultSet.getString(2);
                int foodAmount = resultSet.getInt(3);
                Food food = new Food();
                food.setProductId(foodID);
                food.setProductName(foodName);
                food.setProductAmount(foodAmount);
                listFood.add(food);
            }


        } catch (Exception e) {
            System.out.println("loi acceptBill() servicerespository");
            e.printStackTrace();
            return null;
        }
        return listFood;
    }
    public static  ArrayList<Revenue> getOrderRevenueEachMonths () {
        ArrayList<Revenue> listRevenues=new ArrayList<>();
        for(int i=1;i<=12;i++) listRevenues.add(new Revenue(i+"",0));


        for ( Revenue r: listRevenues
             ) {
            try {
                Connection con = DBConnect.getConnection();
                String query = "select SUM(x.PriceAtPuchase*(1-rate))\n" +
                        "from(\n" +
                        "select od.BillID,od.PriceAtPuchase,isnull(p.Rate,0) as rate from tblOrderDetails od\n" +
                        "join tblBill b on b.BillID=od.BillID\n" +
                        "left join tblPreferential p on b.PreferentialID=p.Preferential \n" +
                        "where  b.StatusBill=N'Đã thanh toán' and YEAR(b.DateCreate)=2023 and MONTH(b.DateCreate)=?\n" +
                        ") x";

                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1,r.getMonth());
                ResultSet resultSet = stmt.executeQuery();


                if (resultSet.next()){
                    r.setMoney(resultSet.getDouble(1));
                }



            } catch (Exception e) {
                System.out.println("loi acceptBill() getOrderRevenueEachMonths");
                e.printStackTrace();

            }

        }

        return listRevenues;
    }
    public static double getServiceRevenueByYear() {

        try {
            Connection con = DBConnect.getConnection();
            String query = "select sum(sb.PriceAtPurchase) from tblServiceBill sb\n" +
                    "where sb.StatusBill=2 \n" +
                    "and YEAR(SetDay)=2023";

            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getDouble(1);


        } catch (Exception e) {
            System.out.println("loi acceptBill() getServiceRevenueByYear");
            e.printStackTrace();
            return 0;
        }
        return 0;
    }
    public static  ArrayList<Revenue> getServiceRevenueEachMonths () {
        ArrayList<Revenue> listRevenues=new ArrayList<>();
        for(int i=1;i<=12;i++) listRevenues.add(new Revenue(i+"",0));


        for ( Revenue r: listRevenues
        ) {
            try {
                Connection con = DBConnect.getConnection();
                String query = "select sum(sb.PriceAtPurchase) from tblServiceBill sb\n" +
                        "where sb.StatusBill=2 \n" +
                        "and YEAR(SetDay)=2023 and MONTH(SetDay)=?";

                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1,r.getMonth());
                ResultSet resultSet = stmt.executeQuery();


                if (resultSet.next()){
                    r.setMoney(resultSet.getDouble(1));
                }



            } catch (Exception e) {
                System.out.println("loi acceptBill() getOrderRevenueEachMonths");
                e.printStackTrace();

            }

        }

        return listRevenues;
    }
    public static void main(String[] args) {
        for (Revenue e:
             getServiceRevenueEachMonths()) {
            System.out.println(e);
        }
    }
}
