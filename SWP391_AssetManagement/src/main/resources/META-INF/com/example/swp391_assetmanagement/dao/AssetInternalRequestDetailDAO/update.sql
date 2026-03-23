UPDATE asset_internal_request_detail
SET asset_id        = /* detail.assetId */0,
    asset_type_id   = /* detail.assetTypeId */'',
    quantity        = /* detail.quantity */0,
    from_location_id = /* detail.fromLocationId */'',
    from_user_id    = /* detail.fromUserId */0,
    to_location_id  = /* detail.toLocationId */'',
    to_user_id      = /* detail.toUserId */0,
    note            = /* detail.note */'',
    is_done         = /* detail.isDone */null
WHERE id = /* detail.id */0
