package com.praktikum.order_service.controller;

import com.praktikum.order_service.client.ProductClient;
import com.praktikum.order_service.messaging.MessageProducer;
import com.praktikum.order_service.model.Order;
import com.praktikum.order_service.model.Product;
import com.praktikum.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        Product product = productClient.getProductById(order.getProductId());
        order.setProductName(product.getName());
        order.setProductPrice(product.getPrice());
        Order saved = orderRepository.save(order);
        messageProducer.sendMessage("Order baru: produk " + product.getName() + ", qty " + order.getQuantity());
        return saved;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return "Order deleted";
        }
        return "Order not found";
    }
}