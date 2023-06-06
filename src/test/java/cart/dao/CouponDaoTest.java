package cart.dao;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountType;
import cart.entity.CouponEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class CouponDaoTest {
    private CouponDao couponDao;
    private MemberDao memberDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        couponDao = new CouponDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    @DisplayName("쿠폰을 확인한다.")
    void checkCouponById() {
        assertThat(couponDao.existsById(1L)).isTrue();
    }

    @Test
    @DisplayName("모든 쿠폰을 조회한다.")
    void findAllCoupons() {
        Coupon coupon = new Coupon("5000원 할인 쿠폰", DiscountType.from("deduction"), 10000, 5000, 0);

        assertThat(couponDao.findAll().get(0).getName()).isEqualTo(coupon.getName());
    }

    @Test
    @DisplayName("쿠폰을 이름으로 조회한다.")
    void findCouponByName() {
        CouponEntity coupon = new CouponEntity("5000원 할인 쿠폰", "deduction", 10000, 5000, 0.0);
        assertThat(couponDao.findByName(coupon).get().getName()).isEqualTo(coupon.getName());
    }
}
