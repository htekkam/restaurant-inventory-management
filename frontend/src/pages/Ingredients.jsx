
import { useEffect, useState } from "react";
import ingredientService from "../services/ingredientService";

function Ingredients() {

    const [ingredients, setIngredients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const fetchIngredients = async () => {

            try {
                const data =
                    await ingredientService.getAllIngredients();

                setIngredients(data);

            } catch (error) {

                console.error("Error fetching ingredients:", error);

                setError("Unable to load ingredients.");

            } finally {

                setLoading(false);

            }
        };

        fetchIngredients();

    }, []);

    if (loading) {
        return <div className="page-content">Loading ingredients...</div>;
    }

    if (error) {
        return <div className="page-content">{error}</div>;
    }

    return (
        <div className="page-content">

            <h2>Ingredient Management</h2>

            <p>Manage your restaurant ingredients.</p>

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
                    </tr>
                </thead>

                <tbody>

                    {ingredients.map((ingredient) => (

                        <tr key={ingredient.id}>

                            <td>{ingredient.id}</td>

                            <td>{ingredient.name}</td>

                            <td>{ingredient.category}</td>

                            <td>{ingredient.unit}</td>

                            <td>{ingredient.minimumStockLevel}</td>

                            <td>
                                {ingredient.active ? "Active" : "Inactive"}
                            </td>

                        </tr>

                    ))}

                </tbody>

            </table>

        </div>
    );
}

export default Ingredients;
