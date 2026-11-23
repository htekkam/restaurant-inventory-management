
import { useEffect, useState } from "react";

import ingredientService from "../services/ingredientService";
import IngredientForm from "../components/IngredientForm";

function Ingredients() {

  const [ingredients, setIngredients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [editingIngredient, setEditingIngredient] = useState(null);
  const [saving, setSaving] = useState(false);

  const fetchIngredients = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await ingredientService.getAllIngredients();
      setIngredients(data);

    } catch (error) {
      console.error("Error fetching ingredients:", error);
      setError("Unable to load ingredients.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchIngredients();
  }, []);

  const handleAdd = () => {
    setEditingIngredient(null);
    setShowForm(true);
  };

  const handleEdit = (ingredient) => {
    setEditingIngredient(ingredient);
    setShowForm(true);
  };

  const handleCancel = () => {
    setShowForm(false);
    setEditingIngredient(null);
  };

  const handleSave = async (ingredientData) => {
    try {
      setSaving(true);
      setError("");

      if (editingIngredient) {
        await ingredientService.updateIngredient(
          editingIngredient.id,
          ingredientData
        );
      } else {
        await ingredientService.createIngredient(ingredientData);
      }

      handleCancel();
      await fetchIngredients();

    } catch (error) {
      console.error("Error saving ingredient:", error);

      const message = error.response?.data?.message;
      setError(message || "Unable to save ingredient. Please try again.");

    } finally {
      setSaving(false);
    }
  };

  const handleDeactivate = async (ingredient) => {

    const confirmed = window.confirm(
      `Are you sure you want to deactivate ${ingredient.name}?`
    );

    if (!confirmed) {
      return;
    }

    try {
      setError("");

      await ingredientService.deactivateIngredient(ingredient.id);
      await fetchIngredients();

    } catch (error) {
      console.error("Error deactivating ingredient:", error);
      setError("Unable to deactivate ingredient.");
    }
  };

  if (loading) {
    return <div className="page-content">Loading ingredients...</div>;
  }

  return (
    <div className="page-content">

      <div className="page-header">
        <div>
          <h2>Ingredient Management</h2>
          <p>Manage your restaurant ingredients.</p>
        </div>

        <button onClick={handleAdd} className="add-button">
          + Add Ingredient
        </button>
      </div>

      {error && <p className="error-message">{error}</p>}

      {showForm && (
        <IngredientForm
          initialData={editingIngredient}
          onSubmit={handleSave}
          onCancel={handleCancel}
          saving={saving}
        />
      )}

      <h3>Total Ingredients: {ingredients.length}</h3>

      <table className="ingredient-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Unit</th>
            <th>Minimum Stock</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {ingredients.length === 0 ? (
            <tr>
              <td colSpan="7">No ingredients found.</td>
            </tr>
          ) : (
            ingredients.map((ingredient) => (
              <tr key={ingredient.id}>
                <td>{ingredient.id}</td>
                <td>{ingredient.name}</td>
                <td>{ingredient.category}</td>
                <td>{ingredient.unit}</td>
                <td>{ingredient.minimumStockLevel}</td>
                <td>{ingredient.active ? "Active" : "Inactive"}</td>
                <td>
                  <button
                    onClick={() => handleEdit(ingredient)}
                    className="edit-button"
                  >
                    Edit
                  </button>

                  <button
                    onClick={() => handleDeactivate(ingredient)}
                    className="delete-button"
                  >
                    Deactivate
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

    </div>
  );
}

export default Ingredients;
