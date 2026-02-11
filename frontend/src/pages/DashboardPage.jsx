import { useState } from 'react';
import AccountLookup from '../components/AccountLookup';
import AmountActionForm from '../components/AmountActionForm';
import CreateAccountForm from '../components/CreateAccountForm';
import TransactionTable from '../components/TransactionTable';
import TransferForm from '../components/TransferForm';
import {
  createAccount,
  depositAmount,
  getAccount,
  getTransactions,
  transferAmount,
  withdrawAmount,
} from '../api/bankingApi';

function DashboardPage() {
  const [account, setAccount] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const extractError = (err) => err?.response?.data?.message || err?.response?.data?.details?.join(', ') || err.message;

  const wrapApiCall = async (callback, successMessage) => {
    try {
      setLoading(true);
      setError('');
      setSuccess('');
      await callback();
      if (successMessage) setSuccess(successMessage);
    } catch (err) {
      setError(extractError(err));
    } finally {
      setLoading(false);
    }
  };

  const refreshTransactions = async (accountNumber) => {
    const txRes = await getTransactions({ accountNumber, page: 0, size: 5, sortBy: 'createdAt', direction: 'DESC' });
    setTransactions(txRes.data.content || []);
  };

  const handleLookup = async (accountNumber) => {
    await wrapApiCall(async () => {
      const accountRes = await getAccount(accountNumber);
      setAccount(accountRes.data);
      await refreshTransactions(accountNumber);
    });
  };

  const handleCreate = async (payload) => {
    await wrapApiCall(async () => {
      const res = await createAccount(payload);
      setAccount(res.data);
      await refreshTransactions(res.data.accountNumber);
    }, 'Account created successfully');
  };

  const handleDeposit = async ({ accountNumber, amount, description }) => {
    await wrapApiCall(async () => {
      const res = await depositAmount(accountNumber, { amount, description });
      setAccount(res.data);
      await refreshTransactions(accountNumber);
    }, 'Deposit successful');
  };

  const handleWithdraw = async ({ accountNumber, amount, description }) => {
    await wrapApiCall(async () => {
      const res = await withdrawAmount(accountNumber, { amount, description });
      setAccount(res.data);
      await refreshTransactions(accountNumber);
    }, 'Withdrawal successful');
  };

  const handleTransfer = async (payload) => {
    await wrapApiCall(async () => {
      await transferAmount(payload);
      if (account?.accountNumber === payload.fromAccountNumber || account?.accountNumber === payload.toAccountNumber) {
        const latest = await getAccount(account.accountNumber);
        setAccount(latest.data);
        await refreshTransactions(account.accountNumber);
      }
    }, 'Transfer successful');
  };

  return (
    <div className="grid gap-16">
      {error && <div className="alert error">{error}</div>}
      {success && <div className="alert success">{success}</div>}
      {loading && <div className="alert info">Loading...</div>}

      <section className="grid columns-2 gap-16">
        <CreateAccountForm onSubmit={handleCreate} />
        <AccountLookup onLookup={handleLookup} />
      </section>

      {account && (
        <section className="card">
          <h3>Account Snapshot</h3>
          <p><strong>Account:</strong> {account.accountNumber}</p>
          <p><strong>Name:</strong> {account.fullName}</p>
          <p><strong>Email:</strong> {account.email}</p>
          <p><strong>Balance:</strong> {account.balance}</p>
        </section>
      )}

      <section className="grid columns-3 gap-16">
        <AmountActionForm title="Deposit" buttonLabel="Deposit" onSubmit={handleDeposit} />
        <AmountActionForm title="Withdraw" buttonLabel="Withdraw" onSubmit={handleWithdraw} />
        <TransferForm onSubmit={handleTransfer} />
      </section>

      <TransactionTable rows={transactions} />
    </div>
  );
}

export default DashboardPage;
