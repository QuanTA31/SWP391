SELECT r.role_code
FROM users u
JOIN role r ON u.role_id = r.id
WHERE u.user_code = /* userCode */''