package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.coupon.Coupon;
import cart.domain.repository.*;
import cart.dto.request.OrderRequest;
import cart.dto.response.*;
import cart.exception.CouponException;
import cart.exception.OrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    public OrderService(CartItemRepository cartItemRepository, CouponRepository couponRepository,
                        OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
        this.orderRepository = orderRepository;
    }

    public Long save(Member member, OrderRequest orderRequest) {
        validationSave(orderRequest);

        List<CartItem> cartItems = cartItemRepository.findAllByIdsAndMemberId(orderRequest.getSelectedCartIds(), member.getId());
        Coupon coupon = couponRepository.findAvailableCouponByIdAndMemberId(orderRequest.getCouponId(),member.getId());
        validateCoupon(coupon);
        Order order = new Order(member, cartItems,coupon);
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }

    private static void validationSave(OrderRequest orderRequest) {
        if (orderRequest.getSelectedCartIds().isEmpty()) {
            throw new OrderException("주문 상품이 비어있습니다.");
        }
    }

    private void validateCoupon(Coupon coupon) {
        if(coupon == null){
            throw new CouponException("유효하지 않은 쿠폰입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> findAllByMemberId(Member member) {
        return orderRepository.findAllByMemberId(member.getId()).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .map(OrdersResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse findByIdAndMemberId(Member member, Long orderId) {
        Order order = orderRepository.findByIdAndMemberId(orderId,member.getId());

        return OrderResponse.of(order);
    }

    public void deleteById(Long orderId, Member member) {
        validationDelete(orderId);
        orderRepository.deleteById(orderId, member.getId());
    }

    private void validationDelete(Long orderId) {
        if (orderRepository.checkConfirmStateById(orderId)) {
            throw new OrderException("주문 확정 주문은 취소할 수 없습니다.");
        }
    }

    public CouponConfirmResponse confirmById(Long orderId, Long memberId) {
        Coupon coupon = orderRepository.confirmById(orderId, memberId);

        return CouponConfirmResponse.from(CouponResponse.from(coupon));
    }
}
