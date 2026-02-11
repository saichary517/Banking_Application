function TransactionTable({ rows }) {
  return (
    <div className="card">
      <h3>Recent Transactions</h3>
      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Account</th>
              <th>Counterparty</th>
              <th>Type</th>
              <th>Amount</th>
              <th>Post Balance</th>
              <th>Description</th>
              <th>Created At</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((tx) => (
              <tr key={tx.id}>
                <td>{tx.id}</td>
                <td>{tx.accountNumber}</td>
                <td>{tx.counterpartyAccountNumber || '-'}</td>
                <td>{tx.type}</td>
                <td>{tx.amount}</td>
                <td>{tx.postBalance}</td>
                <td>{tx.description || '-'}</td>
                <td>{new Date(tx.createdAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
        {!rows.length && <p>No transactions found.</p>}
      </div>
    </div>
  );
}

export default TransactionTable;
