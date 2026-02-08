SELECT u.id,
       u.username,
       ud.name,
       u.user_code,
       u.role_id
FROM users AS u
    INNER JOIN user_detail AS ud ON ud.user_id = u.id
WHERE username = /* userDAORequest.username */''
AND password = /* userDAORequest.password */''
