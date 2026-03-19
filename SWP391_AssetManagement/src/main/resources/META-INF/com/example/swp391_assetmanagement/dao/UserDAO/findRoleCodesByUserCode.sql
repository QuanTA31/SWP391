SELECT r.id
FROM users u
JOIN roles r ON u.role_id = r.id
WHERE u.user_code = /* userCode */''