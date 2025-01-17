

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="container-fluid d-none d-lg-block pt-5">
  <div class="container">
    <div class="border-start border-5 border-primary ps-5 mb-5" style="max-width: 600px;">
      <h6 class="text-primary text-uppercase">Thức ăn </h6>
      <h1 class="display-5 text-uppercase mb-0">Thức ăn cho thú cưng</h1>
    </div>
    <div class="row g-5 d-flex flex-row owl-carousel product-carousel">
      <c:forEach var="food" items="${listFood}">
        <div style="height: 350px;" class="pb-5 mb-4 ">
          <div class="product-item owl-item position-relative bg-light d-flex flex-column text-center">
            <img class="img-fluid mb-4 w-100" src="${food.getListImg().get(0).url}" alt="">
            <h6 class="text-uppercase">${food.productName}</h6>
            <h5 class="text-primary mb-0">${food.getPriceString()}</h5>
            <div class="btn-action d-flex justify-content-center">
              <a class="btn btn-primary py-2 px-3" href="getfooddetail?id=${food.productId}"><i class="bi bi-cart"></i></a>
              <a class="btn btn-primary py-2 px-3" href="getfooddetail?id=${food.productId}"><i class="bi bi-eye"></i></a>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>
<div class="d-flex justify-content-center "><a href="food" style="margin-bottom: 50px" class="btn btn-primary p-2">Xem thêm</a></div>



<div class="container-fluid d-none d-lg-block pt-5">
  <div class="container">
    <div class="border-start border-5 border-primary ps-5 mb-5" style="max-width: 600px;">
      <h6 class="text-primary text-uppercase">Thú cưng</h6>
      <h1 class="display-5 text-uppercase mb-0">Lựa chọn người bạn đồng hành của bạn</h1>
    </div>


 

    <div class="owl-carousel product-carousel row g-5 d-flex flex-row">
      <c:forEach var="pet" items="${listPet}">
        <div style="height: 350px;" class="pb-5 d-flex">
          <div class="product-item owl-item position-relative bg-light d-flex flex-column text-center">
            <img class="img-fluid mb-4 w-100" src="${pet.getListImg().get(0).url}" alt="loi">
            <h6 class="text-uppercase">${pet.productName}</h6>
            <h5 class="text-primary mb-0">${pet.getPriceString()}</h5>
            <div class="btn-action d-flex justify-content-center">
              <a class="btn btn-primary py-2 px-3" href="getpetdetail?id=${pet.productId}"><i class="bi bi-cart"></i></a>
              <a class="btn btn-primary py-2 px-3" href="getpetdetail?id=${pet.productId}"><i class="bi bi-eye"></i></a>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>
<div class="d-flex justify-content-center "><a  href="pet" style="margin-bottom: 50px" class="btn btn-primary p-2">Xem thêm</a></div>