import { useEffect, useState } from 'react';
import TransactionTable from '../components/TransactionTable';
import { getTransactions } from '../api/bankingApi';

function TransactionsPage() {
  const [rows, setRows] = useState([]);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [type, setType] = useState('');
  const [sortBy, setSortBy] = useState('createdAt');
  const [direction, setDirection] = useState('DESC');
  const [meta, setMeta] = useState({ totalPages: 0, totalElements: 0 });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        setLoading(true);
        setError('');
        const res = await getTransactions({ page, size, type: type || undefined, sortBy, direction });
        setRows(res.data.content || []);
        setMeta({ totalPages: res.data.totalPages, totalElements: res.data.totalElements });
      } catch (err) {
        setError(err?.response?.data?.message || err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchTransactions();
  }, [page, size, type, sortBy, direction]);

  return (
    <div className="grid gap-16">
      <section className="card filters">
        <h3>Transaction History Filters</h3>
        <select value={type} onChange={(e) => { setPage(0); setType(e.target.value); }}>
          <option value="">All Types</option>
          <option value="DEPOSIT">Deposit</option>
          <option value="WITHDRAWAL">Withdrawal</option>
          <option value="TRANSFER">Transfer</option>
        </select>
        <select value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
          <option value="createdAt">Created At</option>
          <option value="amount">Amount</option>
          <option value="type">Type</option>
        </select>
        <select value={direction} onChange={(e) => setDirection(e.target.value)}>
          <option value="DESC">DESC</option>
          <option value="ASC">ASC</option>
        </select>
        <select value={size} onChange={(e) => { setPage(0); setSize(Number(e.target.value)); }}>
          <option value={5}>5</option>
          <option value={10}>10</option>
          <option value={20}>20</option>
        </select>
      </section>

      {error && <div className="alert error">{error}</div>}
      {loading && <div className="alert info">Loading...</div>}

      <TransactionTable rows={rows} />

      <section className="card pagination">
        <button onClick={() => setPage((prev) => Math.max(prev - 1, 0))} disabled={page === 0}>Previous</button>
        <span>Page {page + 1} of {meta.totalPages || 1} | Total Records: {meta.totalElements}</span>
        <button onClick={() => setPage((prev) => prev + 1)} disabled={page + 1 >= meta.totalPages}>Next</button>
      </section>
    </div>
  );
}

export default TransactionsPage;
