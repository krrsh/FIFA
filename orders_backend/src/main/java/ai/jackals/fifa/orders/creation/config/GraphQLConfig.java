package ai.jackals.fifa.orders.creation.config;

import ai.jackals.fifa.orders.creation.datafetcher.OrdersDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    private final OrdersDataFetcher ordersDataFetcher;

    // Explicit constructor to ensure ordersDataFetcher is always initialized, even if Lombok is not processed
    public GraphQLConfig(OrdersDataFetcher ordersDataFetcher) {
        this.ordersDataFetcher = ordersDataFetcher;
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("orders", ordersDataFetcher.orders())
                        .dataFetcher("order", ordersDataFetcher.order()))
                .type("Mutation", typeWiring -> typeWiring
                        .dataFetcher("createOrder", ordersDataFetcher.createOrder())
                        .dataFetcher("updateOrder", ordersDataFetcher.updateOrder())
                        .dataFetcher("deleteOrder", ordersDataFetcher.deleteOrder())
                        .dataFetcher("correctOrderItem", ordersDataFetcher.correctOrderItem()));
    }
}