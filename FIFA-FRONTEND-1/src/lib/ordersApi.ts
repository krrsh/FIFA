// ordersApi.ts
// Minimal GraphQL client for Orders backend
import { ORDERS_GRAPHQL_URL } from '../config/env';

const GRAPHQL_ENDPOINT = ORDERS_GRAPHQL_URL || process.env.EXPO_PUBLIC_ORDERS_GRAPHQL_URL || 'http://192.168.1.110:8080/graphql';

interface GraphQLResponse<T> {
	data?: T;
	errors?: Array<{ message: string }>;
}

async function graphqlFetch<T>(query: string, variables?: Record<string, unknown>): Promise<T> {
	const response = await fetch(GRAPHQL_ENDPOINT, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({ query, variables }),
	});

	const json = (await response.json()) as GraphQLResponse<T>;
	if (!response.ok || json.errors) {
		const message = json.errors?.map(e => e.message).join('; ') || `HTTP ${response.status}`;
		throw new Error(message);
	}
	if (!json.data) {
		throw new Error('Empty response');
	}
	return json.data;
}

// ===== GraphQL documents =====
const ORDERS_QUERY = `
	query Orders {
		orders {
			success
			message
			orders {
				orderId
				supplierId
				orderType
				orderService
				companyId
				personId
				customerId
				materialId
				isUrgent
				isForeign
				dueDate
				deliveryDate
				dcNumber
				poNumber
				orderItems {
					itemId
					productName
					productStyle
					productSize
					orderQty
					isAccessories
					status
				}
			}
		}
	}
`;

const CREATE_ORDER_MUTATION = `
	mutation CreateOrder($input: OrderCreateInput!) {
		createOrder(input: $input) {
			success
			message
			order {
				orderId
			}
		}
	}
`;

const UPDATE_ORDER_MUTATION = `
	mutation UpdateOrder($id: ID!, $input: OrderCreateInput!) {
		updateOrder(id: $id, input: $input) {
			success
			message
			order { orderId }
		}
	}
`;

const DELETE_ORDER_MUTATION = `
	mutation DeleteOrder($id: ID!) {
		deleteOrder(id: $id) {
			success
			message
		}
	}
`;

const CORRECT_ORDER_ITEM_MUTATION = `
	mutation CorrectOrderItem($orderId: ID!, $itemId: ID!, $correctedItem: OrderItemInput!) {
		correctOrderItem(orderId: $orderId, itemId: $itemId, correctedItem: $correctedItem) {
			success
			message
			order { orderId }
		}
	}
`;

// ===== API surface =====
export async function fetchOrders() {
	return graphqlFetch<{ orders: { success: boolean; message?: string; orders: Array<any> } }>(ORDERS_QUERY);
}

export async function createOrder(input: any) {
	return graphqlFetch<{ createOrder: { success: boolean; message?: string; order?: { orderId: string } } }>(
		CREATE_ORDER_MUTATION,
		{ input }
	);
}

export async function updateOrder(id: string, input: any) {
	return graphqlFetch<{ updateOrder: { success: boolean; message?: string } }>(UPDATE_ORDER_MUTATION, { id, input });
}

export async function deleteOrder(id: string) {
	return graphqlFetch<{ deleteOrder: { success: boolean; message?: string } }>(DELETE_ORDER_MUTATION, { id });
}

export async function correctOrderItem(orderId: string, itemId: string, correctedItem: any) {
	return graphqlFetch<{ correctOrderItem: { success: boolean; message?: string } }>(
		CORRECT_ORDER_ITEM_MUTATION,
		{ orderId, itemId, correctedItem }
	);
}
