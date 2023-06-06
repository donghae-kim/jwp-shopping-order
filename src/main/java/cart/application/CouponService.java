package cart.application;

import cart.domain.coupon.Coupon;
import cart.domain.repository.CouponRepository;
import cart.dto.MemberDto;
import cart.dto.request.CouponCreateRequest;
import cart.dto.response.CouponIssuableResponse;
import cart.dto.response.CouponResponse;
import cart.exception.CouponException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Long save(MemberDto member, CouponCreateRequest request) {
        validateExistCoupon(request);
        validateDuplicateCoupon(member, request);
        Coupon coupon = couponRepository.save(member.getId(), request.getId());
        return coupon.getId();
    }

    private void validateDuplicateCoupon(MemberDto member, CouponCreateRequest request) {
        if (couponRepository.existsByCouponIdAndMemberId(request.getId(), member.getId())) {
            throw new CouponException("이미 존재하는 쿠폰입니다.");
        }
    }

    private void validateExistCoupon(CouponCreateRequest request) {
        if (!couponRepository.existsById(request.getId())) {
            throw new CouponException("해당 쿠폰을 찾을 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> findByMemberId(MemberDto member) {
        return couponRepository.findByMemberId(member.getId()).stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CouponIssuableResponse> findAll(MemberDto member) {
        List<Coupon> coupons = couponRepository.findAll();
        List<Coupon> memberCoupons = couponRepository.findByMemberId(member.getId());

        return coupons.stream()
                .map(it -> CouponIssuableResponse.of(it, !memberCoupons.contains(it)))
                .collect(Collectors.toList());
    }
}
