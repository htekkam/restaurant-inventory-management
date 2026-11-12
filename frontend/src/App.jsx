
import { useState } from "react";

import Navbar from "./components/Navbar";
import Sidebar from "./components/Sidebar";

import Dashboard from "./pages/Dashboard";
import Ingredients from "./pages/Ingredients";

import "./App.css";

function App() {

  const [activePage, setActivePage] = useState("dashboard");

  const renderPage = () => {

    switch (activePage) {

      case "dashboard":
        return <Dashboard />;

      case "ingredients":
        return <Ingredients />;

      default:
        return <div className="page-content">
          <h2>Coming Soon</h2>
          <p>This module is under development.</p>
        </div>;
    }
  };

  const pageTitles = {
    dashboard: "Dashboard",
    ingredients: "Ingredient Management",
    stock: "Stock Management",
    suppliers: "Supplier Management",
    purchases: "Purchase Management"
  };

  return (
    <div className="app-layout">

      <Sidebar
        activePage={activePage}
        onNavigate={setActivePage}
      />

      <div className="main-area">

        <Navbar title={pageTitles[activePage]} />

        <main className="main-content">
          {renderPage()}
        </main>

      </div>

    </div>
  );
}

export default App;
