-- CTR (click-through-rate) = clicks / impressions
WITH impressions_with_clicks AS (
    SELECT extract(year from "date")  AS year,
           extract(month from "date") AS month,
           product_id,
           count(*)                   AS count_impressions
    FROM impressions
    WHERE click
    GROUP BY 1, 2, 3
)
SELECT extract(year from i."date")                  AS year,
       extract(month from i."date")                 AS month,
       i.product_id,
       max(iwc.count_impressions) / count(*)::float AS click_through_rate -- here I could multiply by 100 to obtain the rate as %
FROM impressions i
         INNER JOIN impressions_with_clicks iwc
                    ON iwc.year = extract(year from i.date) AND iwc.month = extract(month from i.date) AND
                       iwc.product_id = i.product_id
GROUP BY 1, 2, 3
ORDER BY 1, 2;