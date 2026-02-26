UPDATE asset_internal_request_detail
SET asset_type_id      = /* details.assetTypeId */'',
    external_status_id = /* details.externalStatusId */'',
    quantity           = /* details.quantity */1,
    note               = /* details.note */''
    WHERE
        id = /* details.id */1
