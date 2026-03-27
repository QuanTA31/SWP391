SELECT
    COUNT(*)
FROM asset_internal_request_detail d
JOIN assets a ON d.asset_id = a.id
LEFT JOIN user_detail ud ON d.from_user_id = ud.user_id
WHERE d.asset_request_id = /* request.requestId */76

/*%if request.assetTypeId != null && request.assetTypeId != "" */
  AND a.asset_type_id = /* request.assetTypeId */'01'
/*%end*/

/*%if request.fullName != null && request.fullName != "" */
  AND Lower(ud.name) LIKE Lower(CONCAT('%', /* request.fullName */'lap', '%'))
/*%end*/
