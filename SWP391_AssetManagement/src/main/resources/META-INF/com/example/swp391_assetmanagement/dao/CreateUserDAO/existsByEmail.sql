SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
FROM user_detail
WHERE email = /* email */'test@example.com'