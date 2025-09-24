import { graphqlFetch } from './ordersApi';

const CREATE_MATERIAL_MUTATION = `
  mutation CreateMaterial($input: MaterialInput!) {
    createMaterial(input: $input) {
      success
      message
      material { id name brand style image }
    }
  }
`;

export async function createMaterial(input: { name: string; brand: string; style: string; image?: string }) {
  return graphqlFetch<{ createMaterial: { success: boolean; message?: string; material?: any } }>(
    CREATE_MATERIAL_MUTATION,
    { input }
  );
}

// future masters APIs (machines, tailors, etc.) can be added here
