<%--
    Document   : ordered-form
    Created on : Mar 18, 2023, 2:42:46 PM
    Author     : quang
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
    Document   : ordered-form
    Created on : Mar 18, 2023, 2:25:02 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="container-fluid">
  <div class="container">
    <div class="donhang">
      <div class="border-start border-5 border-primary ps-5 mb-5 mt-5">
        <h6 class="text-primary text-uppercase">Đơn hàng</h6>
        <h1 class="display-5 text-uppercase mb-0">Kiểm tra đơn hàng </h1>
      </div>
      <div class="d-flex justify-content-center">
        <h2 class="text-primary m-1 p-0">Tình trạng đơn hàng: </h2>
        <h2 class="text-danger m-1 p-0"> ${orderStatus}</h2>
      </div>
      <div class="mt-5 text-center">

        <p>Đơn hàng đã được gửi cho hãng vận chuyển Viettel Post</p>
        <p class="text-danger">Mã đơn hàng: ${orderId}</p>
      </div>
      <div class="d-flex justify-content-around">
        <div class="border-top pt-3 col-md-7 kt-left">
          <h3>Thông tin đơn hàng</h3>
          <c:forEach items="${orderedCart.cart}" var="i">
            <div class="border-bottom">
              <div class="d-flex">
                <div class="p-1">
                  <img width="60px" height="60px" src="img/product/${i.product.productId}.jpg" alt="">
                </div>
                <div class="p-1">
                  <strong>${i.product.productName}</strong>
                </div>
              </div>
              <div style="text-align: right; font-size: larger;" class="p-1">
                <strong>X ${i.getAmmout()}</strong>
              </div>
            </div>
          </c:forEach>
          <div class="mt-3">
            <div class="w-50 d-flex justify-content-between">
              <span>Tổng giá trị sản phẩm:</span>
              <strong style="font-size: larger;">${orderedCart.getThanhTienStringAfterPurchase(0)}</strong>
            </div>
            <div class="w-50 d-flex justify-content-between">
              <span>Phí vận chuyển:</span>
              <strong style="font-size: larger;">30000</strong>
            </div>
            <div class="w-50 border-top pt-2 mt-3 d-flex justify-content-between">
              <span>Tổng cộng:</span>
              <c:if test="${orderedCart.discountCode == null}">
                <strong style="font-size: x-large;">${orderedCart.getThanhTienStringAfterPurchase(30000)}</strong>
              </c:if>
              <c:if test="${orderedCart.discountCode != null}">
                <strong style="font-size: x-large;"><del>${orderedCart.getThanhTienStringAfterPurchase(30000)} </del> ${orderedCart.getThanhTienStringAfterPurchaseDiscount(30000)}</strong>

              </c:if>
            </div>
          </div>
        </div>
        <div class="border-top pt-3 col-md-4 kt-right">
          <h3>Thông tin khách hàng</h3>
          <div>
            <p>${user.firstname} ${user.lastname}</p>
            <p>Số điện thoại: ${user.phone}</p>
            
            <p>Địa chỉ: ${user.address}</p>
          </div>
        </div>
      </div>
      <div class="mt-5 d-flex justify-content-center">
        <p>Nếu bạn có bất cứ câu hỏi nào, đừng ngần ngại liên lạc với chúng tôi qua Hotline: 0123456789 (9h
          -
          19h các ngày thứ 2 đến thứ 7)</p>
      </div>
    </div>

  </div>
</div>