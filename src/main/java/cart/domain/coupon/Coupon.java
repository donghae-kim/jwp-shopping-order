package cart.domain.coupon;

import java.util.Objects;

public class Coupon {
    private final Long id;
    private final String name;
    private final CouponTypes couponTypes;
    private final int minimumPrice;
    private final int discountPrice;
    private final double discountRate;

    public static Coupon EMPTY =
            new Coupon(null,"",DiscountType.EMPTY_DISCOUNT.getType(),0,0,0);

    public static Coupon BONUS_COUPON = new Coupon(null,
            "주문확정_1000원_할인_보너스쿠폰", DiscountType.DEDUCTION_DISCOUNT.getType(), 5000, 1000, 0);

    public Coupon(String name, CouponTypes couponTypes, int minimumPrice, int discountPrice, double discountRate) {
        this(null, name, couponTypes, minimumPrice, discountPrice, discountRate);
    }

    public Coupon(Long id, String name, CouponTypes couponTypes, int minimumPrice, int discountPrice, double discountRate) {
        this.id = id;
        this.name = name;
        this.couponTypes = couponTypes;
        this.minimumPrice = minimumPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
    }

    public int applyCouponPrice(int totalPrice) {
        return couponTypes.calculatePrice(totalPrice, minimumPrice, discountPrice, discountRate);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCouponTypesName() {
        return couponTypes.getCouponTypeName();
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return minimumPrice == coupon.minimumPrice && discountPrice == coupon.discountPrice && Double.compare(coupon.discountRate, discountRate) == 0 && Objects.equals(name, coupon.name) && Objects.equals(couponTypes, coupon.couponTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, couponTypes, minimumPrice, discountPrice, discountRate);
    }
}
