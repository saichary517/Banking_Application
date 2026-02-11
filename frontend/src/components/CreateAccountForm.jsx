import { useState } from 'react';

const initialState = { fullName: '', email: '', initialDeposit: '' };

function CreateAccountForm({ onSubmit }) {
  const [formData, setFormData] = useState(initialState);

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ ...formData, initialDeposit: Number(formData.initialDeposit) });
    setFormData(initialState);
  };

  return (
    <form className="card form-grid" onSubmit={handleSubmit}>
      <h3>Create Account</h3>
      <input name="fullName" value={formData.fullName} onChange={handleChange} placeholder="Full Name" required />
      <input name="email" type="email" value={formData.email} onChange={handleChange} placeholder="Email" required />
      <input
        name="initialDeposit"
        type="number"
        step="0.01"
        min="100"
        value={formData.initialDeposit}
        onChange={handleChange}
        placeholder="Initial Deposit (min 100)"
        required
      />
      <button type="submit">Create Account</button>
    </form>
  );
}

export default CreateAccountForm;
