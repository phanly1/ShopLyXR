package com.project.shopapp.service.imp;

import com.project.shopapp.models.Coupon;
import com.project.shopapp.models.CouponStatus;
import com.project.shopapp.repository.CouponRepository;
import com.project.shopapp.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;

    public static ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }

    @Scheduled(cron = "0 * * * * ?", zone = "Asia/Ho_Chi_Minh")
    public void updateVoucherStatusDaily() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<Coupon> allCoupons = couponRepository.findAll();

        for (Coupon coupon : allCoupons) {
            // Kiểm tra nếu endDate đã đến hoặc vượt qua currentDate
            if (coupon.getEndDate().isBefore(currentDate) || coupon.getEndDate().isEqual(currentDate)) {
                coupon.setStatus("EXPIRED");
                coupon.setActive(false);
            }
            // Kiểm tra nếu currentDate trước startDate
            else if (coupon.getStartDate().isAfter(currentDate)) {
                coupon.setStatus("INACTIVE");
                coupon.setActive(false);


            }
            // Nếu trong khoảng startDate đến endDate thì là ACTIVE
            else {
                coupon.setStatus("ACTIVE");
                coupon.setActive(true);

            }
        }
        couponRepository.saveAll(allCoupons);
    }




    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public List<Coupon> findAllActiveCoupons() {
        return couponRepository.findByActiveTrue();
    }

    @Override
    public List<Coupon> findAllCouponByStatus(String status) {
        return couponRepository.findAllByStatus(status);
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        coupon.setActive(false);
        coupon.setCode(autoGenerateCode());

        LocalDateTime currentDate = LocalDateTime.now();

        if (coupon.getStartDate() != null && coupon.getEndDate() != null) {
            if (coupon.getEndDate().isBefore(currentDate)) {
                coupon.setStatus("EXPIRED"); // Mã giảm giá hết hạn
            } else if (coupon.getStartDate().isAfter(currentDate)) {
                coupon.setStatus("INACTIVE"); // Mã giảm giá chưa bắt đầu
            } else {
                coupon.setStatus("ACTIVE"); // Mã giảm giá đang hoạt động
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date and end date are required");
        }


        return couponRepository.save(coupon);
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();
    private String autoGenerateCode() {
        String generatedCode;
        // Lặp cho đến khi tạo ra mã duy nhất
        do {
            StringBuilder code = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                code.append(CHARACTERS.charAt(index));
            }
            generatedCode = code.toString();
        } while (couponRepository.existsByCode(generatedCode));

        return generatedCode;
    }

    @Override
    public Coupon updateCoupon(Coupon model) {
        Coupon coupon = couponRepository.findById(model.getId()).orElse(null);
        if (coupon == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found");
        }

        coupon.setStartDate(model.getStartDate());
        coupon.setEndDate(model.getEndDate());

        mapper().map(model, coupon);
        LocalDateTime currentDate = LocalDateTime.now();

        if (coupon.getStartDate() != null && coupon.getEndDate() != null) {
            if (coupon.getEndDate().isBefore(currentDate)) {
                coupon.setStatus("EXPIRED"); // Mã giảm giá hết hạn
            } else if (coupon.getStartDate().isAfter(currentDate)) {
                coupon.setStatus("INACTIVE"); // Mã giảm giá chưa bắt đầu
            } else {
                coupon.setStatus("ACTIVE"); // Mã giảm giá đang hoạt động
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date and end date are required");
        }


        return couponRepository.save(coupon);
    }

    private boolean isCouponExpired(Coupon coupon) {
        LocalDateTime currentDate = LocalDateTime.now();
        return coupon.getEndDate().isBefore(currentDate);
    }



    @Override
    public Coupon findCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found");
        }

        return coupon;
    }

    @Override
    public Coupon applyCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found");
        }
        coupon.setQuantity(coupon.getQuantity() - 1);
        couponRepository.save(coupon);
        return coupon;
    }

    @Override
    public void deleteCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found");
        }
        couponRepository.delete(coupon);
    }
}
