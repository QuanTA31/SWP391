SELECT ud.name,
       u.role_id
FROM users AS u
    INNER JOIN user_detail AS ud ON ud.user_id = u.id
WHERE username = /* userDAORequest.username */''
AND password = /* userDAORequest.password */''
