UPDATE asset_request
SET
/*%if assetRequest.requestStatusId != null */
    request_status_id = /* assetRequest.requestStatusId */'APPROVED',
/*%end */
/*%if assetRequest.approvedBy != null */
    approved_by = /* assetRequest.approvedBy */1,
    approved_date = CURRENT_DATE
/*%end */
/*%if assetRequest.note != null */
    ,note = /* assetRequest.note */'Updated'
/*%end */
WHERE id = /* assetRequest.id */1