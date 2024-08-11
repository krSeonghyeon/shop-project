package com.bcsd.shop;

import com.bcsd.shop.controller.dto.request.PurchaseCreateRequest;
import com.bcsd.shop.domain.*;
import com.bcsd.shop.repository.PaymentRepository;
import com.bcsd.shop.repository.ProductRepository;
import com.bcsd.shop.repository.PurchaseRepository;
import com.bcsd.shop.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConcurrencyTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    static int REQUEST_NUMBER = 100;

    private User buyerUser;
    private User sellerUser;
    private Product product;
    private MockHttpSession session;

    private Set<Long> createdPaymentsId = new HashSet<>();

    @BeforeEach
    void setUp() {
        buyerUser = userRepository.save(User.builder()
                .email("buyer12fsa123d3@example.com")
                .password("buyer")
                .name("구매자")
                .phoneNumber("010-1234-5678")
                .address("솔빛관")
                .build());

        sellerUser = userRepository.save(User.builder()
                .email("seller3sa123fd@example.com")
                .password("seller")
                .name("판매자")
                .phoneNumber("041-123-4567")
                .address("한기대")
                .build());

        product = productRepository.save(Product.builder()
                .seller(sellerUser)
                .name("테스트 상품")
                .price(2000000L)
                .stock(200)
                .shippingCost(3000)
                .build());

        session = new MockHttpSession();
        session.setAttribute("userId", buyerUser.getId());
    }

    @AfterEach
    void remove() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            purchaseRepository.deleteByProductId(product.getId()); // 주문삭제
            paymentRepository.deleteAllById(createdPaymentsId); // 결제삭제
            productRepository.deleteById(product.getId()); // 상품삭제
            userRepository.deleteById(buyerUser.getId()); // 주문자삭제
            userRepository.deleteById(sellerUser.getId()); // 판매자삭제
            transactionManager.commit(status);
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }

    @Test
    @DisplayName("주문생성 재고 동시성 테스트 - 100개 요청")
    void 주문생성_재고_동시성_테스트() throws Exception {
        long productId = product.getId();
        ExecutorService executorService = Executors.newFixedThreadPool(REQUEST_NUMBER);
        CountDownLatch latch = new CountDownLatch(REQUEST_NUMBER);

        for (int i = 0; i < REQUEST_NUMBER; i++) {
            executorService.execute(() -> {
                try {
                    Payment payment = Payment.builder()
                            .transactionId("payment" + UUID.randomUUID())
                            .amount(2003000L)
                            .method(PaymentMethod.카드)
                            .build();
                    payment = paymentRepository.save(payment);
                    createdPaymentsId.add(payment.getId());

                    PurchaseCreateRequest request = new PurchaseCreateRequest(
                            productId,
                            payment.getId(),
                            1,
                            "안전 배송 부탁드립니다!"
                    );

                    createPurchase(request, session).andExpect(status().isCreated());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        int updatedStock = productRepository.findById(productId).orElseThrow().getStock();
        assertThat(updatedStock).isEqualTo(100);
    }

    private ResultActions createPurchase(PurchaseCreateRequest request, MockHttpSession session) throws Exception {
        return mockMvc.perform(post("/purchases")
                .session(session)
                .with(user("buyerUser").authorities(() -> "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        );
        }
}
