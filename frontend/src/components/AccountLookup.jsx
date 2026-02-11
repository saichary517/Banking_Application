import { useState } from 'react';

function AccountLookup({ onLookup }) {
  const [accountNumber, setAccountNumber] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onLookup(accountNumber.trim());
  };

  return (
    <form className="card form-grid" onSubmit={handleSubmit}>
      <h3>Fetch Account Details</h3>
      <input
        type="text"
        placeholder="Account Number"
        value={accountNumber}
        onChange={(e) => setAccountNumber(e.target.value)}
        required
      />
      <button type="submit">Fetch</button>
    </form>
  );
}

export default AccountLookup;
