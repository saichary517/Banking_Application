import { useState } from 'react';

const initialState = { fromAccountNumber: '', toAccountNumber: '', amount: '', description: '' };

function TransferForm({ onSubmit }) {
  const [formData, setFormData] = useState(initialState);

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ ...formData, amount: Number(formData.amount) });
    setFormData(initialState);
  };

  return (
    <form className="card form-grid" onSubmit={handleSubmit}>
      <h3>Transfer Money</h3>
      <input name="fromAccountNumber" value={formData.fromAccountNumber} onChange={handleChange} placeholder="From Account" required />
      <input name="toAccountNumber" value={formData.toAccountNumber} onChange={handleChange} placeholder="To Account" required />
      <input name="amount" type="number" min="0.01" step="0.01" value={formData.amount} onChange={handleChange} placeholder="Amount" required />
      <input name="description" value={formData.description} onChange={handleChange} placeholder="Description (optional)" />
      <button type="submit">Transfer</button>
    </form>
  );
}

export default TransferForm;
