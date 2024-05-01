package com.devbackend.web_telephone_ttcn.repository;

import com.devbackend.web_telephone_ttcn.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.orderNumber = ?1")
    Order findOrderByOrderNumber(String orderNumber);

    // lay danh sach don hang voi trang thai: chua xac nhan, da xac nhan, dang giao
    @Query("select ol from Order ol where (ol.orderStatus = 1 or ol.orderStatus = 2 or ol.orderStatus = 3) and ol.user.id = ?1")
    List<Order> getListOrder(Long userId);

    // lấy danh sách hóa đơn đã giao hàng (lịch sử mua hàng)
    @Query("select ol from Order ol where ol.orderStatus = 4 and ol.user.id = ?1")
    List<Order> getListOrderPurchased(Long userId);

    // lay danh sach hoa don theo trang thai hoa don
    @Query("select ol from Order ol where ol.orderStatus = ?1")
    List<Order> getOrderByOrderStatus(int orderStatus);

    // update hoa don theo trang thai
//    @Modifying
//    @Query("update Order ol set ol.orderStatus = ?1 where ol.id = ?2")
//    void updateOrderByOrderStatus(int orderStatus, Long idOrder);

    // thống kê doanh thu theo tháng
    @Query("SELECT DATE_FORMAT(o.deliveredDate, '%m-%Y') AS month, SUM(o.totalCost) AS revenue " +
            "FROM Order o " +
            "WHERE o.orderStatus = 4 " +
            "AND MONTH(o.deliveredDate) = :month " +
            "AND YEAR(o.deliveredDate) = :year " +
            "GROUP BY DATE_FORMAT(o.deliveredDate, '%m-%Y')")
    List<Object[]> getRevenueByMonthYear(@Param("month") int month, @Param("year") int year);

    // tính tổng doanh thu cửa hang
    @Query("select sum(o.totalCost) as revenue from Order o where o.orderStatus = 4")
    Long calculateTotalRevenue();

    // thống kê don hang bi huy
    @Query("select count(o.id) from Order o where o.orderStatus = 5")
    int calculateOrderCanceled();

    // đếm tong so don hang
    @Query("select count(o.id) from Order o")
    int countOrderById();


    // thống kê 5 sản phẩm bán chạy nhất
    @Query(value = "SELECT od.product_id, p.product_name, SUM(od.quantity) AS total_quantity, sum(od.total_price) as revenue " +
            "FROM order_detail od " +
            "JOIN products p ON od.product_id = p.id " +
            "join orders o on od.order_id = o.id " +
            "where o.order_status = 4 " +
            "GROUP BY od.product_id, p.product_name " +
            "ORDER BY total_quantity DESC, revenue desc " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> getTopSellingProducts();
}
