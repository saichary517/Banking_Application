import { useState } from 'react';

function AmountActionForm({ title, buttonLabel, onSubmit }) {
  const [accountNumber, setAccountNumber] = useState('');
  const [amount, setAmount] = useState('');
  const [description, setDescription] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ accountNumber, amount: Number(amount), description });
    setAmount('');
    setDescription('');
  };

  return (
    <form className="card form-grid" onSubmit={handleSubmit}>
      <h3>{title}</h3>
      <input value={accountNumber} onChange={(e) => setAccountNumber(e.target.value)} placeholder="Account Number" required />
      <input type="number" min="0.01" step="0.01" value={amount} onChange={(e) => setAmount(e.target.value)} placeholder="Amount" required />
      <input value={description} onChange={(e) => setDescription(e.target.value)} placeholder="Description (optional)" />
      <button type="submit">{buttonLabel}</button>
    </form>
  );
}

export default AmountActionForm;
