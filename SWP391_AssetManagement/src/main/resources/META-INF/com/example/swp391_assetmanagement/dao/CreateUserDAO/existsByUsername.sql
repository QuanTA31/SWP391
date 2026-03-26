SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
FROM users
WHERE username = /* username */'test_user'