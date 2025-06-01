package com.project.shopapp.service.imp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.info.OrderPageInfo;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.*;
import com.project.shopapp.repository.OrderRepository;
import com.project.shopapp.service.CartService;
import com.project.shopapp.service.MailService;
import com.project.shopapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class   OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    @Autowired
    CartService cartService;

    @Autowired
    MailService mailService;

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    public UserDTO getUserById(String userId, String token) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8088/api/v1/users/" + userId))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        UserDTO userDTO = mapper.readValue(response.body(), UserDTO.class);
        //JsonNode root = mapper.readTree(response.body());
//        JsonNode userNode = root.get("data");

        // ⚠️ Nếu "data" là array:
        //JsonNode userNode = root.get("data").get(0);

       // return mapper.treeToValue(root, UserDTO.class);
        return userDTO;
    }

    public void updateTotalProduct(List<OrderDetail> orderDetails) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8088/api/v1/users/total"))
                .GET()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }

    public static OrderDTO mapToView(Order order) {
        if (order == null) {
            return null;
        }

        return mapper().map(order, OrderDTO.class);
    }

    public static List<OrderDTO> mapToView(List<Order> orders) {
        return orders.stream().map(d -> mapper().map(d, OrderDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public List<OrderDTO> getAllOrder() {
             return mapToView(orderRepository.findAll());
    }

    @Override
    public OrderDTO getOrderById(String id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return mapToView(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(String userId) {

        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public OrderDTO createOrder(OrderDTO model, String token) throws DataNotFoundException {
        UserDTO user = null;
        try {
            user = getUserById(model.getUserId(), token);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User_id not found " + model.getUserId());
        }

        Order order = mapper().map(model, Order.class);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setActive(true);

        OrderStatusHistory initialStatus = new OrderStatusHistory();
        initialStatus.setStatus("pending");  // Hoặc trạng thái mặc định bạn muốn
        initialStatus.setTime(new Date());
        order.getStatusHistory().add(initialStatus);

        if (order.getTotalMoney() == null || order.getTotalMoney() <= 0) {
            float totalMoney = order.getOrderDetails().stream()
                    .map(OrderDetail::getTotalPrice)
                    .reduce(0.0f, Float::sum);
            order.setTotalMoney(totalMoney);
        }

        // Lưu order trước để có ID
        Order savedOrder = orderRepository.save(order);

        // Gửi email thông báo
        Mail mail = new Mail();
        mail.setMailFrom("phanly10102003@gmail.com");
        mail.setMailTo(user.getEmail());
        mail.setMailSubject("Xác nhận đơn hàng #" + savedOrder.getId() + " - LyXR ");

        String emailContent = createOrderConfirmationEmail(user, savedOrder);
        mail.setMailContent(emailContent);

        try {
            mailService.sendEmail(mail);
            logger.info("Order confirmation email sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send order confirmation email", e);
            // Không throw exception để không ảnh hưởng đến việc tạo order
        }


        return mapToView(orderRepository.save(order));
    }

    // Method tạo nội dung email đặt hàng sang trọng
// Method tạo nội dung email đặt hàng sang trọng
    private String createOrderConfirmationEmail(UserDTO user, Order order) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat deliveryFormatter = new SimpleDateFormat("dd/MM/yyyy");

        // Ước tính ngày giao hàng (3-5 ngày)
        Calendar deliveryDate = Calendar.getInstance();
        deliveryDate.add(Calendar.DAY_OF_MONTH, 5);

        // Tính toán các giá trị
        float subtotal = order.getTotalMoney() - Float.parseFloat(order.getFeeShip());
        float discount = 0; // Có thể thêm logic tính discount nếu cần

        return String.format("""
   XÁC NHẬN ĐƠN HÀNG THÀNH CÔNG
   ──────────────────────────────────────────────────────────────────────
   
   Kính gửi Quý khách hàng %s,
   
   Công ty TNHH Trang Sức LyXR trân trọng cảm ơn Quý khách đã tin tưởng 
   lựa chọn sản phẩm của chúng tôi. Đơn hàng của Quý khách đã được tiếp 
   nhận và đang trong quá trình xử lý.

   THÔNG TIN ĐƠN HÀNG
   ──────────────────────────────────────────────────────────────────────
   Mã đơn hàng:            #LyXR-%s
   Ngày đặt hàng:          %s
   Phương thức thanh toán:  %s
   Trạng thái hiện tại:    %s
  
   THÔNG TIN KHÁCH HÀNG
   ──────────────────────────────────────────────────────────────────────
   Họ và tên:             %s
   Số điện thoại:          %s  
   Địa chỉ email:          %s
   Địa chỉ nhận hàng:      %s
   Ghi chú đặc biệt:       %s
   
   THÔNG TIN THANH TOÁN
   ──────────────────────────────────────────────────────────────────────
   Tạm tính sản phẩm:      %,.0f VNĐ
   Phí vận chuyển:         %s VNĐ  
   Giảm giá áp dụng:       -%.0f VNĐ
   ══════════════════════════════════════════════════════════════════════
   TỔNG THANH TOÁN:       %,.0f VNĐ                        
   ══════════════════════════════════════════════════════════════════════
           
   THÔNG TIN VẬN CHUYỂN
   ──────────────────────────────────────────────────────────────────────
   Đơn vị vận chuyển:      LyXR Express Delivery
   Hình thức đóng gói:     Hộp carton chuyên dụng + Bọc bong bóng
   Dự kiến giao hàng:     %s
   Khung giờ giao hàng:   08:00 - 18:00 (Thứ 2 - Thứ 7)
   Bảo hiểm hàng hóa:     Được bảo hiểm 100%% giá trị
   Lưu ý giao hàng:       Vui lòng có mặt để nhận hàng
   
   THÔNG TIN LIÊN HỆ & HỖ TRỢ
   ──────────────────────────────────────────────────────────────────────
   Công ty:               TNHH Trang Sức LyXR
   Hotline hỗ trợ:        0985.150.599 (8:00 - 22:00)
   Email hỗ trợ:          support@lyxr.com
   Website:               www.lyxr.com
   Địa chỉ showroom:      123 Phố Huế, Hai Bà Trưng, Hà Nội
   
   CHÍNH SÁCH & CAM KẾT
   ──────────────────────────────────────────────────────────────────────
   Cam kết chất lượng:    Sản phẩm chính hãng 100%%
   Chính sách đổi trả:    30 ngày đổi trả không điều kiện
   Chế độ bảo hành:      12 tháng miễn phí
   Chứng nhận chất lượng: Đầy đủ CO, CQ, chứng nhận nguồn gốc
   Cân đo kiểm định:     Tại Trung tâm Chuẩn đo lường VN
   Giải thưởng:          Top 10 thương hiệu uy tín 2024
  
   ──────────────────────────────────────────────────────────────────────
   
   Chúng tôi cam kết sẽ xử lý đơn hàng của Quý khách một cách nhanh chóng 
   và chính xác nhất. Mọi thắc mắc xin vui lòng liên hệ qua các kênh hỗ trợ 
   ở trên để được giải đáp kịp thời.
   
   Một lần nữa, LyXR xin chân thành cảm ơn sự tin tưởng của Quý khách và 
   rất mong được phục vụ Quý khách trong những lần mua sắm tiếp theo.
   
   Trân trọng,
   
   CÔNG TY TNHH TRANG SỨC LYXR
   Ban Quản lý Khách hàng
   Email: lyxr@gmail.com
   Tel: 0985.150.599 | Fax: 024.3456.7890
   ──────────────────────────────────────────────────────────────────────
   Đây là email tự động từ hệ thống. Vui lòng không reply trực tiếp.
   Liên hệ qua hotline hoặc email hỗ trợ để được hỗ trợ tốt nhất.
   ──────────────────────────────────────────────────────────────────────
   
   """,
                user.getFullName(),                                    // Tên khách hàng
                order.getId(),                                         // Mã đơn hàng (String)
                dateFormatter.format(order.getOrderDate()),           // Ngày đặt hàng (Date)
                order.getPaymentMethod(),                              // Phương thức thanh toán (String)
                order.getStatus(),                                     // Trạng thái đơn hàng (String)
                user.getFullName(),                                    // Họ tên
                user.getPhoneNumber(),                                 // SĐT (String)
                user.getEmail(),                                       // Email (String)
                order.getAddress(),                                    // Địa chỉ (String)
                order.getNote() != null ? order.getNote() : "Không có yêu cầu đặc biệt", // Ghi chú
                subtotal,                                              // Giá trị sản phẩm (Float)
                order.getFeeShip(),                                    // Phí ship (String)
                discount,                                              // Giảm giá (float)
                order.getTotalMoney(),                                 // Tổng cộng (Float)
                deliveryFormatter.format(deliveryDate.getTime())      // Ngày giao dự kiến
        );
    }    @Override
    public OrderDTO updateOrder(OrderDTO model) {
        Order order = orderRepository.findById(model.getId()).orElse(null);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        order.getStatusHistory().add(new OrderStatusHistory(model.getStatus(), new Date()));

        // Cập nhật đơn hàng
        if (model.getStatus().equals("prepare")) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            //todo: goi sang sv product de update so luong
            try {
                updateTotalProduct(orderDetails);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // Nếu trạng thái đã thay đổi, lưu vào lịch sử trạng thái
//        if (!oldStatus.equals(order.getStatus())) {
//            order.getStatusHistory().add(new OrderStatusHistory(oldStatus, new Date())); // Thêm trạng thái cũ vào lịch sử
//        }

        mapper().map(model, order);
        return mapToView(orderRepository.save(order));
    }



    @Override
    public OrderDTO deleteOrderById(String id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
        return mapToView(order);
    }

    @Override
    public List<OrderDTO> search(OrderPageInfo model) {
        return mapToView(orderRepository.search(model)) ;
    }

    @Override
    public Long count(OrderPageInfo model) {
        return orderRepository.count(model);
    }
}
