package com.modernjava.structuredconcurrency;


import com.modernjava.domain.Product;
import com.modernjava.domain.ProductV2;
import com.modernjava.service.DeliveryService;
import com.modernjava.service.ProductInfoService;
import com.modernjava.service.ReviewService;

import java.util.concurrent.StructuredTaskScope;


public class ProductServiceStructuredConcurrency {

    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;
    private final DeliveryService deliveryService;

    public ProductServiceStructuredConcurrency(ProductInfoService productInfoService, ReviewService reviewService, DeliveryService deliveryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.deliveryService = deliveryService;
    }

    public ProductServiceStructuredConcurrency(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.deliveryService = null;
    }


    public Product retrieveProductDetails(String productId) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Virtual Threads are created for the below tasks
            // Fork the tasks
            var productInfoSubTask = scope.fork(() -> productInfoService.retrieveProductInfo(productId));
            var reviewSubTask = scope.fork(() -> reviewService.retrieveReviews(productId));

            // Join the tasks
            scope.join().throwIfFailed(); // This is a completely non-blocking call

            var productInfo = productInfoSubTask.get();
            var reviews = reviewSubTask.get();

            return new Product(productId, productInfo, reviews);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ProductV2 retrieveProductDetailsV2(String productId) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Virtual Threads are created for the below tasks
            // Fork the tasks
            var productInfoSubTask = scope.fork(() -> productInfoService.retrieveProductInfo(productId));
            var reviewSubTask = scope.fork(() -> reviewService.retrieveReviews(productId));
            // Join the tasks
            scope.join().throwIfFailed(); // This is a completely non-blocking call

            var productInfo = productInfoSubTask.get();
            var reviews = reviewSubTask.get();

            var deliveryDetailsSubTask = scope.fork(() -> deliveryService.retrieveDeliveryInfo(productInfo));
            scope.join().throwIfFailed();

            return new ProductV2(productId, productInfo, reviews, deliveryDetailsSubTask.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ProductV2 retrieveProductDetailsHttp(String productId) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Virtual Threads are created for the below tasks
            // Fork the tasks
            var productInfoSubTask = scope.fork(() -> productInfoService.retrieveProductInfoHttp(productId));
            var reviewSubTask = scope.fork(() -> reviewService.retrieveReviewsHttp(productId));
            // Join the tasks
            scope.join().throwIfFailed(); // This is a completely non-blocking call

            var productInfo = productInfoSubTask.get();
            var reviews = reviewSubTask.get();

            var deliveryDetailsSubTask = scope.fork(() -> deliveryService.retrieveDeliveryInfoHttp(productInfo));
            scope.join().throwIfFailed();

            return new ProductV2(productId, productInfo, reviews, deliveryDetailsSubTask.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
