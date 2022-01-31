WITH impr_with_clicks_per_cat AS (
    SELECT p.category_id,
           count(*) AS count_impressions
    FROM impressions i
             INNER JOIN products p on i.product_id = p.product_id
    WHERE click
    GROUP BY 1
)
SELECT p.category_id,
       max(iwcpc.count_impressions) / count(*)::float AS click_through_rate
FROM impressions i
         INNER JOIN products p on i.product_id = p.product_id
         INNER JOIN impr_with_clicks_per_cat iwcpc ON iwcpc.category_id = p.category_id
GROUP BY 1
ORDER BY 2 DESC
LIMIT 3;
