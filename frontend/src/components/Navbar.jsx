
function Navbar({ title }) {
  return (
    <header className="navbar">
      <div>
        <h2>{title}</h2>
        <p>Restaurant Inventory Management</p>
      </div>

      <div className="navbar-right">
        <span className="user-avatar">A</span>
        <div>
          <strong>Admin</strong>
          <small>Administrator</small>
        </div>
      </div>
    </header>
  );
}

export default Navbar;
