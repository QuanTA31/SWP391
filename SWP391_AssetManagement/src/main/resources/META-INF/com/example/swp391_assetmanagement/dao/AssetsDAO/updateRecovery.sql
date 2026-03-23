UPDATE assets
SET
    asset_status_id = /* asset.assetStatusId */'08',
    location_id = /* asset.locationId */'05',
    current_user_id = NULL, -- Xóa định danh người dùng cũ
    note = /* asset.note */'Thu hồi về kho'
WHERE
    id = /* asset.id */1