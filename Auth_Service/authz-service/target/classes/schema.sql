DROP TABLE IF EXISTS user_permissions;
CREATE TABLE user_permissions (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id TEXT NOT NULL,
  action TEXT NOT NULL,     -- 'read' | 'write' | 'delete'
  resource TEXT NOT NULL,   -- e.g. 'transactions', 'wallets/*', 'wallets/*/transactions/*'
  effect TEXT NOT NULL      -- 'allow' | 'deny'
);
