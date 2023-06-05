package cart.domain.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CartItemRepository {

    List<CartItem> findByMemberId(Long id);

    CartItem save(CartItem cartItem);

    CartItem findById(Long id);

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);

    List<CartItem> findAllByIdsAndMemberId(Member member, List<Long> cartProductIds);

}
