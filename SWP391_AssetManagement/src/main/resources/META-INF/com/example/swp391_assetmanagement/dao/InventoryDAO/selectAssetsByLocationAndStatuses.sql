SELECT
    *
FROM assets
WHERE location_id = /* locationId */'05'
/*%if statusIds != null && statusIds.size() > 0 */
  AND asset_status_id IN /* statusIds */('01', '08', '05')
/*%end*/
