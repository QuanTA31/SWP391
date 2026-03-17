SELECT id,
       asset_id,
       asset_request_id,
       asset_type_id,
       quantity,
       from_location_id,
       to_location_id,
       from_user_id,
       to_user_id,
       note,
       created_at,
       is_done
FROM asset_internal_request_detail
WHERE asset_request_id = /* assetRequestId */0
