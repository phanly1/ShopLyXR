//package com.project.shopapp.service.imp;
//
//import com.project.shopapp.dtos.OrderDTO;
//import com.project.shopapp.dtos.OrderDetailDTO;
//import com.project.shopapp.exception.DataNotFoundException;
//import com.project.shopapp.models.Order;
//import com.project.shopapp.models.OrderDetail;
//import com.project.shopapp.models.Product;
//import com.project.shopapp.repository.OrderDetailRepository;
//import com.project.shopapp.repository.OrderRepository;
//import com.project.shopapp.repository.ProductRepository;
//import com.project.shopapp.service.OrderDetailService;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.convention.MatchingStrategies;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class OrderDetailServiceImpl implements OrderDetailService {
//
//    private final OrderDetailRepository orderDetailRepository;
//    private final ProductRepository productRepository;
//    private final OrderRepository orderRepository;
//
//    public static ModelMapper mapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        return modelMapper;
//    }
//
//    public static OrderDetailDTO mapToView(OrderDetail orderDetail) {
//        if (orderDetail == null) {
//            return null;
//        }
//
//        return mapper().map(orderDetail, OrderDetailDTO.class);
//    }
//
//    public static List<OrderDetailDTO> mapToView(List<OrderDetail> orderDetails) {
//        return orderDetails.stream().map(d -> mapper().map(d, OrderDetailDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<OrderDetailDTO> getOrderDetailsByOrderId(String orderId) {
//        Order order = orderRepository.findById(orderId).orElse(null);
//        if(order == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order id not found");
//        }
//
//        return mapToView(orderDetailRepository.findAllByOrderId(orderId));
//    }
//
//    @Override
//    public OrderDetailDTO getOrderDetailById(String id) {
//        OrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);
//        if(orderDetail == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetail not found");
//        }
//        return mapToView(orderDetail);
//    }
//
//    @Override
//    public OrderDetailDTO createOrderDetail(OrderDetailDTO model) {
//        Order order = orderRepository.findById(model.getOrderId()).orElse(null);
//        if(order == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
//        }
//
//        Product product = productRepository.findById(model.getProductId()).orElse(null);
//        if(product == null) {
//            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id not found");
//        }
//
//        OrderDetail orderDetail = mapper().map(model, OrderDetail.class);
//
//        return mapToView(orderDetailRepository.save(orderDetail));
//    }
//
//    @Override
//    public OrderDetailDTO updateOrderDetail(OrderDetailDTO model) {
//        OrderDetail orderDetail = orderDetailRepository.findById(model.getId()).orElse(null);
//        if(orderDetail == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetail not found");
//        }
//
//        mapper().map(model, orderDetail);
//
//        return mapToView(orderDetailRepository.save(orderDetail));
//    }
//
//    @Override
//    public OrderDetailDTO deleteOrderDetailById(String id) {
//        OrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);
//        if(orderDetail == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetail not found");
//        }
//        orderDetailRepository.delete(orderDetail);
//        return mapToView(orderDetail);
//    }
//}
