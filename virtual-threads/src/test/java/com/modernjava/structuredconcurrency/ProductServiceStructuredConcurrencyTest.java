package com.modernjava.structuredconcurrency;

import com.modernjava.service.DeliveryService;
import com.modernjava.service.ProductInfoService;
import com.modernjava.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.modernjava.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceStructuredConcurrencyTest {

    /* Verbose approach
    ProductInfoService productInfoService = Mockito.spy(ProductInfoService.class);
    ReviewService reviewService = Mockito.spy(ReviewService.class);
     DeliveryService deliveryService = Mockito.spy(DeliveryService.class);

    ProductServiceStructuredConcurrency productServiceStructuredConcurrency
            = new ProductServiceStructuredConcurrency(productInfoService, reviewService, deliveryService);
    */
    @Spy
    ProductInfoService productInfoService;

    @Spy
    ReviewService reviewService;

    @Spy
    DeliveryService deliveryService;

    @InjectMocks
    ProductServiceStructuredConcurrency productServiceStructuredConcurrency;

    @Test
    void retrieveProductDetails() {
        var product = productServiceStructuredConcurrency.retrieveProductDetails("ABC");

        assertNotNull(product);
        assertNotNull(product.productInfo());
        assertNotNull(product.reviews());
    }

    @Test
    void retrieveProductDetailsV2() {
        var productV2 = productServiceStructuredConcurrency.retrieveProductDetailsV2("ABC");

        assertNotNull(productV2);
        assertNotNull(productV2.productInfo());
        assertNotNull(productV2.reviews());
        assertNotNull(productV2.deliveryDetails());
    }

    @Test
    void retrieveProductDetailsHttp() {
        var productV2 = productServiceStructuredConcurrency.retrieveProductDetailsHttp("ABC");

        assertNotNull(productV2);
        assertNotNull(productV2.productInfo());
        assertNotNull(productV2.reviews());
        assertNotNull(productV2.deliveryDetails());
    }

    @Test
    void retrieveProductDetails_Exception() {
        when(reviewService.retrieveReviews(anyString()))
                .thenThrow(new RuntimeException("Exception calling ReviewService"));
        var exception = assertThrows(RuntimeException.class, () -> productServiceStructuredConcurrency.retrieveProductDetails("ABC"));
        assertTrue(exception.getMessage().contains("Exception calling ReviewService"));
    }

}