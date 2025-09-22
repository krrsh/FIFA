import { ApolloClient, InMemoryCache, HttpLink } from '@apollo/client';
import { API_URL } from '../config/env';

const apolloClient = new ApolloClient({
  link: new HttpLink({ uri: API_URL }),
  cache: new InMemoryCache(),
});

export default apolloClient;