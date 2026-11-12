
function Sidebar({ activePage, onNavigate }) {

  const menuItems = [
    { id: "dashboard", label: "Dashboard", icon: "📊" },
    { id: "ingredients", label: "Ingredients", icon: "🥬" },
    { id: "stock", label: "Stock Management", icon: "📦" },
    { id: "suppliers", label: "Suppliers", icon: "🚚" },
    { id: "purchases", label: "Purchases", icon: "🛒" }
  ];

  return (
    <aside className="sidebar">
      <div className="brand">
        <span>🍽️</span>
        <h2>RestroStock</h2>
      </div>

      <p className="menu-heading">MAIN MENU</p>

      <nav>
        {menuItems.map((item) => (
          <button
            key={item.id}
            className={`menu-item ${
              activePage === item.id ? "active" : ""
            }`}
            onClick={() => onNavigate(item.id)}
          >
            <span>{item.icon}</span>
            {item.label}
          </button>
        ))}
      </nav>

      <div className="sidebar-footer">
        Inventory Management System
        <small>Version 1.0</small>
      </div>
    </aside>
  );
}

export default Sidebar;
