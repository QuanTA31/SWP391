UPDATE assets
SET
/*%if asset.assetCode != null */
    asset_code = /* asset.assetCode */'AC001',
/*%end */
/*%if asset.assetStatusId != null */
    asset_status_id = /* asset.assetStatusId */'01',
/*%end */
/*%if asset.assetTypeId != null */
    asset_type_id = /* asset.assetTypeId */'01',
/*%end */
/*%if asset.warrantyPeriod != null */
    warranty_period = /* asset.warrantyPeriod */'2024-01-01',
/*%end */
/*%if asset.originalPrice != null */
    original_price = /* asset.originalPrice */1000,
/*%end */
/*%if asset.description != null */
    description = /* asset.description */'Desc',
/*%end */
/*%if asset.currentUserId != null */
    current_user_id = /* asset.currentUserId */1,
/*%end */
/*%if asset.locationId != null */
    location_id = /* asset.locationId */'01',
/*%end */
/*%if asset.depreciation != null */
    depreciation = /* asset.depreciation */10,
/*%end */
/*%if asset.receivedDate != null */
    received_date = /* asset.receivedDate */'2024-01-01',
/*%end */
/*%if asset.note != null */
    note = /* asset.note */'Note',
/*%end */
    id = id
WHERE id = /* asset.id */1
