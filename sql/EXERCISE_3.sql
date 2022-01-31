WITH prod_with_price_tier AS ( -- Fist I get the price tier category for each product
    SELECT product_id,
           CASE
               WHEN price <= 5 THEN 'low'
               WHEN price <= 15 THEN 'mid'
               ELSE 'high' END AS price_tier
    FROM products
),
     impr_with_clicks_per_pt AS ( -- then I compute the count of all impression clicked
         SELECT p.price_tier,
                count(*) AS count_impressions
         FROM impressions i
                  INNER JOIN prod_with_price_tier p on i.product_id = p.product_id
         WHERE click
         GROUP BY 1
     )
SELECT p.price_tier,
       max(iwcpp.count_impressions) / count(*)::float AS click_through_rate -- finally I obtain the ctr by the result of (impressions with clicks / total impressions)
FROM impressions i
         INNER JOIN prod_with_price_tier p on i.product_id = p.product_id
         INNER JOIN impr_with_clicks_per_pt iwcpp ON iwcpp.price_tier = p.price_tier
GROUP BY 1
ORDER BY 2 DESC;
