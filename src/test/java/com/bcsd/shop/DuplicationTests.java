package com.bcsd.shop;

import com.bcsd.shop.controller.dto.request.PaymentCreateRequest;
import com.bcsd.shop.controller.dto.request.PurchaseCreateRequest;
import com.bcsd.shop.controller.dto.request.UserJoinRequest;
import com.bcsd.shop.domain.*;
import com.bcsd.shop.repository.PaymentRepository;
import com.bcsd.shop.repository.ProductRepository;
import com.bcsd.shop.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DuplicationTests {

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

    @Test
    @DisplayName("회원가입 중복 방지 테스트")
    void 회원가입_중복_방지_테스트() throws Exception {
        UserJoinRequest request = new UserJoinRequest(
                "testtest@naver.com",
                "test1234",
                "test1",
                "010-9993-2323",
                "BCSD"
        );

        // 첫번째 회원가입
        createUser(request).andExpect(status().isCreated());

        // 중복 회원가입 시도
        createUser(request).andExpect(status().isConflict());
    }

    private ResultActions createUser(UserJoinRequest request) throws Exception {
        return mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        );
    }

    @Test
    @WithMockUser
    @DisplayName("결제 중복 방지 테스트")
    void 결제_중복_방지_테스트() throws Exception {
        PaymentCreateRequest request = new PaymentCreateRequest(
                "sdfasfsdafs1231",
                1000000L,
                "카드"
        );

        // 첫번째 결제
        createPayment(request).andExpect(status().isCreated());

        // 중복 결제 시도
        createPayment(request).andExpect(status().isConflict());
    }

    private ResultActions createPayment(PaymentCreateRequest request) throws Exception {
        return mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        );
    }

    @Test
    @WithMockUser(username = "sellerUser", authorities = {"SELLER"})
    @DisplayName("상품등록 중복 방지 테스트")
    void 상품등록_중복_방지_테스트() throws Exception {
        User sellerUser = User.builder()
                .email("seller@example.com")
                .password("seller")
                .name("판매자")
                .phoneNumber("041-123-4567")
                .address("한기대")
                .build();
        sellerUser = userRepository.save(sellerUser);

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "image.png",
                "image/png",
                "image-content".getBytes()
        );

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", sellerUser.getId());

        // 첫번째 상품등록
        createProduct(imageFile, session).andExpect(status().isCreated());

        // 중복 등록 시도
        createProduct(imageFile, session).andExpect(status().isConflict());
    }

    private ResultActions createProduct(MockMultipartFile imageFile, MockHttpSession session) throws Exception {
        return mockMvc.perform(multipart("/products")
                .file(imageFile)
                .param("categoryId", "1")
                .param("name", "중복테스트상품")
                .param("description", "상품등록중복테스트입니다")
                .param("price", "2000000")
                .param("shippingCost", "3000")
                .param("stock", "100")
                .session(session)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );
    }

    @Test
    @WithMockUser(username = "buyerUser", authorities = {"USER"})
    @DisplayName("주문생성 중복 방지 테스트")
    void 주문생성_중복_방지_테스트() throws Exception {
        User buyerUser = User.builder()
                .email("buyer@example.com")
                .password("buyer")
                .name("구매자")
                .phoneNumber("010-1234-5678")
                .address("솔빛관")
                .build();
        buyerUser = userRepository.save(buyerUser);

        User sellerUser = User.builder()
                .email("seller@example.com")
                .password("seller")
                .name("판매자")
                .phoneNumber("041-123-4567")
                .address("한기대")
                .build();
        sellerUser = userRepository.save(sellerUser);

        Product product = Product.builder()
                .seller(sellerUser)
                .name("테스트 상품")
                .price(2000000L)
                .stock(100)
                .shippingCost(3000)
                .build();
        product = productRepository.save(product);

        Payment payment = Payment.builder()
                .transactionId("payment1234")
                .amount(2003000L)
                .method(PaymentMethod.카드)
                .status(PaymentStatus.정상결제)
                .build();
        payment = paymentRepository.save(payment);

        PurchaseCreateRequest request = new PurchaseCreateRequest(
                product.getId(),
                payment.getId(),
                1,
                "안전 배송 부탁드립니다!"
        );

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", buyerUser.getId());

        // 첫번째 주문생성
        createPurchase(request, session).andExpect(status().isCreated());

        // 중복 주문 시도
        createPurchase(request, session).andExpect(status().isConflict());
    }

    private ResultActions createPurchase(PurchaseCreateRequest request, MockHttpSession session) throws Exception {
        return mockMvc.perform(post("/purchases")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        );
    }
}
