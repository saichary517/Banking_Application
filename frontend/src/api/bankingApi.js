import apiClient from './client';

export const createAccount = (payload) => apiClient.post('/accounts', payload);
export const getAccount = (accountNumber) => apiClient.get(`/accounts/${accountNumber}`);
export const depositAmount = (accountNumber, payload) => apiClient.post(`/accounts/${accountNumber}/deposit`, payload);
export const withdrawAmount = (accountNumber, payload) => apiClient.post(`/accounts/${accountNumber}/withdraw`, payload);
export const transferAmount = (payload) => apiClient.post('/transactions/transfer', payload);
export const getTransactions = (params) => apiClient.get('/transactions', { params });
