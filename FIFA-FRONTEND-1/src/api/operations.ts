  import { gql } from '@apollo/client';

export const ORDERS_QUERY = gql`
  query Orders {
    orders {
      success
      orders {
        orderId
        supplierId
        dcNumber
        poNumber
        dueDate
        isUrgent
      }
    }
  }
`;

export const CREATE_ORDER = gql`
  mutation CreateOrder($input: OrderCreateInput!) {
    createOrder(input: $input) {
      success
      message
      order {
        orderId
        supplierId
        dcNumber
        poNumber
        dueDate
        isUrgent
      }
    }
  }
`;

export const DELETE_ORDER = gql`
  mutation DeleteOrder($id: ID!) {
    deleteOrder(id: $id) {
      success
      message
    }
  }
`;

// Single order query
export const ORDER_QUERY = gql`
  query Order($id: ID!) {
    order(id: $id) {
      success
      message
      order {
        orderId
        supplierId
        dcNumber
        poNumber
        dueDate
        isUrgent
        orderItems {
          productStyle
          productSize
          orderQty
          isAccessories
        }
      }
    }
  }
`;
