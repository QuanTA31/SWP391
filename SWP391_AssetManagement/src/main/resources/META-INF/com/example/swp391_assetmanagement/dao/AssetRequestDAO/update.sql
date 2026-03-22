UPDATE asset_request
SET
/*%if assetRequest.requestStatusId != null */
    request_status_id = /* assetRequest.requestStatusId */'APPROVED'
/*%end */
/*%if assetRequest.approvedBy != null */
    /*%if assetRequest.requestStatusId != null */ , /*%end */
    approved_by = /* assetRequest.approvedBy */1,
    approved_date = CURRENT_DATE
/*%end */
/*%if assetRequest.note != null */
    /*%if assetRequest.requestStatusId != null || assetRequest.approvedBy != null */ , /*%end */
    note = /* assetRequest.note */'Updated'
/*%end */
WHERE id = /* assetRequest.id */1