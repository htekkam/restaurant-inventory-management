
import { useEffect, useMemo, useState } from "react";
import ingredientService from "../services/ingredientService";

function Dashboard() {

  const [ingredients, setIngredients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {

    const fetchDashboardData = async () => {
      try {
        const data = await ingredientService.getAllIngredients();
        setIngredients(data);
      } catch (error) {
        console.error("Error loading dashboard:", error);
        setError("Unable to load dashboard information.");
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();

  }, []);

  const categoryCount = useMemo(() => {
    return new Set(
      ingredients.map((ingredient) => ingredient.category)
    ).size;
  }, [ingredients]);

  const recentIngredients = useMemo(() => {
    return [...ingredients]
      .sort((a, b) =>
        new Date(b.createdAt) - new Date(a.createdAt)
      )
      .slice(0, 5);
  }, [ingredients]);

  const categoryBreakdown = useMemo(() => {

    return ingredients.reduce((result, ingredient) => {

      const category = ingredient.category;

      result[category] = (result[category] || 0) + 1;

      return result;

    }, {});

  }, [ingredients]);

  if (loading) {
    return <div className="page-content">Loading dashboard...</div>;
  }

  if (error) {
    return <div className="page-content error-message">{error}</div>;
  }

  return (
    <div className="page-content">

      <div className="dashboard-heading">
        <div>
          <h2>Dashboard Overview</h2>
          <p>Here's an overview of your restaurant inventory.</p>
        </div>
      </div>

      {/* Summary Cards */}

      <div className="dashboard-cards">

        <div className="dashboard-card">
          <div className="dashboard-card-icon">📦</div>
          <div>
            <p>Total Ingredients</p>
            <h2>{ingredients.length}</h2>
            <small>Active ingredients</small>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="dashboard-card-icon">🏷️</div>
          <div>
            <p>Total Categories</p>
            <h2>{categoryCount}</h2>
            <small>Ingredient categories</small>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="dashboard-card-icon">🕒</div>
          <div>
            <p>Recently Added</p>
            <h2>{recentIngredients.length}</h2>
            <small>Latest ingredients shown below</small>
          </div>
        </div>

      </div>

      {/* Dashboard Details */}

      <div className="dashboard-sections">

        <section className="dashboard-panel">
          <h3>Recently Added Ingredients</h3>

          {recentIngredients.length === 0 ? (
            <p>No ingredients available.</p>
          ) : (
            <table className="dashboard-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Category</th>
                  <th>Unit</th>
                  <th>Added On</th>
                </tr>
              </thead>

              <tbody>
                {recentIngredients.map((ingredient) => (
                  <tr key={ingredient.id}>
                    <td>{ingredient.name}</td>
                    <td>{ingredient.category}</td>
                    <td>{ingredient.unit}</td>
                    <td>
                      {ingredient.createdAt
                        ? new Date(ingredient.createdAt).toLocaleDateString()
                        : "-"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </section>

        <section className="dashboard-panel">
          <h3>Ingredient Category Breakdown</h3>

          {Object.entries(categoryBreakdown).length === 0 ? (
            <p>No category information available.</p>
          ) : (
            Object.entries(categoryBreakdown).map(([category, count]) => (
              <div className="category-row" key={category}>
                <div className="category-info">
                  <span>{category}</span>
                  <strong>{count}</strong>
                </div>

                <div className="category-progress">
                  <div
                    className="category-progress-fill"
                    style={{
                      width: `${(count / ingredients.length) * 100}%`
                    }}
                  />
                </div>
              </div>
            ))
          )}
        </section>

      </div>

    </div>
  );
}

export default Dashboard;
