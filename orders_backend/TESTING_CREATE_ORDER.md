Backend createOrder Testing
===========================

Steps to build and run the backend, then test the GraphQL `createOrder` mutation:

1. Build the project:

```powershell
cd orders_backend
mvn -U clean package
```

2. Run the Spring Boot app:

```powershell
mvn spring-boot:run
# or
java -jar target/jack-fifa-orders-creation-0.0.1-SNAPSHOT.jar
```

3. Open GraphiQL/Playground at `http://localhost:8080/graphiql` (or the GraphQL endpoint `http://localhost:8080/graphql`).

4. Example mutation to create an order (adjust `supplierId` to a valid value in your DB):

```graphql
mutation {
  createOrder(input: {
    companyId: 1,
    supplierId: 123,
    dcNumber: "DC-1001",
    poNumber: "PO-2002",
    isUrgent: false,
    isForeign: false,
    dueDate: "2025-10-01",
    items: [
      {
        productName: "T-Shirt",
        productStyle: "TS-RED",
        productColour: "Red",
        productSize: "M",
        productSizeCm: 40.5,
        threadType: "Poly",
        orderQty: 100,
        qtyUom: "pcs",
        unitRate: 5.5,
        isAccessories: false,
        currency: "INR"
      }
    ]
  }) {
    success
    message
    order {
      orderId
      dcNumber
      poNumber
      dueDate
      orderItems { itemId productName productStyle productSize orderQty }
    }
  }
}
```

5. Verify the response returns `order.orderId` and the `orderItems.itemId` values (assigned server-side). If not, check backend logs for exceptions.

6. Inspect DB table `orders` and `order_items` to confirm rows were inserted with non-null `order_id` and `order_item_id`.
