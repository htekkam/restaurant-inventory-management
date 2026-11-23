
import { useState } from "react";

const initialFormData = {
  name: "",
  category: "",
  unit: "KG",
  description: "",
  minimumStockLevel: ""
};

function IngredientForm({
  initialData,
  onSubmit,
  onCancel,
  saving
}) {
  const [formData, setFormData] = useState(() => ({
    ...initialFormData,
    ...(initialData || {}),
    minimumStockLevel: initialData?.minimumStockLevel?.toString() ?? ""
  }));

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((previousData) => ({
      ...previousData,
      [name]: value
    }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    onSubmit({
      ...formData,
      minimumStockLevel: Number(formData.minimumStockLevel)
    });
  };

  return (
    <div className="ingredient-form-container">
      <h3>{initialData ? "Edit Ingredient" : "Add Ingredient"}</h3>

      <form onSubmit={handleSubmit} className="ingredient-form">

        <div className="form-group">
          <label htmlFor="name">Ingredient Name</label>
          <input
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
            maxLength="100"
            placeholder="Enter ingredient name"
          />
        </div>

        <div className="form-group">
          <label htmlFor="category">Category</label>
          <input
            id="category"
            name="category"
            value={formData.category}
            onChange={handleChange}
            required
            maxLength="50"
            placeholder="e.g. Vegetables"
          />
        </div>

        <div className="form-group">
          <label htmlFor="unit">Unit</label>
          <select
            id="unit"
            name="unit"
            value={formData.unit}
            onChange={handleChange}
            required
          >
            <option value="KG">KG</option>
            <option value="GRAM">GRAM</option>
            <option value="LITRE">LITRE</option>
            <option value="ML">ML</option>
            <option value="PIECE">PIECE</option>
            <option value="PACKET">PACKET</option>
            <option value="DOZEN">DOZEN</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="minimumStockLevel">Minimum Stock Level</label>
          <input
            id="minimumStockLevel"
            name="minimumStockLevel"
            type="number"
            min="0"
            step="0.01"
            value={formData.minimumStockLevel}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group full-width">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description || ""}
            onChange={handleChange}
            maxLength="500"
            placeholder="Enter description"
          />
        </div>

        <div className="form-actions">
          <button type="submit" disabled={saving}>
            {saving
              ? "Saving..."
              : initialData
                ? "Update Ingredient"
                : "Save Ingredient"}
          </button>

          <button
            type="button"
            className="cancel-button"
            onClick={onCancel}
            disabled={saving}
          >
            Cancel
          </button>
        </div>

      </form>
    </div>
  );
}

export default IngredientForm;
